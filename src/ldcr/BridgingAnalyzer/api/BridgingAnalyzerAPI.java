package ldcr.BridgingAnalyzer.api;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import ldcr.BridgingAnalyzer.BridgingAnalyzer;

public class BridgingAnalyzerAPI {
	public static void setBlockSkinProvider(final BlockSkinProvider blockSkinProvider) {
		BridgingAnalyzer.setBlockSkinProvider(blockSkinProvider);
	}
	public static void clearEffect(final Player player) {
		BridgingAnalyzer.clearEffect(player);
	}
	public static void clearInventory(final Player player) {
		BridgingAnalyzer.clearInventory(player);
	}
	public static void respawnVillager() {
		BridgingAnalyzer.spawnVillager();
	}
	public static boolean isPlacedByPlayer(final Block block) {
		return BridgingAnalyzer.isPlacedByPlayer(block);
	}
	public static void teleportCheckPoint(final Player player) {
		BridgingAnalyzer.teleportCheckPoint(player);
	}
	public static void setPlayerHighlightEnabled(final Player player, final boolean enabled) {
		BridgingAnalyzer.getCounter(player).setHighlightEnabled(enabled);
	}
	public static void setPlayerPvPEnabled(final Player player, final boolean enabled) {
		BridgingAnalyzer.getCounter(player).setPvPEnabled(enabled);
	}
	public static void setPlayerSpeedDisplayEnabled(final Player player, final boolean enabled) {
		BridgingAnalyzer.getCounter(player).setSpeedCountEnabled(enabled);
	}
	public static void setPlayerStandBridgeMarkerEnabled(final Player player, final boolean enabled) {
		BridgingAnalyzer.getCounter(player).setStandBridgeMarkerEnabled(enabled);
	}

	public static boolean isPlayerHighlightEnabled(final Player player) {
		return BridgingAnalyzer.getCounter(player).isHighlightEnabled();
	}
	public static boolean isPlayerPvPEnabled(final Player player) {
		return BridgingAnalyzer.getCounter(player).isPvPEnabled();
	}
	public static boolean isPlayerSpeedDisplayEnabled(final Player player) {
		return BridgingAnalyzer.getCounter(player).isSpeedCountEnabled();
	}
	public static boolean isPlayerStandBridgeMarkerEnabled(final Player player) {
		return BridgingAnalyzer.getCounter(player).isStandBridgeMarkerEnabled();
	}
}
