package neo.command;

import neo.mineral.Mineral;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
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
            if(label.equals("광물설정")){
                Player p = (Player)sender;
                switch(args.length){
                    case 3:
                        Mineral mineral = new Mineral();
                        World world = Bukkit.getWorld("world");
                        int x = Integer.parseInt(args[1]);
                        int y = Integer.parseInt(args[2]);
                        int z = Integer.parseInt(args[3]);
                        Location loc = new Location(world, x, y, z);
                        mineral.createMinal(x, y, z, p);
                }
            }
        }
        return true;
    }

}
