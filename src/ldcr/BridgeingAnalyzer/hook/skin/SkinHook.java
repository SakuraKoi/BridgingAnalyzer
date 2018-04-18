package ldcr.BridgeingAnalyzer.hook.skin;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ldcr.BridgingSkin.BridgingSkin;

public class SkinHook implements ISkinHook {

    @Override
    public ItemStack getItem(final Player p) {
	return BridgingSkin.getItem(p);
    }

}
