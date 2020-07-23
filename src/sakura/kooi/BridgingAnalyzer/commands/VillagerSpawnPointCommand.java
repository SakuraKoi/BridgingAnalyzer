package sakura.kooi.BridgingAnalyzer.commands;

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
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        if (!sender.hasPermission("bridginganalyzer.admin")) return true;
        Player player = (Player) sender;

        Location loc = player.getLocation().getBlock().getLocation().add(0.5, -1, 0.5);
        ArmorStand stand = (ArmorStand) player.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);

        stand.setSmall(true);
        stand.setGravity(false);
        stand.setVisible(false);
        stand.setHelmet(new ItemStack(Material.REDSTONE_BLOCK, 1));
        stand.setMarker(true);
        stand.setCustomName("VillagerSpawnPoint");

        player.sendMessage("§b§l搭路练习 §7>> §a村民刷新点已设置");
        return true;
    }

}
