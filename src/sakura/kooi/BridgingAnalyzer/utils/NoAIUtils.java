package sakura.kooi.BridgingAnalyzer.utils;

import org.bukkit.entity.Entity;

import java.lang.reflect.Method;

public class NoAIUtils {
    private static boolean works;
    private static Class<?> clsCraftEntity = null;
    private static Method mhdGetHandle = null;
    private static Method mhdGetNBTTag = null;
    private static Class<?> clsNBTTagCompound = null;
    private static Method mhdSetInt = null;
    private static Method mhdReadTag = null;
    private static Method mhdSetTag = null;

    static {
        try {
            String nmsver = org.bukkit.Bukkit.getServer().getClass().getPackage()
                    .getName().split("\\.")[3];
            clsCraftEntity = Class.forName("org.bukkit.craftbukkit." + nmsver
                    + ".entity.CraftEntity");
            mhdGetHandle = clsCraftEntity.getDeclaredMethod("getHandle");
            Class<?> clsNMSEntity = Class.forName("net.minecraft.server." + nmsver
                    + ".Entity");
            mhdGetNBTTag = clsNMSEntity.getDeclaredMethod("getNBTTag");
            clsNBTTagCompound = Class.forName("net.minecraft.server." + nmsver
                    + ".NBTTagCompound");
            mhdSetInt = clsNBTTagCompound.getDeclaredMethod("setInt",
                    String.class, int.class);
            mhdReadTag = clsNMSEntity.getDeclaredMethod("c", clsNBTTagCompound);
            mhdSetTag = clsNMSEntity.getDeclaredMethod("f", clsNBTTagCompound);
        } catch (Exception e) {
            e.printStackTrace();
            works = false;
        }
        works = true;
    }

    public static void setAI(Entity bukkitEntity, boolean hasAI) {
        if (!works)
            return;
        try {
            Object objCraftEntity = clsCraftEntity.cast(bukkitEntity);
            Object objNMSEntity = mhdGetHandle.invoke(objCraftEntity);
            Object objNBTTag = mhdGetNBTTag.invoke(objNMSEntity);
            if (objNBTTag == null) {
                objNBTTag = clsNBTTagCompound.getConstructor().newInstance();
            }
            mhdReadTag.invoke(objNMSEntity, objNBTTag);
            mhdSetInt.invoke(objNBTTag, "NoAI", hasAI ? 0 : 1);
            mhdSetTag.invoke(objNMSEntity, objNBTTag);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*
         * net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity)
         * bukkitEntity).getHandle(); NBTTagCompound tag =
         * nmsEntity.getNBTTag(); if (tag == null) { tag = new NBTTagCompound();
         * } nmsEntity.c(tag); tag.setInt("NoAI", 1); nmsEntity.f(tag);
         */
    }
}
