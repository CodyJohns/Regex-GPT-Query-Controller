package com.cdjmdev.regex.chatservice;

import com.cdjmdev.oracle.dao.DAOFactory;
import com.cdjmdev.oracle.model.PromptHistory;
import com.cdjmdev.oracle.model.User;
import com.cdjmdev.regex.key.ServiceKey;
import com.cdjmdev.regex.prompt.Prompt;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;

import java.util.List;

public class ChatGPTService implements ChatService {

	private OpenAiService service;
	private DAOFactory factory;
	private Prompt prompt;
	//private final String MODEL = "gpt-4-1106-preview";
	private final String MODEL = "gpt-3.5-turbo";
	private final String SYSTEM = "system";
	private final String USER = "user";

	public ChatGPTService(DAOFactory factory, ServiceKey key, Prompt prompt) {
		this.factory = factory;
		this.service = new OpenAiService(key.getAPIKey());
		this.prompt = prompt;
	}
	@Override
	public String getResponse(User user, String input) {

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

		String response = service.createChatCompletion(request).getChoices().get(0).getMessage().getContent().toString();

		PromptHistory prompt = new PromptHistory(user, input, response);

		factory.getPromptHistoryDAO().createNew(prompt);

		return response;
	}
}
