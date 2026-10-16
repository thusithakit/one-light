package com.thusithakit.authservice.service;

import com.thusithakit.authservice.dto.RefreshResponseDTO;
import com.thusithakit.authservice.model.AuthUser;
import com.thusithakit.authservice.model.RefreshToken;
import com.thusithakit.authservice.repository.AuthUserRepository;
import com.thusithakit.authservice.repository.RefreshTokenRepository;
import com.thusithakit.authservice.security.JwtService;
import com.thusithakit.authservice.security.RefreshTokenGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

  private final RefreshTokenRepository refreshTokenRepository;
  private final RefreshTokenGenerator refreshTokenGenerator;
  private final AuthUserRepository authUserRepository;
  private final long refreshTokenTtlSeconds;
  private final JwtService jwtService;

  public RefreshTokenService(
      RefreshTokenRepository repository,
      RefreshTokenGenerator generator,
      @Value("${auth.refresh-token-ttl-seconds:604800}")
      long refreshTokenTtlSeconds,
      AuthUserRepository authUserRepository,
      JwtService jwtService) {
    this.refreshTokenRepository = repository;
    this.refreshTokenGenerator = generator;
    this.refreshTokenTtlSeconds = refreshTokenTtlSeconds;
    this.authUserRepository = authUserRepository;
    this.jwtService = jwtService;
  }

  @Transactional
  public String createRefreshToken(UUID userId) {
    AuthUser user = authUserRepository.findById(userId)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.UNAUTHORIZED,
            "Invalid user"
        ));

    if (!user.isEnabled()) {
      throw new ResponseStatusException(
          HttpStatus.FORBIDDEN,
          "Account is disabled"
      );
    }

    String rawToken = refreshTokenGenerator.generate();
    Instant now = Instant.now();

    RefreshToken refreshToken = RefreshToken.builder()
        .id(UUID.randomUUID())
        .userId(userId)
        .familyId(UUID.randomUUID())
        .tokenHash(refreshTokenGenerator.hash(rawToken))
        .createdAt(now)
        .expiresAt(now.plusSeconds(refreshTokenTtlSeconds))
        .build();

    refreshTokenRepository.save(refreshToken);

    return rawToken;
  }

  @Transactional
  public RefreshResponseDTO refresh(String rawRefreshToken) {
    if (rawRefreshToken == null || rawRefreshToken.isBlank()) {
      throw invalidRefreshToken();
    }

    String oldHash = refreshTokenGenerator.hash(rawRefreshToken);

    RefreshToken oldToken = refreshTokenRepository
        .findByTokenHashForUpdate(oldHash)
        .orElseThrow(RefreshTokenService::invalidRefreshToken);

    Instant now = Instant.now();

    if (oldToken.getRevokedAt() != null
        || !oldToken.getExpiresAt().isAfter(now)) {
      throw invalidRefreshToken();
    }

    AuthUser user = authUserRepository.findById(oldToken.getUserId())
        .orElseThrow(RefreshTokenService::invalidRefreshToken);

    if (!user.isEnabled()) {
      throw new ResponseStatusException(
          HttpStatus.FORBIDDEN,
          "Account is disabled"
      );
    }

    String newRawToken = refreshTokenGenerator.generate();
    String newHash = refreshTokenGenerator.hash(newRawToken);

    RefreshToken replacement = RefreshToken.builder()
        .id(UUID.randomUUID())
        .userId(oldToken.getUserId())
        .familyId(oldToken.getFamilyId())
        .tokenHash(newHash)
        .createdAt(now)
        .expiresAt(now.plusSeconds(refreshTokenTtlSeconds))
        .build();

    // Rotate the token in the same database transaction.
    oldToken.setRevokedAt(now);
    oldToken.setReplacedByHash(newHash);

    refreshTokenRepository.save(oldToken);
    refreshTokenRepository.save(replacement);

    String accessToken = jwtService.generateToken(
        user.getId(),
        user.getEmail()
    );

    return new RefreshResponseDTO(
        accessToken,
        newRawToken,
        refreshTokenTtlSeconds
    );
  }

  @Transactional
  public void logout(String rawRefreshToken) {
    if (rawRefreshToken == null || rawRefreshToken.isBlank()) {
      return;
    }

    refreshTokenRepository.findByTokenHashForUpdate(
        refreshTokenGenerator.hash(rawRefreshToken)
    ).ifPresent(token -> {
      if (token.getRevokedAt() == null) {
        token.setRevokedAt(Instant.now());
        refreshTokenRepository.save(token);
      }
    });
  }

  private static ResponseStatusException invalidRefreshToken() {
    return new ResponseStatusException(
        HttpStatus.UNAUTHORIZED,
        "Invalid or expired refresh token"
    );
  }
}
