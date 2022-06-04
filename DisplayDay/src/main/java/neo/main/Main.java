package neo.main;
import neo.command.Commands;
import neo.data.DataManager;
import neo.event.EventListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static final int DAY = 24000;
    public static DataManager data;
    public static Long dayTime = 0L;
    @Override
    public void onEnable() {
        startDayDisplay();
        //data = new DataManager(this);
//        getServer().getPluginManager().registerEvents(new EventListener(), this);
        getCommand("날짜").setExecutor(new Commands());
    }

    @Override
    public void onDisable() {

    }

    public static DataManager getData() {
        return data;
    }

    public void startDayDisplay(){ // 마크 날짜 지나면 표시해주기
        Long worldTime = Bukkit.getWorld("World").getFullTime();
        dayTime = worldTime / 24000;
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                Long worldTime = Bukkit.getWorld("World").getFullTime();
                Long currentDayTime = worldTime / 24000;
                if(currentDayTime > dayTime){
                    dayTime = currentDayTime;
                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.sendTitle(ChatColor.BOLD + "" + ChatColor.WHITE + "DAY " + dayTime, "", 10, 100, 20);
                    }
                }
            }
        },0, 10);
    }
}

