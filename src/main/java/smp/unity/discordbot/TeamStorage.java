package smp.unity.discordbot;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import net.dv8tion.jda.api.entities.User;

import java.io.*;
import java.util.*;

public class TeamStorage {

    private static final String filePath = "resources/assets/unity-bot/teams.json";
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();

    private static Map<String, List<String>> storageTeams = new LinkedHashMap<>();
	
	public static void save() {
		for(Team team : Team.teams) {
			List<User> teamMembers = team.getTeamMembers();
			List<String> memberIDs = new ArrayList<>();
			for(User member : teamMembers) {
				memberIDs.add(member.getId());
			}
			memberIDs.addFirst(team.getTeamLeader().getId());
			storageTeams.put(team.name, memberIDs);
		}
		
		try(OutputStream out = new FileOutputStream(filePath)) {
			writer.writeValue(out, storageTeams);
		} catch (FileNotFoundException e) {
			System.out.println("Team file not found");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Failed to save teams to file");
			e.printStackTrace();
		}
	}
	
	public static void load() {
		try {
			storageTeams = mapper.readValue(new File(filePath),
					new TypeReference<>() {}
			);
		} catch (IOException e) {
			System.out.println("Failed to load teams from file");
		}
		for(String teamName : storageTeams.keySet()) {
		
		}
	}
}
