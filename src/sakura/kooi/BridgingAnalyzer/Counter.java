package sakura.kooi.BridgingAnalyzer;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import sakura.kooi.BridgingAnalyzer.utils.SoundMachine;
import sakura.kooi.BridgingAnalyzer.utils.Utils;

import java.util.ArrayList;
import java.util.HashSet;

public class Counter {
    public static HashSet<Block> scheduledBreakBlocks = new HashSet<>();
    private ArrayList<Long> counterCPS = new ArrayList<>();
    private int maxCPS = 0;
    private ArrayList<Long> counterBridge = new ArrayList<>();
    private double maxBridge = 0;
    private int currentLength = 0;
    private int maxLength = 0;
    private ArrayList<Block> allBlock = new ArrayList<>();
    private Block lastBlock;
    private Location checkPoint = Bukkit.getWorld("world").getSpawnLocation().add(0.5, 1, 0.5);
    private Player player;
    @Getter
    @Setter
    private boolean speedCountEnabled = true;
    @Getter
    @Setter
    private boolean PvPEnabled = false;
    @Getter
    @Setter
    private boolean highlightEnabled = true;
    @Getter
    @Setter
    private boolean standBridgeMarkerEnabled = false;
    public Counter(Player p) {
        player = p;
    }

    public void addLogBlock(Block block) {
        allBlock.add(block);
        BridgingAnalyzer.getPlacedBlocks().put(block, block.getState().getData());
    }

    public void breakBlock() {
        scheduledBreakBlocks.addAll(allBlock);
        new BreakRunnable(new ArrayList<>(allBlock));
        allBlock.clear();
    }

    public void countBridge(Block block) {
        allBlock.add(block);
        BridgingAnalyzer.getPlacedBlocks().put(block, block.getState().getData());
        if ((lastBlock != null) && ((lastBlock.getY() + 1) != block.getY())) {
            counterBridge.add(System.currentTimeMillis());
            currentLength++;
            if (currentLength > maxLength) {
                maxLength = currentLength;
            }
        }
        lastBlock = block;
        removeBridgeTimeout();
        getBridgeSpeed();
    }

    public void countCPS() {
        counterCPS.add(System.currentTimeMillis());
        removeCPSTimeout();
        if (counterCPS.size() > maxCPS) {
            maxCPS = counterCPS.size();
        }
    }

    public ArrayList<Block> getAllBlocks() {
        return allBlock;
    }

    public int getBridgeLength() {
        return currentLength;
    }

    public double getBridgeSpeed() {
        double result;
        if (counterBridge.isEmpty()) {
            result = 0.00;
        } else {
            long peri = counterBridge.get(counterBridge.size() - 1) - counterBridge.get(0);
            if (peri > 1000L) {
                result = counterBridge.size() / (peri / 1000.0);
                if (result > maxBridge) {
                    maxBridge = Utils.formatDouble(result);
                }
            } else {
                result = counterBridge.size();
            }
        }
        return Utils.formatDouble(result);
    }

    public int getCPS() {
        return counterCPS.size();
    }

    public int getMaxBridgeLength() {
        return maxLength;
    }

    public double getMaxBridgeSpeed() {
        return maxBridge;
    }

    public int getMaxCPS() {
        return maxCPS;
    }

    public void instantBreakBlock() {
        for (Block b : allBlock) {
            Utils.breakBlock(b);
            BridgingAnalyzer.getPlacedBlocks().remove(b);
        }
        allBlock.clear();
    }

    public void removeBlockRecord(Block b) {
        allBlock.remove(b);
        BridgingAnalyzer.getPlacedBlocks().remove(b);
    }

    private void removeBridgeTimeout() {
        while (!counterBridge.isEmpty() && ((System.currentTimeMillis() - counterBridge.get(0)) > 3000)) {
            counterBridge.remove(0);
        }
    }

    private void removeCPSTimeout() {
        while (!counterCPS.isEmpty() && ((System.currentTimeMillis() - counterCPS.get(0)) > 1000)) {
            counterCPS.remove(0);
        }
    }

    public void reset() {
        counterCPS.clear();
        maxCPS = 0;
        counterBridge.clear();
        maxBridge = 0;
        currentLength = 0;
        breakBlock();
    }

    public void resetMax() {
        maxCPS = 0;
        maxLength = 0;
    }

    public void resetMaxLength() {
        maxLength = 0;
    }

    public void setCheckPoint(Location loc) {
        checkPoint = loc;
        Block target = loc.add(0, -1, 0).getBlock().getRelative(BlockFace.DOWN, 3);
        if (target.getType() == Material.CHEST) {
            BridgingAnalyzer.clearInventory(player);
            Chest chest = (Chest) target.getState();
            for (ItemStack stack : chest.getBlockInventory().getContents())
                if (stack != null) {
                    Utils.addItem(player.getInventory(), stack.clone());
                }
            player.getWorld().playSound(player.getLocation(), SoundMachine.get("ITEM_PICKUP", "ENTITY_ITEM_PICKUP"), 1,
                    1);
        }

    }

    public void teleportCheckPoint() {
        player.teleport(checkPoint);
        Block target = checkPoint.getBlock().getRelative(BlockFace.DOWN, 3);
        if (target.getType() == Material.CHEST) {
            BridgingAnalyzer.clearInventory(player);
            Chest chest = (Chest) target.getState();
            for (ItemStack stack : chest.getBlockInventory().getContents())
                if (stack != null) {
                    Utils.addItem(player.getInventory(), stack.clone());
                }
            player.getWorld().playSound(player.getLocation(), SoundMachine.get("ITEM_PICKUP", "ENTITY_ITEM_PICKUP"), 1,
                    1);
        }

    }

    public void vectoryBreakBlock() {
        counterCPS.clear();
        counterBridge.clear();
        currentLength = 0;
        for (Block b : allBlock)
            if (b.getType() != Material.AIR) {
                b.setType(Material.SEA_LANTERN);
            }
        BridgingAnalyzer.teleportCheckPoint(player);
        breakBlock();
    }

    public class BreakRunnable implements Runnable {
        BukkitTask task;
        ArrayList<Block> blocks = new ArrayList<>();

        public BreakRunnable(ArrayList<Block> allBlocks) {
            blocks.addAll(allBlocks);
            scheduledBreakBlocks.addAll(blocks);
            if (blocks.isEmpty()) return;
            int tick = 1 + (60 / blocks.size());
            if (tick > 3) {
                tick = 3;
            }
            task = Bukkit.getScheduler().runTaskTimer(BridgingAnalyzer.getInstance(), this, 10, tick);
        }

        @Override
        public void run() {
            if (!blocks.isEmpty()) {
                Block b = null;
                while (!blocks.isEmpty() && ((b == null) || (b.getType() == Material.AIR))) {
                    b = blocks.get(0);
                    scheduledBreakBlocks.remove(b);
                    blocks.remove(0);
                    BridgingAnalyzer.getPlacedBlocks().remove(b);
                }
                if (b != null) {
                    Utils.breakBlock(b);
                    BridgingAnalyzer.getPlacedBlocks().remove(b);
                }
            } else {
                task.cancel();
            }
        }
    }

}
