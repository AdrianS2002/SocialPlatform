package com.example.social_platform.service.ports.incoming;

import com.example.social_platform.controller.model.ChatMessage;
import com.example.social_platform.persistance.model.MessageEntity;
import com.example.social_platform.persistance.model.UserEntity;
import com.example.social_platform.persistance.repository.MessageRepository;
import com.example.social_platform.persistance.repository.UserPsqlRepository;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private final MessageRepository messageRepo;
    private final UserPsqlRepository userRepo;

    public ChatService(MessageRepository messageRepo,
                       UserPsqlRepository userRepo) {
        this.messageRepo = messageRepo;
        this.userRepo    = userRepo;
    }


    @Transactional
    public ChatMessage sendMessage(Long fromId, Long toId, String text) {
        UserEntity sender   = userRepo.findById(fromId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        UserEntity receiver = userRepo.findById(toId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        MessageEntity msg = new MessageEntity();
        msg.setSender(sender);
        msg.setReceiver(receiver);
        msg.setContent(text);
        messageRepo.save(msg);

        return toDto(msg);
    }


    public List<ChatMessage> receiveMessages(Long meId, Long otherId) {
        return messageRepo.findChatHistory(meId, otherId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private ChatMessage toDto(MessageEntity m) {
        return new ChatMessage(
                m.getId(),
                m.getSender().getId(),
                m.getReceiver().getId(),
                m.getContent(),
                m.getSentAt()
        );
    }
}