package ldcr.BridgingAnalyzer;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import ldcr.Utils.Bukkit.ParticleEffects;

public class HighlightListener implements Listener {
	private final HashMap<Player, Block> highlightHistory = new HashMap<Player, Block>();

	private Block getRelativeBrick(final Block b) {
		Block relative = b.getRelative(BlockFace.EAST);
		if (relative.getType() == Material.SMOOTH_BRICK) return relative;
		relative = b.getRelative(BlockFace.WEST);
		if (relative.getType() == Material.SMOOTH_BRICK) return relative;
		relative = b.getRelative(BlockFace.SOUTH);
		if (relative.getType() == Material.SMOOTH_BRICK) return relative;
		relative = b.getRelative(BlockFace.NORTH);
		if (relative.getType() == Material.SMOOTH_BRICK) return relative;
		return null;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onFallDown(final PlayerMoveEvent e) {
		if (e.getTo().getY() < 0) {
			final Block historyBlock = highlightHistory.get(e.getPlayer());
			if (historyBlock != null) e.getPlayer().sendBlockChange(historyBlock.getLocation(), historyBlock.getType(),
																	historyBlock.getData());
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onMove(final PlayerMoveEvent e) {
		if (!BridgingAnalyzer.getCounter(e.getPlayer()).isHighlightEnabled()) return;
		if (e.getFrom().getBlock() != e.getTo().getBlock()) {
			final Block target = getRelativeBrick(roundLocation(e.getTo().clone().add(0, -1, 0)).getBlock());
			if (target != null) {
				final Block historyBlock = highlightHistory.get(e.getPlayer());
				if (historyBlock != null) e.getPlayer().sendBlockChange(historyBlock.getLocation(),
																		historyBlock.getType(), historyBlock.getData());
				e.getPlayer().sendBlockChange(target.getLocation(), Material.SNOW_BLOCK, (byte) 0);
				highlightHistory.put(e.getPlayer(), target);
			}
		}
	}

	@EventHandler
	public void onStandBridgeMove(final PlayerMoveEvent e) {
		if (!BridgingAnalyzer.getCounter(e.getPlayer()).isStandBridgeMarkerEnabled()) return;
		ParticleEffects.TOWN_AURA.display(5, e.getTo().clone().add(0.08, 0.0, 0.08), 2);
	}

	private Location roundLocation(final Location location) {
		final Location loc = location.clone();
		loc.setX(Math.round(loc.getX()));
		loc.setZ(Math.round(loc.getZ()));
		return loc;
	}

}
