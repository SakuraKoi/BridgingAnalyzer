package sakura.kooi.BridgingAnalyzer;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import sakura.kooi.BridgingAnalyzer.utils.*;

public class TriggerBlockListener implements Listener {
    @EventHandler
    public void antiTriggerBlockCover(BlockPlaceEvent e) {
        if (e.getPlayer() != null) {
            if (e.getPlayer().getGameMode() == GameMode.CREATIVE) return;
            if (isTriggerBlock(e.getBlock().getRelative(BlockFace.DOWN)) || isTriggerBlock(e.getBlock().getRelative(BlockFace.DOWN,
                    2))) {
                Bukkit.getScheduler().runTaskLater(BridgingAnalyzer.getInstance(), () -> {
                    Utils.breakBlock(e.getBlock());
                    BridgingAnalyzer.getCounter(e.getPlayer()).removeBlockRecord(e.getBlock());
                }, 100);
            }
        }
    }

    private Vector getAttackVector(Location location) {
        double ran = 90 + Math.random() * 30 - 15;
        float newZ = (float) (location.getZ() + 3.0D * Math.sin(Math.toRadians(location.getYaw() + ran)));
        float newX = (float) (location.getX() + 3.0D * Math.cos(Math.toRadians(location.getYaw() + ran)));
        return new Vector(newX - location.getX(), 0.0D, newZ - location.getZ());
    }

    private boolean isTriggerBlock(Block b) {
        if (b.getType() == Material.EMERALD_BLOCK) return true;
        if (b.getType() == Material.REDSTONE_BLOCK) return true;
        if (b.getType() == Material.LAPIS_BLOCK) return true;
        return b.getType() == Material.BEACON;
    }

    @EventHandler
    public void triggerCheckPointBlock(PlayerMoveEvent e) {
        if (e.getFrom().getBlock().equals(e.getTo().getBlock())) return;
        if (e.getPlayer().getNoDamageTicks() != 0) return;
        if (e.getPlayer().getGameMode() == GameMode.SPECTATOR) return;
        if (e.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == Material.EMERALD_BLOCK) {
            e.getPlayer().setNoDamageTicks(40);
            Location spawnLoc = e.getTo().getBlock().getLocation().add(0.5, 1, 0.5);
            spawnLoc.setYaw(e.getPlayer().getLocation().getYaw());
            spawnLoc.setPitch(e.getPlayer().getLocation().getPitch());
            Counter c = BridgingAnalyzer.getCounter(e.getPlayer());
            c.setCheckPoint(spawnLoc);
            new ParticleRing(e.getTo().getBlock().getLocation().add(0.5, 1.5, 0.5), ParticleEffects.CLOUD, 1) {

                @Override
                public void onFinish() {
                }

            };
            TitleUtils.sendTitle(e.getPlayer(), "", "§a传送点已设置", 5, 10, 5);
            e.getPlayer().getWorld().playSound(e.getTo(),
                    SoundMachine.get("ORB_PICKUP", "ENTITY_EXPERIENCE_ORB_PICKUP"), 1, 1);
        }
    }

