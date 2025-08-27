package smp.unity.discordbot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import smp.unity.discordbot.Team;

public class Joinrequest {
	public static void execute(SlashCommandInteractionEvent event) {
		//Check if the user is eligible to join a team
		//Conditions:
		//	The user is not already in a team
		//	The team that the user wants to join exists

		if(Team.isUserInTeam(event.getUser())) {
			event.reply("You are already in a team!")
					.setEphemeral(true)
					.queue();
			return;
		}
	}
}
