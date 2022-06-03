package neo.event;

import neo.data.DataManager;
import neo.main.Main;
import org.bukkit.Bukkit;
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
import org.bukkit.scheduler.BukkitScheduler;

public class EventListener implements Listener {
    // 부활시간 1시간, 단축시간 10분
    private final int SHORT_REVIVE_TIME = 60;
    private final int REVIVE_TIME = 3600;
    Main plugin;
    public EventListener(Main plugin){
        this.plugin = plugin;
    }
    static DataManager data = Main.getData();
    /*  
        플레이어가 죽을 때 관전모드 변경 및 부활시간 yml에 입력
     */
    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent e){
        Player p = e.getPlayer();
        String name = p.getName();
        p.setGameMode(GameMode.SPECTATOR);
        // 플레이어가 죽은 위치로 이동
        Location loc = e.getPlayer().getLocation();
        p.spigot().respawn();
        p.teleport(loc);

        // 부활 단축기능이 존재한다면
        if(data.getFile().contains("reviveShorten."+name+".count")){
            int count = data.getFile().getInt("reviveShorten."+name+".count") - 1;
            if(count <= 0){ // 남은 부활 단축이 0개라면
                data.getFile().set("reviveShorten."+name+".count", null);
                data.saveConfig();
            }else {
                data.getFile().set("reviveShorten." + name + ".count", count);
                data.saveConfig();
            }
            p.sendTitle(ChatColor.RED + "" + ChatColor.BOLD+ "당신은 죽었습니다 !", "부활 단축 남은 횟수 " +count +"회",10, 60, 20);
            // 10분 뒤에 부활 (타이머 이용)
            data.getFile().set(name+".revive.time", System.currentTimeMillis() + 1000 * SHORT_REVIVE_TIME);
            data.saveConfig();
            playerRevive(p, 20 * SHORT_REVIVE_TIME);
        }else{
            p.sendTitle(ChatColor.RED + "" + ChatColor.BOLD+ "당신은 죽었습니다 !", "한시간 후에 자동으로 부활합니다",10, 60, 20);
            long reviveTime = data.getFile().getLong("revive.time"); // 부활시간 가져오기
            data.getFile().set(name+".revive.time", System.currentTimeMillis() + reviveTime);
            data.saveConfig();
            // 1시간 뒤에 부활 (타이머 이용)
            playerRevive(p, 20 * REVIVE_TIME);
        }
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
            long time = data.getFile().getLong(name+".revive.time");
            long tmp_time = System.currentTimeMillis();
            if(time <= System.currentTimeMillis()){ // 부활 딜레이가 지났으면
                data.getFile().set(name+".revive.time", null);
                Location loc = p.getWorld().getSpawnLocation();
                p.teleport(loc); // 스폰 장소로 이동
                p.setGameMode(GameMode.SURVIVAL);
            }else{ // 부활 딜레이가 안지났으면 남은 시간 틱으로 변환 후 부활
                time = (time - System.currentTimeMillis()) / 1000 * 20;
                playerRevive(p, (int)time);
            }
        }
    }
    // 부활
    private void playerRevive(Player p, int tick){
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(plugin, new Runnable(){
            @Override
            public void run(){
                Location loc = p.getWorld().getSpawnLocation();
                p.spigot().respawn();
                p.teleport(loc);
                p.setGameMode(GameMode.SURVIVAL);
                p.sendTitle(ChatColor.RED + "" + ChatColor.BOLD+ "부활 !", "",10, 60, 20);
            }
        }, tick);
    }
}