package sakura.kooi.BridgingAnalyzer.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

public class TitleUtils {
    private static String version = org.bukkit.Bukkit.getServer().getClass().getPackage().getName()
            .split("\\.")[3];

    // 这个效率实在太低了, 但是我懒得改了, 性能洁癖的自己改吧 (又不是不能用.jpg

    private static Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void sendPacket(Player player, Object packet) throws Exception {
        Object handle = player.getClass().getMethod("getHandle", new Class[0]).invoke(player);
        Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
        playerConnection.getClass().getMethod("sendPacket", new Class[]{getNMSClass("Packet")}).invoke(playerConnection, packet);
    }

    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        try {
            Object enumTitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
            Object titleChat = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
                    .getMethod("a", new Class[]{String.class})
                    .invoke(null, "{\"text\":\"" + title + "\"}");
            Object enumSubtitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE")
                    .get(null);
            Object subtitleChat = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
                    .getMethod("a", new Class[]{String.class})
                    .invoke(null, "{\"text\":\"" + subtitle + "\"}");
            Constructor<?> titleConstructor = getNMSClass("PacketPlayOutTitle")
                    .getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0],
                            getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
            Object titlePacket = titleConstructor.newInstance(enumTitle, titleChat,
                    fadeIn, stay, fadeOut);
            Object subtitlePacket = titleConstructor.newInstance(enumSubtitle, subtitleChat,
                    fadeIn, stay, fadeOut);
            sendPacket(player, titlePacket);
            sendPacket(player, subtitlePacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void boardcastTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            try {
                sendTitle(p, title, subtitle, fadeIn, stay, fadeOut);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
