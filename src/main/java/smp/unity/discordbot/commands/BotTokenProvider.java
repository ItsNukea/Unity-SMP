package smp.unity.discordbot.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import smp.unity.Unity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class BotTokenProvider {
	private static final String BOT_TOKEN;
	private static final String PATH = System.getProperty("user.home") + "/CodeProjects/Java/Private Data/bot_token.json";
	
	static {
		ObjectMapper mapper = new ObjectMapper();
		try {
			TokenConfig tc = mapper.readValue(new File(PATH), TokenConfig.class);
			BOT_TOKEN = tc.token();
		} catch(FileNotFoundException e) {
			Unity.LOGGER.error("Bot token file not found");
			throw new RuntimeException(e);
		} catch(IOException e) {
			Unity.LOGGER.error("Failed to read bot token from file");
			Unity.LOGGER.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}
	
	public BotTokenProvider() {
	
	}
	
	private record TokenConfig(@JsonProperty("token") String token) { }
	
	public static String getToken() {
		return BOT_TOKEN;
	}
}
