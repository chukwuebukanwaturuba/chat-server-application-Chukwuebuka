package com.spring.chatserver.service;

import com.spring.chatserver.dto.MessageSummaryDto;
import com.spring.chatserver.dto.MessagesDto;
import com.spring.chatserver.dto.UpdateMessageStatusDto;
import com.spring.chatserver.dto.UserDto;
import jakarta.transaction.Transactional;

import java.util.List;

public interface MessageService {
    List<MessageSummaryDto> getUserMessages(Long userId, Integer pageNumber, Integer pageSize);

    List<MessagesDto> getUserMessagesWithUser(Long loggedInUserId, Long chatRecipientId, Integer pageNumber, Integer pageSize);

    MessagesDto postMessage(UserDto loggedInUser, MessagesDto messagesDto);

    @Transactional
    void markMessagesReadForUsers(Long loggedInUserId, UpdateMessageStatusDto updateMessageStatusDto);
}
