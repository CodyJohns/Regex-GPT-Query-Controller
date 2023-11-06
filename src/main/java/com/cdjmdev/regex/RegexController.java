package com.cdjmdev.regex;

import com.cdjmdev.regex.chatservice.ChatGPTService;
import com.cdjmdev.regex.chatservice.ChatService;
import com.cdjmdev.regex.key.OpenAIKey;
import com.cdjmdev.regex.prompt.ChatGPTPrompt;
import com.cdjmdev.regex.prompt.Prompt;
import com.google.gson.Gson;
import java.util.Map;

public class RegexController {

    private Gson gson;
    private ChatService service;

    public RegexController() {
        gson = new Gson();
        Prompt prompt = new ChatGPTPrompt("Respond with only a syntactically correct regex for the following request. ");
        service = new ChatGPTService(new OpenAIKey(), prompt);
    }

    public String handleRequest(String input) {

        String response = service.getResponse(input);

        return gson.toJson(Map.of("data", response));
    }

}