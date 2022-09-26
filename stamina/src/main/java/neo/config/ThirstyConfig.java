package neo.config;

import neo.main.Main;
import org.bukkit.configuration.file.FileConfiguration;

public class ThirstyConfig {
    static FileConfiguration file = Main.getData().getFile();
    final public static double THIRSTY_MAX = file.getDouble("thirsty.max");
    final public static double RECOVERY_TIME = file.getDouble("thirsty.reduceTime");
    final public static double WATER_HEAL = file.getDouble("thirsty.waterHeal");
    final public static double DAMAGE = file.getDouble("thirsty.damage");
}
