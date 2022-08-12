package neo.main;
import neo.command.Commands;
import neo.data.AreaData;
import neo.data.DataManager;
import neo.event.ChattingEventListener;
import neo.event.EventListener;
import neo.feature.ChannelManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;

public class Main extends JavaPlugin {
    public static DataManager data;
    public static HashMap<String, AreaData> areaMap;
    public static HashMap<String, String> warMap;

    public ChannelManager cM;
    @Override
    public void onEnable() {
        data = new DataManager(this);
        areaMap = new HashMap<>();
        cM = new ChannelManager();
        getServer().getPluginManager().registerEvents(new EventListener(), this);
        getServer().getPluginManager().registerEvents(new ChattingEventListener(this), this);
        getCommand("해적단").setExecutor(new Commands(this));
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
}

