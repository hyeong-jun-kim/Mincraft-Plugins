package neo.feature;

import neo.data.DataManager;
import neo.main.Main;
import neo.util.EventUtil;
import neo.util.Util;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

// 일반 명령어
public class Sailor {
    Main main;
    Util util;
    static DataManager data = Main.getData();
    static FileConfiguration file = Main.getData().getFile();
    static HashMap<String, String> inviteMap = Main.inviteMap;
    Player p;

    public Sailor(Main main, Player p) {
        this.main = main;
        this.p = p;
        util = new Util(p);
    }

    public void printPirateInfo() {
        String name = p.getName();
        String pirateName = util.findPirateName(name);
        if (pirateName == null)
            return;

        String captainName = file.getString("pirates." + pirateName
                + ".captain");
        p.sendMessage(ChatColor.DARK_GRAY + "==============================================");
        p.sendMessage(EventUtil.getColoredPirateName(pirateName) + ChatColor.DARK_AQUA + "해적단");
        p.sendMessage(ChatColor.YELLOW + "선장 - " + captainName);
        int x = file.getInt("area." + captainName + ".x1") + 35;
        int y = file.getInt("area." + captainName + ".y");
        int z = file.getInt("area." + captainName + ".z1") + 35;
        p.sendMessage("신호기 좌표 - " + ChatColor.YELLOW + "x: " + x + ", y: " + y +", z: " + z);
        if(file.get("pirates." + pirateName + ".member") != null) {
            file.getConfigurationSection("pirates." + pirateName + ".member").getKeys(false)
                    .forEach(sailor ->
                            p.sendMessage(ChatColor.DARK_PURPLE + "선원 - " + sailor));
        }
        p.sendMessage(ChatColor.DARK_GRAY + "==============================================");
    }

    public void exitPirate() {
        String name = p.getName();
        String pirateName = util.findPirateName(name);
        if (pirateName == null)
            return;

        // 선장일 경우 파티 사라짐
        if (util.checkCaptain(p.getName())) {
            EventUtil.deleteBorder(p, p.getName());
            Set<Player> players =  EventUtil.getPiratePlayers(pirateName);
            for(Player player : players){
                Main.cM.joinChannel(player, "General");
                player.playerListName(Component.text(name));
            }
            file.set("pirates." + pirateName, null);
            file.set("area." + p.getName(), null);
            data.saveConfig();
        } else {
            Main.cM.joinChannel(p, "General");
            p.playerListName(Component.text(name));
            file.set("pirates." + pirateName + ".member." + name, null);
            data.saveConfig();
        }
        p.sendMessage(ChatColor.GREEN + "성공적으로 탈퇴가 완료되었습니다.");

    }

    public void printCaptainAndPirateName() {
        String name = p.getName();
        String pirateName = util.findPirateName(name);
        if (pirateName == null)
            return;

        String captainName = file.getString("pirates." + pirateName
                + ".captain");
        p.sendMessage(ChatColor.DARK_GRAY + "==============================================");
        p.sendMessage(ChatColor.AQUA + EventUtil.getColoredPirateName(pirateName) + "해적단 - " + ChatColor.YELLOW + captainName);
        p.sendMessage(ChatColor.DARK_GRAY + "==============================================");
    }

    public void joinChatChannels(Player p) {
        String pirateName = util.findPirateName(p.getName());
        if (pirateName == null)
            return;
        main.cM.joinChannel(p, pirateName);
    }

    public void acceptInvite(){
        String name = p.getName();
        if(inviteMap.containsKey(name)){
            String pirateName = inviteMap.get(name);
            if(!file.contains("pirates." + pirateName)){
                p.sendMessage(ChatColor.RED + "존재하지 않는 해적단입니다.");
                inviteMap.remove(name);
                return;
            }
            p.sendMessage( EventUtil.getColoredPirateName(pirateName) + ChatColor.GREEN + "해적단 초대 수락 요청이 처리되었습니다.");
            p.playerListName(Component.text("[" + EventUtil.getColoredPirateName(pirateName) + "해적단]" + p.getName()));

            file.set("pirates." + pirateName + ".member." + name, name);
            data.saveConfig();
            inviteMap.remove(name);

        }else{
            p.sendMessage(ChatColor.RED + "해적단 초대가 와야 위 명령어를 사용하실 수 있습니다.");
        }
    }

    public void rejectInvite(){
        String name = p.getName();
        if(inviteMap.containsKey(name)){
            String pirateName = inviteMap.get(name);
            if(!file.contains("pirates." + pirateName)){
                p.sendMessage(ChatColor.RED + "존재하지 않는 해적단입니다.");
                inviteMap.remove(name);
                return;
            }
            p.sendMessage(ChatColor.GREEN + pirateName +"해적단 초대 수락 요청을 거절하였습니다.");
            inviteMap.remove(name);
        }else{
            p.sendMessage(ChatColor.RED + "해적단 초대가 와야 위 명령어를 사용하실 수 있습니다.");
        }
    }
}
