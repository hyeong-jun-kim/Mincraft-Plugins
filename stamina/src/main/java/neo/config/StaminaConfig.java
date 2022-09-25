package neo.config;

import neo.data.DataManager;
import neo.main.Main;
import org.bukkit.configuration.file.FileConfiguration;

public class StaminaConfig {
    static FileConfiguration file = Main.getData().getFile();
    final public static double STAMINA_MAX = file.getDouble("stamina.max");
    final public static double RECOVERY_TIME = file.getDouble("stamina.recoveryTime");
}
