package com.cdjmdev.regex.key;

import io.github.cdimascio.dotenv.Dotenv;

public class OpenAIKey implements ServiceKey {

	@Override
	public String getAPIKey() {
		return System.getenv().get("OPENAI_KEY");
	}
}
