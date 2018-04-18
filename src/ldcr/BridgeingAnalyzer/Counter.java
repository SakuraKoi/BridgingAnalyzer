package ldcr.BridgeingAnalyzer;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitTask;

import ldcr.BridgeingAnalyzer.Utils.SoundMachine;
import ldcr.BridgeingAnalyzer.Utils.Util;

public class Counter {
    public class BreakRunnable implements Runnable {
	BukkitTask task;
	ArrayList<Block> blocks = new ArrayList<Block>();

	public BreakRunnable(final ArrayList<Block> allBlocks) {
	    blocks.addAll(allBlocks);
	    scheduledBreakBlocks.addAll(blocks);
	    if (blocks.isEmpty())
		return;
	    int tick = 1 + (60 / blocks.size());
	    if (tick > 3) {
		tick = 3;
	    }
	    task = Bukkit.getScheduler().runTaskTimer(BridgingAnalyzer.instance, this, 10, tick);
	}

	@Override
	public void run() {
	    if (!blocks.isEmpty()) {
		Block b = null;
		while(!blocks.isEmpty() && ((b==null) || (b.getType()==Material.AIR))) {
		    b = blocks.get(0);
		    scheduledBreakBlocks.remove(b);
		    blocks.remove(0);
		    BridgingAnalyzer.placedBlock.remove(b);
		}
		if (b!=null) {
		    Util.breakBlock(b);
		}
	    } else {
		task.cancel();
	    }
	}
    }

    public static HashSet<Block> scheduledBreakBlocks = new HashSet<Block>();
    private final ArrayList<Long> counterCPS = new ArrayList<Long>();
    private int maxCPS = 0;
    private final ArrayList<Long> counterBridge = new ArrayList<Long>();
    private double maxBridge = 0;
    private int currentLength = 0;
    private int maxLength = 0;
    private final ArrayList<Block> allBlock = new ArrayList<Block>();

    private Block lastBlock;

    private Location checkPoint = Bukkit.getWorld("world").getSpawnLocation().add(0.5, 1, 0.5);

    private final Player player;

    private boolean speedCountEnabled = true;

    private boolean damageEnabled = false;

    private boolean highlightEnabled = true;

    public ArrayList<Block> getAllBlocks() {
	return allBlock;
    }
    public Counter(final Player p) {
	player = p;
    }

    public void addLogBlock(final Block block) {
	allBlock.add(block);
	BridgingAnalyzer.placedBlock.add(block);
    }

    public void breakBlock() {
	final ArrayList<Block> block = new ArrayList<Block>();
	block.addAll(allBlock);
	scheduledBreakBlocks.addAll(allBlock);
	new BreakRunnable(block);
	allBlock.clear();
    }

    public void countBridge(final Block block) {
	allBlock.add(block);
	BridgingAnalyzer.placedBlock.add(block);
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

    public int getBridgeLength() {
	return currentLength;
    }

    public double getBridgeSpeed() {
	double result;
	if (counterBridge.isEmpty()) {
	    result = 0.00;
	} else {
	    final long peri = counterBridge.get(counterBridge.size() - 1) - counterBridge.get(0);
	    if (peri > 1000L) {
		result = counterBridge.size() / (peri / 1000.0);
		if (result > maxBridge) {
		    maxBridge = Util.formatDouble(result);
		    ;
		}
	    } else {
		result = counterBridge.size();
	    }
	}
	return Util.formatDouble(result);
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
	for (final Block b : allBlock) {
	    b.setType(Material.AIR);
	    b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getType());
	    BridgingAnalyzer.placedBlock.remove(b);
	}
	allBlock.clear();
    }

    public boolean isDamageEnabled() {
	return damageEnabled;
    }

    public boolean isHighlightEnabled() {
	return highlightEnabled;
    }

    public static boolean isPlacedByPlayer(final Block b) {
	return BridgingAnalyzer.placedBlock.contains(b);
    }

    public boolean isSpeedCountEnabled() {
	return speedCountEnabled;
    }

    public void removeBlockRecord(final Block b) {
	allBlock.remove(b);
	BridgingAnalyzer.placedBlock.remove(b);
    }

