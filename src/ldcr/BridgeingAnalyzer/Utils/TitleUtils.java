package ldcr.BridgeingAnalyzer.Utils;

import java.lang.reflect.Constructor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TitleUtils {
    private static final String version = org.bukkit.Bukkit.getServer().getClass().getPackage().getName()
	    .split("\\.")[3];

    public static void boardcastTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
	for (final Player p : Bukkit.getOnlinePlayers()) {
	    try {
		sendTitle(p, title, subtitle, fadeIn, stay, fadeOut);
	    } catch (final Exception e) {
		e.printStackTrace();
	    }
	}
    }

    private static Class<?> getNMSClass(String name) {
	try {
	    return Class.forName("net.minecraft.server." + version + "." + name);
	} catch (final ClassNotFoundException e) {
	    e.printStackTrace();
	}
	return null;
    }

    private static void sendPacket(Player player, Object packet) throws Exception {
	final Object handle = player.getClass().getMethod("getHandle", new Class[0]).invoke(player, new Object[0]);
	final Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
	playerConnection.getClass().getMethod("sendPacket", new Class[] { getNMSClass("Packet") })
		.invoke(playerConnection, new Object[] { packet });
    }

    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
	try {
	    final Object enumTitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE")
		    .get(null);
	    final Object titleChat = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
		    .getMethod("a", new Class[] { String.class })
		    .invoke(null, new Object[] { "{\"text\":\"" + title + "\"}" });
	    final Object enumSubtitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE")
		    .get(null);
	    final Object subtitleChat = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
		    .getMethod("a", new Class[] { String.class })
		    .invoke(null, new Object[] { "{\"text\":\"" + subtitle + "\"}" });
	    final Constructor<?> titleConstructor = getNMSClass("PacketPlayOutTitle")
		    .getConstructor(new Class[] { getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0],
			    getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE });
	    final Object titlePacket = titleConstructor.newInstance(new Object[] { enumTitle, titleChat,
		    Integer.valueOf(fadeIn), Integer.valueOf(stay), Integer.valueOf(fadeOut) });
	    final Object subtitlePacket = titleConstructor.newInstance(new Object[] { enumSubtitle, subtitleChat,
		    Integer.valueOf(fadeIn), Integer.valueOf(stay), Integer.valueOf(fadeOut) });
	    sendPacket(player, titlePacket);
	    sendPacket(player, subtitlePacket);
	} catch (final Exception e) {
	    e.printStackTrace();
	}
    }
}
