package smp.unity.discordbot.commands;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import smp.unity.discordbot.Team;
import smp.unity.discordbot.buttons.AcceptInviteButton;
import smp.unity.discordbot.buttons.DenyInviteButton;

import java.util.Objects;

public class TeamInvite {
	public static void execute(SlashCommandInteractionEvent event) {
		//Check if all requirements are met:
		//	The requester is in a team
		//  The target user is not in a team
		//  The target user is not the requester
		
		User requestUser = event.getUser();
		User targetUser = Objects.requireNonNull(event.getOption("user")).getAsUser();
		
		if(Team.isUserInTeam(targetUser)) {
			event.reply(targetUser.getGlobalName() + " is already on a team!").setEphemeral(true).queue();
			return;
		}
		if(!Team.isUserInTeam(requestUser)) {
			event.reply("You are not in a team!").setEphemeral(true).queue();
		}
		
		Button acceptButton = Button.primary(AcceptInviteButton.ID, "Accept");
		Button denyButton = Button.danger(DenyInviteButton.ID, "Deny");
		
		targetUser.openPrivateChannel()
				.flatMap(
						channel -> channel.sendMessage(
										requestUser.getGlobalName() + " wants you on their team!"
								)
								.setActionRow(
										acceptButton,
										denyButton
								)
				)
				.queue();
		event.reply("Sent invite to " + targetUser.getAsMention()).queue();
	}
}
