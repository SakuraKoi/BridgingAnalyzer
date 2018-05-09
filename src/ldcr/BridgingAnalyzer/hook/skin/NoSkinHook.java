package ldcr.BridgingAnalyzer.hook.skin;

import org.bukkit.SandstoneType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Sandstone;

public class NoSkinHook implements ISkinHook {

    @Override
    public ItemStack getItem(final Player p) {
	return new Sandstone(SandstoneType.SMOOTH).toItemStack(64);
    }
}
