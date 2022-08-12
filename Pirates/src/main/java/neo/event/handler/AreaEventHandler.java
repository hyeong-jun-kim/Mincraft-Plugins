package neo.event.handler;

import neo.data.AreaData;
import neo.data.DataManager;
import neo.feature.Area;
import neo.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class AreaEventHandler {
    static DataManager data = Main.getData();
    static FileConfiguration file = Main.getData().getFile();
    static HashMap<String, AreaData> areaMap = Main.getAreaMap();
    public static void createArea(Player p){
        Area.createArea(p);
    }

    public static boolean checkStepIntoArea(Player p){
        Location loc = p.getLocation();
        Map.Entry<String, AreaData> data = getAreaData(loc);
        if(data == null){
            return true;
        }else{
            String areaName = data.getKey();
            String pirateName = findPirateCaptainName(p.getName());
            if(areaName.equals(pirateName)){
                return true;
            }else{
                AreaData areaData = data.getValue();
                double x = areaData.x1 - 2;
                double y = loc.getY();
                double z = areaData.z1 - 2;
                loc.set(x, y, z);
                p.teleport(loc);
                p.sendMessage(ChatColor.RED + "전쟁중이 아닐 경우에는 해당 영토로 들어오실 수 없으십니다.");
                return false;
            }
        }
    }

    public static boolean checkPlaceorBreakIntoArea(Player p, Location loc){
        Map.Entry<String, AreaData> data = getAreaData(loc);
        if(data == null){
            return true;
        }else{
            String areaName = data.getKey();
            String pirateName = findPirateCaptainName(p.getName());
            if(pirateName == null)
                return false;

            if(areaName.equals(pirateName)){
                return true;
            }else{
                return false;
            }
        }
    }

    public static Map.Entry<String, AreaData> getAreaData(Location loc){
        int x = loc.getBlockX();
        int z = loc.getBlockZ();
        Map.Entry<String, AreaData> data = areaMap.entrySet().stream().filter(
                map -> ((map.getValue().x1 <= x && map.getValue().x2 >= x)
                        && (map.getValue().z1 <= z && map.getValue().z2 >= z))
        ).findAny().orElse(null);
        return data;
    }

    public static String findPirateCaptainName(String name){
        String pirateName = file.getConfigurationSection("pirates").getKeys(false)
                .stream().filter(
                        key -> file.contains("pirates." + key + ".member." + name)
                ).findAny().orElse(null);
        if(pirateName == null)
            return null;

        String captainName = file.getString("pirates." + pirateName + ".captain");
        return captainName;
    }
}
