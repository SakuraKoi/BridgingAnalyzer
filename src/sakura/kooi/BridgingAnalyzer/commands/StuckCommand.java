package sakura.kooi.BridgingAnalyzer.commands;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import sakura.kooi.BridgingAnalyzer.BridgingAnalyzer;

public class StuckCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§b§l搭路练习 §7>> §c仅玩家可以执行.");
            return true;
        }
        Player p = (Player) sender;
        // break near sandstone
        if (p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.EMERALD_BLOCK) {
            for (int x = -3; x < 3; x++) {
                for (int y = -3; y < 3; y++) {
                    for (int z = -3; z < 3; z++) {
                        Block b = p.getLocation().add(x, y, z).getBlock();
                        if (BridgingAnalyzer.isPlacedByPlayer(b)) {
                            b.setType(Material.AIR);
                        }
                    }
                }
            }
            sender.sendMessage("§b§l搭路练习 §7>> §a你周围的方块已被清除.");
        } else {
            if (p.getInventory().contains(Material.GOLD_PICKAXE)) {
                sender.sendMessage("§b§l搭路练习 §7>> §a你背包里有稿子, 自己挖开=-=");
            }
            // 不在出生点 -> 给个稿子自己挖开
            // 避免恶意利用范围清除来影响别人, 脚下路没了啥的(
            ItemStack pickaxe = new ItemStack(Material.GOLD_PICKAXE, 1);
            pickaxe.setDurability((short) 27);
            p.getInventory().addItem(pickaxe);
            sender.sendMessage("§b§l搭路练习 §7>> §a你似乎不在出生点, 给你一把稿子, 被卡住请自行挖开");
        }
        return true;
    }

}
