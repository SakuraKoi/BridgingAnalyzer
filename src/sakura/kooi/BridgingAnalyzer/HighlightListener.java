package sakura.kooi.BridgingAnalyzer;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import sakura.kooi.BridgingAnalyzer.Utils.ParticleEffects;

import java.util.HashMap;

public class HighlightListener implements Listener {
    private HashMap<Player, Block> highlightHistory = new HashMap<>();

    private Block getRelativeBrick(Block b) {
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
    public void onFallDown(PlayerMoveEvent e) {
        if (e.getTo().getY() < 0) {
            Block historyBlock = highlightHistory.get(e.getPlayer());
            if (historyBlock != null) {
                e.getPlayer().sendBlockChange(historyBlock.getLocation(), historyBlock.getType(),
                        historyBlock.getData());
            }
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (!BridgingAnalyzer.getCounter(e.getPlayer()).isHighlightEnabled()) return;
        if (e.getFrom().getBlock() != e.getTo().getBlock()) {
            Block target = getRelativeBrick(roundLocation(e.getTo().clone().add(0, -1, 0)).getBlock());
            if (target != null) {
                Block historyBlock = highlightHistory.get(e.getPlayer());
                if (historyBlock != null) {
                    e.getPlayer().sendBlockChange(historyBlock.getLocation(),
                            historyBlock.getType(), historyBlock.getData());
                }
                e.getPlayer().sendBlockChange(target.getLocation(), Material.SNOW_BLOCK, (byte) 0);
                highlightHistory.put(e.getPlayer(), target);
            }
        }
    }

    @EventHandler
    public void onStandBridgeMove(PlayerMoveEvent e) {
        if (!BridgingAnalyzer.getCounter(e.getPlayer()).isStandBridgeMarkerEnabled()) return;
        ParticleEffects.TOWN_AURA.display(5, e.getTo().clone().add(0.08, 0.0, 0.08), 2);
    }

    private Location roundLocation(Location location) {
        Location loc = location.clone();
        loc.setX(Math.round(loc.getX()));
        loc.setZ(Math.round(loc.getZ()));
        return loc;
    }

}
