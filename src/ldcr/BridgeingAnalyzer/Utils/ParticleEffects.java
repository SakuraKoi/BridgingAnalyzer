package ldcr.BridgeingAnalyzer.Utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public enum ParticleEffects {
    EXPLOSION_NORMAL("explode", 0, -1, new ParticleProperty[] { ParticleProperty.DIRECTIONAL }), EXPLOSION_LARGE(
	    "largeexplode", 1, -1,
	    new ParticleProperty[0]), EXPLOSION_HUGE("hugeexplosion", 2, -1, new ParticleProperty[0]), FIREWORKS_SPARK(
		    "fireworksSpark", 3, -1,
		    new ParticleProperty[] { ParticleProperty.DIRECTIONAL }), WATER_BUBBLE("bubble", 4, -1,
			    new ParticleProperty[] { ParticleProperty.DIRECTIONAL,
				    ParticleProperty.REQUIRES_WATER }), WATER_SPLASH("splash", 5, -1,
					    new ParticleProperty[] { ParticleProperty.DIRECTIONAL }), WATER_WAKE("wake",
						    6, 7,
						    new ParticleProperty[] { ParticleProperty.DIRECTIONAL }), SUSPENDED(
							    "suspended", 7, -1,
							    new ParticleProperty[] {
								    ParticleProperty.REQUIRES_WATER }), SUSPENDED_DEPTH(
									    "depthSuspend", 8, -1,
									    new ParticleProperty[] {
										    ParticleProperty.DIRECTIONAL }), CRIT(
											    "crit", 9, -1,
											    new ParticleProperty[] {
												    ParticleProperty.DIRECTIONAL }), CRIT_MAGIC(
													    "magicCrit",
													    10, -1,
													    new ParticleProperty[] {
														    ParticleProperty.DIRECTIONAL }), SMOKE_NORMAL(
															    "smoke",
															    11,
															    -1,
															    new ParticleProperty[] {
																    ParticleProperty.DIRECTIONAL }), SMOKE_LARGE(
																	    "largesmoke",
																	    12,
																	    -1,
																	    new ParticleProperty[] {
																		    ParticleProperty.DIRECTIONAL }), SPELL(
																			    "spell",
																			    13,
																			    -1,
																			    new ParticleProperty[0]), SPELL_INSTANT(
																				    "instantSpell",
																				    14,
																				    -1,
																				    new ParticleProperty[0]), SPELL_MOB(
																					    "mobSpell",
																					    15,
																					    -1,
																					    new ParticleProperty[] {
																						    ParticleProperty.COLORABLE }), SPELL_MOB_AMBIENT(
																							    "mobSpellAmbient",
																							    16,
																							    -1,
																							    new ParticleProperty[] {
																								    ParticleProperty.COLORABLE }), SPELL_WITCH(
																									    "witchMagic",
																									    17,
																									    -1,
																									    new ParticleProperty[0]), DRIP_WATER(
																										    "dripWater",
																										    18,
																										    -1,
																										    new ParticleProperty[0]), DRIP_LAVA(
																											    "dripLava",
																											    19,
																											    -1,
																											    new ParticleProperty[0]), VILLAGER_ANGRY(
																												    "angryVillager",
																												    20,
																												    -1,
																												    new ParticleProperty[0]), VILLAGER_HAPPY(
																													    "happyVillager",
																													    21,
																													    -1,
																													    new ParticleProperty[] {
																														    ParticleProperty.DIRECTIONAL }), TOWN_AURA(
																															    "townaura",
																															    22,
																															    -1,
																															    new ParticleProperty[] {
																																    ParticleProperty.DIRECTIONAL }), NOTE(
																																	    "note",
																																	    23,
																																	    -1,
																																	    new ParticleProperty[] {
																																		    ParticleProperty.COLORABLE }), PORTAL(
																																			    "portal",
																																			    24,
																																			    -1,
																																			    new ParticleProperty[] {
																																				    ParticleProperty.DIRECTIONAL }), ENCHANTMENT_TABLE(
																																					    "enchantmenttable",
																																					    25,
																																					    -1,
																																					    new ParticleProperty[] {
																																						    ParticleProperty.DIRECTIONAL }), FLAME(
																																							    "flame",
																																							    26,
																																							    -1,
																																							    new ParticleProperty[] {
																																								    ParticleProperty.DIRECTIONAL }), LAVA(
																																									    "lava",
																																									    27,
																																									    -1,
																																									    new ParticleProperty[0]), FOOTSTEP(
																																										    "footstep",
																																										    28,
																																										    -1,
																																										    new ParticleProperty[0]), CLOUD(
																																											    "cloud",
																																											    29,
																																											    -1,
																																											    new ParticleProperty[] {
																																												    ParticleProperty.DIRECTIONAL }), REDSTONE(
																																													    "reddust",
																																													    30,
																																													    -1,
																																													    new ParticleProperty[] {
																																														    ParticleProperty.COLORABLE }), SNOWBALL(
																																															    "snowballpoof",
																																															    31,
																																															    -1,
																																															    new ParticleProperty[0]), SNOW_SHOVEL(
																																																    "snowshovel",
																																																    32,
																																																    -1,
																																																    new ParticleProperty[] {
																																																	    ParticleProperty.DIRECTIONAL }), SLIME(
																																																		    "slime",
																																																		    33,
																																																		    -1,
																																																		    new ParticleProperty[0]), HEART(
																																																			    "heart",
																																																			    34,
																																																			    -1,
																																																			    new ParticleProperty[0]), BARRIER(
																																																				    "barrier",
																																																				    35,
																																																				    8,
																																																				    new ParticleProperty[0]), ITEM_CRACK(
																																																					    "iconcrack",
																																																					    36,
																																																					    -1,
																																																					    new ParticleProperty[] {
																																																						    ParticleProperty.DIRECTIONAL,
																																																						    ParticleProperty.REQUIRES_DATA }), BLOCK_CRACK(
																																																							    "blockcrack",
																																																							    37,
																																																							    -1,
																																																							    new ParticleProperty[] {
																																																								    ParticleProperty.DIRECTIONAL,
																																																								    ParticleProperty.REQUIRES_DATA }), BLOCK_DUST(
																																																									    "blockdust",
																																																									    38,
																																																									    7,
																																																									    new ParticleProperty[] {
																																																										    ParticleProperty.DIRECTIONAL,
																																																										    ParticleProperty.REQUIRES_DATA }), WATER_DROP(
																																																											    "droplet",
																																																											    39,
																																																											    8,
																																																											    new ParticleProperty[0]), ITEM_TAKE(
																																																												    "take",
																																																												    40,
																																																												    8,
																																																												    new ParticleProperty[0]), MOB_APPEARANCE(
																																																													    "mobappearance",
																																																													    41,
																																																													    8,
																																																													    new ParticleProperty[0]);

    public static final class BlockData extends ParticleEffects.ParticleData {
	public BlockData(final Material material, final byte data) throws IllegalArgumentException {
	    super(material, data);
	    if (!material.isBlock())
		throw new IllegalArgumentException("The material is not a block");
	}
    }
    public static final class ItemData extends ParticleEffects.ParticleData {
	public ItemData(final Material material, final byte data) {
	    super(material, data);
	}
    }
    public static final class NoteColor extends ParticleEffects.ParticleColor {
	private final int note;

	public NoteColor(final int note) throws IllegalArgumentException {
	    if (note < 0)
		throw new IllegalArgumentException("The note value is lower than 0");
	    if (note > 24)
		throw new IllegalArgumentException("The note value is higher than 24");
	    this.note = note;
	}

	@Override
	public float getValueX() {
	    return note / 24.0F;
	}

	@Override
	public float getValueY() {
	    return 0.0F;
	}

	@Override
	public float getValueZ() {
	    return 0.0F;
	}
    }
    public static final class OrdinaryColor extends ParticleEffects.ParticleColor {
	private final int red;
	private final int green;
	private final int blue;

	public OrdinaryColor(final int red, final int green, final int blue) throws IllegalArgumentException {
	    if (red < 0)
		throw new IllegalArgumentException("The red value is lower than 0");
	    if (red > 255)
		throw new IllegalArgumentException("The red value is higher than 255");
	    this.red = red;
	    if (green < 0)
		throw new IllegalArgumentException("The green value is lower than 0");
	    if (green > 255)
		throw new IllegalArgumentException("The green value is higher than 255");
	    this.green = green;
	    if (blue < 0)
		throw new IllegalArgumentException("The blue value is lower than 0");
	    if (blue > 255)
		throw new IllegalArgumentException("The blue value is higher than 255");
	    this.blue = blue;
	}

	public int getBlue() {
	    return blue;
	}

	public int getGreen() {
	    return green;
	}

	public int getRed() {
	    return red;
	}

	@Override
	public float getValueX() {
	    return red / 255.0F;
	}

	@Override
	public float getValueY() {
	    return green / 255.0F;
	}

	@Override
	public float getValueZ() {
	    return blue / 255.0F;
	}
    }
    public static abstract class ParticleColor {
	public abstract float getValueX();

	public abstract float getValueY();

	public abstract float getValueZ();
    }
    private static final class ParticleColorException extends RuntimeException {
	private static final long serialVersionUID = 3203085387160737484L;

	public ParticleColorException(final String message) {
	    super();
	}
    }

    public static abstract class ParticleData {
	private final Material material;
	private final byte data;
	private final int[] packetData;

	@SuppressWarnings("deprecation")
	public ParticleData(final Material material, final byte data) {
	    this.material = material;
	    this.data = data;
	    packetData = new int[] { material.getId(), data };
	}

	public byte getData() {
	    return data;
	}

	public Material getMaterial() {
	    return material;
	}

	public int[] getPacketData() {
	    return packetData;
	}

	public String getPacketDataString() {
	    return "_" + packetData[0] + "_" + packetData[1];
	}
    }

    private static final class ParticleDataException extends RuntimeException {
	private static final long serialVersionUID = 3203085387160737484L;

	public ParticleDataException(final String message) {
	    super();
	}
    }

    public static final class ParticlePacket {
	private static final class PacketInstantiationException extends RuntimeException {
	    private static final long serialVersionUID = 3203085387160737484L;

	    public PacketInstantiationException(final String message, final Throwable cause) {
		super(cause);
	    }
	}
	private static final class PacketSendingException extends RuntimeException {
	    private static final long serialVersionUID = 3203085387160737484L;

	    public PacketSendingException(final String message, final Throwable cause) {
		super(cause);
	    }
	}
	private static final class VersionIncompatibleException extends RuntimeException {
	    private static final long serialVersionUID = 3203085387160737484L;

	    public VersionIncompatibleException(final String message, final Throwable cause) {
		super(cause);
	    }
	}
	private static int version;
	private static Class<?> enumParticle;
	private static Constructor<?> packetConstructor;
	private static Method getHandle;
	private static Field playerConnection;
	private static Method sendPacket;
	private static boolean initialized;
	public static int getVersion() {
	    return version;
	}
	public static void initialize() throws ParticleEffects.ParticlePacket.VersionIncompatibleException {
	    if (initialized)
		return;
	    try {
		version = Integer
			.parseInt(Character.toString(ReflectionUtils.PackageType.getServerVersion().charAt(3)));
		if (version > 7) {
		    enumParticle = ReflectionUtils.PackageType.MINECRAFT_SERVER.getClass("EnumParticle");
		}
		final Class<?> packetClass = ReflectionUtils.PackageType.MINECRAFT_SERVER
			.getClass(version < 7 ? "Packet63WorldParticles" : "PacketPlayOutWorldParticles");
		packetConstructor = ReflectionUtils.getConstructor(packetClass, new Class[0]);
		getHandle = ReflectionUtils.getMethod("CraftPlayer", ReflectionUtils.PackageType.CRAFTBUKKIT_ENTITY,
			"getHandle", new Class[0]);
		playerConnection = ReflectionUtils.getField("EntityPlayer",
			ReflectionUtils.PackageType.MINECRAFT_SERVER, false, "playerConnection");
		sendPacket = ReflectionUtils.getMethod(playerConnection.getType(), "sendPacket",
			new Class[] { ReflectionUtils.PackageType.MINECRAFT_SERVER.getClass("Packet") });
	    } catch (final Exception exception) {
		throw new VersionIncompatibleException(
			"Your current bukkit version seems to be incompatible with this library", exception);
	    }
	    initialized = true;
	}
	public static boolean isInitialized() {
	    return initialized;
	}
	private final ParticleEffects effect;
	private final float offsetX;
	private final float offsetY;

	private final float offsetZ;

	private final float speed;

	private final int amount;

	private final boolean longDistance;

	private final ParticleEffects.ParticleData data;

	private Object packet;

	public ParticlePacket(final ParticleEffects effect, final float offsetX, final float offsetY,
		final float offsetZ, final float speed, final int amount, final boolean longDistance,
		final ParticleEffects.ParticleData data) throws IllegalArgumentException {
	    initialize();
	    if (speed < 0.0F)
		throw new IllegalArgumentException("The speed is lower than 0");
	    if (amount < 0)
		throw new IllegalArgumentException("The amount is lower than 0");
	    this.effect = effect;
	    this.offsetX = offsetX;
	    this.offsetY = offsetY;
	    this.offsetZ = offsetZ;
	    this.speed = speed;
	    this.amount = amount;
	    this.longDistance = longDistance;
	    this.data = data;
	}

	public ParticlePacket(final ParticleEffects effect, final ParticleEffects.ParticleColor color,
		final boolean longDistance) {
	    this(effect, color.getValueX(), color.getValueY(), color.getValueZ(), 1.0F, 0, longDistance, null);
	}

	public ParticlePacket(final ParticleEffects effect, final Vector direction, final float speed,
		final boolean longDistance, final ParticleEffects.ParticleData data) throws IllegalArgumentException {
	    this(effect, (float) direction.getX(), (float) direction.getY(), (float) direction.getZ(), speed, 0,
		    longDistance, data);
	}

	private void initializePacket(final Location center)
		throws ParticleEffects.ParticlePacket.PacketInstantiationException {
	    if (packet != null)
		return;
	    try {
		packet = packetConstructor.newInstance(new Object[0]);
		if (version < 8) {
		    String name = effect.getName();
		    if (data != null) {
			name = name + data.getPacketDataString();
		    }
		    ReflectionUtils.setValue(packet, true, "a", name);
		} else {
		    ReflectionUtils.setValue(packet, true, "a", enumParticle.getEnumConstants()[effect.getId()]);
		    ReflectionUtils.setValue(packet, true, "j", Boolean.valueOf(longDistance));
		    if (data != null) {
			ReflectionUtils.setValue(packet, true, "k", data.getPacketData());
		    }
		}
		ReflectionUtils.setValue(packet, true, "b", Float.valueOf((float) center.getX()));
		ReflectionUtils.setValue(packet, true, "c", Float.valueOf((float) center.getY()));
		ReflectionUtils.setValue(packet, true, "d", Float.valueOf((float) center.getZ()));
		ReflectionUtils.setValue(packet, true, "e", Float.valueOf(offsetX));
		ReflectionUtils.setValue(packet, true, "f", Float.valueOf(offsetY));
		ReflectionUtils.setValue(packet, true, "g", Float.valueOf(offsetZ));
		ReflectionUtils.setValue(packet, true, "h", Float.valueOf(speed));
		ReflectionUtils.setValue(packet, true, "i", Integer.valueOf(amount));
	    } catch (final Exception exception) {
		throw new PacketInstantiationException("Packet instantiation failed", exception);
	    }
	}

	public void sendTo(final Location center, final double range) throws IllegalArgumentException {
	    if (range < 1.0D)
		throw new IllegalArgumentException("The range is lower than 1");
	    final String worldName = center.getWorld().getName();
	    final double squared = range * range;
	    for (final Player player : Bukkit.getOnlinePlayers()) {
		if ((player.getWorld().getName().equals(worldName))
			&& (player.getLocation().distanceSquared(center) <= squared)) {
		    sendTo(center, player);
		}
	    }
	}

	public void sendTo(final Location center, final List<Player> players) throws IllegalArgumentException {
	    if (players.isEmpty())
		throw new IllegalArgumentException("The player list is empty");
	    for (final Player player : players) {
		sendTo(center, player);
	    }
	}

	public void sendTo(final Location center, final Player player)
		throws ParticleEffects.ParticlePacket.PacketInstantiationException,
		ParticleEffects.ParticlePacket.PacketSendingException {
	    initializePacket(center);
	    try {
		sendPacket.invoke(playerConnection.get(getHandle.invoke(player, new Object[0])),
			new Object[] { packet });
	    } catch (final Exception exception) {
		throw new PacketSendingException("Failed to send the packet to player '" + player.getName() + "'",
			exception);
	    }
	}
    }

    /*
     * public static ParticleEffects getEffect(String name) { for
     * (ParticleEffects e : ) { if (e.name().equalsIgnoreCase(name)) { return e;
     * } } return null; }
     */
    public static enum ParticleProperty {
	REQUIRES_WATER, REQUIRES_DATA, DIRECTIONAL, COLORABLE;

	private ParticleProperty() {
	}
    }

    private static final class ParticleVersionException extends RuntimeException {
	private static final long serialVersionUID = 3203085387160737484L;

	public ParticleVersionException(final String message) {
	    super();
	}
    }

    private static final Map<String, ParticleEffects> NAME_MAP;

    private static final Map<Integer, ParticleEffects> ID_MAP;

    static {
	NAME_MAP = new HashMap<String, ParticleEffects>();
	ID_MAP = new HashMap<Integer, ParticleEffects>();
	for (final ParticleEffects effect : values()) {
	    NAME_MAP.put(effect.name, effect);
	    ID_MAP.put(Integer.valueOf(effect.id), effect);
	}
    }

    public static ParticleEffects fromId(final int id) {
	for (final Map.Entry<Integer, ParticleEffects> entry : ID_MAP.entrySet()) {
	    if (entry.getKey().intValue() == id)
		return entry.getValue();
	}
	return null;
    }

    public static ParticleEffects fromName(final String name) {
	for (final Map.Entry<String, ParticleEffects> entry : NAME_MAP.entrySet()) {
	    if (entry.getKey().equalsIgnoreCase(name))
		return entry.getValue();
	}
	return null;
    }

    private static boolean isColorCorrect(final ParticleEffects effect, final ParticleColor color) {
	return ((effect != SPELL_MOB) && (effect != SPELL_MOB_AMBIENT) && (effect != REDSTONE))
		|| (((color instanceof OrdinaryColor)) || ((effect == NOTE) && ((color instanceof NoteColor))));
    }

    private static boolean isDataCorrect(final ParticleEffects effect, final ParticleData data) {
	return ((effect != BLOCK_CRACK) && (effect != BLOCK_DUST))
		|| (((data instanceof BlockData)) || ((effect == ITEM_CRACK) && ((data instanceof ItemData))));
    }

    private static boolean isLongDistance(final Location location, final List<Player> players) {
	for (final Player player : players) {
	    if (player.getLocation().distanceSquared(location) >= 65536.0D)
		return true;
	}
	return false;
    }

    private static boolean isWater(final Location location) {
	final Material material = location.getBlock().getType();
	return (material == Material.WATER) || (material == Material.STATIONARY_WATER);
    }

    private final String name;

    private final int id;

    private final int requiredVersion;

    private final List<ParticleProperty> properties;

    private ParticleEffects(final String name, final int id, final int requiredVersion,
	    final ParticleProperty... properties) {
	this.name = name;
	this.id = id;
	this.requiredVersion = requiredVersion;
	this.properties = Arrays.asList(properties);
    }

    public void display(final float offsetX, final float offsetY, final float offsetZ, final float speed,
	    final int amount, final Location center, final double range)
	    throws ParticleEffects.ParticleVersionException, ParticleEffects.ParticleDataException,
	    IllegalArgumentException {
	if (!isSupported())
	    throw new ParticleVersionException("This particle effect is not supported by your server version");
	if (hasProperty(ParticleProperty.REQUIRES_DATA))
	    throw new ParticleDataException("This particle effect requires additional data");
	if ((hasProperty(ParticleProperty.REQUIRES_WATER)) && (!isWater(center)))
	    throw new IllegalArgumentException("There is no water at the center location");
	new ParticlePacket(this, offsetX, offsetY, offsetZ, speed, amount, range > 256.0D, null).sendTo(center, range);
    }

    public void display(final float offsetX, final float offsetY, final float offsetZ, final float speed,
	    final int amount, final Location center, final List<Player> players)
	    throws ParticleEffects.ParticleVersionException, ParticleEffects.ParticleDataException,
	    IllegalArgumentException {
	if (!isSupported())
	    throw new ParticleVersionException("This particle effect is not supported by your server version");
	if (hasProperty(ParticleProperty.REQUIRES_DATA))
	    throw new ParticleDataException("This particle effect requires additional data");
	if ((hasProperty(ParticleProperty.REQUIRES_WATER)) && (!isWater(center)))
	    throw new IllegalArgumentException("There is no water at the center location");
	new ParticlePacket(this, offsetX, offsetY, offsetZ, speed, amount, isLongDistance(center, players), null)
		.sendTo(center, players);
    }

    public void display(final float offsetX, final float offsetY, final float offsetZ, final float speed,
	    final int amount, final Location center, final Player... players)
	    throws ParticleEffects.ParticleVersionException, ParticleEffects.ParticleDataException,
	    IllegalArgumentException {
	display(offsetX, offsetY, offsetZ, speed, amount, center, Arrays.asList(players));
    }

    public void display(final int amount, final Location center, final double range)
	    throws ParticleEffects.ParticleVersionException, ParticleEffects.ParticleDataException,
	    IllegalArgumentException {
	this.display(0f, 0f, 0f, 0f, amount, center, range);
    }

    public void display(final Location center, final double range) throws ParticleEffects.ParticleVersionException,
	    ParticleEffects.ParticleDataException, IllegalArgumentException {
	this.display(0f, 0f, 0f, 0f, 1, center, range);
    }

    public void display(final ParticleColor color, final Location center, final double range)
	    throws ParticleEffects.ParticleVersionException, ParticleEffects.ParticleColorException {
	if (!isSupported())
	    throw new ParticleVersionException("This particle effect is not supported by your server version");
	if (!hasProperty(ParticleProperty.COLORABLE))
	    throw new ParticleColorException("This particle effect is not colorable");
	if (!isColorCorrect(this, color))
	    throw new ParticleColorException("The particle color type is incorrect");
	new ParticlePacket(this, color, range > 256.0D).sendTo(center, range);
    }

    public void display(final ParticleColor color, final Location center, final List<Player> players)
	    throws ParticleEffects.ParticleVersionException, ParticleEffects.ParticleColorException {
	if (!isSupported())
	    throw new ParticleVersionException("This particle effect is not supported by your server version");
	if (!hasProperty(ParticleProperty.COLORABLE))
	    throw new ParticleColorException("This particle effect is not colorable");
	if (!isColorCorrect(this, color))
	    throw new ParticleColorException("The particle color type is incorrect");
	new ParticlePacket(this, color, isLongDistance(center, players)).sendTo(center, players);
    }

    public void display(final ParticleColor color, final Location center, final Player... players)
	    throws ParticleEffects.ParticleVersionException, ParticleEffects.ParticleColorException {
	display(color, center, Arrays.asList(players));
    }

    public void display(final ParticleData data, final Color color, float offsetX, float offsetY, float offsetZ,
	    float speed, int amount, final Location center, final double range) {
	if ((color != null) && ((this == ParticleEffects.REDSTONE) || (this == ParticleEffects.SPELL_MOB)
		|| (this == ParticleEffects.SPELL_MOB_AMBIENT))) {
	    amount = 0;
	    if (speed == 0.0f) {
		speed = 1.0f;
	    }
	    offsetX = color.getRed() / 255.0f;
	    offsetY = color.getGreen() / 255.0f;
	    offsetZ = color.getBlue() / 255.0f;
	    if (offsetX < Float.MIN_NORMAL) {
		offsetX = Float.MIN_NORMAL;
	    }
	}
	if (hasProperty(ParticleProperty.REQUIRES_DATA)) {
	    this.display(data, offsetX, offsetY, offsetZ, speed, amount, center, range);
	} else {
	    this.display(offsetX, offsetY, offsetZ, speed, amount, center, range);
	}
    }

    public void display(final ParticleData data, final float offsetX, final float offsetY, final float offsetZ,
	    final float speed, final int amount, final Location center, final double range)
	    throws ParticleEffects.ParticleVersionException, ParticleEffects.ParticleDataException {
	if (!isSupported())
	    throw new ParticleVersionException("This particle effect is not supported by your server version");
	if (!hasProperty(ParticleProperty.REQUIRES_DATA))
	    throw new ParticleDataException("This particle effect does not require additional data");
	if (!isDataCorrect(this, data))
	    throw new ParticleDataException("The particle data type is incorrect");
	new ParticlePacket(this, offsetX, offsetY, offsetZ, speed, amount, range > 256.0D, data).sendTo(center, range);
    }

    public void display(final ParticleData data, final float offsetX, final float offsetY, final float offsetZ,
	    final float speed, final int amount, final Location center, final List<Player> players)
	    throws ParticleEffects.ParticleVersionException, ParticleEffects.ParticleDataException {
	if (!isSupported())
	    throw new ParticleVersionException("This particle effect is not supported by your server version");
	if (!hasProperty(ParticleProperty.REQUIRES_DATA))
	    throw new ParticleDataException("This particle effect does not require additional data");
	if (!isDataCorrect(this, data))
	    throw new ParticleDataException("The particle data type is incorrect");
	new ParticlePacket(this, offsetX, offsetY, offsetZ, speed, amount, isLongDistance(center, players), data)
		.sendTo(center, players);
    }

    public void display(final ParticleData data, final float offsetX, final float offsetY, final float offsetZ,
	    final float speed, final int amount, final Location center, final Player... players)
	    throws ParticleEffects.ParticleVersionException, ParticleEffects.ParticleDataException {
	display(data, offsetX, offsetY, offsetZ, speed, amount, center, Arrays.asList(players));
    }

    public void display(final ParticleData data, final Vector direction, final float speed, final Location center,
	    final double range) throws ParticleEffects.ParticleVersionException, ParticleEffects.ParticleDataException {
	if (!isSupported())
	    throw new ParticleVersionException("This particle effect is not supported by your server version");
	if (!hasProperty(ParticleProperty.REQUIRES_DATA))
	    throw new ParticleDataException("This particle effect does not require additional data");
	if (!isDataCorrect(this, data))
	    throw new ParticleDataException("The particle data type is incorrect");
	new ParticlePacket(this, direction, speed, range > 256.0D, data).sendTo(center, range);
    }

    public void display(final ParticleData data, final Vector direction, final float speed, final Location center,
	    final List<Player> players)
	    throws ParticleEffects.ParticleVersionException, ParticleEffects.ParticleDataException {
	if (!isSupported())
	    throw new ParticleVersionException("This particle effect is not supported by your server version");
	if (!hasProperty(ParticleProperty.REQUIRES_DATA))
	    throw new ParticleDataException("This particle effect does not require additional data");
	if (!isDataCorrect(this, data))
	    throw new ParticleDataException("The particle data type is incorrect");
	new ParticlePacket(this, direction, speed, isLongDistance(center, players), data).sendTo(center, players);
    }

    public void display(final ParticleData data, final Vector direction, final float speed, final Location center,
	    final Player... players)
	    throws ParticleEffects.ParticleVersionException, ParticleEffects.ParticleDataException {
	display(data, direction, speed, center, Arrays.asList(players));
    }

    public void display(final Vector direction, final float speed, final Location center, final double range)
	    throws ParticleEffects.ParticleVersionException, ParticleEffects.ParticleDataException,
	    IllegalArgumentException {
	if (!isSupported())
	    throw new ParticleVersionException("This particle effect is not supported by your server version");
	if (hasProperty(ParticleProperty.REQUIRES_DATA))
	    throw new ParticleDataException("This particle effect requires additional data");
	if (!hasProperty(ParticleProperty.DIRECTIONAL))
	    throw new IllegalArgumentException("This particle effect is not directional");
	if ((hasProperty(ParticleProperty.REQUIRES_WATER)) && (!isWater(center)))
	    throw new IllegalArgumentException("There is no water at the center location");
	new ParticlePacket(this, direction, speed, range > 256.0D, null).sendTo(center, range);
    }

    public void display(final Vector direction, final float speed, final Location center, final List<Player> players)
	    throws ParticleEffects.ParticleVersionException, ParticleEffects.ParticleDataException,
	    IllegalArgumentException {
	if (!isSupported())
	    throw new ParticleVersionException("This particle effect is not supported by your server version");
	if (hasProperty(ParticleProperty.REQUIRES_DATA))
	    throw new ParticleDataException("This particle effect requires additional data");
	if (!hasProperty(ParticleProperty.DIRECTIONAL))
	    throw new IllegalArgumentException("This particle effect is not directional");
	if ((hasProperty(ParticleProperty.REQUIRES_WATER)) && (!isWater(center)))
	    throw new IllegalArgumentException("There is no water at the center location");
	new ParticlePacket(this, direction, speed, isLongDistance(center, players), null).sendTo(center, players);
    }

    public void display(final Vector direction, final float speed, final Location center, final Player... players)
	    throws ParticleEffects.ParticleVersionException, ParticleEffects.ParticleDataException,
	    IllegalArgumentException {
	display(direction, speed, center, Arrays.asList(players));
    }

    public ParticleData getData(final Material material, Byte blockData) {
	ParticleData data = null;
	if (blockData == null) {
	    blockData = 0;
	}
	if (((this == ParticleEffects.BLOCK_CRACK) || (this == ParticleEffects.ITEM_CRACK)
		|| (this == ParticleEffects.BLOCK_DUST)) && (material != null) && (material != Material.AIR)) {
	    if (this == ParticleEffects.ITEM_CRACK) {
		data = new ItemData(material, blockData);
	    } else {
		data = new BlockData(material, blockData);
	    }
	}
	return data;
    }

    public int getId() {
	return id;
    }

    public String getName() {
	return name;
    }

    public int getRequiredVersion() {
	return requiredVersion;
    }

    public boolean hasProperty(final ParticleProperty property) {
	return properties.contains(property);
    }

    public boolean isSupported() {
	if (requiredVersion == -1)
	    return true;
	return ParticlePacket.getVersion() >= requiredVersion;
    }
}
