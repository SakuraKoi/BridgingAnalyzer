package ldcr.BridgeingAnalyzer.Cmmands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ldcr.BridgeingAnalyzer.BridgingAnalyzer;
import ldcr.BridgeingAnalyzer.Counter;
import ldcr.BridgeingAnalyzer.Utils.SendMessageUtils;

public class BridgeCommand implements CommandExecutor {

	@Override
	public boolean onCommand(final CommandSender sender, final Command command,
			final String label, final String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("§6§l[BridgingAnalyzer] §c此命令用于配置插件参数, 仅玩家可以执行.");
			return true;
		}
		if (args.length != 2) {
			SendMessageUtils
					.sendMessage(
							sender,
							"§6§l[BridgingAnalyzer] §b骚操作练习插件 §lBy.Ldcr",
							"§6§l[BridgingAnalyzer] §e/bridge highlight <true/false>   §a启用/禁用侧搭辅助指示",
							"§6§l[BridgingAnalyzer] §e/bridge damage   <true/false>   §a启用/禁用伤害屏蔽",
							"§6§l[BridgingAnalyzer] §e/bridge speed    <true/false>   §a启用/禁用搭路速度统计",
							"§6§l[BridgingAnalyzer] §d所配置的参数仅对您有效, 其他玩家不受影响.");

			return true;
		}
		final Counter counter = BridgingAnalyzer.getCounter((Player) sender);
		switch (args[0].toLowerCase()) {
		case "highlight": {
			counter.setHighlightEnabled(toBoolean(args[1]));
			sender.sendMessage("§6§l[BridgingAnalyzer] §a侧搭辅助指示已 "
					+ (counter.isHighlightEnabled() ? "开启" : "关闭"));
			break;
		}
		case "damage": {
			counter.setDamageEnabled(toBoolean(args[1]));
			sender.sendMessage("§6§l[BridgingAnalyzer] §a伤害已 "
					+ (counter.isDamageEnabled() ? "开启" : "关闭"));
			break;
		}
		case "speed": {
			counter.setSpeedCountEnabled(toBoolean(args[1]));
			sender.sendMessage("§6§l[BridgingAnalyzer] §a搭路速度统计已 "
					+ (counter.isSpeedCountEnabled() ? "开启" : "关闭"));
			break;
		}
		}
		return true;
	}

	private boolean toBoolean(final String enable) {
		return enable.equalsIgnoreCase("true");
	}

}
