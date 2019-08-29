package sakura.kooi.BridgingAnalyzer.api;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface BlockSkinProvider {
	public ItemStack provide(Player player);
}
