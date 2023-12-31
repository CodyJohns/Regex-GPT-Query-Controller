package com.cdjmdev.regex.chatservice;

import com.cdjmdev.oracle.model.User;
import com.cdjmdev.regex.exception.ChatMessageTooLargeException;

public interface ChatService {
	String getResponse(User user, String input) throws ChatMessageTooLargeException;
}
