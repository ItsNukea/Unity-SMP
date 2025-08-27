package smp.unity;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import smp.unity.discordbot.UnityBot;

public class Unity implements ModInitializer {
	public static final String MOD_ID = "unity";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		UnityBot.start();
	}
}