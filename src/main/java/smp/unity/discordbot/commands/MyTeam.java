package smp.unity.discordbot.commands;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import smp.unity.discordbot.Team;

public class MyTeam {
	public static void execute(SlashCommandInteractionEvent event) {
		//Check if the user is in a team, if not: return
		User user = event.getUser();
		if(!Team.isUserInTeam(user)) {
			event.reply("You are not in a team!")
					.setEphemeral(true)
					.queue();
			return;
		}
		
		//Find the team the user is in
		Team userTeam = null;
		for(Team t : Team.teams) {
			for(int i = 0; i < t.getEntireTeam().size(); i++) {
				if(t.getTeamLeader().getId().equals(user.getId())
				|| t.getTeamMembers().get(i).getId().equals(user.getId())) {
					userTeam = t;
				}
			}
		}
		//Null check (userTeam should not be null)
		if(userTeam == null) {
			return;
		}
		
		//Format the team info
		StringBuilder msgcontent = new StringBuilder("Your team: " + userTeam.name + "\n");
		msgcontent.append("Team Leader: ").append(userTeam.getTeamLeader().getGlobalName()).append("\n");
		msgcontent.append("Team Members:\n");
		for(User u : userTeam.getTeamMembers()) {
			msgcontent.append(u.getGlobalName()).append("\n");
		}
		String msg = msgcontent.toString();
		event.reply(msg).queue();
	}
}
