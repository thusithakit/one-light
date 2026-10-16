package com.thusithakit.authservice.repository;

import com.thusithakit.authservice.model.RefreshToken;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository
    extends JpaRepository<RefreshToken, UUID> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("""
        SELECT t FROM RefreshToken t
        WHERE t.tokenHash = :tokenHash
        """)
  Optional<RefreshToken> findByTokenHashForUpdate(
      @Param("tokenHash") String tokenHash
  );

  Optional<RefreshToken> findByTokenHash(String tokenHash);

  List<RefreshToken> findByFamilyId(UUID familyId);
}
