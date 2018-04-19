package ldcr.BridgeingAnalyzer;

import java.util.HashMap;
import java.util.HashSet;

import ldcr.BridgeingAnalyzer.Cmmands.BridgeCommand;
import ldcr.BridgeingAnalyzer.Cmmands.CPSCommand;
import ldcr.BridgeingAnalyzer.Cmmands.ClearCommand;
import ldcr.BridgeingAnalyzer.Cmmands.StuckCommand;
import ldcr.BridgeingAnalyzer.Cmmands.VillagerSpawnPointCommand;
import ldcr.BridgeingAnalyzer.Utils.NoAIUtils;
import ldcr.BridgeingAnalyzer.Utils.Util;
import ldcr.BridgeingAnalyzer.hook.skin.ISkinHook;
import ldcr.BridgeingAnalyzer.hook.skin.NoSkinHook;
import ldcr.BridgeingAnalyzer.hook.skin.SkinHook;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BridgingAnalyzer extends JavaPlugin implements Listener {
	public static BridgingAnalyzer instance;
	public static final HashMap<Player, Counter> counter = new HashMap<Player, Counter>();
	public static HashSet<Block> placedBlock = new HashSet<Block>();

	public static Counter getCounter(final Player p) {
		Counter c = counter.get(p);
		if (c == null) {
			c = new Counter(p);
			counter.put(p, c);
		}
		return c;
	}

	private static void spawnVillager() {
		for (final Entity en : Bukkit.getWorld("world").getEntities()) {
			if (en.getType() == EntityType.VILLAGER) {
				if ("靶子→_→".equals(en.getCustomName())) {
					en.remove();
				}
			}
		}
		for (final ArmorStand stand : Bukkit.getWorld("world")
				.getEntitiesByClass(ArmorStand.class)) {
			if (stand.getCustomName() == null) {
				continue;
			}
			if (stand.getCustomName().contains("VillagerSpawnPoint")) {
				final Villager vi = (Villager) stand.getWorld().spawnEntity(
						stand.getLocation().add(0, 1, 0), EntityType.VILLAGER);
				vi.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,
						32766, 254, false, false), true);
				vi.setProfession(Profession.LIBRARIAN);
				vi.setMaxHealth(1);
				vi.setHealth(1);
				vi.setCustomName("靶子→_→");
				vi.setCustomNameVisible(false);
				NoAIUtils.setAI(vi, false);
			}
		}
	}

	public static void teleportCheckPoint(final Player p) {
		p.setFallDistance(0);
		clearInv(p);
		p.getInventory().addItem(skinHook.getItem(p));
		p.setFoodLevel(20);
		p.setHealth(20);
		p.getWorld().setTime(0);
		p.setNoDamageTicks(10);
		getCounter(p).teleportCheckPoint();
		p.setGameMode(GameMode.SURVIVAL);
	}

	public static void clearInv(final Player p) {
		final Inventory inv = p.getInventory();
		for (int i = 0; i < inv.getSize(); i++) {
			final ItemStack item = inv.getItem(i);
			if (item != null)
				if (item.getItemMeta() != null)
					if (item.getItemMeta().getDisplayName() != null)
						if (item.getItemMeta().getDisplayName().contains("Key")) {
							continue;
						}
			inv.setItem(i, null);
		}
	}

	@EventHandler
	public void disableWeather(final WeatherChangeEvent e) {
		e.setCancelled(true);
	}
	@EventHandler
	public void antiArmorStandManipulate(final PlayerArmorStandManipulateEvent e) {
		e.setCancelled(true);
		if (e.getPlayer().getGameMode() == GameMode.CREATIVE
				&& e.getPlayer().isOp()) {
			if (e.getRightClicked().getCustomName()
					.contains("VillagerSpawnPoint")) {
				e.getRightClicked().remove();
				Util.f__kUnknownTitleFadeBug(e.getPlayer(), "", "§a村民刷新点已移除",
						10, 20, 10);
			}
		}
	}

	@EventHandler
	public void disableVillagerShop(final PlayerInteractEntityEvent e) {
		if (e.getRightClicked().getType() == EntityType.VILLAGER) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void logoutBreak(final PlayerQuitEvent e) {
		getCounter(e.getPlayer()).instantBreakBlock();
		Bukkit.getConsoleSender().sendMessage(
				"§b[BridgingAnalyzer] §a玩家 " + e.getPlayer().getName()
				+ " 离线, 已清除其放置的方块.");
	}

	@EventHandler
	public void noHunger(final FoodLevelChangeEvent e) {
		e.setFoodLevel(20);
	}

	private HashMap<Player, Player> lastDamage;

	@EventHandler
	public void onPvP(final EntityDamageByEntityEvent e) {
		if (e.isCancelled())
			return;
		if (e.getEntity() == null)
			return;
		if (e.getDamager() == null)
			return;
		if (e.getEntity().getType() == EntityType.PLAYER) {
			if (e.getDamager().getType() == EntityType.PLAYER) {
				onPvPDamage((Player) e.getEntity(), (Player) e.getDamager());
			} else if (e.getDamager() instanceof Projectile) {
				final Projectile proj = (Projectile) e.getDamager();
				if (proj.getShooter() instanceof Player) {
					onPvPDamage((Player) e.getEntity(),
							(Player) proj.getShooter());
				}
			}
		}
	}

	private void onPvPDamage(final Player player, final Player damager) {
		lastDamage.put(player, damager);
		final Counter gotDamageCounter = BridgingAnalyzer.getCounter(player);
		if (!gotDamageCounter.isDamageEnabled()) {
			gotDamageCounter.setDamageEnabled(true);
			player.sendMessage("§6§l[BridgingAnalyzer] §cHey man! 你的伤害已被自动开启!");
		}
		final Counter damagerCounter = BridgingAnalyzer.getCounter(damager);
		if (!damagerCounter.isDamageEnabled()) {
			damagerCounter.setDamageEnabled(true);
			damager.sendMessage("§6§l[BridgingAnalyzer] §cHey man! 你的伤害已被自动开启!");
		}
	}

	@EventHandler
	public void onDamage(final EntityDamageEvent e) {
		if (e.getEntity().getType() == EntityType.PLAYER) {
			final Counter c = BridgingAnalyzer.getCounter((Player) e
					.getEntity());
			if (e.getFinalDamage() > 20) {
				c.reset();
				teleportCheckPoint((Player) e.getEntity());
				Util.f__kUnknownTitleFadeBug((Player) e.getEntity(), "",
						"§4致命伤害 - " + Util.formatDouble(e.getFinalDamage() / 2)
						+ " ❤", 10, 20, 10);
				e.setDamage(0.0);
			} else if (e.getFinalDamage() > 10) {
				Util.f__kUnknownTitleFadeBug((Player) e.getEntity(), "",
						"§c严重伤害 - " + Util.formatDouble(e.getFinalDamage() / 2)
						+ " ❤", 10, 20, 10);
			}
			if (c.isDamageEnabled()) {
				if (e.isCancelled())
					return;
				if (((Player) e.getEntity()).getHealth() - e.getFinalDamage() <= 0) {
					e.setDamage(0.0);
					c.reset();

					teleportCheckPoint((Player) e.getEntity());
					if (!lastDamage.containsKey(e.getEntity()))
						return;
					try {
						final Player last = lastDamage.get(e.getEntity());
						for (final Player p : Bukkit.getOnlinePlayers()) {
							if (p.getWorld().equals(e.getEntity().getWorld())) {
								p.sendMessage("§c "
										+ last.getName()
										+ " (§4"
										+ Util.formatDouble(last.getHealth() / 2)
										+ "❤§c) 锤死了 " + e.getEntity().getName());
							}
						}
						// BridgingAnalyzer.clearEffect(last);

					} catch (final Exception ex) {
					} finally {
						lastDamage.remove(e.getEntity());
					}
				}
				return;
			}
			e.setDamage(0.0);
		}
	}

	@Override
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage(
				"§b[BridgingAnalyzer] §c正在清除所有已放置方块....");
		for (final Counter c : counter.values()) {
			c.instantBreakBlock();
		}
		counter.clear();
		for (final Block b : Counter.scheduledBreakBlocks) {
			b.setType(Material.AIR);
		}
		Counter.scheduledBreakBlocks.clear();
		Bukkit.getConsoleSender().sendMessage("§b[BridgingAnalyzer] §a方块清除完毕.");
	}

	private static ISkinHook skinHook;

	@Override
	public void onEnable() {
		instance = this;
		lastDamage = new HashMap<Player, Player>();
		if (Bukkit.getPluginManager().isPluginEnabled("BridgingSkin")) {
			skinHook = new SkinHook();
		} else {
			skinHook = new NoSkinHook();
		}

		Bukkit.getPluginManager().registerEvents(this, this);
		Bukkit.getPluginManager().registerEvents(new CounterListener(), this);
		Bukkit.getPluginManager().registerEvents(
				new WallJumpHighlightListener(), this);
		Bukkit.getPluginManager().registerEvents(new TriggerBlockListener(),
				this);

		getCommand("bridge").setExecutor(new BridgeCommand());
		getCommand("clearblock").setExecutor(new ClearCommand());
		getCommand("imstuck").setExecutor(new StuckCommand());
		getCommand("cps").setExecutor(new CPSCommand());
		getCommand("genvillager").setExecutor(new VillagerSpawnPointCommand());

		spawnVillager();
		Util.titleFadeBugFix_ClearTask();
		Bukkit.getScheduler().runTaskTimer(this, new Runnable() {

			@Override
			public void run() {
				if (Bukkit.getOnlinePlayers().isEmpty())
					return;
				if (Bukkit.getWorld("world").getPlayers().isEmpty())
					return;
				spawnVillager();
			}

		}, 300, 300);
		Bukkit.getConsoleSender().sendMessage(
				"§b[BridgingAnalyzer] §d起床战争操作练习专用插件 已加载 §bBy.Ldcr");
		Bukkit.getConsoleSender().sendMessage(
				"§b[BridgingAnalyzer] §e踩在 §a绿宝石块 §e上可以设置传送点");
		Bukkit.getConsoleSender().sendMessage(
				"§b[BridgingAnalyzer] §e踩在 §c红石块 §e上可以回到传送点");
		Bukkit.getConsoleSender().sendMessage(
				"§b[BridgingAnalyzer] §e踩在 §b青金石块 §e上可以回到出生点");
		Bukkit.getConsoleSender().sendMessage(
				"§b[BridgingAnalyzer] §e放置 §a盔甲架 §e在下次启动服务器时会变为村民刷新点");
		Bukkit.getConsoleSender().sendMessage(
				"§b[BridgingAnalyzer] §c掉入虚空会自动回到 §a传送点 §c并重置地图");
		Bukkit.getConsoleSender().sendMessage(
				"§b[BridgingAnalyzer] §c注意: 创造模式放置的方块不会被重置, 请在生存模式下练习");
	}

	public static void clearEffect(final Player player) {
		for (final PotionEffect eff : player.getActivePotionEffects()) {
			if (eff.getType() == PotionEffectType.INVISIBILITY)
				if (player.isOp()) {
					continue;
				}
			player.removePotionEffect(eff.getType());
		}
	}

}
