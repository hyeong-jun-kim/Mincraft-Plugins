package neo.data;

import neo.main.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.logging.Level;


public class DataManager {
    private Main plugin;
    private File configFile = null;
    private FileConfiguration dataFile = null;

    public DataManager(Main plugin){
        this.plugin = plugin;
        saveDefaultconfig();
    }

    public void reloadFile() {
        if(configFile == null) {
            configFile = new File(this.plugin.getDataFolder(),"data.yml");
        }
        dataFile = YamlConfiguration.loadConfiguration(configFile);

        InputStream defaultStream = this.plugin.getResource("data.yml");
        if(defaultStream!=null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            dataFile.setDefaults(defaultConfig);
        }
        /*   <광물 확률표>
                돌 (40)
                석탄 (10)
                청금석 (10)
                레드스톤 (10)
                철 (10)
                금 (10)
                다이아 (7)
                고대잔해 (3)
         */
        if(!dataFile.contains("blockRate")){
            dataFile.set("blockRate.stone", 40);
            dataFile.set("blockRate.coal", 10);
            dataFile.set("blockRate.lapis", 10);
            dataFile.set("blockRate.redstone", 10);
            dataFile.set("blockRate.iron", 10);
            dataFile.set("blockRate.gold", 10);
            dataFile.set("blockRate.diamond", 7);
            dataFile.set("blockRate.ancient", 3);
            saveConfig();
        }
    }
    public FileConfiguration getFile() {
        if(dataFile == null)
            reloadFile();
        return dataFile;
    }
    public void saveConfig() {
        if(dataFile == null || configFile == null)
            return;
        try {
            getFile().save(configFile);
        }catch(IOException e) {
            this.plugin.getLogger().log(Level.SEVERE, "파일 저장에 실패했습니다.");
        }
    }
    public void saveDefaultconfig() {
        if(configFile == null)
            configFile = new File(this.plugin.getDataFolder(), "data.yml");
        if(!configFile.exists())
            this.plugin.saveResource("data.yml", false);
    }
}
