package neo.command;

import neo.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("cmd에서는 이 작업을 수행하지 못합니다.");
            return true;
        }else {
            Player p = (Player) sender;
            if (p.isOp()) {
                if (label.equals("날짜")) {
                    switch (args.length) {
                        case 0:
                            p.sendMessage(ChatColor.GREEN + "----------------------------------------------------");
                            p.sendMessage(ChatColor.YELLOW + "/날짜 초기화" + ChatColor.WHITE + " - 월드의 날짜를 0일로 초기화 합니다.");
                            p.sendMessage(ChatColor.GREEN + "----------------------------------------------------");
                            return true;
                        case 1:
                            if (args[0].equals("초기화")) {
                                Main.dayTime = 0L;
                                Bukkit.getWorld("World").setFullTime(0L);
                                p.sendMessage(ChatColor.GREEN + "정상적으로 월드 시간 초기화가 완료되었습니다.");
                                return true;
                            }
                    }
                }
            }
        }
        return true;
    }

}
