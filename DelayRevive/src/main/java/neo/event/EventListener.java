package neo.event;

import neo.data.DataManager;
import neo.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
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
        int reviveTime = data.getFile().getInt(name+ ".revive.time"); // 부활시간 가져오기
        data.getFile().set(name+".revive.time", System.currentTimeMillis() + reviveTime);
        data.saveConfig();
    }
    /*
        서바이벌 모드 변경 이벤트 처리
     */
    @EventHandler
    public void onGameModeChangedEvent(PlayerGameModeChangeEvent e) {
        if (e.getPlayer().getGameMode() == GameMode.SURVIVAL) {
            Player p = e.getPlayer();
            String name = p.getName();
            if (data.getFile().contains(name + ".revive.time")) {
                int time = data.getFile().getInt(name + ".revive.time");
                if (time >= System.currentTimeMillis()) { // 부활 딜레이가 안지났으면 타이틀 메시지 출력
                    p.sendTitle(ChatColor.RED + "" + ChatColor.BOLD+ "부활 !", "",10, 60, 20);
                }
            }
        }
    }
    /*
        관전자 모드에서 1시간이 지나고 들어올 때 서바이벌 모드로 변경 및 스폰 장소 이동
     */
    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e){
        Player p = e.getPlayer();
        String name = p.getName();
        if(data.getFile().contains(name+".revive.time")){
            int time = data.getFile().getInt(name+".revive.time");
            if(time <= System.currentTimeMillis()){ // 부활 딜레이가 지났으면
                data.getFile().set(name+".revive.time", null);
                Location loc = p.getWorld().getSpawnLocation();
                p.teleport(loc); // 스폰 장소로 이동
                p.setGameMode(GameMode.SURVIVAL);
            }
        }
    }
}
