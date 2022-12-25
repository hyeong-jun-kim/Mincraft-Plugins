package neo.main;
import neo.command.MsgCommands;
import neo.command.RemCommands;
import neo.data.DataManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static DataManager data;

    @Override
    public void onEnable() {
        data = new DataManager(this);
//        getServer().getPluginManager().registerEvents(new EventListener(), this);
        getCommand("advmsg").setExecutor(new MsgCommands());
        getCommand("advrem").setExecutor(new RemCommands());
    }

    @Override
    public void onDisable() {

    }

    public static DataManager getData() {
        return data;
    }

}

