package neo.main;
import neo.command.Commands;
import neo.data.DataManager;
import neo.event.EventListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static DataManager data;

    @Override
    public void onEnable() {
        data = new DataManager(this);
        getServer().getPluginManager().registerEvents(new EventListener(), this);
        printPluginIntro();
//        getCommand("커맨드").setExecutor(new Commands());
    }

    @Override
    public void onDisable() {

    }

    public static FileConfiguration getFileConfiguration() {
        return data.getFile();
    }

    public void printPluginIntro(){
        int count = data.getFile().getConfigurationSection("data").getKeys(false).size();
        Bukkit.getLogger().info("[ItemCommand] " + count + " commands was loaded.");
    }

}

