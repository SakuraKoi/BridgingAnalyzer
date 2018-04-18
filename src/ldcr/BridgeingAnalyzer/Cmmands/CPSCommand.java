package ldcr.BridgeingAnalyzer.Cmmands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ldcr.BridgeingAnalyzer.BridgingAnalyzer;
import ldcr.BridgeingAnalyzer.Counter;

public class CPSCommand implements CommandExecutor {

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
	if (args.length==0) {
	    sender.sendMessage("§b[BridgingAnalyzer] §e/cps <玩家>   查看其他玩家的CPS值");
	} else {
	    final String player = args[0];
	    final OfflinePlayer offp = Bukkit.getOfflinePlayer(player);
	    if (offp==null) {
		sender.sendMessage("§b[BridgingAnalyzer] §c错误: 玩家 "+player+" 不存在.");
		return true;
	    }
	    if (!offp.isOnline()) {
		sender.sendMessage("§b[BridgingAnalyzer] §c错误: 玩家 "+offp.getName()+" 不在线.");
		return true;
	    }
	    final Player p = offp.getPlayer();
	    final Counter c = BridgingAnalyzer.getCounter(p);
	    sender.sendMessage("§b[BridgingAnalyzer] §a玩家 "+p.getName()+" 当前CPS ["+c.getCPS()+"] 最大CPS ["+c.getMaxCPS()+"].");
	}
	return true;
    }

}
