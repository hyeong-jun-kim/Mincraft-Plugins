package neo.command;

import neo.feature.CustomAdvencement;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("cmd에서는 이 작업을 수행하지 못합니다.");
            return true;
        }else {
            if(label.equals("remmsg")){
                Player p = (Player)sender;
                CustomAdvencement advancement = new CustomAdvencement();
                if(args.length == 4){
                    String nickName = args[0];
                    String iconItemName = args[1];
                    String title = args[2];
                    String content = args[3];
                    Player targetPlayer = Bukkit.getPlayer(nickName);
                    advancement.sendMsgToPlayer(targetPlayer, iconItemName, title, content);
                }
            }
        }
        return true;
    }
}