    private void removeBridgeTimeout() {
	while ((!counterBridge.isEmpty()) && ((System.currentTimeMillis() - counterBridge.get(0)) > 3000)) {
	    counterBridge.remove(0);
	}
    }

    private void removeCPSTimeout() {
	while ((!counterCPS.isEmpty()) && ((System.currentTimeMillis() - counterCPS.get(0)) > 1000)) {
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

    public void setCheckPoint(final Location loc) {
	checkPoint = loc;
	final Block target = loc.add(0, -1, 0).getBlock().getRelative(BlockFace.DOWN, 3);
	if (target.getType() == Material.CHEST) {
	    BridgingAnalyzer.clearInv(player);
	    final Chest chest = (Chest) target.getState();
	    for (final ItemStack stack : chest.getBlockInventory().getContents()) {
		if (stack != null) {
		    addItem(player.getInventory(),stack.clone(),false);
		    // player.getInventory().addItem(stack.clone());
		}
	    }
	    player.getWorld().playSound(player.getLocation(), SoundMachine.get("ITEM_PICKUP", "ENTITY_ITEM_PICKUP"), 1,
		    1);
	}

    }

    private void addItem(final PlayerInventory inv,final ItemStack item,final boolean add) {
	if (isEmptySlot(item)) return;
	if (item.getType().toString().endsWith("_HELMET")) {
	    //Material.DIAMOND_HELMET
	    if (isEmptySlot(inv.getHelmet())) {
		inv.setHelmet(item);
	    } else if (add) {
		inv.addItem(item);
	    }
	} else if (item.getType().toString().endsWith("_CHESTPLATE")) {
	    //Material.DIAMOND_CHESTPLATE
	    if (isEmptySlot(inv.getChestplate())) {
		inv.setChestplate(item);
	    } else if (add) {
		inv.addItem(item);
	    }
	} else if (item.getType().toString().endsWith("_LEGGINGS")) {
	    //Material.DIAMOND_LEGGINGS
	    if (isEmptySlot(inv.getLeggings())) {
		inv.setLeggings(item);
	    } else if (add) {
		inv.addItem(item);
	    }
	} else if (item.getType().toString().endsWith("_BOOTS")) {
	    //Material.DIAMOND_BOOTS
	    if (isEmptySlot(inv.getBoots())) {
		inv.setBoots(item);
	    } else if (add) {
		inv.addItem(item);
	    }
	} else {
	    inv.addItem(item);
	}
    }
    private boolean isEmptySlot(final ItemStack item) {
	if (item==null) return true;
	if (item.getType()==Material.AIR) return true;
	return false;
    }
    public void setDamageEnabled(final boolean dmagaeEnabled) {
	damageEnabled = dmagaeEnabled;
    }

    public void setHighlightEnabled(final boolean highlightEnabled) {
	this.highlightEnabled = highlightEnabled;
    }

    public void setSpeedCountEnabled(final boolean speedCountEnabled) {
	this.speedCountEnabled = speedCountEnabled;
    }

    public void teleportCheckPoint() {
	player.teleport(checkPoint);
	final Block target = checkPoint.getBlock().getRelative(BlockFace.DOWN, 3);
	if (target.getType() == Material.CHEST) {
	    BridgingAnalyzer.clearInv(player);
	    final Chest chest = (Chest) target.getState();
	    for (final ItemStack stack : chest.getBlockInventory().getContents()) {
		if (stack != null) {
		    addItem(player.getInventory(),stack.clone(),false);
		}
	    }
	    player.getWorld().playSound(player.getLocation(), SoundMachine.get("ITEM_PICKUP", "ENTITY_ITEM_PICKUP"), 1,
		    1);
	}

    }

    public void vectoryBreakBlock() {
	counterCPS.clear();
	counterBridge.clear();
	currentLength = 0;
	for (final Block b : allBlock) {
	    if (b.getType()!=Material.AIR) {
		b.setType(Material.SEA_LANTERN);
	    }
	}
	BridgingAnalyzer.teleportCheckPoint(player);
	breakBlock();
    }

}
