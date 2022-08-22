package neo.util;

import neo.data.AreaData;
import neo.data.DataManager;
import neo.main.Main;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EventUtil {
    static HashMap<String, String> warMap = Main.getWarMap();
    static DataManager data = Main.getData();
    static FileConfiguration file = Main.getData().getFile();
    static HashMap<String, AreaData> areaMap = Main.getAreaMap();
    static HashMap<String, BossBar> bossBarMap = Main.bossBarMap;

    // CaptainEventHandler
    public static boolean checkCaptain(Player p) {
        if (file.get("area") == null)
            return false;
        String name = p.getName();
        if (file.getConfigurationSection("area").getKeys(false).contains(name)) {
            return true;
        } else {
            return false;
        }
    }

    // AreaEventHandler Util
    public static Map.Entry<String, AreaData> getAreaData(Location loc) {
        int x = loc.getBlockX();
        int z = loc.getBlockZ();
        Map.Entry<String, AreaData> data = areaMap.entrySet().stream().filter(
                map -> ((map.getValue().x1 <= x && map.getValue().x2 >= x)
                        && (map.getValue().z1 <= z && map.getValue().z2 >= z))
        ).findAny().orElse(null);
        return data;
    }

    public static boolean checkCreateArea(Location loc, Player p) {
        World world = p.getWorld();
        if(p.getWorld().getName().equals("island"))
            return false;

        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();
        for (int i = x - 35; i <= x + 35; i++) {
            for (int j = z - 35; j <= z + 35; j++) {
                // 경계선
                if (i == x - 35 || i == x + 35 || j == z - 35 || j == z + 35) {
                    Location locBlock = new Location(world, i, y, j);
                    if (checkAreaExist(locBlock, p)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static boolean checkAreaExist(Location loc, Player p) {
        int x = loc.getBlockX();
        int z = loc.getBlockZ();
        Map.Entry<String, AreaData> data = areaMap.entrySet().stream().filter(
                map -> ((map.getValue().x1 <= x && map.getValue().x2 >= x)
                        && (map.getValue().z1 <= z && map.getValue().z2 >= z))
        ).findAny().orElse(null);
        if (data == null) {
            return false;
        } else {
            p.sendMessage(ChatColor.RED + "다른 해적단이랑 좀더 떨어진 위치에서 신호기를 설치 해주세요.");
            return true;
        }
    }

    public static void insertAreaMap(Player p, Location loc) {
        String name = p.getName();
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();
        AreaData areaData = new AreaData(x - 35, x + 35, y, z - 35, z + 35);
        areaMap.put(name, areaData);
    }

    public static String findPirateCaptainName(String name) {
        if (file.get("pirates") == null)
            return null;
        // 선원일 경우
        String pirateName = file.getConfigurationSection("pirates").getKeys(false)
                .stream().filter(
                        key -> file.contains("pirates." + key + ".member." + name)
                ).findAny().orElse(null);
        if(pirateName == null){
            // 선장일 경우 서치
            pirateName = file.getConfigurationSection("pirates").getKeys(false)
                    .stream().filter(
                            key -> file.getString("pirates." + key + ".captain").equals(name)
                    ).findAny().orElse(null);
        }
        if (pirateName == null)
            return null;
        // 선장일 경우
        String captainName = file.getString("pirates." + pirateName + ".captain");
        return captainName;
    }

    public static String findPirateName(String name) {
        if (file.get("pirates") == null)
            return null;
        String pirateName = file.getConfigurationSection("pirates").getKeys(false)
                .stream().filter(
                        key -> file.contains("pirates." + key + ".member." + name)
                ).findAny().orElse(null);
        if (pirateName == null) {
            // 선장일 경우 서치
            pirateName = file.getConfigurationSection("pirates").getKeys(false)
                    .stream().filter(
                            key -> file.getString("pirates." + key + ".captain").equals(name)
                    ).findAny().orElse(null);
        }
        return pirateName;
    }

    public static String getWarTargetPirateName(Player p, String captainName, String areaName) {
        String pirateName = findPirateName(p.getName());
        if (pirateName == null)
            return null;
        // 전쟁 중 일때
        if (warMap.containsKey(pirateName)) {
            String targetPirate = warMap.get(pirateName);
            String targetCaptain = file.getString("pirates." + targetPirate + ".captain");
            if (targetCaptain.equals(areaName))
                return targetPirate;
        }
        return null;
    }

    // 해적단 선원들 가져오기
    public static Set<Player> getPiratePlayers(String pirateName){
        Set<String> playersNameSet = new HashSet<>();
        Set<Player> piratePlayers = new HashSet<>();
        // 선장 가져오기
        String captain = file.getString("pirates." + pirateName + ".captain");
        playersNameSet.add(captain);

        // 선원 가져오기
        if(file.get("pirates." + pirateName + ".member") != null){
            file.getConfigurationSection("pirates." + pirateName + ".member").getKeys(false)
                    .stream().forEach(name -> playersNameSet.add(name));
        }
        for(String name: playersNameSet){
            Player player = Bukkit.getPlayer(name);
            if(player != null && player.isOnline()){
                piratePlayers.add(player);
            }
        }
        return piratePlayers;
    }

    public static void destoryPirate(String myPirateName, String targetPirateName, String areaName, Player p) {
        EventUtil.sendMessagePiratePlayer(myPirateName, ChatColor.GOLD + EventUtil.getColoredPirateName(targetPirateName) +
                ChatColor.GOLD  + "의 전쟁에서 승리했습니다!");
        EventUtil.sendMessagePiratePlayer(targetPirateName, ChatColor.RED + EventUtil.getColoredPirateName(myPirateName) +
                ChatColor.RED + "의 전쟁에서 패배했습니다!");
        // 해적단 경계선 제거
        deletePirate(p, areaName);

        //Map 제거
        areaMap.remove(areaName);
        warMap.remove(myPirateName);
        warMap.remove(targetPirateName);

        // 일반 채널로 이동, 이름 기본으로 변경
        Set<Player> targetPlayers = getPiratePlayers(targetPirateName);
        for (Player player : targetPlayers) {
            Main.cM.joinChannel(player, "General");
            player.playerListName(Component.text(player.getName()));
        }

        file.set("pirates." + targetPirateName, null);
        file.set("area." + areaName, null);
        data.saveConfig();
    }

    public static void sendMessagePiratePlayer(String pirateName, String message) {
        for (Player p : getPiratePlayers(pirateName)) {
            p.sendMessage(message);
        }
    }

    public static void deletePirate(Player p, String areaName){
        AreaData areaData = areaMap.get(areaName);
        int y = areaData.y;
        int x1 = areaData.x1;
        int x2 = areaData.x2;
        int z1 = areaData.z1;
        int z2 = areaData.z2;

        // 신호기 삭제하기
        int bX = x1 + 35;
        int bY = y;
        int bZ = z1 + 35;
        p.getWorld().getBlockAt(bX, bY, bZ).setType(Material.AIR);

        // 경계선 삭제하기
        for(int i = x1; i <= x2; i++){
            for(int j = z1 - 35; j <= z2 + 35; j++){
                if(i == x1 || i == x2 || j == z1 || j == z2){
                    p.getWorld().getBlockAt(i, y, j).setType(Material.AIR);
                }
            }
        }
    }

    // 같은 해적단끼리는 못때림
    public static boolean checkHitPlayer(Player p, Player hitPlayer){
        if(!p.getWorld().getName().equals("island")) // TODO 이부분 나중에 island로 수정
            return true;

        String myPirateName = findPirateName(p.getName());
        String theirPirateName = findPirateName(hitPlayer.getName());
        if(myPirateName == null || theirPirateName == null)
            return true;
        if(myPirateName.equals(theirPirateName))
            return false;

        return true;
    }

    // 보스바 추가하기
    public static void addBossBar(BossBar bossBar, String pirateName){
        getPiratePlayers(pirateName).stream().forEach(
                p -> {
                    bossBar.addPlayer(p);
                    bossBarMap.put(pirateName, bossBar);
                }
        );
    }

    public static String getColoredPirateName(String pirateName){
        String coloredPirateName = ChatColor.translateAlternateColorCodes('&', pirateName);
        coloredPirateName += ChatColor.RESET + "";
        return coloredPirateName;
    }
}
