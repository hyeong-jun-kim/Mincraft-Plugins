package neo.feature;

import neo.data.DataManager;
import neo.main.Main;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Area {
    static DataManager data = Main.getData();
    static FileConfiguration file = Main.getData().getFile();
    Player p;
    Area(Player p){
        this.p = p;
    }
    // 신호기 설치 시 70 X 70 영토 생성
    public static void createArea(Player p){
        // 예외처리
        if(!checkExistArea(p))
            return;

        World world = p.getWorld();
        if(world.getName().equals("island")){
            Location loc = p.getLocation();
            String key = p.getName();
            int x = loc.getBlockX();
            int y = loc.getBlockY();
            int z = loc.getBlockZ();
            for(int i = 0; i < 70; i++){
                int blockX = loc.getBlockX() - 35 + i;
                world.getBlockAt(blockX, y, z).setType(Material.BEDROCK);
            }
            for(int i = 0; i < 70; i++){
                int blockZ = loc.getBlockZ() - 35 + i;
                world.getBlockAt(x, y, blockZ).setType(Material.BEDROCK);
            }
            file.set("area."+key+"x1", x-35);
            file.set("area."+key+"x2", x+35);
            file.set("area."+key+"y1", y-35);
            file.set("area."+key+"y2", y+35);
            file.set("area."+key+"z1", z-35);
            file.set("area."+key+"z2", z+35);
            data.saveConfig();
            p.sendMessage(ChatColor.GREEN + "성공적으로 영토가 생성되었습니다.");
        }
    }
    // 이미 설치한 신호기가 있는지 확인
    public static boolean checkExistArea(Player p){
        String key = p.getName();
        if(file.getConfigurationSection("area").contains(key)){
            p.sendMessage(ChatColor.RED + "이미 생성된 영토가 있습니다.");
            return true;
        }
        return false;
    }
}
