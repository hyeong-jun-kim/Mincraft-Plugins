package neo.feature;

import neo.data.AreaData;
import neo.data.DataManager;
import neo.main.Main;
import neo.util.EventUtil;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.HashMap;

public class Area {
    static DataManager data = Main.getData();
    static FileConfiguration file = Main.getData().getFile();
    Player p;
    Area(Player p){
        this.p = p;
    }
    // 신호기 설치 시 70 X 70 영토 생성
    public static void createArea(Player p, BlockPlaceEvent e){
        // 예외처리
        if(checkExistArea(p)){
            e.setCancelled(true);
            return;
        }

        World world = p.getWorld();
        if(world.getName().equals("island")){ // TODO 이부분 나중에 island로 변경
            Location loc = e.getBlock().getLocation();
            // 겹치는 구역 없는지 확인
            if(!EventUtil.checkCreateArea(loc, p)){
                e.setCancelled(true);
                return;
            }
            String key = p.getName();
            // 블록 설치
            int x = loc.getBlockX();
            int y = loc.getBlockY();
            int z = loc.getBlockZ();

            for(int i = x - 35; i <= x + 35; i++){
                for(int j = z - 35; j <= z + 35; j++){
                    if(i == x - 35 || i == x + 35 || j == z - 35 || j == z + 35){
                        world.getBlockAt(i, y, j).setType(Material.BEDROCK);
                    }
                }
            }
            file.set("area."+key+".x1", x-35);
            file.set("area."+key+".x2", x+35);
            file.set("area."+key+".y", y);
            file.set("area."+key+".z1", z-35);
            file.set("area."+key+".z2", z+35);
            data.saveConfig();
            EventUtil.insertAreaMap(p, loc);
            p.sendMessage(ChatColor.GREEN + "성공적으로 영토가 생성되었습니다.");
        }
    }
    // 이미 설치한 신호기가 있는지 확인
    public static boolean checkExistArea(Player p){
        String key = p.getName();
        if(file.getConfigurationSection("area") == null)
            return false;

        if(file.getConfigurationSection("area").contains(key)){
            p.sendMessage(ChatColor.RED + "이미 생성된 영토가 있습니다.");
            return true;
        }
        return false;
    }
}
