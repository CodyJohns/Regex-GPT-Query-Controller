package com.cdjmdev.regex.key;

import io.github.cdimascio.dotenv.Dotenv;

public class OpenAIKey implements ServiceKey {

	@Override
	public String getAPIKey() {
		Dotenv dotenv = Dotenv.configure()
			.directory("./")
			.filename(".env")
			.load();

		return dotenv.get("OPENAI_KEY");
	}
}
