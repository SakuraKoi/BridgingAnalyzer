package ldcr.BridgeingAnalyzer.hook.duel;

import org.bukkit.entity.Player;

import ldcr.RodDuel.RodDuel;

public class DuelHook implements IDuelHook {

    @Override
    public void onKill(final Player p, final Player killer) {
	RodDuel.onDuelKilled(p, killer);
    }


}
