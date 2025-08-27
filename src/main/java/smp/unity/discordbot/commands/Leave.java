package smp.unity.discordbot.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import smp.unity.discordbot.Team;

import java.util.Objects;
import java.util.Random;

public class Leave {
	public static void execute(SlashCommandInteractionEvent event) {
		//Conditions to be able to leave a team:
		//	The user is in a team
		//Actions to do to leave:
		//	Remove team role
		//	Send a message in the team text channel
		//	Send a reply to the command usage
		//	If the user is the team leader, change the team leader to someone random
		
		User requestUser = event.getUser();
		
		if(!Team.isUserInTeam(requestUser)) {
			event.reply("You are not in a team!")
					.setEphemeral(true)
					.queue();
			return;
		}
		
		Team userTeam = Objects.requireNonNull(Team.getTeamByMember(requestUser));
		
		userTeam.removeMember(requestUser);
		event.reply("You left the team").queue();
		
		Guild guild = Objects.requireNonNull(event.getGuild());
		
		guild.removeRoleFromMember(requestUser, guild.getRolesByName(userTeam.name, true).getFirst()).queue();
		
		if(userTeam.getTeamLeader().getId().equals(requestUser.getId())) {
			guild.removeRoleFromMember(requestUser, guild.getRolesByName("Team Leader", true).getFirst()).queue();
		}
		
		userTeam.sendTextMessage(requestUser.getGlobalName() + " left the team", event);
		
		if(requestUser.getId().equals(userTeam.getTeamLeader().getId())) {
			Random random = new Random();
			User newLeader = userTeam.getTeamMembers().get(random.nextInt(userTeam.getTeamMembers().size()));
			userTeam.setTeamLeader(newLeader, event);
			userTeam.sendTextMessage("The new team leader is " + newLeader.getGlobalName(), event);
		}
		
		if(userTeam.getEntireTeam().isEmpty()) {
			Team.teams.remove(userTeam);
			
		}
	}
}
