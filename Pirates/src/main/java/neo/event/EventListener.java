package neo.event;


import io.papermc.paper.event.server.ServerResourcesReloadedEvent;
import neo.data.AreaData;
import neo.data.DataManager;
import neo.event.handler.AreaEventHandler;
import neo.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.server.ServerLoadEvent;

import java.util.HashMap;
import java.util.Set;

public class EventListener implements Listener {
    static HashMap<String, AreaData> areaMap = Main.getAreaMap();
    static DataManager data = Main.getData();
    static FileConfiguration file = Main.getData().getFile();

    @EventHandler
    public void onBeconPlaceEvent(BlockPlaceEvent e){
        Player p = e.getPlayer();
        if(e.getBlock().getType() == Material.BEACON){
            AreaEventHandler.createArea(p);
        }
    }

    // 성 안에서 블럭 부시는 이벤트
    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent e){
        Player p = e.getPlayer();
        Location loc = e.getBlock().getLocation();
        if(!AreaEventHandler.checkPlaceorBreakIntoArea(p, loc))
            e.setCancelled(true);
    }

    // 성 안에서 블럭 설치하는 이벤트
    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent e){
        Player p = e.getPlayer();
        Location loc = e.getBlock().getLocation();
        if(AreaEventHandler.checkPlaceorBreakIntoArea(p, loc))
            e.setCancelled(true);
    }

    // 성 안에서 움직이는 이벤트
    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent e){
        Player p = e.getPlayer();
        AreaEventHandler.checkStepIntoArea(p);
    }

    @EventHandler
    public void onReloadEvent(ServerLoadEvent e) {
        // 서버 areaMap 로딩하기
        if (file.get("area") == null)
            return;
        Set<String> areaSet = file.getConfigurationSection("area").getKeys(false);
        for (String name : areaSet) {
            int x1 = file.getInt("area." + name + ".x1");
            int x2 = file.getInt("area." + name + ".x2");
            int z1 = file.getInt("area." + name + ".z1");
            int z2 = file.getInt("area." + name + ".z2");
            areaMap.put(name, new AreaData(x1, x2, z1, z2));
        }
    }
}
