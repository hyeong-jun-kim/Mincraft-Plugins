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
import java.util.Set;

// 일반 명령어
public class Sailor {
    Main main;
    Util util;
    static DataManager data = Main.getData();
    static FileConfiguration file = Main.getData().getFile();
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
        p.sendMessage(ChatColor.DARK_AQUA + pirateName + "해적단");
        p.sendMessage(ChatColor.YELLOW + "선장 - " + captainName);
        file.getConfigurationSection("pirates." + pirateName + ".member").getKeys(false)
                .forEach(sailor ->
                        p.sendMessage(ChatColor.DARK_PURPLE + "선원 - " + sailor));
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
            data.saveConfig();
            Set<Player> players =  EventUtil.getPiratePlayers(pirateName);
            for(Player player : players){
                Main.cM.joinChannel(player, "General");
                player.playerListName(Component.text(name));
            }
            file.set("pirates." + pirateName, null);
            file.set("area." + p.getName(), null);
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
        p.sendMessage(ChatColor.AQUA + pirateName + "해적단 - " + ChatColor.YELLOW + captainName);
        p.sendMessage(ChatColor.DARK_GRAY + "==============================================");
    }

    public void joinChatChannels(Player p) {
        String pirateName = util.findPirateName(p.getName());
        if (pirateName == null)
            return;
        main.cM.joinChannel(p, pirateName);
    }


}
