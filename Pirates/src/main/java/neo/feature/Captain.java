package neo.feature;

import neo.data.DataManager;
import neo.main.Main;
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
    Captain(Player p){
        this.p = p;
    }

    public void create(String name){
        if(name.length() > 7){
            p.sendMessage(ChatColor.RED + "최대 7글자까지 설정이 가능합니다.");
            return;
        }
        // 예외 처리
        if(!checkCaptain(name))
            return;
        if(file.contains("pirates." + name)){
            p.sendMessage(ChatColor.RED + "이미 존재하는 해적단 이름입니다.");
        }
        if(!checkUsernameInPirates(name))
            return;

        file.set("pirates." + name + ".captain", p.getName());
        data.saveConfig();
        p.sendMessage(ChatColor.GREEN + "성공적으로 해적단이 형성되었습니다!");
    }

    public void invitePlayer(Player targetPlayer){
        String name = targetPlayer.getName();
        String pirateName = file.getConfigurationSection("pirates").getKeys(false)
                .stream().filter(n -> n.equals(p.getName())).findAny().orElse(null);
        // 예외처리
        if(!checkCaptain(name))
            return;
        if(!checkUserExist(name))
            return;
        if(checkUsernameInPirates(name))
            return;
        if(pirateName == null)
            return;

        file.set("pirates." + pirateName + ".member." + name, name);
        data.saveConfig();
        p.sendMessage(ChatColor.GREEN + targetPlayer.getName() + "님이 해적단에 초대되셨습니다.");
    }

    public void kickPlayer(Player targetPlayer){
        String name = targetPlayer.getName();
        String pirateName = file.getConfigurationSection("pirates").getKeys(false)
                .stream().filter(n -> n.equals(p.getName())).findAny().orElse(null);
        // 예외처리
        if(!checkUserExist(name))
            return;
        if(pirateName == null)
            return;
        if(!file.contains("pirates." + pirateName + ".member." + name)){
            p.sendMessage(ChatColor.RED + "위 플레이어는 " + pirateName + "해적단에 속해있지 않습니다.");
            return;
        }

        file.set("pirates." + pirateName + ".member." + name, null);
        data.saveConfig();
        p.sendMessage(ChatColor.GREEN + "성공적으로 " +name +"을 해적단에서 제거했습니다.");
    }

    public void declareWar(String pirateName){

    }

    public boolean checkUserExist(String name){
        long count = Arrays.stream(Bukkit.getOfflinePlayers()).filter(
                n -> n.equals(name)
        ).count();
        if(count == 0){
            p.sendMessage(ChatColor.RED + "존재하지 않는 플레이어입니다.");
            return false;
        }
        return true;
    }

    public boolean checkUsernameInPirates(String name){
        // 해적단에 속해있는지 확인
        long count = file.getConfigurationSection("pirates").getKeys(true).stream().filter(
                n -> n.equals(name)).count();
        if(count > 0){
            p.sendMessage(ChatColor.YELLOW + "해당 작업을 수행하실려면 현재 해적단을 탈퇴하신 뒤에 다시 수행해주세요");
            return true;
        }
        return false;
    }

    public boolean checkCaptain(String name){
        if(file.getConfigurationSection("area").contains(name)){
            p.sendMessage(ChatColor.RED + "위 명령어는 선장만 가능한 명령어입니다.");
            return true;
        }else{
            return false;
        }
    }
}
