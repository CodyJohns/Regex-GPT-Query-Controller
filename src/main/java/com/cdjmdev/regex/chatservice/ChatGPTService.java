package com.cdjmdev.regex.chatservice;

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

	public ChatGPTService(ServiceKey key, Prompt prompt) {
		this.service = new OpenAiService(key.getAPIKey());
		this.prompt = prompt;
	}
	@Override
	public String getResponse(String input) {

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
