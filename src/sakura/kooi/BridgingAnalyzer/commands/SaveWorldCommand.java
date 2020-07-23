package sakura.kooi.BridgingAnalyzer.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import sakura.kooi.BridgingAnalyzer.BridgingAnalyzer;
import sakura.kooi.BridgingAnalyzer.Counter;
import sakura.kooi.BridgingAnalyzer.utils.Utils;

public class SaveWorldCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("bridginganalyzer.admin")) {
            sender.sendMessage("§b§l搭路练习 §7>> §c正在保存世界....");
            for (Counter c : BridgingAnalyzer.getCounters().values()) {
                c.instantBreakBlock();
            }
            for (Block b : Counter.scheduledBreakBlocks) {
                Utils.breakBlock(b);
            }
            for (World world : Bukkit.getWorlds()) {
                world.save();
            }
            sender.sendMessage("§b§l搭路练习 §7>> §a地图保存完毕.");
        }
        return true;
    }

}
