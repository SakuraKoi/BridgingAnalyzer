package sakura.kooi.BridgingAnalyzer.utils;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum ParticleEffects {
    FIREWORKS_SPARK("fireworksSpark", 3, -1, ParticleProperty.DIRECTIONAL),
    SPELL_MOB("mobSpell", 15, -1, ParticleProperty.COLORABLE),
    SPELL_MOB_AMBIENT("mobSpellAmbient", 16, -1, ParticleProperty.COLORABLE),
    SPELL_WITCH("witchMagic", 17, -1),
    TOWN_AURA("townaura", 22, -1, ParticleProperty.DIRECTIONAL),
    NOTE("note", 23, -1, ParticleProperty.COLORABLE),
    CLOUD("cloud", 29, -1, ParticleProperty.DIRECTIONAL),
    REDSTONE("reddust", 30, -1, ParticleProperty.COLORABLE),
    ITEM_CRACK("iconcrack", 36, -1, ParticleProperty.DIRECTIONAL, ParticleProperty.REQUIRES_DATA),
    BLOCK_CRACK("blockcrack", 37, -1, ParticleProperty.DIRECTIONAL, ParticleProperty.REQUIRES_DATA),
    BLOCK_DUST("blockdust", 38, 7, ParticleProperty.DIRECTIONAL, ParticleProperty.REQUIRES_DATA);

    private static Map<String, ParticleEffects> NAME_MAP;
    private static Map<Integer, ParticleEffects> ID_MAP;

    static {
        NAME_MAP = new HashMap<>();
        ID_MAP = new HashMap<>();
        for (ParticleEffects effect : values()) {
            NAME_MAP.put(effect.name, effect);
            ID_MAP.put(effect.id, effect);
        }
    }

    private String name;
    private int id;
    private int requiredVersion;
    private List<ParticleProperty> properties;

    ParticleEffects(String name, int id, int requiredVersion,
                    ParticleProperty... properties) {
        this.name = name;
        this.id = id;
        this.requiredVersion = requiredVersion;
        this.properties = Arrays.asList(properties);
    }

    private static boolean isWater(Location location) {
        Material material = location.getBlock().getType();
        return material == Material.WATER
                || material == Material.STATIONARY_WATER;
    }

    private static boolean isLongDistance(Location location,
                                          List<Player> players) {
        for (Player player : players) {
            if (player.getLocation().distanceSquared(location) >= 65536.0D)
                return true;
        }
        return false;
    }

    private static boolean isDataCorrect(ParticleEffects effect,
                                         ParticleData data) {
        return effect != BLOCK_CRACK && effect != BLOCK_DUST
                || data instanceof BlockData || effect == ITEM_CRACK && data instanceof ItemData;
    }

    private static boolean isColorCorrect(ParticleEffects effect,
                                          ParticleColor color) {
        return effect != SPELL_MOB && effect != SPELL_MOB_AMBIENT && effect != REDSTONE
                || color instanceof OrdinaryColor || effect == NOTE && color instanceof NoteColor;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public boolean hasProperty(ParticleProperty property) {
        return properties.contains(property);
    }

    public boolean isSupported() {
        if (requiredVersion == -1)
            return true;
        return ParticlePacket.getVersion() >= requiredVersion;
    }

    public void display(float offsetX, float offsetY, float offsetZ,
                        float speed, int amount, Location center, double range)
            throws ParticleEffects.ParticleVersionException,
            ParticleEffects.ParticleDataException, IllegalArgumentException {
        if (!isSupported())
            throw new ParticleVersionException(
                    "This particle effect is not supported by your server version");
        if (hasProperty(ParticleProperty.REQUIRES_DATA))
            throw new ParticleDataException(
                    "This particle effect requires additional data");
        if (hasProperty(ParticleProperty.REQUIRES_WATER)
                && !isWater(center))
            throw new IllegalArgumentException(
                    "There is no water at the center location");
        new ParticlePacket(this, offsetX, offsetY, offsetZ, speed, amount,
                range > 256.0D, null).sendTo(center, range);
    }

    public void display(int amount, Location center, double range)
            throws ParticleEffects.ParticleVersionException,
            ParticleEffects.ParticleDataException, IllegalArgumentException {
        this.display(0f, 0f, 0f, 0f, amount, center, range);
    }

    public void display(ParticleData data, float offsetX, float offsetY,
                        float offsetZ, float speed, int amount, Location center,
                        double range) throws ParticleEffects.ParticleVersionException,
            ParticleEffects.ParticleDataException {
        if (!isSupported())
            throw new ParticleVersionException(
                    "This particle effect is not supported by your server version");
        if (!hasProperty(ParticleProperty.REQUIRES_DATA))
            throw new ParticleDataException(
                    "This particle effect does not require additional data");
        if (!isDataCorrect(this, data))
            throw new ParticleDataException(
                    "The particle data type is incorrect");
        new ParticlePacket(this, offsetX, offsetY, offsetZ, speed, amount,
                range > 256.0D, data).sendTo(center, range);
    }

    public void display(ParticleData data, float offsetX, float offsetY,
                        float offsetZ, float speed, int amount, Location center,
                        List<Player> players)
            throws ParticleEffects.ParticleVersionException,
            ParticleEffects.ParticleDataException {
        if (!isSupported())
            throw new ParticleVersionException(
                    "This particle effect is not supported by your server version");
        if (!hasProperty(ParticleProperty.REQUIRES_DATA))
            throw new ParticleDataException(
                    "This particle effect does not require additional data");
        if (!isDataCorrect(this, data))
            throw new ParticleDataException(
                    "The particle data type is incorrect");
        new ParticlePacket(this, offsetX, offsetY, offsetZ, speed, amount,
                isLongDistance(center, players), data).sendTo(center, players);
    }

    public void display(ParticleData data, float offsetX, float offsetY,
                        float offsetZ, float speed, int amount, Location center,
                        Player... players) throws ParticleEffects.ParticleVersionException,
            ParticleEffects.ParticleDataException {
        display(data, offsetX, offsetY, offsetZ, speed, amount, center,
                Arrays.asList(players));
    }

    public void display(ParticleData data, Vector direction, float speed,
                        Location center, double range)
            throws ParticleEffects.ParticleVersionException,
            ParticleEffects.ParticleDataException {
        if (!isSupported())
            throw new ParticleVersionException(
                    "This particle effect is not supported by your server version");
        if (!hasProperty(ParticleProperty.REQUIRES_DATA))
            throw new ParticleDataException(
                    "This particle effect does not require additional data");
        if (!isDataCorrect(this, data))
            throw new ParticleDataException(
                    "The particle data type is incorrect");
        new ParticlePacket(this, direction, speed, range > 256.0D, data)
                .sendTo(center, range);
    }

    public void display(ParticleData data, Vector direction, float speed,
                        Location center, List<Player> players)
            throws ParticleEffects.ParticleVersionException,
            ParticleEffects.ParticleDataException {
        if (!isSupported())
            throw new ParticleVersionException(
                    "This particle effect is not supported by your server version");
        if (!hasProperty(ParticleProperty.REQUIRES_DATA))
            throw new ParticleDataException(
                    "This particle effect does not require additional data");
        if (!isDataCorrect(this, data))
            throw new ParticleDataException(
                    "The particle data type is incorrect");
        new ParticlePacket(this, direction, speed, isLongDistance(center,
                players), data).sendTo(center, players);
    }

    public void display(ParticleData data, Color color,
                        float offsetX, float offsetY, float offsetZ, float speed,
                        int amount, Location center, double range) {
        if (color != null
                && (this == ParticleEffects.REDSTONE
                || this == ParticleEffects.SPELL_MOB || this == ParticleEffects.SPELL_MOB_AMBIENT)) {
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
            this.display(data, offsetX, offsetY, offsetZ, speed, amount,
                    center, range);
        } else {
            this.display(offsetX, offsetY, offsetZ, speed, amount, center,
                    range);
        }
    }

    public void display(ParticleData data, Vector direction, float speed,
                        Location center, Player... players)
            throws ParticleEffects.ParticleVersionException,
            ParticleEffects.ParticleDataException {
        display(data, direction, speed, center, Arrays.asList(players));
    }

    public ParticleData getData(Material material, Byte blockData) {
        ParticleData data = null;
        if (blockData == null) {
            blockData = 0;
        }
        if ((this == ParticleEffects.BLOCK_CRACK
                || this == ParticleEffects.ITEM_CRACK || this == ParticleEffects.BLOCK_DUST)
                && material != null && material != Material.AIR) {
            if (this == ParticleEffects.ITEM_CRACK) {
                data = new ItemData(material, blockData);
            } else {
                data = new BlockData(material, blockData);
            }
        }
        return data;
    }

    /*
     * public static ParticleEffects getEffect(String name) { for
     * (ParticleEffects e : ) { if (e.name().equalsIgnoreCase(name)) { return e;
     * } } return null; }
     */
    public enum ParticleProperty {
        REQUIRES_WATER, REQUIRES_DATA, DIRECTIONAL, COLORABLE;

        ParticleProperty() {
        }
    }

    public static abstract class ParticleData {
        private Material material;
        private byte data;
        private int[] packetData;

        public ParticleData(Material material, byte data) {
            this.material = material;
            this.data = data;
            packetData = new int[]{material.getId(), data};
        }

        public Material getMaterial() {
            return material;
        }

        public byte getData() {
            return data;
        }

        public int[] getPacketData() {
            return packetData;
        }

        public String getPacketDataString() {
            return "_" + packetData[0] + "_" + packetData[1];
        }
    }

    public static class ItemData extends ParticleEffects.ParticleData {
        public ItemData(Material material, byte data) {
            super(material, data);
        }
    }

    public static class BlockData extends ParticleEffects.ParticleData {
        public BlockData(Material material, byte data)
                throws IllegalArgumentException {
            super(material, data);
            if (!material.isBlock())
                throw new IllegalArgumentException(
                        "The material is not a block");
        }
    }

    public static abstract class ParticleColor {
        public abstract float getValueX();

        public abstract float getValueY();

        public abstract float getValueZ();
    }

    public static class OrdinaryColor extends
            ParticleEffects.ParticleColor {
        private int red;
        private int green;
        private int blue;

        public OrdinaryColor(int red, int green, int blue)
                throws IllegalArgumentException {
            if (red < 0)
                throw new IllegalArgumentException(
                        "The red value is lower than 0");
            if (red > 255)
                throw new IllegalArgumentException(
                        "The red value is higher than 255");
            this.red = red;
            if (green < 0)
                throw new IllegalArgumentException(
                        "The green value is lower than 0");
            if (green > 255)
                throw new IllegalArgumentException(
                        "The green value is higher than 255");
            this.green = green;
            if (blue < 0)
                throw new IllegalArgumentException(
                        "The blue value is lower than 0");
            if (blue > 255)
                throw new IllegalArgumentException(
                        "The blue value is higher than 255");
            this.blue = blue;
        }

        public int getRed() {
            return red;
        }

        public int getGreen() {
            return green;
        }

        public int getBlue() {
            return blue;
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

    public static class NoteColor extends ParticleEffects.ParticleColor {
        private int note;

        public NoteColor(int note) throws IllegalArgumentException {
            if (note < 0)
                throw new IllegalArgumentException(
                        "The note value is lower than 0");
            if (note > 24)
                throw new IllegalArgumentException(
                        "The note value is higher than 24");
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

    private static class ParticleDataException extends RuntimeException {
        private static long serialVersionUID = 3203085387160737484L;

        public ParticleDataException(String message) {
            super();
        }
    }

    private static class ParticleColorException extends RuntimeException {
        private static long serialVersionUID = 3203085387160737484L;

        public ParticleColorException(String message) {
            super();
        }
    }

    private static class ParticleVersionException extends
            RuntimeException {
        private static long serialVersionUID = 3203085387160737484L;

        public ParticleVersionException(String message) {
            super();
        }
    }

    public static class ParticlePacket {
        private static int version;
        private static Class<?> enumParticle;
        private static Constructor<?> packetConstructor;
        private static Method getHandle;
        private static Field playerConnection;
        private static Method sendPacket;
        private static boolean initialized;
        private ParticleEffects effect;
        private float offsetX;
        private float offsetY;
        private float offsetZ;
        private float speed;
        private int amount;
        private boolean longDistance;
        private ParticleEffects.ParticleData data;
        private Object packet;

        public ParticlePacket(ParticleEffects effect, float offsetX,
                              float offsetY, float offsetZ, float speed, int amount,
                              boolean longDistance, ParticleEffects.ParticleData data)
                throws IllegalArgumentException {
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

        public ParticlePacket(ParticleEffects effect, Vector direction,
                              float speed, boolean longDistance,
                              ParticleEffects.ParticleData data)
                throws IllegalArgumentException {
            this(effect, (float) direction.getX(), (float) direction.getY(),
                    (float) direction.getZ(), speed, 0, longDistance, data);
        }

        public ParticlePacket(ParticleEffects effect,
                              ParticleEffects.ParticleColor color, boolean longDistance) {
            this(effect, color.getValueX(), color.getValueY(), color
                    .getValueZ(), 1.0F, 0, longDistance, null);
        }

        public static void initialize()
                throws ParticleEffects.ParticlePacket.VersionIncompatibleException {
            if (initialized)
                return;
            try {
                version = Integer.parseInt(Character
                        .toString(ReflectionUtils.PackageType
                                .getServerVersion().charAt(3)));
                if (version > 7) {
                    enumParticle = ReflectionUtils.PackageType.MINECRAFT_SERVER
                            .getClass("EnumParticle");
                }
                Class<?> packetClass = ReflectionUtils.PackageType.MINECRAFT_SERVER
                        .getClass(version < 7 ? "Packet63WorldParticles"
                                : "PacketPlayOutWorldParticles");
                packetConstructor = ReflectionUtils.getConstructor(packetClass
                );
                getHandle = ReflectionUtils.getMethod("CraftPlayer",
                        ReflectionUtils.PackageType.CRAFTBUKKIT_ENTITY,
                        "getHandle");
                playerConnection = ReflectionUtils.getField("EntityPlayer",
                        ReflectionUtils.PackageType.MINECRAFT_SERVER, false,
                        "playerConnection");
                sendPacket = ReflectionUtils
                        .getMethod(
                                playerConnection.getType(),
                                "sendPacket",
                                ReflectionUtils.PackageType.MINECRAFT_SERVER
                                        .getClass("Packet"));
            } catch (Exception exception) {
                throw new VersionIncompatibleException(
                        "Your current bukkit version seems to be incompatible with this library",
                        exception);
            }
            initialized = true;
        }

        public static int getVersion() {
            return version;
        }

        public static boolean isInitialized() {
            return initialized;
        }

        private void initializePacket(Location center)
                throws ParticleEffects.ParticlePacket.PacketInstantiationException {
            if (packet != null)
                return;
            try {
                packet = packetConstructor.newInstance();
                if (version < 8) {
                    String name = effect.getName();
                    if (data != null) {
                        name = name + data.getPacketDataString();
                    }
                    ReflectionUtils.setValue(packet, true, "a", name);
                } else {
                    ReflectionUtils
                            .setValue(packet, true, "a", enumParticle
                                    .getEnumConstants()[effect.getId()]);
                    ReflectionUtils.setValue(packet, true, "j",
                            longDistance);
                    if (data != null) {
                        ReflectionUtils.setValue(packet, true, "k",
                                data.getPacketData());
                    }
                }
                ReflectionUtils.setValue(packet, true, "b",
                        (float) center.getX());
                ReflectionUtils.setValue(packet, true, "c",
                        (float) center.getY());
                ReflectionUtils.setValue(packet, true, "d",
                        (float) center.getZ());
                ReflectionUtils.setValue(packet, true, "e",
                        offsetX);
                ReflectionUtils.setValue(packet, true, "f",
                        offsetY);
                ReflectionUtils.setValue(packet, true, "g",
                        offsetZ);
                ReflectionUtils.setValue(packet, true, "h",
                        speed);
                ReflectionUtils.setValue(packet, true, "i",
                        amount);
            } catch (Exception exception) {
                throw new PacketInstantiationException(
                        "Packet instantiation failed", exception);
            }
        }

        public void sendTo(Location center, Player player)
                throws ParticleEffects.ParticlePacket.PacketInstantiationException,
                ParticleEffects.ParticlePacket.PacketSendingException {
            initializePacket(center);
            try {
                sendPacket.invoke(playerConnection.get(getHandle.invoke(player
                )), packet);
            } catch (Exception exception) {
                throw new PacketSendingException(
                        "Failed to send the packet to player '"
                                + player.getName() + "'", exception);
            }
        }

        public void sendTo(Location center, List<Player> players)
                throws IllegalArgumentException {
            if (players.isEmpty())
                throw new IllegalArgumentException("The player list is empty");
            for (Player player : players) {
                sendTo(center, player);
            }
        }

        public void sendTo(Location center, double range)
                throws IllegalArgumentException {
            if (range < 1.0D)
                throw new IllegalArgumentException("The range is lower than 1");
            String worldName = center.getWorld().getName();
            double squared = range * range;
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getWorld().getName().equals(worldName)
                        && player.getLocation().distanceSquared(center) <= squared) {
                    sendTo(center, player);
                }
            }
        }

        private static class VersionIncompatibleException extends
                RuntimeException {
            private static long serialVersionUID = 3203085387160737484L;

            public VersionIncompatibleException(String message, Throwable cause) {
                super(cause);
            }
        }

        private static class PacketInstantiationException extends
                RuntimeException {
            private static long serialVersionUID = 3203085387160737484L;

            public PacketInstantiationException(String message, Throwable cause) {
                super(cause);
            }
        }

        private static class PacketSendingException extends
                RuntimeException {
            private static long serialVersionUID = 3203085387160737484L;

            public PacketSendingException(String message, Throwable cause) {
                super(cause);
            }
        }
    }
}
