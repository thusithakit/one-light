package com.thusithakit.messageservice.repository;

import com.thusithakit.messageservice.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MessageRepository
    extends JpaRepository<Message, UUID> {

  List<Message> findAllByDeviceIdOrderByCreatedAtDesc(
      UUID deviceId
  );
}
