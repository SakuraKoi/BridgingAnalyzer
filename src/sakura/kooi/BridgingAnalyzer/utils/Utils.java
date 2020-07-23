package sakura.kooi.BridgingAnalyzer.utils;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Utils {
    public static void breakBlock(Block b) {
        if (!b.getChunk().isLoaded()) {
            b.getChunk().load(false);
        }
        b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getType());
        b.setType(Material.AIR);
    }

    public static double formatDouble(double d) {
        return (double) Math.round(d * 100) / 100;
    }

    public static void addItem(PlayerInventory inv, ItemStack item) {
        if (isEmptySlot(item)) return;
        if (item.getType().toString().endsWith("_HELMET")) {
            if (isEmptySlot(inv.getHelmet())) {
                inv.setHelmet(item);
            }
        } else if (item.getType().toString().endsWith("_CHESTPLATE")) {
            if (isEmptySlot(inv.getChestplate())) {
                inv.setChestplate(item);
            }
        } else if (item.getType().toString().endsWith("_LEGGINGS")) {
            if (isEmptySlot(inv.getLeggings())) {
                inv.setLeggings(item);
            }
        } else if (item.getType().toString().endsWith("_BOOTS")) {
            if (isEmptySlot(inv.getBoots())) {
                inv.setBoots(item);
            }
        } else {
            inv.addItem(item);
        }
    }

    public static boolean isEmptySlot(ItemStack item) {
        if (item == null) return true;
        return item.getType() == Material.AIR;
    }
}
