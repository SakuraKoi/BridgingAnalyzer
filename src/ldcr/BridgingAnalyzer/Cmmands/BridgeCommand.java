package ldcr.BridgingAnalyzer.Cmmands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ldcr.BridgingAnalyzer.BridgingAnalyzer;
import ldcr.BridgingAnalyzer.Counter;
import ldcr.BridgingAnalyzer.Utils.SendMessageUtils;

public class BridgeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(final CommandSender sender, final Command command,
	    final String label, final String[] args) {
	if (!(sender instanceof Player)) {
	    sender.sendMessage("§6§l[BridgingAnalyzer] §c此命令用于配置插件参数, 仅玩家可以执行.");
	    return true;
	}
	if (args.length != 1) {
	    SendMessageUtils
	    .sendMessage(
		    sender,
		    "§6§l[BridgingAnalyzer] §b骚操作练习插件 §lBy.Ldcr",
		    "§6§l[BridgingAnalyzer] §e/bridge highlight    §a启用/禁用侧搭辅助指示",
		    "§6§l[BridgingAnalyzer] §e/bridge pvp         §a启用/禁用伤害屏蔽",
		    "§6§l[BridgingAnalyzer] §e/bridge speed       §a启用/禁用搭路速度统计",
		    "§6§l[BridgingAnalyzer] §e/bridge stand       §a启用/禁用走搭位置指示",
		    "§6§l[BridgingAnalyzer] §d所配置的参数仅对您有效, 其他玩家不受影响.");

	    return true;
	}
	final Counter counter = BridgingAnalyzer.getCounter((Player) sender);
	switch (args[0].toLowerCase()) {
	case "highlight": {
	    counter.setHighlightEnabled(!counter.isHighlightEnabled());
	    sender.sendMessage("§6§l[BridgingAnalyzer] §a侧搭辅助指示已"
		    + (counter.isHighlightEnabled() ? "开启" : "关闭"));
	    break;
	}
	case "pvp": {
	    counter.setPvPEnabled(!counter.isPvPEnabled());
	    sender.sendMessage("§6§l[BridgingAnalyzer] §aPvP已"
		    + (counter.isPvPEnabled() ? "开启" : "关闭"));
	    break;
	}
	case "speed": {
	    counter.setSpeedCountEnabled(!counter.isSpeedCountEnabled());
	    sender.sendMessage("§6§l[BridgingAnalyzer] §a搭路速度统计已"
		    + (counter.isSpeedCountEnabled() ? "开启" : "关闭"));
	    break;
	}
	case "stand": {
	    counter.setStandBridgeHighlightEnabled(!counter.isStandBridgeHighlightEnabled());
	    sender.sendMessage("§6§l[BridgingAnalyzer] §a走搭位置指示已"
		    + (counter.isStandBridgeHighlightEnabled() ? "开启" : "关闭"));
	    break;
	}
	}
	return true;
    }
}
