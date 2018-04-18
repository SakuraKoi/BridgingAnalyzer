package ldcr.BridgeingAnalyzer;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import ldcr.BridgeingAnalyzer.Utils.FireworkUtils;
import ldcr.BridgeingAnalyzer.Utils.ParticleEffects;
import ldcr.BridgeingAnalyzer.Utils.ParticleRing;
import ldcr.BridgeingAnalyzer.Utils.SoundMachine;
import ldcr.BridgeingAnalyzer.Utils.TitleUtils;
import ldcr.BridgeingAnalyzer.Utils.Util;

public class TriggerBlockListener implements Listener {
    @EventHandler
    public void antiTriggerBlockCover(final BlockPlaceEvent e) {
	if (e.getPlayer() != null) {
	    if (e.getPlayer().getWorld().getName().startsWith("bedwars")) return;
	    if (e.getPlayer().getGameMode() == GameMode.CREATIVE)
		return;
	    if (isTriggerBlock(e.getBlock().getRelative(BlockFace.DOWN))
		    || isTriggerBlock(e.getBlock().getRelative(BlockFace.DOWN, 2))) {
		Bukkit.getScheduler().runTaskLater(BridgingAnalyzer.instance, new Runnable() {

		    @Override
		    public void run() {
			Util.breakBlock(e.getBlock());
			BridgingAnalyzer.getCounter(e.getPlayer()).removeBlockRecord(e.getBlock());
		    }
		}, 100);
	    }
	}
    }

    private boolean isTriggerBlock(final Block b) {
	if (b.getType() == Material.EMERALD_BLOCK)
	    return true;
	if (b.getType() == Material.REDSTONE_BLOCK)
	    return true;
	if (b.getType() == Material.LAPIS_BLOCK)
	    return true;
	if (b.getType() == Material.BEACON)
	    return true;
	return false;
    }

    @EventHandler
    public void triggerCheckPointBlock(final PlayerMoveEvent e) {
	if (e.getPlayer().getWorld().getName().startsWith("bedwars")) return;
	if (e.getFrom().getBlock().equals(e.getTo().getBlock()))
	    return;
	if (e.getPlayer().getNoDamageTicks() != 0)
	    return;
	if (e.getPlayer().getGameMode()==GameMode.SPECTATOR) return;
	if (e.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == Material.EMERALD_BLOCK) {
	    e.getPlayer().setNoDamageTicks(40);
	    final Location spawnLoc = e.getTo().getBlock().getLocation().add(0.5, 1, 0.5);
	    spawnLoc.setYaw(e.getPlayer().getLocation().getYaw());
	    spawnLoc.setPitch(e.getPlayer().getLocation().getPitch());
	    final Counter c = BridgingAnalyzer.getCounter(e.getPlayer());
	    c.setCheckPoint(spawnLoc);
	    new ParticleRing(e.getTo().getBlock().getLocation().add(0.5, 1.5, 0.5), ParticleEffects.CLOUD, 1) {

		@Override
		public void onFinish() {
		}

	    };
	    Util.f__kUnknownTitleFadeBug(e.getPlayer(), "", "§a传送点已设置", 5, 10, 5);
	    e.getPlayer().getWorld().playSound(e.getTo(),
		    SoundMachine.get("ORB_PICKUP", "ENTITY_EXPERIENCE_ORB_PICKUP"), 1, 1);
	}
    }

    @EventHandler
    public void triggerEndPointBlock(final PlayerMoveEvent e) {
	if (e.getPlayer().getWorld().getName().startsWith("bedwars")) return;
	if (e.getFrom().getBlock().equals(e.getTo().getBlock()))
	    return;
	if (e.getPlayer().getNoDamageTicks() != 0)
	    return;
	if (e.getPlayer().getGameMode()==GameMode.SPECTATOR) return;
	if (e.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == Material.REDSTONE_BLOCK) {
	    e.getPlayer().setNoDamageTicks(40);
	    new ParticleRing(e.getTo().getBlock().getLocation().add(0.5, 0.1, 0.5), ParticleEffects.SPELL_WITCH, 20) {
		@Override
		public void onFinish() {
		    // BridgingAnalyzer.teleportCheckPoint(e.getPlayer());
		    FireworkUtils.shootFirework(e.getPlayer());
		}

	    };
	    BridgingAnalyzer.getCounter(e.getPlayer()).vectoryBreakBlock();
	    TitleUtils.sendTitle(e.getPlayer(), "§6§lVICTORY", "", 5, 20, 5);
	    e.getPlayer().getWorld().playSound(e.getTo(), Sound.LEVEL_UP, 1, 1);
	}
    }

