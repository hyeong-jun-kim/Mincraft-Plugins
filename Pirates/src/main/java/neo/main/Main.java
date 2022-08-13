package neo.main;
import neo.command.Commands;
import neo.command.WarCommands;
import neo.data.AreaData;
import neo.data.DataManager;
import neo.event.ChattingEventListener;
import neo.event.EventListener;
import neo.feature.ChannelManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;

public class Main extends JavaPlugin {
    public static DataManager data;
    public static HashMap<String, AreaData> areaMap;
    public static HashMap<String, String> warMap;
    public static HashMap<String, BukkitTask> warTaskMap; // 전쟁시 스케쥴러 맵

    public static ChannelManager cM;
    @Override
    public void onEnable() {
        data = new DataManager(this);
        areaMap = new HashMap<>();
        warMap = new HashMap<>();
        warTaskMap = new HashMap<>();
        cM = new ChannelManager();
        getServer().getPluginManager().registerEvents(new EventListener(), this);
        getServer().getPluginManager().registerEvents(new ChattingEventListener(this), this);
        getCommand("해적단").setExecutor(new Commands(this));
        getCommand("전쟁선포").setExecutor(new WarCommands(this));
    }


    public Main getMain(){
        return this;
    }
    public static DataManager getData() {
        return data;
    }

    public static HashMap<String, AreaData> getAreaMap(){
        return areaMap;
    }

    public static HashMap<String, String> getWarMap(){
        return warMap;
    }
    public static HashMap<String, BukkitTask> getWarTaskMap(){
        return warTaskMap;
    }
}

