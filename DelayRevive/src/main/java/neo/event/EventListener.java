package neo.event;

import neo.data.DataManager;
import neo.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.server.TabCompleteEvent;

public class EventListener implements Listener {
    static DataManager data = Main.getData();
    /*  
        플레이어가 죽을 때 관전모드 변경 및 부활시간 yml에 입력
     */
    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent e){
        Player p = e.getPlayer();
        String name = p.getName();
        p.setGameMode(GameMode.SPECTATOR);
        p.spigot().respawn();
        p.sendTitle(ChatColor.RED + "" + ChatColor.BOLD+ "당신은 죽었습니다 !", "한시간 후에 자동으로 부활합니다",10, 60, 20);
        int reviveTime = data.getFile().getInt(name+ ".revive.lefttime");
        data.getFile().set("name.revive.lefttime", System.currentTimeMillis() + reviveTime);
        data.saveConfig();
    }
    @EventHandler
    public void onGameModeChangedEvent(PlayerGameModeChangeEvent e){

    }
}
