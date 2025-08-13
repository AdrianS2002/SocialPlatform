package com.example.social_platform.persistance.repository;

import com.example.social_platform.persistance.model.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    @Query("""
      SELECT m FROM MessageEntity m
       WHERE (m.sender.id = :u1 AND m.receiver.id = :u2)
          OR (m.sender.id = :u2 AND m.receiver.id = :u1)
       ORDER BY m.sentAt
    """)
    List<MessageEntity> findChatHistory(@Param("u1") Long user1,
                                        @Param("u2") Long user2);
}