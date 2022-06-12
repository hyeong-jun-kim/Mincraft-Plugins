package neo.event;

import neo.data.DataManager;
import neo.main.Main;
import neo.mineral.Mineral;
import neo.mineral.MineralScheduler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;

public class EventListener implements Listener {
    DataManager data = Main.getData();
    Main plugin = Main.getPlugin();

    // 설정된 위치의 블록을 캐면 광물 생성 스케쥴러 실행
    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent e) {
        Location loc = e.getBlock().getLocation();
        int x = (int) loc.getX();
        int y = (int) loc.getY();
        int z = (int) loc.getZ();
        if (Mineral.getPlacedBlockKey(x, y, z) != 0 && Mineral.checkBlockAllBreak(loc)) { // 모든 블록이 다 깨졌는지 체크
            int key = Mineral.getPlacedBlockKey(x, y, z);
            // 인접한 광물 모두 5초뒤에 똑같은걸로 생성
            if(key != 0){
                MineralScheduler.runRadomMineralTask(loc, key);
            }
        }
    }
}




