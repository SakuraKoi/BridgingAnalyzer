package sakura.kooi.BridgingAnalyzer;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import sakura.kooi.BridgingAnalyzer.commands.*;
import sakura.kooi.BridgingAnalyzer.utils.Metrics;
import sakura.kooi.BridgingAnalyzer.utils.NoAIUtils;
import sakura.kooi.BridgingAnalyzer.utils.TitleUtils;
import sakura.kooi.BridgingAnalyzer.utils.Utils;
import sakura.kooi.BridgingAnalyzer.api.BlockSkinProvider;

import java.util.HashMap;

public class BridgingAnalyzer extends JavaPlugin implements Listener {
    @Getter
    private static BridgingAnalyzer instance;
    @Getter
    private static HashMap<Player, Counter> counters = new HashMap<>();
    @Getter
    private static HashMap<Block, MaterialData> placedBlocks = new HashMap<>();
    @Setter
    private static BlockSkinProvider blockSkinProvider;

    public static void clearEffect(Player player) {
        for (PotionEffect eff : player.getActivePotionEffects()) {
            if (eff.getType() == PotionEffectType.INVISIBILITY && player.isOp()) {
                continue;
            }
            player.removePotionEffect(eff.getType());
        }
    }

    public static void clearInventory(Player p) {
        Inventory inv = p.getInventory();
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack item = inv.getItem(i);
            if (item != null && item.getItemMeta() != null && item.getItemMeta().getDisplayName() != null && item.getItemMeta().getDisplayName().contains("Key")) {
                    continue;
                }
            inv.setItem(i, null);
        }
    }

    public static Counter getCounter(Player p) {
        Counter c = counters.get(p);
        if (c == null) {
            c = new Counter(p);
            counters.put(p, c);
        }
        return c;
    }

    public static void spawnVillager() {
        for (Entity en : Bukkit.getWorld("world").getEntities())
            if (en.getType() == EntityType.VILLAGER) if ("靶子".equals(en.getCustomName())) {
                en.remove();
            }
        for (ArmorStand stand : Bukkit.getWorld("world").getEntitiesByClass(ArmorStand.class)) {
            if (stand.getCustomName() == null) {
                continue;
            }
            if (stand.getCustomName().contains("VillagerSpawnPoint")) {
                Villager vi = (Villager) stand.getWorld().spawnEntity(stand.getLocation().add(0, 1, 0),
                        EntityType.VILLAGER);
                vi.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 32766, 254, false, false), true);
                vi.setProfession(Profession.LIBRARIAN);
                vi.setMaxHealth(1);
                vi.setHealth(1);
                vi.setCustomName("靶子");
                vi.setCustomNameVisible(false);
                NoAIUtils.setAI(vi, false);
            }
        }
    }

    public static void teleportCheckPoint(Player p) {
        p.setFallDistance(0);
        clearInventory(p);
        p.getInventory().addItem(blockSkinProvider.provide(p));
        p.setFoodLevel(20);
        p.setHealth(20);
        p.setNoDamageTicks(10);
        getCounter(p).teleportCheckPoint();
        p.setGameMode(GameMode.SURVIVAL);
    }

    public static void refreshItem(Player p) {
        clearInventory(p);
        p.getInventory().addItem(blockSkinProvider.provide(p));
    }

    public static boolean isPlacedByPlayer(Block b) {
        if (getPlacedBlocks().containsKey(b)) return getPlacedBlocks().get(b).equals(b.getState().getData());
        return false;
    }

    @EventHandler
    public void antiArmorStandManipulate(PlayerArmorStandManipulateEvent e) {
        e.setCancelled(true);
        if (e.getPlayer().getGameMode() == GameMode.CREATIVE && e.getPlayer().isOp())
            if (e.getRightClicked().getCustomName().contains("VillagerSpawnPoint")) {
                e.getRightClicked().remove();
                TitleUtils.sendTitle(e.getPlayer(), "", "§a村民刷新点已移除", 10, 20, 10);
            }
    }

    @EventHandler
    public void disableVillagerShop(PlayerInteractEntityEvent e) {
        if (e.getRightClicked().getType() == EntityType.VILLAGER) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void disableWeather(WeatherChangeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void logoutBreak(PlayerQuitEvent e) {
        getCounter(e.getPlayer()).instantBreakBlock();
        Bukkit.getConsoleSender().sendMessage("§bBridgingAnalyzer §7>> §a玩家 " + e.getPlayer().getName() + " 离线, 已清除其放置的方块.");
    }

    @EventHandler
    public void noHunger(FoodLevelChangeEvent e) {
        e.setFoodLevel(20);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity().getType() == EntityType.PLAYER) {
            Counter c = BridgingAnalyzer.getCounter((Player) e.getEntity());
            if (e.getFinalDamage() > 20) {
                c.reset();
                teleportCheckPoint((Player) e.getEntity());
                TitleUtils.sendTitle((Player) e.getEntity(), "",
                        "§4致命伤害 - " + Utils.formatDouble(e.getFinalDamage() / 2) + " ❤", 10, 20, 10);
                e.setDamage(0.0);
            } else if (e.getFinalDamage() > 10) {
                TitleUtils.sendTitle((Player) e.getEntity(), "",
                        "§c严重伤害 - " + Utils.formatDouble(e.getFinalDamage() / 2) + " ❤", 10, 20, 10);
            }
            e.setDamage(0.0);
        }
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("§bBridgingAnalyzer §7>> §c正在清除所有已放置方块....");
        for (Counter c : counters.values()) {
            c.instantBreakBlock();
        }
        counters.clear();
        for (Block b : Counter.scheduledBreakBlocks) {
            b.setType(Material.AIR);
        }
        Counter.scheduledBreakBlocks.clear();
        Bukkit.getConsoleSender().sendMessage("§bBridgingAnalyzer §7>> §a方块清除完毕.");
    }

    @Override
    public void onEnable() {
        instance = this;
        Metrics metrics = new Metrics(this);
        metrics.addCustomChart(new Metrics.SimplePie("distributeversion", () -> "Open source"));
        blockSkinProvider = new DefaultBlockSkinProvider();
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(this, this);
        pluginManager.registerEvents(new CounterListener(), this);
        pluginManager.registerEvents(new HighlightListener(), this);
        pluginManager.registerEvents(new TriggerBlockListener(), this);
        // 资源包
        // pluginManager.registerEvents(new ResourcePackLoader(), this);
        getCommand("bridge").setExecutor(new BridgeCommand());
        getCommand("clearblock").setExecutor(new ClearCommand());
        getCommand("bsaveworld").setExecutor(new SaveWorldCommand());
        getCommand("imstuck").setExecutor(new StuckCommand());
        getCommand("genvillager").setExecutor(new VillagerSpawnPointCommand());
        spawnVillager();
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            if (Bukkit.getOnlinePlayers().isEmpty()) return;
            spawnVillager();
        }, 300, 300);

        Bukkit.getConsoleSender().sendMessage(new String[]{
                "§bBridgingAnalyzer §7>> §f----------------------------------------------------------------",
                "§bBridgingAnalyzer §7>> §a搭路练习 已加载 §bBy.SakuraKooi",
                "§bBridgingAnalyzer §7>> §chttps://github.com/SakuraKoi/BridgingAnalyzer/",
                "§bBridgingAnalyzer §7>> §f----------------------------------------------------------------",
                "§bBridgingAnalyzer §7>> §e踩在 §a绿宝石块 §e上可以设置传送点",
                "§bBridgingAnalyzer §7>> §e踩在 §c红石块 §e上可以回到传送点",
                "§bBridgingAnalyzer §7>> §e踩在 §b青金石块 §e上可以回到出生点",
                "§bBridgingAnalyzer §7>> §e使用 §a/genvillager §e可在站立位置创建村民刷新点",
                "§bBridgingAnalyzer §7>> §c掉入虚空会自动回到 §a传送点 §c并重置地图",
                "§bBridgingAnalyzer §7>> §c注意: 创造模式放置的方块不会被重置, 请在生存模式下练习",
                "§bBridgingAnalyzer §7>> §f----------------------------------------------------------------"
        });
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.getPlayer().sendMessage(new String[]{
                "§b§l搭路练习 §7>> §e输入 §6/bridge §e更改练习参数",
                "§b§l搭路练习 §7>> §6Bilibili @SakuraKooi"
        });
        if (e.getPlayer().hasPermission("bridginganalyzer.noclear")) return;
        teleportCheckPoint(e.getPlayer());
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent e) {
        if (e.getPlayer().hasPermission("bridginganalyzer.noclear")) return;
        if (e.getItemDrop().getItemStack().getType() == Material.GOLD_PICKAXE) {
            e.getItemDrop().remove();
        }
    }

    @EventHandler
    public void onPvP(EntityDamageByEntityEvent e) {
        if (e.isCancelled()) return;
        if (e.getEntity() == null) return;
        if (e.getDamager() == null) return;
        if (e.getEntity().getType() == EntityType.PLAYER) if (e.getDamager().getType() == EntityType.PLAYER) {
            int state = onPvPDamage((Player) e.getEntity(), (Player) e.getDamager());
            if (state == -1) {
                e.setCancelled(true);
            } else if (state == 1) {
                e.setCancelled(true);
                BridgingAnalyzer.getCounter((Player) e.getDamager()).setPvPEnabled(true);
                TitleUtils.sendTitle((Player) e.getDamager(), "", "§c注意: §aPvP已开启", 10, 20, 10);
                ((Player) e.getEntity()).damage(0.00);
                ((Player) e.getEntity()).setNoDamageTicks(60);
                ((Player) e.getDamager()).setNoDamageTicks(60);
            }
        } else if (e.getDamager() instanceof Projectile) {
            Projectile proj = (Projectile) e.getDamager();
            if (proj.getShooter() instanceof Player) {
                int state = onPvPDamage((Player) e.getEntity(), (Player) proj.getShooter());
                if (state == -1) {
                    e.setCancelled(true);
                } else if (state == 1) {
                    e.setCancelled(true);
                    BridgingAnalyzer.getCounter((Player) proj.getShooter()).setPvPEnabled(true);
                    TitleUtils.sendTitle((Player) proj.getShooter(), "", "§c注意: §aPvP已开启", 10, 20, 10);
                    ((Player) e.getEntity()).damage(0.00);
                    ((Player) e.getEntity()).setNoDamageTicks(60);
                    ((Player) proj.getShooter()).setNoDamageTicks(60);
                }
            }
        }
    }

    private int onPvPDamage(Player player, Player damager) {
        if (!BridgingAnalyzer.getCounter(player).isPvPEnabled()) return -1; // cancel
        if (!BridgingAnalyzer.getCounter(damager).isPvPEnabled()) return 1; // enable
        return 0; // accept
    }

}
