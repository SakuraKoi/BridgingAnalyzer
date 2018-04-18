package ldcr.BridgeingAnalyzer.hook.duel;

import org.bukkit.entity.Player;

public class NoDuelHook implements IDuelHook {

    @Override
    public void onKill(final Player p, final Player killer) {}

}
