package com.spring.chatserver.service.ServiceImpl;

import com.spring.chatserver.dto.MessageSummaryDto;
import com.spring.chatserver.dto.MessagesDto;
import com.spring.chatserver.dto.UpdateMessageStatusDto;
import com.spring.chatserver.dto.UserDto;
import com.spring.chatserver.model.Messages;
import com.spring.chatserver.model.User;
import com.spring.chatserver.repository.MessagesRepository;
import com.spring.chatserver.service.MessageService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessagesServiceImpl implements MessageService {

    private final MessagesRepository messagesRepository;
    private final SimpMessagingTemplate messagingTemplate;


    public MessagesServiceImpl(MessagesRepository messagesRepository, SimpMessagingTemplate messagingTemplate) {
        this.messagesRepository = messagesRepository;
        this.messagingTemplate = messagingTemplate;
    }


   @Override
    public List<MessageSummaryDto> getUserMessages(Long userId, Integer pageNumber, Integer pageSize) {
        Pageable paginationObject = PageRequest.of(pageNumber, pageSize);

        List<Object[]> messages = messagesRepository.getUserDistinctMessages(userId, paginationObject).getContent();
        return messages.stream()
                .map(messageSummaryObject -> new MessageSummaryDto(userId, Long.valueOf(messageSummaryObject[0].toString()), Long.valueOf(messageSummaryObject[1].toString()), messageSummaryObject[2].toString(), messageSummaryObject[3].toString(), messageSummaryObject[4].toString(), Long.valueOf(messageSummaryObject[5].toString())))
                .toList();
    }

    @Override
    public List<MessagesDto> getUserMessagesWithUser(Long loggedInUserId, Long chatRecipientId, Integer pageNumber, Integer pageSize) {
        Pageable paginationObject = PageRequest.of(pageNumber, pageSize);

        List<Messages> messages = messagesRepository.getUserMessagesWithUser(loggedInUserId, chatRecipientId, paginationObject);

        return messages.stream().map(message -> MessagesDto.builder()
                .sentBy(message.getSentBy().getId())
                .message(message.getMessage())
                .messageId(message.getId())
                .status(message.getStatus())
                .sentTo(message.getSentTo().getId())
                .build()).toList();
    }

    @Override
    public MessagesDto postMessage(UserDto loggedInUser, MessagesDto messagesDto) {

        Messages savedMessage = messagesRepository.save(
                Messages.builder()
                        .sentBy(new User(loggedInUser.getUserId()))
                        .sentTo(new User(messagesDto.getSentTo()))
                        .message(messagesDto.getMessage())
                        .status("U")
                        .build());


        MessagesDto savedMessagesDto = MessagesDto.builder()
                .message(savedMessage.getMessage())
                .sentTo(savedMessage.getSentTo().getId())
                .sentBy(savedMessage.getSentBy().getId())
                .messageId(savedMessage.getId())
                .status(savedMessage.getStatus())
                .build();

        MessageSummaryDto dto = new MessageSummaryDto(savedMessage.getSentTo().getId(),
                loggedInUser.getUserId(),
                savedMessagesDto.getMessageId(),
                loggedInUser.getName(),
                savedMessage.getMessage(),
                savedMessagesDto.getStatus(),
                loggedInUser.getUserId()
        );

        messagingTemplate.convertAndSendToUser(String.valueOf(savedMessagesDto.getSentTo()), "/reply", dto);

        return savedMessagesDto;
    }

    @Override
    @Transactional
    public void markMessagesReadForUsers(Long loggedInUserId, UpdateMessageStatusDto updateMessageStatusDto) {
        Long chatRecipientId = updateMessageStatusDto.getChatRecipientId();
        messagesRepository.markMessagesReadForUsers(loggedInUserId, chatRecipientId);
    }
}
