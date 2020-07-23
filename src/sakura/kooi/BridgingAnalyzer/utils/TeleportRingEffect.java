package sakura.kooi.BridgingAnalyzer.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitTask;
import sakura.kooi.BridgingAnalyzer.BridgingAnalyzer;

public abstract class TeleportRingEffect implements Runnable {
    int circleElements = 40;
    double radius = 1.0;
    private int round;
    private ParticleEffects type = ParticleEffects.SPELL_WITCH;// SPELL;
    private Location loc;
    private Location target;
    private BukkitTask task;
    private int dela;

    private int cr = 0;

    private int de;

    public TeleportRingEffect(Location centerLoc, Location target, long delay, int dela, int round) {
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
        Location centerLoc = loc.clone();// .add(0, (round-cr)/20.0, 0);
        Location targetLoc = target.clone();// .add(0, (round-cr)/20.0,
        // 0);
        for (int i = 0; i < circleElements; ++i) {
            double alpha = 360.0 / circleElements * i;
            double x = radius * Math.sin(Math.toRadians(alpha));
            double z = radius * Math.cos(Math.toRadians(alpha));
            Location particle = new Location(centerLoc.getWorld(), centerLoc.getX() + x, centerLoc.getY(), centerLoc.getZ() + z);
            type.display(1, particle, 128);
            Location particlet = new Location(targetLoc.getWorld(), targetLoc.getX() + x, targetLoc.getY(), targetLoc.getZ() + z);
            type.display(1, particlet, 128);
        }
    }
}
