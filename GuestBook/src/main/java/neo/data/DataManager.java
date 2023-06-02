package neo.data;

import neo.main.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.A;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;


public class DataManager {
    private String name;
    private Main plugin;
    private File configFile = null;
    private FileConfiguration dataFile = null;

    public DataManager(Main plugin, String name) {
        this.plugin = plugin;
        this.name = name.toLowerCase();
        saveDefaultconfig();
    }

    public void reloadFile() {
        if (configFile == null) {
            configFile = new File(this.plugin.getDataFolder(), getFileName());
        }
        dataFile = YamlConfiguration.loadConfiguration(configFile);

        InputStream defaultStream = this.plugin.getResource(getFileName());
        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            dataFile.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getFile() {
        if (dataFile == null)
            reloadFile();
        return dataFile;
    }

    public void saveConfig() {
        if (dataFile == null || configFile == null)
            return;
        try {
            getFile().save(configFile);
        } catch (IOException e) {
            this.plugin.getLogger().log(Level.SEVERE, "파일 저장에 실패했습니다.");
        }
    }

    public void saveDefaultconfig() {
        File outDir = new File(plugin.getDataFolder().getPath());
        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), getFileName());
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        if(!configFile.exists())
//            this.plugin.saveResource(getFileName(), false);
    }

    private String getFileName() {
        return name + ".yml";
    }

    public void deleteFile(){
        configFile.delete();
    }

    public static List<String> getFileNames(){
        File dir = new File(Main.instance.getDataFolder().getPath());
        File[] files = dir.listFiles();

        ArrayList<String> fileList = new ArrayList<>();
        for(File file : files){
            String fileName = file.getName().replace(".yml", "");
            fileList.add(fileName);
        }

        return fileList;
    }
}
