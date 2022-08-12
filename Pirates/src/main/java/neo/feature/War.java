package neo.feature;

import neo.data.AreaData;
import neo.data.DataManager;
import neo.main.Main;
import neo.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class War {
    final static int readyTime = 5 * 60;
    final static int warTime = 25 * 60;

    static DataManager data = Main.getData();
    static FileConfiguration file = Main.getData().getFile();
    public static HashMap<String, AreaData> areaMap = Main.getAreaMap();
    Player p;
    Util util;
    Main main;

    public War(Main main, Player p) {
        this.p = p;
        util = new Util(p);
    }

    public void declare(String targetPirateName) {
        if(!util.checkCaptain(p.getName()))
            return;
        if (!checkPirateName(targetPirateName))
            return;=
        String captainName = util.getCaptainName(targetPirateName);
        if (!checkPiratePlayerNum(captainName))
            return;

    }

    public boolean checkPirateName(String name) {
        if (file.get("pirates") == null)
            return false;

        double count = file.getConfigurationSection("pirates").getKeys(false).stream()
                .filter(key -> file.contains("pirates." + key)).count();
        if (count == 0) {
            p.sendMessage(ChatColor.RED + "존재하지 않는 해적단 이름입니다.");
            return false;
        }
        return true;
    }


    public boolean checkPiratePlayerNum(String captain) {
        AreaData areaData = areaMap.get(captain);
        int x1 = areaData.x1;
        int x2 = areaData.x2;
        int z1 = areaData.z1;
        int z2 = areaData.z2;

        double count = Bukkit.getOnlinePlayers().stream()
                .filter(p -> (x1 <= p.getLocation().getX() && x2 >= p.getLocation().getX())
                        && (z1 <= p.getLocation().getZ() && z2 >= p.getLocation().getZ()))
                .count();
        // TODO count 숫자 4로 수정하기
        if (count >= 1) {
            return true;
        } else {
            p.sendMessage(ChatColor.RED + "상대방의 영토에 4명이상의 플레이어가 접속해있지 않아서, 전쟁 선포가 불가능합니다.");
            return false;
        }
    }

    // 전쟁 시작
    public void startWar(String myPirateaName, String targetPirateName) {
        for(Player p: getPiratePlayers(myPirateaName)){
            p.sendMessage(ChatColor.GREEN + targetPirateName + ChatColor.GREEN + "에게 전쟁을 선포하였습니다." +
                    " 5분뒤에 전쟁이 시작됩니다.");
        }
        for(Player p: getPiratePlayers(targetPirateName)){
            p.sendMessage(ChatColor.GREEN + targetPirateName + ChatColor.GREEN + "이 전쟁을 선포하였습니다." +
                    " 5분뒤에 전쟁이 시작됩니다.");
        }

        Bukkit.getScheduler().runTaskLater(main, () -> {

        }, 1000L);
    }

    // 해적단 선원들 가져오기
    public Set<Player> getPiratePlayers(String pirateName){
        Set<String> playersNameSet = new HashSet<>();
        Set<Player> piratePlayers = new HashSet<>();
        file.getConfigurationSection("pirates." + pirateName).getKeys(false)
                .stream().forEach(name -> playersNameSet.add(name));
        for(String name: playersNameSet){
            Player player = Bukkit.getPlayer(name);
            if(player.isOnline()){
                piratePlayers.add(player);
            }
        }
        return piratePlayers;
    }
}