    @EventHandler
    public void triggerSpawnPointBlock(final PlayerMoveEvent e) {
	if (e.getPlayer().getWorld().getName().startsWith("bedwars")) return;
	if (e.getFrom().getBlock().equals(e.getTo().getBlock()))
	    return;
	if (e.getPlayer().getNoDamageTicks() != 0)
	    return;
	if (e.getPlayer().getGameMode()==GameMode.SPECTATOR) return;
	if (e.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == Material.LAPIS_BLOCK) {
	    e.getPlayer().setNoDamageTicks(40);
	    final Counter c = BridgingAnalyzer.getCounter(e.getPlayer());
	    c.setCheckPoint(Bukkit.getWorld("world").getSpawnLocation().add(0.5, 1, 0.5));
	    c.resetMax();
	    new ParticleRing(e.getTo().getBlock().getLocation().add(0.5, 1.5, 0.5), ParticleEffects.FIREWORKS_SPARK,
		    35) {

		@Override
		public void onFinish() {
		    BridgingAnalyzer.teleportCheckPoint(e.getPlayer());
		    BridgingAnalyzer.clearEffect(e.getPlayer());
		    if (!e.getPlayer().hasPermission("bridginganalyzer.noclear")) {
			e.getPlayer().getInventory().setHelmet(null);
			e.getPlayer().getInventory().setChestplate(null);
			e.getPlayer().getInventory().setLeggings(null);
			e.getPlayer().getInventory().setBoots(null);
		    }
		}

	    };
	    Util.f__kUnknownTitleFadeBug(e.getPlayer(), "", "§b正在返回出生点...", 5, 25, 5);
	    e.getPlayer().getWorld().playSound(e.getTo(),
		    SoundMachine.get("ORB_PICKUP", "ENTITY_EXPERIENCE_ORB_PICKUP"), 1, 1);
	}
    }

    @EventHandler
    public void triggerTeleportBlock(final PlayerMoveEvent e) {
	if (e.getPlayer().getWorld().getName().startsWith("bedwars")) return;
	if (e.getFrom().getBlock().equals(e.getTo().getBlock()))
	    return;
	if (e.getPlayer().getNoDamageTicks() != 0)
	    return;
	if (e.getPlayer().getGameMode()==GameMode.SPECTATOR) return;
	if (e.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == Material.BEACON) {
	    e.getPlayer().setNoDamageTicks(20);
	    Block to = e.getTo().getBlock();
	    while ((to.getType() == Material.AIR) && (to.getY() < 255)) {
		to = to.getRelative(BlockFace.UP);
	    }
	    if (to.getType() == Material.BEACON) {
		e.getPlayer().setNoDamageTicks(40);
		final Block teleportTarget = to;
		for (int i = 0; i < 29; i++) {
		    new ParticleRing(e.getTo().getBlock().getLocation().add(0.5, ((double) i) / 10, 0.5),
			    ParticleEffects.FOOTSTEP, 1) {
			@Override
			public void onFinish() {
			}
		    };
		}
		new ParticleRing(e.getTo().getBlock().getLocation().add(0.5, 2, 0.5), ParticleEffects.FOOTSTEP, 35) {

		    @Override
		    public void onFinish() {
			final Location loc = teleportTarget.getLocation().add(0.5, 1.5, 0.5);
			loc.setYaw(e.getPlayer().getLocation().getYaw());
			loc.setPitch(e.getPlayer().getLocation().getPitch());
			e.getPlayer().teleport(loc);
		    }

		};
		e.getPlayer().getWorld().playSound(e.getTo(),
			SoundMachine.get("ENDERMAN_TELEPORT", "ENTITY_ENDERMEN_TELEPORT"), 1, 1);
	    }

	}
    }
}
