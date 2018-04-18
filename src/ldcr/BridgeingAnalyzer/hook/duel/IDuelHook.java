package ldcr.BridgeingAnalyzer.hook.duel;

import org.bukkit.entity.Player;

public interface IDuelHook {
    public void onKill(Player p,Player killer);
}
