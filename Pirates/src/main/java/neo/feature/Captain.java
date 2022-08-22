package neo.feature;

import neo.data.DataManager;
import neo.main.Main;
import neo.util.EventUtil;
import neo.util.Regex;
import neo.util.Util;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

// 선장 명령어
public class Captain {
    static DataManager data = Main.getData();
    static FileConfiguration file = Main.getData().getFile();
    static HashMap<String, String> inviteMap = Main.inviteMap;
    Player p;
    Util util;
    public Captain(Player p){
        this.p = p;
        util = new Util(p);
    }

    public void create(String pirateName){
        // 예외 처리
        if(pirateName.length() > 7){
            p.sendMessage(ChatColor.RED + "최대 7글자까지 설정이 가능합니다.");
            return;
        }if(!util.checkCaptain(p.getName())){
            return;
        }
        if(file.contains("pirates." + pirateName)){
            p.sendMessage(ChatColor.RED + "이미 존재하는 해적단 이름입니다.");
            return;
        }
        // 해적단 이름 변경
        if(util.checkUsernameInPirates(pirateName))
            return;
        file.set("pirates." + pirateName + ".captain", p.getName());
        data.saveConfig();
        p.playerListName(Component.text("[" + EventUtil.getColoredPirateName(pirateName) + "해적단]" + p.getName()));
        p.sendMessage(ChatColor.GREEN + "성공적으로 해적단이 형성되었습니다!");
    }

    public void invitePlayer(String name){
        if(!util.checkUserExist(name))
            return;// 예외처리
        if(!util.checkCaptain(p.getName()))
            return;
        if(util.checkUsernameInPirates(name))
            return;

        String pirateName = util.findPirateNameByCaptain(p.getName());
        if(pirateName == null)
            return;

        Player targetPlayer = Bukkit.getPlayer(name);
        if(targetPlayer.isOnline()){
            targetPlayer.sendMessage(ChatColor.GREEN + p.getName() + "님이 " + EventUtil.getColoredPirateName(pirateName) + ChatColor.GREEN + " 해적단에 초대하셨습니다. "
                    + ChatColor.YELLOW + "수락하실려면 /해적단 수락 명령어를 입력해주세요.");
            inviteMap.put(name, pirateName);
        }

        p.sendMessage(ChatColor.GREEN + targetPlayer.getName() + "님한테 해적단 초대 요청을 전송하였습니다.");
    }

    public void kickPlayer(String targetName){
        if(file.get("pirates") == null)
            return;
        if(!util.checkUserExist(targetName))
            return;

        String pirateName = file.getConfigurationSection("pirates").getKeys(false)
                .stream().filter(k -> file.contains("pirates." + k + ".member." + targetName))
                .findAny().orElse(null);
        if(pirateName == null)
            return;

        if(!file.contains("pirates." + pirateName + ".member." + targetName)){
            p.sendMessage(ChatColor.RED + "위 플레이어는 " + pirateName + "해적단에 속해있지 않습니다.");
            return;
        }

        if(Bukkit.getPlayer(targetName).isOnline()){
            Bukkit.getPlayer(targetName).sendMessage(ChatColor.RED + p.getName() + "님이 " + pirateName + " 해적단에 추방 당하셨습니다.");
            Main.cM.joinChannel(p, "General");
            p.playerListName(Component.text(p.getName()));
        }

        file.set("pirates." + pirateName + ".member." + targetName, null);
        data.saveConfig();
        p.sendMessage(ChatColor.GREEN + "성공적으로 " +targetName +"을 해적단에서 제거했습니다.");
    }
}
