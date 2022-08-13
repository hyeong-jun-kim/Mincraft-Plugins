package neo.event.handler;

import neo.data.DataManager;
import neo.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class SailorEventHandler {
    static DataManager data = Main.getData();
    static FileConfiguration file = Main.getData().getFile();

    public static String getPirateName(Player p){
        String pirateName = null;
        String name = p.getName();
        if(file.get("pirates") == null)
            return null;
        pirateName = file.getConfigurationSection("pirates").getKeys(false).stream().filter(
                key -> file.contains("pirates." + key + ".member." + name)).findAny().orElse(null);
        return pirateName;
    }

    /*public static boolean checkSailor(Player p){
        String name = p.getName();
        if(file.getConfigurationSection("pirates").getKeys(false).contains(name)){
            return true;
        }else{
            return false;
        }
    }*/
}
