package neo.mineral;

import neo.data.DataManager;
import neo.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class Mineral {
    DataManager data = Main.getData();
    public void createMinal(int x, int y, int z, Player p){
        int key = 1;
        World world = Bukkit.getWorld("world");
        Location loc = new Location(world, x, y, z);
        if(data.getFile().contains("mineral")){ // 처음 생성이 아닐때
            ConfigurationSection section = data.getFile().getConfigurationSection("mineral");
            // 같은 위치가 있는지 체크
            for(String k: section.getKeys(false)){
                Location dataLoc = data.getFile().getLocation("mineral."+ k +".location");
                if(dataLoc == loc){ // 이미 현재 위치가 저장되어 있다면
                    key = Integer.parseInt(k) + 1;
                    break;
                }
                key++;
            }
            data.getFile().set("mineral." + key + ".location", loc);
            data.saveConfig();
        }else{ // 처음 생성일 때
            data.getFile().set("mineral." + key + ".location", loc);
            data.saveConfig();
        }
        p.sendMessage(ChatColor.GREEN + "x: " + x + " y " + y + " z " + z + "위치에 광물 자동 생성이 설정이 완료되었습니다.");
    }
}
