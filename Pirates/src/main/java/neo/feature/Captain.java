package neo.feature;

import neo.data.DataManager;
import neo.main.Main;
import neo.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;

// 선장 명령어
public class Captain {
    static DataManager data = Main.getData();
    static FileConfiguration file = Main.getData().getFile();
    Player p;
    Util util;
    public Captain(Player p){
        this.p = p;
        util = new Util(p);
    }

    public void create(String name){
        if(name.length() > 7){
            p.sendMessage(ChatColor.RED + "최대 7글자까지 설정이 가능합니다.");
            return;
        }
        // 예외 처리
        if(!util.checkCaptain(name))
            return;
        if(file.contains("pirates." + name)){
            p.sendMessage(ChatColor.RED + "이미 존재하는 해적단 이름입니다.");
        }
        if(util.checkUsernameInPirates(name))
            return;

        file.set("pirates." + name + ".captain", p.getName());
        data.saveConfig();
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

        if(Bukkit.getPlayer(name).isOnline()){
            Bukkit.getPlayer(name).sendMessage(ChatColor.GREEN + p.getName() + "님이 " + pirateName + " 해적단에 초대하셨습니다.");
        }
        file.set("pirates." + pirateName + ".member." + name, name);
        data.saveConfig();
        p.sendMessage(ChatColor.GREEN + name + "님이 해적단에 초대되셨습니다.");
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
        }

        file.set("pirates." + pirateName + ".member." + targetName, null);
        data.saveConfig();
        p.sendMessage(ChatColor.GREEN + "성공적으로 " +targetName +"을 해적단에서 제거했습니다.");
    }
}
