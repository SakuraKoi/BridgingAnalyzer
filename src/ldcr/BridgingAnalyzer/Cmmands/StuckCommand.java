package ldcr.BridgingAnalyzer.Cmmands;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ldcr.BridgingAnalyzer.BridgingAnalyzer;

public class StuckCommand implements CommandExecutor {

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("§6§l[BridgingAnalyzer] §c仅玩家可以执行.");
			return true;
		}
		final Player p = (Player) sender;
		// break near sandstone
		if (p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.EMERALD_BLOCK) {
			for (int x = -3; x < 3; x++) {
				for (int y = -3; y < 3; y++) {
					for (int z = -3; z < 3; z++) {
						final Block b = p.getLocation().add(x, y, z).getBlock();
						if (BridgingAnalyzer.isPlacedByPlayer(b)) {
							b.setType(Material.AIR);
						}
					}
				}
			}
			sender.sendMessage("§6§l[BridgingAnalyzer] §a你周围的方块已被清除.");
		} else {
			if (p.getInventory().contains(Material.GOLD_PICKAXE)) {
				sender.sendMessage("§6§l[BridgingAnalyzer] §a你背包里有稿子, 自己挖开=-=");
			}
			final ItemStack pickaxe = new ItemStack(Material.GOLD_PICKAXE, 1);
			pickaxe.setDurability((short) 27);
			p.getInventory().addItem(pickaxe);
			sender.sendMessage("§6§l[BridgingAnalyzer] §a你似乎不在出生点, 给你一把稿子, 被卡住请自行挖开");
		}
		return true;
	}

}
