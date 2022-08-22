package neo.event;


import neo.data.AreaData;
import neo.data.DataManager;
import neo.event.handler.AreaEventHandler;
import neo.main.Main;
import neo.util.EventUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.server.ServerLoadEvent;

import java.util.HashMap;
import java.util.Set;

public class EventListener implements Listener {
    static HashMap<String, AreaData> areaMap = Main.getAreaMap();
    static HashMap<String, BossBar> bossBarMap = Main.bossBarMap;
    static HashMap<String, String> warMap = Main.getWarMap();
    static DataManager data = Main.getData();
    static FileConfiguration file = Main.getData().getFile();

    // 성 안에서 블럭 부시는 이벤트
    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Location loc = e.getBlock().getLocation();
        Material material = e.getBlock().getType();
        if (!AreaEventHandler.checkPlaceorBreakIntoArea(p, loc, material))
            e.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        // 비콘설치할 때 이벤트
        if (e.getBlock().getType() == Material.BEACON) {
            AreaEventHandler.createArea(p, e);
        } else {
            // 성 안에서 블럭 설치하는 이벤트
            Location loc = e.getBlock().getLocation();
            Material material = e.getBlock().getType();
            if (!AreaEventHandler.checkPlaceorBreakIntoArea(p, loc, material))
                e.setCancelled(true);
        }
    }

    // 성 안에서 움직이는 이벤트
    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        AreaEventHandler.checkStepIntoArea(p);
    }

    // 리로드 이벤트
    @EventHandler
    public void onReloadEvent(ServerLoadEvent e) {
        // 서버 areaMap 로딩하기
        if (file.get("area") == null)
            return;
        Set<String> areaSet = file.getConfigurationSection("area").getKeys(false);
        for (String name : areaSet) {
            int x1 = file.getInt("area." + name + ".x1");
            int x2 = file.getInt("area." + name + ".x2");
            int y = file.getInt("area." + name + ".y");
            int z1 = file.getInt("area." + name + ".z1");
            int z2 = file.getInt("area." + name + ".z2");
            areaMap.put(name, new AreaData(x1, x2, y, z1, z2));
        }
    }

    // 같은 해적단끼리 PVP 불가능
    @EventHandler
    public void onInteractEvent(EntityDamageByEntityEvent e) {
        if(e.getDamager().getType() == null || e.getEntity().getType() == null)
            return;

        if (e.getDamager().getType() == EntityType.PLAYER && e.getEntity().getType() == EntityType.PLAYER) {
            Player p = (Player) e.getEntity();
            Player hitPlayer = (Player) e.getDamager();
            if (!EventUtil.checkHitPlayer(p, hitPlayer))
                e.setCancelled(true);
        }
    }

    // NPC 클릭시 영토 이동 이벤트
    @EventHandler
    public void onWarpNpcClickEvent(PlayerInteractEntityEvent e){
        Entity entity = e.getRightClicked();
        Player p = e.getPlayer();
        String npcName = "[ [W] &a영토 워프 ]";
        if(entity.getCustomName().equals(npcName)){
            AreaEventHandler.teleportMyPirateArea(p);
        }
    }

    // 보스바 이벤트
    @EventHandler
    public void onPirateJoinEvent(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        String name = p.getName();
        String pirateName = EventUtil.findPirateName(name);

        if(pirateName == null)
            return;
        if(bossBarMap.containsKey(pirateName)){
            BossBar bossBar = bossBarMap.get(pirateName);
            if(!warMap.containsKey(pirateName)){
                bossBar.removePlayer(p);
            }else{
                bossBar.addPlayer(p);
            }
        }
    }
}