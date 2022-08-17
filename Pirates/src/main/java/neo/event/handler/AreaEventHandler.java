package neo.event.handler;

import neo.data.AreaData;
import neo.data.DataManager;
import neo.feature.Area;
import neo.main.Main;
import neo.util.EventUtil;
import neo.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Map;

public class AreaEventHandler {
    static DataManager data = Main.getData();
    static FileConfiguration file = Main.getData().getFile();
    static Util util;
    public static void createArea(Player p, BlockPlaceEvent e) {
        Area.createArea(p, e);
    }

    public static boolean checkStepIntoArea(Player p) {
        if(p.isOp())
            return true;

        Location loc = p.getLocation();
        Map.Entry<String, AreaData> data = EventUtil.getAreaData(loc);
        if (data == null) {
            return true;
        } else {
            String areaName = data.getKey();
            String captainName = EventUtil.findPirateCaptainName(p.getName());

            // 전쟁 중 확인
            if (EventUtil.getWarTargetPirateName(p, captainName, areaName) != null)
                return true;

            if (areaName.equals(captainName)) {
                return true;
            } else {
                // 영토 선언하고 해적단은 안만들었을 경우
                if(data.getKey().equals(p.getName()))
                    return true;

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

    public static boolean checkPlaceorBreakIntoArea(Player p, Location loc, Material material) {
        Map.Entry<String, AreaData> data = EventUtil.getAreaData(loc);
        if (data == null) {
            return true;
        } else {
            String areaName = data.getKey();
            String captainName = EventUtil.findPirateCaptainName(p.getName());
            if (captainName == null)
                return false;

            // 자기 해적단의 신호기는 못깸
            if (material == Material.BEACON) {
                if(areaName.equals(captainName))
                    return false;

            // 전쟁 중 확인
            String targetPirateName = EventUtil.getWarTargetPirateName(p, captainName, areaName);
            if (targetPirateName != null){
                    // 상대방 영토 파괴
                    String myPirateName = EventUtil.findPirateName(p.getName());
                    EventUtil.destoryPirate(myPirateName, targetPirateName, areaName, p);
                    return true;
                }
                return true;
            }

            if (areaName.equals(captainName)) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static void teleportMyPirateArea(Player p){
        String name = p.getName();
        util = new Util(p);
        String pirateName = util.findPirateName(name);
        if(pirateName == null)
            return;

        String captainName = util.getCaptainName(pirateName);
        if(captainName == null)
            return;

        if(file.get("area." + captainName) == null)
            return;

        int x = file.getInt("area." + captainName + ".x1") + 35;
        int y = file.getInt("area." + captainName + ".y") + 1;
        int z = file.getInt("area." + captainName + ".z1") + 35;

        World world = p.getWorld();
        Location loc = new Location(world, x, y, z);

        p.teleport(loc);
        return;
    }
}
