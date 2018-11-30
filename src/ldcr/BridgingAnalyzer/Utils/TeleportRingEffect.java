package ldcr.BridgingAnalyzer.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitTask;

import ldcr.BridgingAnalyzer.BridgingAnalyzer;
import ldcr.Utils.Bukkit.ParticleEffects;

public abstract class TeleportRingEffect implements Runnable {
	private final int round;
	private final ParticleEffects type = ParticleEffects.SPELL_WITCH;// SPELL;
	private final Location loc;
	private final Location target;
	final int circleElements = 40;
	final double radius = 1.0;
	private final BukkitTask task;
	private final int dela;

	private int cr = 0;

	private int de;
	public TeleportRingEffect(final Location centerLoc, final Location target, final long delay, final int dela, final int round) {
		this.round = round;
		this.target = target;
		loc = centerLoc;
		this.dela = dela;
		task = Bukkit.getScheduler().runTaskTimer(BridgingAnalyzer.getInstance(), this, delay, delay);
	}

	public abstract void onFinish();

	@Override
	public void run() {
		cr++;
		if (cr > round) {
			de++;
			if (de > dela) {
				task.cancel();
				onFinish();
			}
			return;
		}
		final Location centerLoc = loc.clone();// .add(0, (round-cr)/20.0, 0);
		final Location targetLoc = target.clone();// .add(0, (round-cr)/20.0,
													// 0);
		for (int i = 0; i < circleElements; ++i) {
			final double alpha = 360.0 / circleElements * i;
			final double x = radius * Math.sin(Math.toRadians(alpha));
			final double z = radius * Math.cos(Math.toRadians(alpha));
			final Location particle = new Location(centerLoc.getWorld(), centerLoc.getX() + x, centerLoc.getY(), centerLoc.getZ() + z);
			type.display(1, particle, 128);
			final Location particlet = new Location(targetLoc.getWorld(), targetLoc.getX() + x, targetLoc.getY(), targetLoc.getZ() + z);
			type.display(1, particlet, 128);
		}
	}
}
