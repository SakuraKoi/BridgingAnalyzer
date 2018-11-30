package ldcr.BridgingAnalyzer.Utils;

import org.bukkit.command.CommandSender;

public class SendMessageUtils {
	public static void sendMessage(final CommandSender sender, final String... message) {
		sender.sendMessage(message);
	}
}
