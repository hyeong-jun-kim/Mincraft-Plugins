package neo.util;

import neo.data.DataManager;
import neo.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Arrays;

// 예외처리 클래스
public class Util {
    static DataManager data = Main.getData();
    static FileConfiguration file = Main.getData().getFile();
    Player p;

    public Util(Player p){
        this.p = p;
    }

    // 해적단 이름 찾기
    public String findPirateName(String name){
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
        if(pirateName == null)
            p.sendMessage(ChatColor.YELLOW + "해당 작업을 수행하실려면 해적단에 가입해 주시길 바랍니다.");
        return pirateName;
    }

    // 존재하는 유저인지 확인
    public boolean checkUserExist(String name){
        long count = Arrays.stream(Bukkit.getOfflinePlayers()).filter(
                n -> n.getName().equals(name)
        ).count();
        if(count == 0){
            p.sendMessage(ChatColor.RED + "존재하지 않는 플레이어입니다.");
            return false;
        }
        return true;
    }

    // 해적단에 속하는 유저인지 확인
    public boolean checkUsernameInPirates(String name){
        if(file.get("pirates") == null)
            return false;
        // 해적단에 속해있는지 확인
        long count = file.getConfigurationSection("pirates").getKeys(true).stream().filter(
                k -> file.contains("pirates." + k + name)).count();
        if(count > 0){
            p.sendMessage(ChatColor.YELLOW + "해당 작업을 수행하실려면 현재 해적단을 탈퇴하신 뒤에 다시 수행해주세요");
            return true;
        }
        return false;
    }

    // 선장인지 확인
    public boolean checkCaptain(String name){
        if(file.get("area") == null){
            p.sendMessage(ChatColor.RED + "영토를 생성하신 뒤에 위 명령어를 실행해주세요.");
            return false;
        }
        if(file.getConfigurationSection("area").getKeys(false).stream()
                .filter(key -> key.equals(name)).findAny().orElse(null) == null){
            p.sendMessage(ChatColor.RED + "위 명령어는 선장만 가능한 명령어입니다.");
            return false;
        }else{
            return true;
        }
    }

    // 선장 이름으로 해적단 찾기
    public String findPirateNameByCaptain(String name){
        if(file.get("pirates") == null)
            return null;

        String pirateName = file.getConfigurationSection("pirates").getKeys(false)
                .stream().filter(k ->
                        file.getString("pirates." + k + ".captain")
                                .equals(name)).findAny().orElse(null);
        return pirateName;
    }

    // 해적단 이름으로 선장 이름 찾기
    public String getCaptainName(String pirateName){
        String captainName = file.getString("pirates." + pirateName + ".captain");
        return captainName;
    }
}
