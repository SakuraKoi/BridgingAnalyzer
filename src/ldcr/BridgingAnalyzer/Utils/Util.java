package ldcr.BridgingAnalyzer.Utils;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class Util {
	public static void breakBlock(final Block b) {
		if (!b.getChunk().isLoaded()) b.getChunk().load(false);
		b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getType());
		b.setType(Material.AIR);
	}

	public static double formatDouble(final double d) {
		return (double) Math.round(d * 100) / 100;
	}

}
