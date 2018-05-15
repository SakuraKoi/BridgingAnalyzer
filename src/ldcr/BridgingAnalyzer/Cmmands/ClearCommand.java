package ldcr.BridgingAnalyzer.Cmmands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ldcr.BridgingAnalyzer.BridgingAnalyzer;
import ldcr.BridgingAnalyzer.Counter;

public class ClearCommand implements CommandExecutor {

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
		if (sender.isOp()) {
			if (args.length == 0) {
				sender.sendMessage("§b[BridgingAnalyzer] §c正在清除所有已放置方块....");
				for (final Counter c : BridgingAnalyzer.counter.values()) {
					c.instantBreakBlock();
				}
				for (final Block b : Counter.scheduledBreakBlocks) {
					b.setType(Material.AIR);
				}
				sender.sendMessage("§b[BridgingAnalyzer] §a方块清除完毕.");
			} else {
				final String player = args[0];
				@SuppressWarnings("deprecation")
				final OfflinePlayer offp = Bukkit.getOfflinePlayer(player);
				if (offp == null) {
					sender.sendMessage("§b[BridgingAnalyzer] §c错误: 玩家 " + player + " 不存在.");
					return true;
				}
				if (!offp.isOnline()) {
					sender.sendMessage("§b[BridgingAnalyzer] §c错误: 玩家 " + offp.getName() + " 不在线.");
					return true;
				}
				final Player p = offp.getPlayer();
				BridgingAnalyzer.getCounter(p).instantBreakBlock();
				sender.sendMessage("§b[BridgingAnalyzer] §a已清除玩家 " + p.getName() + " 放置的方块.");
			}
		}
		return true;
	}

}
