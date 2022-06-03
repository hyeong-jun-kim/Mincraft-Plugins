package neo.command;

import neo.data.DataManager;
import neo.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
    DataManager data = Main.getData();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("cmd에서는 이 작업을 수행하지 못합니다.");
            return true;
        }else {
            Player p = (Player)sender;
            if(label.equals("부활단축")){
                switch(args.length) {
                    case 3:
                        if (p.isOp()) {
                            String arg = args[0];
                            if (arg.equals("추가")) {
                                String nickName = args[1];
                                int count = Integer.parseInt(args[2]);
                                boolean check = false;
                                OfflinePlayer[] players = Bukkit.getServer().getOfflinePlayers();
                                for (OfflinePlayer op : players) {
                                    if (nickName.equals(op.getName())) {
                                        check = true;
                                        break;
                                    }
                                }
                                if (!check) { // 유저가 서버에 존재하지 않을 때
                                    p.sendMessage(ChatColor.RED + "존재하지 않는 유저입니다. 다시 확인해주세요");
                                    return true;
                                }
                                // data.yml에 추가
                                data.getFile().set("reviveShorten." + nickName + ".count", count);
                                data.saveConfig();
                                p.sendMessage(ChatColor.GREEN + nickName + "의 부활 단축 횟수가 " + count + "로 지정되었습니다.");
                                return true;
                            } else if (arg.equals("제거")) {
                                String nickName = args[1];
                                int count = Integer.parseInt(args[2]);
                                if (!data.getFile().contains("reviveShorten." + nickName + ".count")) {
                                    p.sendMessage(ChatColor.RED + "해당 유저는 부활 단축이 0개이거나 등록되지 않았습니다.");
                                    return true;
                                }
                                int revive_count = data.getFile().getInt("reviveShorten." + nickName + ".count");
                                if (revive_count < count) {
                                    p.sendMessage(ChatColor.RED + "알맞은 숫자를 입력해주세요! " + ChatColor.GOLD + "해당 유저의 부활 단축 갯수: " + revive_count + "개");
                                    return true;
                                }
                                revive_count = revive_count - count;
                                if (revive_count <= 0) {
                                    data.getFile().set("reviveShorten." + nickName + ".count", null);
                                    data.saveConfig();
                                } else {
                                    data.getFile().set("reviveShorten." + nickName + ".count", revive_count);
                                    data.saveConfig();
                                }
                                p.sendMessage(ChatColor.GREEN + "부활 단축을 완료했습니다." + ChatColor.GOLD + nickName + "님의 남은 부활 단축 갯수: " + revive_count + "개");
                                return true;
                            }
                        }else{
                            p.sendMessage(ChatColor.RED + "이 명령어는 OP만 사용 가능합니다.");
                        }
                }
            }
        }
        return true;
    }

}
