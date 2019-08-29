package sakura.kooi.BridgingAnalyzer.Utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ActionBarUtils {
	public static boolean works;
	public static String nmsver;
	private static Class<?> classCraftPlayer;
	private static Class<?> classPacketChat;
	private static Class<?> classPacket;
	private static Class<?> classChatSerializer;
	private static Class<?> classIChatComponent;
	private static Method methodSeralizeString;
	private static Class<?> classChatComponentText;
	private static Method methodGetHandle;
	private static Class<?> classEntityPlayer;
	private static Field fieldConnection;
	private static Class<?> classPlayerConnection;
	private static Method methodSendPacket;
	static {
		try {
			nmsver = org.bukkit.Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
			classCraftPlayer = Class.forName("org.bukkit.craftbukkit." + nmsver + ".entity.CraftPlayer");
			classPacketChat = Class.forName("net.minecraft.server." + nmsver + ".PacketPlayOutChat");
			classPacket = Class.forName("net.minecraft.server." + nmsver + ".Packet");
			if (nmsver.equalsIgnoreCase("v1_8_R1") || nmsver.equalsIgnoreCase("v1_7_")) {
				classChatSerializer = Class.forName("net.minecraft.server." + nmsver + ".ChatSerializer");
				classIChatComponent = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
				methodSeralizeString = classChatSerializer.getDeclaredMethod("a", String.class);
			} else {
				classChatComponentText = Class.forName("net.minecraft.server." + nmsver + ".ChatComponentText");
				classIChatComponent = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
			}
			methodGetHandle = classCraftPlayer.getDeclaredMethod("getHandle");
			classEntityPlayer = Class.forName("net.minecraft.server." + nmsver + ".EntityPlayer");
			fieldConnection = classEntityPlayer.getDeclaredField("playerConnection");
			classPlayerConnection = Class.forName("net.minecraft.server." + nmsver + ".PlayerConnection");
			methodSendPacket = classPlayerConnection.getDeclaredMethod("sendPacket", classPacket);
			works = true;
		} catch (final Exception e) {
			works = false;
		}
	}

	public static void sendActionBar(final Player player, final String message) {
		if (!works) return;
		try {
			final Object p = classCraftPlayer.cast(player);
			Object ppoc;
			if (nmsver.equalsIgnoreCase("v1_8_R1") || nmsver.equalsIgnoreCase("v1_7_")) {
				final Object cbc = classIChatComponent.cast(methodSeralizeString.invoke(classChatSerializer, "{\"text\": \"" + message + "\"}"));
				ppoc = classPacketChat.getConstructor(new Class<?>[] { classIChatComponent, byte.class }).newInstance(cbc, (byte) 2);
			} else {
				final Object o = classChatComponentText.getConstructor(new Class<?>[] { String.class }).newInstance(message);
				ppoc = classPacketChat.getConstructor(new Class<?>[] { classIChatComponent, byte.class }).newInstance(o, (byte) 2);
			}
			final Object h = methodGetHandle.invoke(p);
			final Object pc = fieldConnection.get(h);
			methodSendPacket.invoke(pc, ppoc);
		} catch (final Exception ex) {
			ex.printStackTrace();
			works = false;
		}
	}

	public static void sendActionBarToAllPlayers(final String message) {
		for (final Player p : Bukkit.getOnlinePlayers()) {
			sendActionBar(p, message);
		}
	}
}