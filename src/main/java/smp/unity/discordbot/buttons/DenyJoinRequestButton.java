package smp.unity.discordbot.buttons;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public class DenyJoinRequestButton {
	public static final String ID = "btn_join_deny";
	
	public static void execute(ButtonInteractionEvent event) {
		event.getInteraction()
				.reply("Denied Join Request")
				.queue();
		event.getUser()
				.openPrivateChannel()
				.flatMap(
						channel -> channel.sendMessage(
								"Your team invite to " + "" + " has been denied"
						))
				.queue();
	}
}
