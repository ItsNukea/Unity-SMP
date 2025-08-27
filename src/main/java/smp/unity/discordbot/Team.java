package smp.unity.discordbot;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.ArrayList;
import java.util.Objects;

public class Team {
	private User teamLeader;
	public String name;
	private ArrayList<User> teamMembers;
	private String teamTextChannelID;
	public static ArrayList<Team> teams = new ArrayList<>();
	
	public Team(User teamLeader, String teamName) {
		this.teamLeader = teamLeader;
		this.name = teamName;
		this.teamMembers = new ArrayList<>();
		teams.add(this);
	}
	
	public void addMember(User user) {
		this.teamMembers.add(user);
	}
	
	public void removeMember(User user) {
		this.teamMembers.remove(user);
	}
	
	public ArrayList<User> getTeamMembers() {
		return this.teamMembers;
	}
	
	public User getTeamLeader() {
		return this.teamLeader;
	}
	
	public void setTeamLeader(User newTeamLeader, SlashCommandInteractionEvent event) {
		//Actions to do:
		// Null checks
		// If the user is not in the team leaders team, return
		// If the user is not in a team, return
		// Remove the old team leader role
		// Add the new team leader role
		// Send a message in the team text channel
		// Send a reply to the command usage
		
		
		if(getTeamByMember(newTeamLeader) != getTeamByMember(event.getUser())) {
			event.reply("This user is not in your team!").setEphemeral(true).queue();
			return;
		}
		
		Team userTeam = getTeamByLeader(newTeamLeader);
		User oldTeamLeader = Objects.requireNonNull(userTeam).teamLeader;
		Guild guild = Objects.requireNonNull(event.getGuild());
		guild.removeRoleFromMember(oldTeamLeader, guild.getRolesByName("Team Leader", true).getFirst()).queue();
		guild.addRoleToMember(newTeamLeader, guild.getRolesByName("Team Leader", true).getFirst()).queue();
	}
	
	public static boolean isUserInTeam(User user) {
		for(Team team : teams) {
			if(team.teamMembers.contains(user) || team.teamLeader.getId().equals(user.getId())) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean exists(String teamName) {
		if(teamName == null) return false;
		final String needle = teamName.trim();
		return teams.stream().anyMatch(team -> team.name.equals(needle));
	}
	
	public static Team getTeamByName(String teamName) {
		if(teamName == null) return null;
		final String needle = teamName.trim();
		return teams.stream().filter(team -> team.name.equals(needle)).findFirst().orElse(null);
	}
	
	public static Team getTeamByLeader(User teamLeader) {
		for(Team team : teams) {
			if(team.teamLeader.getId().equals(teamLeader.getId())) {
				return team;
			}
		}
		return null;
	}
	
	public static Team getTeamByMember(User member) {
		for(Team team : teams) {
			if(team.teamMembers.contains(member)) {
				return team;
			}
		}
		return null;
	}
	
	public int getTeamSize() {
		return this.teamMembers.size() + 1;
	}
	
	public ArrayList<User> getEntireTeam() {
		ArrayList<User> entireTeam = new ArrayList<>(this.teamMembers);
		entireTeam.add(this.teamLeader);
		return entireTeam;
	}
	
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Team team = (Team) o;
		return this.name.equals(team.name) && this.teamLeader.getId().equals(team.teamLeader.getId());
	}
	
	public static void saveOnShutDown() {
		TeamStorage.save();
	}
	
	private static void readTeams() {
		TeamStorage.load();
	}
	
	public void setTeamTextChannelID(String channelID) {
		this.teamTextChannelID = channelID;
	}
	
	public String getTeamTextChannelID() {
		return this.teamTextChannelID;
	}
	
	public void sendTextMessage(String message, SlashCommandInteractionEvent event) {
		Guild guild = Objects.requireNonNull(event.getGuild());
		
		Objects.requireNonNull(
				guild.getTextChannelById(
						this.getTeamTextChannelID()))
				.sendMessage(message)
				.queue();
	}
}
