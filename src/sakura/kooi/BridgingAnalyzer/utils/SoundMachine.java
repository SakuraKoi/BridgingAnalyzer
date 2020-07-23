package sakura.kooi.BridgingAnalyzer.utils;

import org.bukkit.Sound;

public class SoundMachine {
    public static Sound get(String v18, String v19) {
        try {
            return Sound.valueOf(v18);
        } catch (IllegalArgumentException ex) {
            return Sound.valueOf(v19);
        }
    }

}
