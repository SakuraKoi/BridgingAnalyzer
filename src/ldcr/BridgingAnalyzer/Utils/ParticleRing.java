package ldcr.BridgingAnalyzer.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import ldcr.BridgingAnalyzer.BridgingAnalyzer;
import ldcr.Utils.Bukkit.ParticleEffects;

public abstract class ParticleRing {
	public ParticleRing(final Location centerLoc, final ParticleEffects type, final long delay) {
		final int circleElements = 20;
		final double radius = 1.0;
		for (int i = 0; i < circleElements; ++i) {
			final double alpha = (360.0 / circleElements) * i;
			final double x = radius * Math.sin(Math.toRadians(alpha));
			final double z = radius * Math.cos(Math.toRadians(alpha));
			final Location particle = new Location(centerLoc.getWorld(), centerLoc.getX() + x, centerLoc.getY(), centerLoc.getZ() + z);
			type.display(0, 0, 0, 0, 1, particle, 64);
		}
		Bukkit.getScheduler().runTaskLater(BridgingAnalyzer.instance, new Runnable() {

			@Override
			public void run() {
				onFinish();
			}

		}, delay);

	}

	public abstract void onFinish();
}
