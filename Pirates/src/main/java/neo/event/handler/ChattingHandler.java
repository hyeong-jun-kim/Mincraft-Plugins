package neo.event.handler;

import neo.data.DataManager;
import neo.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ChattingHandler {
    static DataManager data = Main.getData();
    static FileConfiguration file = Main.getData().getFile();

    public static String getPirateName(Player p){
        String pirateName = null;
        String name = p.getName();
        if(checkCaptain(p)){
            pirateName = CaptainEventHandler.getPirateName(p);
        }else{
            pirateName = SailorEventHandler.getPirateName(p);
        }
        return pirateName;
    }

    public static boolean checkCaptain(Player p){
        String name = p.getName();
        if(file.getConfigurationSection("area").getKeys(false).contains(name)){
            return true;
        }else{
            return false;
        }
    }
}
