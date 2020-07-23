package sakura.kooi.BridgingAnalyzer.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sakura.kooi.BridgingAnalyzer.BridgingAnalyzer;
import sakura.kooi.BridgingAnalyzer.Counter;

public class ClearCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("bridginganalyzer.clear")) if (args.length == 0) {
            sender.sendMessage("§b§l搭路练习 §7>> §c正在清除所有已放置方块....");
            for (Counter c : BridgingAnalyzer.getCounters().values()) {
                c.instantBreakBlock();
            }
            for (Block b : Counter.scheduledBreakBlocks) {
                b.setType(Material.AIR);
            }
            sender.sendMessage("§b§l搭路练习 §7>> §a方块清除完毕.");
        } else {
            String player = args[0];
            @SuppressWarnings("deprecation") OfflinePlayer offp = Bukkit.getOfflinePlayer(player);
            if (offp == null) {
                sender.sendMessage("§b§l搭路练习 §7>> §c错误: 玩家 " + player + " 不存在.");
                return true;
            }
            if (!offp.isOnline()) {
                sender.sendMessage("§b§l搭路练习 §7>> §c错误: 玩家 " + offp.getName() + " 不在线.");
                return true;
            }
            Player p = offp.getPlayer();
            BridgingAnalyzer.getCounter(p).instantBreakBlock();
            sender.sendMessage("§b§l搭路练习 §7>> §a已清除玩家 " + p.getName() + " 放置的方块.");
        }
        return true;
    }

}
