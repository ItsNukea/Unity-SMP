package smp.unity.discordbot.buttons;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public class DenyInviteButton {
	public static final String ID = "btn_deny";
	
	public static void execute(ButtonInteractionEvent event) {
		event.getInteraction()
				.reply("Request Denied")
				.queue();
		event.getUser()
				.openPrivateChannel()
				.flatMap(
						channel -> channel.sendMessage(
								"Your team invite has been denied"
						))
				.queue();
	}
}
