package com.cdjmdev.regex.chatservice;

import com.cdjmdev.oracle.model.Tiers;
import com.cdjmdev.oracle.model.User;
import com.cdjmdev.regex.exception.ChatMessageTooLargeException;
import com.cdjmdev.regex.key.ServiceKey;
import com.cdjmdev.regex.prompt.ChatGPTPrompt;
import com.cdjmdev.regex.prompt.Prompt;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;

import java.util.List;

public class ChatGPTService implements ChatService {

	private OpenAiService service;
	private Prompt prompt;
	private final String MODEL = "gpt-4-1106-preview";
	private final String SYSTEM = "system";
	private final String USER = "user";
	private final int MAX_FREE_QUERY = 500;

	public ChatGPTService(ServiceKey key, Prompt prompt) {
		this.service = new OpenAiService(key.getAPIKey());
		this.prompt = prompt;
	}
	@Override
	public String getResponse(User user, String input) throws ChatMessageTooLargeException {

		if(user.tier.equals(Tiers.FREE) && input.length() > MAX_FREE_QUERY)
			throw new ChatMessageTooLargeException("Request is too large. Try using a more concise request or upgrade your account to remove restrictions.");

		ChatMessage message = new ChatMessage();
		message.setRole(SYSTEM);
		message.setContent(prompt.getPrompt());

		ChatMessage message2 = new ChatMessage();
		message2.setRole(USER);
		message2.setContent(input);

		ChatCompletionRequest request = ChatCompletionRequest.builder()
			.model(MODEL)
			.messages(List.of(message, message2))
			.temperature(0.0)
			.build();

		return service.createChatCompletion(request).getChoices().get(0).getMessage().getContent().toString();
	}
}
