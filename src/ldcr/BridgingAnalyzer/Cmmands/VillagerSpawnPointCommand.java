package ldcr.BridgingAnalyzer.Cmmands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class VillagerSpawnPointCommand implements CommandExecutor {

	@Override
	public boolean onCommand(final CommandSender sender, final Command command,
			final String label, final String[] args) {
		if (!(sender instanceof Player))
			return true;
		if (!sender.isOp())
			return true;
		final Player player = (Player) sender;

		final Location loc = player.getLocation().getBlock().getLocation()
				.add(0.5, -1, 0.5);
		final ArmorStand stand = (ArmorStand) player.getWorld().spawnEntity(
				loc, EntityType.ARMOR_STAND);

		stand.setSmall(true);
		stand.setGravity(false);
		stand.setVisible(false);
		stand.setHelmet(new ItemStack(Material.REDSTONE_BLOCK, 1));
		stand.setMarker(true);
		stand.setCustomName("VillagerSpawnPoint");

		player.sendMessage("§b[BridgingAnalyzer] §a村民刷新点已设置");
		return true;
	}

}
