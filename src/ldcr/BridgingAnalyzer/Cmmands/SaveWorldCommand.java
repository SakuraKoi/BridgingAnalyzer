package ldcr.BridgingAnalyzer.Cmmands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ldcr.BridgingAnalyzer.BridgingAnalyzer;
import ldcr.BridgingAnalyzer.Counter;

public class SaveWorldCommand implements CommandExecutor {

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
		if (sender.hasPermission("bridginganalyzer.admin")) {
			sender.sendMessage("§b[BridgingAnalyzer] §c正在保存世界....");
			for (final Counter c : BridgingAnalyzer.getCounters().values()) {
				c.instantBreakBlock();
			}
			for (final Block b : Counter.scheduledBreakBlocks) {
				b.setType(Material.AIR);
			}
			for (final World world : Bukkit.getWorlds()) {
				world.save();
			}
			sender.sendMessage("§b[BridgingAnalyzer] §a地图保存完毕.");
		}
		return true;
	}

}