    @EventHandler
    public void triggerEndPointBlock(PlayerMoveEvent e) {
        if (e.getFrom().getBlock().equals(e.getTo().getBlock())) return;
        if (e.getPlayer().getNoDamageTicks() != 0) return;
        if (e.getPlayer().getGameMode() == GameMode.SPECTATOR) return;
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
    public void triggerKnockbackBlock(PlayerMoveEvent e) {
        if (e.getFrom().getBlock().equals(e.getTo().getBlock())) return;
        if (e.getPlayer().getNoDamageTicks() != 0) return;
        if (e.getPlayer().getGameMode() != GameMode.SURVIVAL) return;
        if (e.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == Material.MELON_BLOCK) {
            e.getPlayer().setNoDamageTicks(20);

            Player player = e.getPlayer();
            Vector finalVector = getAttackVector(player.getLocation());
            Location finalAttackFrom = player.getLocation().add(finalVector);
            finalAttackFrom.setY(player.getLocation().getY() + 1.2);
            Vector normalize = finalAttackFrom.toVector().subtract(player.getLocation().toVector()).normalize();
            Bukkit.getScheduler().runTaskLater(BridgingAnalyzer.getInstance(), () -> {
                player.setNoDamageTicks(0);
                player.damage(0.0);
                player.setVelocity(normalize.multiply(-1.25).setY(0.45));
            }, 7);
        }
    }

    @EventHandler
    public void triggerSpawnPointBlock(PlayerMoveEvent e) {
        if (e.getFrom().getBlock().equals(e.getTo().getBlock())) return;
        if (e.getPlayer().getNoDamageTicks() != 0) return;
        if (e.getPlayer().getGameMode() == GameMode.SPECTATOR) return;
        if (e.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == Material.LAPIS_BLOCK) {
            e.getPlayer().setNoDamageTicks(40);
            Counter c = BridgingAnalyzer.getCounter(e.getPlayer());
            c.setCheckPoint(Bukkit.getWorld("world").getSpawnLocation().add(0.5, 1, 0.5));
            c.resetMax();
            new ParticleRing(e.getTo().getBlock().getLocation().add(0.5, 1.5,
                    0.5), ParticleEffects.FIREWORKS_SPARK, 35) {

                @Override
                public void onFinish() {
                    BridgingAnalyzer.teleportCheckPoint(e.getPlayer());
                    BridgingAnalyzer.clearEffect(e.getPlayer());
                    if (!e.getPlayer().isOp()) {
                        e.getPlayer().getInventory().setHelmet(null);
                        e.getPlayer().getInventory().setChestplate(null);
                        e.getPlayer().getInventory().setLeggings(null);
                        e.getPlayer().getInventory().setBoots(null);
                    }
                }

            };
            TitleUtils.sendTitle(e.getPlayer(), "", "§b正在返回出生点...", 5, 25, 5);
            e.getPlayer().getWorld().playSound(e.getTo(),
                    SoundMachine.get("ORB_PICKUP", "ENTITY_EXPERIENCE_ORB_PICKUP"), 1, 1);
        }
    }

    @EventHandler
    public void triggerSpeedPlate(PlayerMoveEvent e) {
        if (e.getFrom().getBlock().equals(e.getTo().getBlock())) return;
        if (e.getPlayer().getNoDamageTicks() != 0) return;
        if (e.getPlayer().getGameMode() == GameMode.SPECTATOR) return;
        if (e.getTo().getBlock().getType() == Material.GOLD_PLATE) {
            e.getPlayer().setNoDamageTicks(20);
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 2), true);
        }
    }

    @EventHandler
    public void triggerTeleportBlock(PlayerMoveEvent e) {
        if (e.getFrom().getBlock().equals(e.getTo().getBlock())) return;
        if (e.getPlayer().getNoDamageTicks() != 0) return;
        if (e.getPlayer().getGameMode() == GameMode.SPECTATOR) return;
        if (e.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == Material.BEACON) {
            e.getPlayer().setNoDamageTicks(20);
            Block to = e.getTo().getBlock();
            while ((to.getType() == Material.AIR || to.getType() == Material.STAINED_GLASS_PANE || to.getType() == Material.WALL_SIGN || to.getType() == Material.SIGN_POST) && to.getY() < 255) {
                to = to.getRelative(BlockFace.UP);
            }
            if (to.getType() == Material.BEACON) {
                e.getPlayer().setNoDamageTicks(50);
                Block teleportTarget = to;
                new TeleportRingEffect(e.getTo().getBlock().getLocation().add(0.5, 0,
                        0.5), teleportTarget.getLocation().add(0.5,
                        1.0,
                        0.5), 1, 0, 40) {

                    @Override
                    public void onFinish() {
                        Location loc = teleportTarget.getLocation().add(0.5, 1.5, 0.5);
                        loc.setYaw(e.getPlayer().getLocation().getYaw());
                        loc.setPitch(e.getPlayer().getLocation().getPitch());
                        e.getPlayer().teleport(loc);
                    }

                };
                e.getPlayer().getWorld().playSound(e.getTo(),
                        SoundMachine.get("ENDERMAN_TELEPORT", "ENTITY_ENDERMEN_TELEPORT"),
                        1, 1);
            }

        }
    }

    @EventHandler
    public void triggerTeleportBlock(PlayerToggleSneakEvent e) {
        if (e.isSneaking()) return;
        if (e.getPlayer().getNoDamageTicks() != 0) return;
        if (e.getPlayer().getGameMode() == GameMode.SPECTATOR) return;
        if (e.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.BEACON) {
            e.getPlayer().setNoDamageTicks(20);
            Block to = e.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN, 2);
            while ((to.getType() == Material.AIR || to.getType() == Material.STAINED_GLASS_PANE || to.getType() == Material.WALL_SIGN || to.getType() == Material.SIGN_POST) && to.getY() > 0) {
                to = to.getRelative(BlockFace.DOWN);
            }
            if (to.getType() == Material.BEACON) {
                e.getPlayer().setNoDamageTicks(50);
                Block teleportTarget = to;
                new TeleportRingEffect(e.getPlayer().getLocation().getBlock().getLocation().add(0.5, 0,
                        0.5), teleportTarget.getLocation().add(0.5,
                        1.0,
                        0.5), 1, 10, 40) {
                    @Override
                    public void onFinish() {
                        Location loc = teleportTarget.getLocation().add(0.5, 1.5, 0.5);
                        loc.setYaw(e.getPlayer().getLocation().getYaw());
                        loc.setPitch(e.getPlayer().getLocation().getPitch());
                        e.getPlayer().teleport(loc);
                    }

                };
                e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(),
                        SoundMachine.get("ENDERMAN_TELEPORT", "ENTITY_ENDERMEN_TELEPORT"),
                        1, 1);
            }

        }
    }
}
