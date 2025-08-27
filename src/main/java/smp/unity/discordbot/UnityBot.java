package smp.unity.discordbot;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import smp.unity.discordbot.buttons.AcceptInviteButton;
import smp.unity.discordbot.buttons.AcceptJoinRequestButton;
import smp.unity.discordbot.buttons.DenyInviteButton;
import smp.unity.discordbot.buttons.DenyJoinRequestButton;
import smp.unity.discordbot.commands.*;

public class UnityBot extends ListenerAdapter {
	public static void start() {
		JDABuilder.createDefault(BotTokenProvider.getToken())
				.addEventListeners(new UnityBot())
				.build()
				.updateCommands()
				.addCommands(
						Commands.slash("createteam", "Creates a new team")
						.addOption(OptionType.STRING, "name", "The name of the team", true)
				)
				.addCommands(
						Commands.slash("joinrequest", "Joins a team")
								.addOption(OptionType.STRING, "teamname", "The team to join", true)
				)
				.addCommands(
						Commands.slash("invite", "Invite someone to be on your team")
								.addOption(OptionType.USER, "user", "The user to request", true)
				)
				.addCommands(
						Commands.slash("myteam", "Shows everyone in a team")
								.addOption(OptionType.STRING, "name", "Shows the members of the given team. If left empty, it shows your own team",false)
				)
				.addCommands(
						Commands.slash("leave", "Leave your team. If you are the team leader, a random new leader is selected!")
				)
				.addCommands(
						Commands.slash("promote", "Promote someone on your team to be a Team Leader (Needs to be a team leader to do this)")
								.addOption(OptionType.USER, "user", "The user to promote", true)
				)
				.addCommands(
						Commands.slash("kick", "Kick someone from your team (Need to be a team leader to do this)")
								.addOption(OptionType.USER, "user", "The user to kick", true)
				)
				.addCommands(
						Commands.slash("linktomc", "Link your Discord to your Minecraft account")
								.addOption(OptionType.STRING, "mcname", "The Minecraft username", true)
				)
				.queue();
		Runtime.getRuntime().addShutdownHook(new Thread(Team::saveOnShutDown));
	}
	
	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		if(!event.getChannel().getId().equals("1404494163319459900")){
			return;
		}
		switch(event.getName()) {
			case "createteam" -> CreateTeam.execute(event); //finished
			case "joinrequest" -> Joinrequest.execute(event); //finish later
			case "invite" -> TeamInvite.execute(event); //TODO: /invite
			case "myteam" -> MyTeam.execute(event); //TODO: add the option from /myteam
			case "leave" -> Leave.execute(event); //finished
			case "promote" -> Promote.execute(event); //TODO: /promote
			case "kick" -> Kick.execute(event); //TODO: /kick
			case "linktomc" -> Link.execute(event); //TODO: /linktomc
		}
	}
	
	@Override
	public void onButtonInteraction(ButtonInteractionEvent event) {
		switch(event.getComponentId()) {
			case AcceptInviteButton.ID -> AcceptInviteButton.execute(event);
			case DenyInviteButton.ID -> DenyInviteButton.execute(event);
			case AcceptJoinRequestButton.ID -> AcceptJoinRequestButton.execute(event);
			case DenyJoinRequestButton.ID -> DenyJoinRequestButton.execute(event);
		}
	}
}
