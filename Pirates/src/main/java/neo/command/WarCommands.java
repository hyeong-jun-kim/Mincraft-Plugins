package neo.command;

import neo.feature.Captain;
import neo.feature.Sailor;
import neo.feature.War;
import neo.main.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarCommands implements CommandExecutor {
    Main main;
    public WarCommands(Main main){
        this.main = main;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("cmd에서는 이 작업을 수행하지 못합니다.");
            return true;
        } else {
            if(label.equals("전쟁선포")){
                Player p = (Player)sender;
                if(args.length == 1){
                    War war = new War(main, p);
                    String targetPirate = args[0];
                    war.declare(targetPirate);
                }
            }
        }
        return true;
    }
}
