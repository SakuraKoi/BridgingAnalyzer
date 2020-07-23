package sakura.kooi.BridgingAnalyzer.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import sakura.kooi.BridgingAnalyzer.BridgingAnalyzer;

public abstract class ParticleRing {
    public ParticleRing(Location centerLoc, ParticleEffects type, long delay) {
        int circleElements = 20;
        double radius = 1.0;
        for (int i = 0; i < circleElements; ++i) {
            double alpha = 360.0 / circleElements * i;
            double x = radius * Math.sin(Math.toRadians(alpha));
            double z = radius * Math.cos(Math.toRadians(alpha));
            Location particle = new Location(centerLoc.getWorld(), centerLoc.getX() + x, centerLoc.getY(), centerLoc.getZ() + z);
            type.display(0, 0, 0, 0, 1, particle, 64);
        }
        Bukkit.getScheduler().runTaskLater(BridgingAnalyzer.getInstance(), this::onFinish, delay);
    }

    public abstract void onFinish();
}
