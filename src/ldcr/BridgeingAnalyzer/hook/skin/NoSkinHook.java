package ldcr.BridgeingAnalyzer.hook.skin;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class NoSkinHook implements ISkinHook {

	@Override
	public ItemStack getItem(final Player p) {
		return new ItemStack(Material.SANDSTONE, 64);
	}
}
