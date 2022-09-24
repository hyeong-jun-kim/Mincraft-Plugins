package neo.main;
import neo.command.Commands;
import neo.data.DataManager;
import neo.event.EventListener;
import neo.stamina.StaminaBoard;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Score;

import java.util.HashMap;

public class Main extends JavaPlugin {
    public static DataManager data;
    public static HashMap<OfflinePlayer, StaminaBoard> staminaBoards;

    @Override
    public void onEnable() {
        data = new DataManager(this);
        staminaBoards = new HashMap<>();
        getServer().getPluginManager().registerEvents(new EventListener(), this);
//        getCommand("커맨드").setExecutor(new Commands());
    }

    @Override
    public void onDisable() {

    }

    public static DataManager getData() {
        return data;
    }

    public static HashMap<OfflinePlayer, StaminaBoard> getStaminaBoards(){
        return staminaBoards;
    }
}

