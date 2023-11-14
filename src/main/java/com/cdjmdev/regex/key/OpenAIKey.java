package com.cdjmdev.regex.key;

public class OpenAIKey implements ServiceKey {

	@Override
	public String getAPIKey() {
		return System.getenv().get("OPENAI_KEY");
	}
}
