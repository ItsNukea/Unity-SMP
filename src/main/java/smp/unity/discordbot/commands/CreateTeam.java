package smp.unity.discordbot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import smp.unity.discordbot.Team;

import java.util.EnumSet;
import java.util.Objects;

public class CreateTeam {
	private static String roleid;
	public static void execute(SlashCommandInteractionEvent event) {
		//Check if a team is illegal by the following arguments:
		//	Team name already exists
		//	Team name contains illegal characters (only a-z, A-Z, 0-9, _, /, #, &, (), )
		//	The command executor is already in a team
		//	The team name is empty or has under 3 characters
		String teamName = Objects.requireNonNull(event.getOption("name")).getAsString();
		User teamLeader = event.getUser();
		Team team;
		
		for(Team t : Team.teams) {
			if(t.name.equals(teamName)) {
				event.reply("A team with that name already exists!")
						.setEphemeral(true)
						.queue();
				return;
			}
		}
		if(teamName.trim().isEmpty()) {
			event.reply("Team names cannot only consist of spaces!")
					.setEphemeral(true)
					.queue();
			return;
		}
		if(!teamName.matches("^[a-zA-Z0-9_/#&()\\-]+$")) {
			event.reply("Team name contains illegal characters! Allowed characters are: a-z, A-Z, 0-9, _, /, #, &, ()")
					.setEphemeral(true)
					.queue();
			return;
		}
		if(Team.isUserInTeam(event.getUser())) {
			event.reply("You are already in a team!")
					.setEphemeral(true)
					.queue();
			return;
		}
		if(teamName.length() <= 2) {
			event.reply("Team names must be at least 3 characters long!")
					.setEphemeral(true)
					.queue();
			return;
		}
		if (Team.isUserInTeam(teamLeader)) {
			event.reply("You are already in a team!").queue();
			return;
		}
		
		//Passed failsafe: Now the team can be created
		//Checklist to make a new team:
		//	Create a new team role with the name of the team that every member on the team gets
		//	Give the team leader the team leader role
		
		team = new Team(teamLeader, teamName);
		Role teamRole = createTeamRole(event, team);
		addTeamTextChannel(event, team, teamRole);
		
		Guild guild = Objects.requireNonNull(event.getGuild());
		guild.addRoleToMember(teamLeader, teamRole).queue();
		guild.addRoleToMember(teamLeader, guild.getRoleById("1405800602159812719")).queue(); //Team Leader role
		
		event.reply("Created team: " + team.name).queue();
	}
	
	private static Role createTeamRole(SlashCommandInteractionEvent event, Team team) {
		Guild guild = Objects.requireNonNull(event.getGuild());
		guild.createRole().setName(team.name).queue(
				role -> {
					roleid = role.getId();
				},
				error -> {
					event.reply("Failed to create team role! Try again")
							.setEphemeral(true)
							.queue();
				}
		);
		while(roleid == null) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			
			}
		}
		return guild.getRoleById(roleid);
	}
	
	private static void addTeamTextChannel(SlashCommandInteractionEvent event, Team team, Role role) {
		Guild guild = event.getGuild();
		
		Objects.requireNonNull(guild).createTextChannel(team.name)
				.setParent(guild.getCategoryById("1404463114363338956"))
				.addPermissionOverride(
						guild.getPublicRole(),
						null,
						EnumSet.of(Permission.VIEW_CHANNEL)
				)
				.addPermissionOverride(
						role,
						EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND),
						null
				)
				.queue();
		Objects.requireNonNull(guild.getCategoryById("1404463114363338956")).getTextChannels().forEach(channel -> {
			if(channel.getName().equals(team.name)) {
				team.setTeamTextChannelID(channel.getId());
			}
		});
	}
}
