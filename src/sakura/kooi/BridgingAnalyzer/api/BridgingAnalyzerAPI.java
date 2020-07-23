package sakura.kooi.BridgingAnalyzer.api;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import sakura.kooi.BridgingAnalyzer.BridgingAnalyzer;

public class BridgingAnalyzerAPI {
    public static void setBlockSkinProvider(BlockSkinProvider blockSkinProvider) {
        BridgingAnalyzer.setBlockSkinProvider(blockSkinProvider);
    }

    public static void clearEffect(Player player) {
        BridgingAnalyzer.clearEffect(player);
    }

    public static void clearInventory(Player player) {
        BridgingAnalyzer.clearInventory(player);
    }

    public static void respawnVillager() {
        BridgingAnalyzer.spawnVillager();
    }

    public static boolean isPlacedByPlayer(Block block) {
        return BridgingAnalyzer.isPlacedByPlayer(block);
    }

    public static void teleportCheckPoint(Player player) {
        BridgingAnalyzer.teleportCheckPoint(player);
    }

    public static void refreshItem(Player p) {
        BridgingAnalyzer.refreshItem(p);
    }

    public static void setPlayerHighlightEnabled(Player player, boolean enabled) {
        BridgingAnalyzer.getCounter(player).setHighlightEnabled(enabled);
    }

    public static void setPlayerPvPEnabled(Player player, boolean enabled) {
        BridgingAnalyzer.getCounter(player).setPvPEnabled(enabled);
    }

    public static void setPlayerSpeedDisplayEnabled(Player player, boolean enabled) {
        BridgingAnalyzer.getCounter(player).setSpeedCountEnabled(enabled);
    }

    public static void setPlayerStandBridgeMarkerEnabled(Player player, boolean enabled) {
        BridgingAnalyzer.getCounter(player).setStandBridgeMarkerEnabled(enabled);
    }

    public static boolean isPlayerHighlightEnabled(Player player) {
        return BridgingAnalyzer.getCounter(player).isHighlightEnabled();
    }

    public static boolean isPlayerPvPEnabled(Player player) {
        return BridgingAnalyzer.getCounter(player).isPvPEnabled();
    }

    public static boolean isPlayerSpeedDisplayEnabled(Player player) {
        return BridgingAnalyzer.getCounter(player).isSpeedCountEnabled();
    }

    public static boolean isPlayerStandBridgeMarkerEnabled(Player player) {
        return BridgingAnalyzer.getCounter(player).isStandBridgeMarkerEnabled();
    }
}
