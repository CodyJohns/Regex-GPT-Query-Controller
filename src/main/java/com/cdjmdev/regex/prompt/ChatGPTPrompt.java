package com.cdjmdev.regex.prompt;

public class ChatGPTPrompt implements Prompt {

	private String prompt;

	public ChatGPTPrompt(String prompt) {
		this.prompt = prompt;
	}
	
	@Override
	public String getPrompt() {
		return prompt;
	}
}
