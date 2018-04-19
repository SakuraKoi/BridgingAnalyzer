package ldcr.BridgeingAnalyzer.Cmmands;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ldcr.BridgeingAnalyzer.Counter;

public class StuckCommand implements CommandExecutor {

	@Override
	public boolean onCommand(final CommandSender sender, final Command command,
			final String label, final String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("§6§l[BridgingAnalyzer] §c仅玩家可以执行.");
			return true;
		}
		final Player p = (Player) sender;
		if (p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.EMERALD_BLOCK) {
			// break near sandstone
			for (int x = -3; x < 3; x++) {
				for (int y = -3; y < 3; y++) {
					for (int z = -3; z < 3; z++) {
						final Block b = p.getLocation().add(x, y, z).getBlock();
						if (Counter.isPlacedByPlayer(b)) {
							b.setType(Material.AIR);
						}
					}
				}
			}
			sender.sendMessage("§6§l[BridgingAnalyzer] §a你周围的方块已被清除.");
			return true;
		} else {
			p.sendMessage("§6§l[BridgingAnalyzer] §c你似乎不在出生点. 给你一把稿子, 如果被方块卡住请手动挖开.");
			p.getInventory().addItem(new ItemStack(Material.IRON_PICKAXE, 1));
			return true;
		}
	}

}
