package neo.command;

import neo.shop.Shop;
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
            if(label.equals("상점")) {
                Player p = (Player) sender;
                Shop shop = new Shop();
                if (p.isOp()) {
                    switch (args.length) {
                        case 2:
                            if (args[0].equals("생성")) {
                                String npcName = args[1];
                                shop.createShop(p, npcName);
                                return true;
                            }else if(args[0].equals("삭제")){
                                String npcName = args[1];
                                shop.deleteShop(p, npcName);
                                return true;
                            }
                    }
                }
            }
        }
        return true;
    }

}
