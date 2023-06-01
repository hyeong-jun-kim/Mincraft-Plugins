package neo.main;
import neo.command.Commands;
import neo.data.DataManager;
import neo.event.EventListener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static DataManager data;

    @Override
    public void onEnable() {
        data = new DataManager(this);
//        getServer().getPluginManager().registerEvents(new EventListener(), this);
        getCommand("방명록").setExecutor(new Commands());
    }

    @Override
    public void onDisable() {

    }

    public static DataManager getData() {
        return data;
    }

}

