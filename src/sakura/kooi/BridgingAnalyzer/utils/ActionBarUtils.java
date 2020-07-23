package sakura.kooi.BridgingAnalyzer.utils;

import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ActionBarUtils {
    public static boolean works;
    public static String nmsver;
    private static Class<?> classCraftPlayer;
    private static Class<?> classPacketChat;
    private static Class<?> classChatSerializer;
    private static Class<?> classIChatComponent;
    private static Method methodSeralizeString;
    private static Class<?> classChatComponentText;
    private static Method methodGetHandle;
    private static Field fieldConnection;
    private static Method methodSendPacket;

    static {
        try {
            nmsver = org.bukkit.Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            classCraftPlayer = Class.forName("org.bukkit.craftbukkit." + nmsver + ".entity.CraftPlayer");
            classPacketChat = Class.forName("net.minecraft.server." + nmsver + ".PacketPlayOutChat");
            Class<?> classPacket = Class.forName("net.minecraft.server." + nmsver + ".Packet");
            if (nmsver.equalsIgnoreCase("v1_8_R1") || nmsver.equalsIgnoreCase("v1_7_")) {
                classChatSerializer = Class.forName("net.minecraft.server." + nmsver + ".ChatSerializer");
                classIChatComponent = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
                methodSeralizeString = classChatSerializer.getDeclaredMethod("a", String.class);
            } else {
                classChatComponentText = Class.forName("net.minecraft.server." + nmsver + ".ChatComponentText");
                classIChatComponent = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
            }
            methodGetHandle = classCraftPlayer.getDeclaredMethod("getHandle");
            Class<?> classEntityPlayer = Class.forName("net.minecraft.server." + nmsver + ".EntityPlayer");
            fieldConnection = classEntityPlayer.getDeclaredField("playerConnection");
            Class<?> classPlayerConnection = Class.forName("net.minecraft.server." + nmsver + ".PlayerConnection");
            methodSendPacket = classPlayerConnection.getDeclaredMethod("sendPacket", classPacket);
            works = true;
        } catch (Exception e) {
            works = false;
        }
    }

    public static void sendActionBar(Player player, String message) {
        if (!works) return;
        try {
            Object p = classCraftPlayer.cast(player);
            Object ppoc;
            if (nmsver.equalsIgnoreCase("v1_8_R1") || nmsver.equalsIgnoreCase("v1_7_")) {
                Object cbc = classIChatComponent.cast(methodSeralizeString.invoke(classChatSerializer, "{\"text\": \"" + message + "\"}"));
                ppoc = classPacketChat.getConstructor(new Class<?>[]{classIChatComponent, byte.class}).newInstance(cbc, (byte) 2);
            } else {
                Object o = classChatComponentText.getConstructor(new Class<?>[]{String.class}).newInstance(message);
                ppoc = classPacketChat.getConstructor(new Class<?>[]{classIChatComponent, byte.class}).newInstance(o, (byte) 2);
            }
            Object h = methodGetHandle.invoke(p);
            Object pc = fieldConnection.get(h);
            methodSendPacket.invoke(pc, ppoc);
        } catch (Exception ex) {
            ex.printStackTrace();
            works = false;
        }
    }

}