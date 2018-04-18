package ldcr.BridgeingAnalyzer;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import ldcr.BridgeingAnalyzer.Utils.ActionBarUtils;
import ldcr.BridgeingAnalyzer.Utils.Util;

public class CounterListener implements Listener {
    @EventHandler
    public void onBreakBlock(final BlockBreakEvent e) {
	if (e.getPlayer()!=null) {
	    if (e.getPlayer().getWorld().getName().startsWith("bedwars")) return;
	    if ( !Counter.isPlacedByPlayer(e.getBlock())) {
		if (e.getPlayer().getGameMode() == GameMode.CREATIVE)
		    return;
		e.setCancelled(true);
	    }
	}
    }

    @EventHandler
    public void onClick(final PlayerInteractEvent e) {
	if (e.getPlayer().getWorld().getName().startsWith("bedwars")) return;
	if (e.getAction().toString().contains("CLICK")) {
	    final Counter c = BridgingAnalyzer.getCounter(e.getPlayer());
	    c.countCPS();
	    if(c.isDamageEnabled()) return;
	    ActionBarUtils.sendActionBar(e.getPlayer(), "§c§l最大CPS - " + c.getMaxCPS() + " §d§l当前CPS - " + c.getCPS()
	    + " §a§l| §c§l最远距离 - " + c.getMaxBridgeLength() + " §d§l当前距离 - " + c.getBridgeLength());
	}
    }

    @EventHandler
    public void onFallDown(final PlayerMoveEvent e) {
	if (e.getPlayer().getWorld().getName().startsWith("bedwars")) return;
	if (e.getTo().getY() < 0) {
	    final Counter c = BridgingAnalyzer.getCounter(e.getPlayer());
	    if (c.isSpeedCountEnabled()) {
		Util.f__kUnknownTitleFadeBug(e.getPlayer(), "", "§cMax - " + c.getMaxBridgeSpeed() + " block/s", 0, 100,
			0);
	    }
	    c.reset();
	    BridgingAnalyzer.teleportCheckPoint(e.getPlayer());
	}
    }

    @EventHandler
    public void onLiqudFlow(final BlockFromToEvent e) {
	e.setCancelled(true);
    }

    @EventHandler
    public void onPlaceBlock(final BlockPlaceEvent e) {
	if (e.isCancelled()) return;
	if (e.getPlayer() != null) {
	    if (e.getPlayer().getWorld().getName().startsWith("bedwars")) return;
	    if (e.getPlayer().getGameMode() == GameMode.CREATIVE)
		return;
	    final Counter c = BridgingAnalyzer.getCounter(e.getPlayer());
	    c.countBridge(e.getBlock());
	    if (c.isSpeedCountEnabled()) {
		Util.f__kUnknownTitleFadeBug(e.getPlayer(), "", "§b" + c.getBridgeSpeed() + " block/s", 0, 100, 0);
	    }
	    Bukkit.getScheduler().runTaskLater(BridgingAnalyzer.instance, new Runnable() {

		@SuppressWarnings("deprecation")
		@Override
		public void run() {
		    e.getPlayer().getInventory()
		    .addItem(new ItemStack(e.getPlayer().getItemInHand().getType(), 1, (short) 0,e.getPlayer().getItemInHand().getData().getData()));
		}
	    }, 1);

	}
    }

    @EventHandler
    public void onPlaceLiqud(final PlayerBucketEmptyEvent e) {
	if (e.isCancelled()) return;
	if (e.getPlayer() != null) {
	    if (e.getPlayer().getWorld().getName().startsWith("bedwars")) return;
	    if (e.getPlayer().getGameMode() == GameMode.CREATIVE)
		return;
	    final Counter c = BridgingAnalyzer.getCounter(e.getPlayer());
	    c.addLogBlock(e.getBlockClicked().getRelative(e.getBlockFace()));
	    Bukkit.getScheduler().runTaskLater(BridgingAnalyzer.instance, new Runnable() {
		@Override
		public void run() {
		    e.getPlayer().getInventory().remove(Material.BUCKET);
		}
	    }, 1);
	}
    }
}
