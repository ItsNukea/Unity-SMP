package smp.unity.discordbot.buttons;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public class AcceptInviteButton {
	public static final String ID = "btn_accept";
	
	public static void execute(ButtonInteractionEvent event) {
		event.getInteraction().reply("Team Invite Accepted").queue();
		event.getUser()
				.openPrivateChannel()
				.flatMap(
						channel -> channel.sendMessage(
								"Your team invite has been accepted"
						))
				.queue();
	}
}
