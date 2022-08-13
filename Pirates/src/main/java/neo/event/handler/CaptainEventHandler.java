package neo.event.handler;

import neo.data.DataManager;
import neo.main.Main;
import neo.util.EventUtil;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CaptainEventHandler {
    static DataManager data = Main.getData();
    static FileConfiguration file = Main.getData().getFile();

    public static String getPirateName(Player p){
        if(!EventUtil.checkCaptain(p))
            return null;
        String name = p.getName();
        String pirateName = null;

        if(file.get("pirates") == null)
            return null;

        pirateName = file.getConfigurationSection("pirates").getKeys(false).
                stream().filter(key -> file.getString("pirates." + key + ".captain").equals(name))
                .findAny().orElse(null);
        return pirateName;
    }
}
