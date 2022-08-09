package neo.feature;

import neo.data.DataManager;
import neo.main.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.Arrays;

// 일반 명령어
public class Sailor {
    static DataManager data = Main.getData();
    static FileConfiguration file = Main.getData().getFile();
    Player p;
    Sailor(Player p){
        this.p = p;
    }
    public void printPirateInfo(){
        String name = p.getName();
        String pirateName = findPirateName(name);
        // 예외처리
        if(!checkUsernameInPirates(name))
            return;

        String captainName = file.getString("pirates." + pirateName
        + ".captain");
        p.sendMessage(ChatColor.GREEN + "==============================================");
        p.sendMessage(ChatColor.DARK_AQUA + pirateName + "해적단");
        p.sendMessage(ChatColor.YELLOW + "선장 - " + captainName);
        file.getConfigurationSection("pirates." + pirateName + ".member").getKeys(false)
                        .forEach(sailor ->
                                p.sendMessage(ChatColor.DARK_PURPLE + "선원 - " + sailor));
        p.sendMessage(ChatColor.GREEN + "==============================================");
    }

    public void exitPirate(){
        String name = p.getName();
        String pirateName = findPirateName(name);
        // 예외처리
        if(!checkUsernameInPirates(name))
            return;

        file.set("pirates." + pirateName + ".member." + name, null);
        data.saveConfig();
        p.sendMessage(ChatColor.GREEN + "성공적으로 탈퇴가 완료되었습니다.");
    }

    public void printCaptainAndPirateName(){
        String name = p.getName();
        String pirateName = findPirateName(name);
        // 예외처리
        if(!checkUsernameInPirates(name))
            return;
        String captainName = file.getString("pirates." + pirateName
                + ".captain");
        p.sendMessage(ChatColor.GREEN + "==============================================");
        p.sendMessage(ChatColor.AQUA + pirateName +"해적단 - " + ChatColor.YELLOW + captainName);
        p.sendMessage(ChatColor.GREEN + "==============================================");
    }

    public boolean checkUsernameInPirates(String name){
        // 해적단에 속해있는지 확인
        long count = file.getConfigurationSection("pirates").getKeys(true).stream().filter(
                n -> n.equals(name)).count();
        if(count > 0){
            return true;
        }else{
            p.sendMessage(ChatColor.YELLOW + "해당 작업을 수행하실려면 해적단에 가입해 주시길 바랍니다.");
            return false;
        }
    }

    public String findPirateName(String name){
        String pirateName = file.getConfigurationSection("pirates").getKeys(false)
                .stream().filter(
                        key -> file.contains("pirates." + key + ".member." + name)
                ).findAny().orElse(null);
        return pirateName;
    }
}
