package neo.command;

import neo.shop.Shop;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("cmd에서는 이 작업을 수행하지 못합니다.");
            return true;
        } else {
            if (label.equals("상점")) {
                Player p = (Player) sender;
                Shop shop = new Shop();
                if (p.isOp()) {
                    String npcName = args[1];
                    switch (args.length) {
                        case 2:
                            if (args[0].equals("생성")) {
                                shop.createShop(p, npcName);
                                return true;
                            } else if (args[0].equals("삭제")) {
                                shop.deleteShop(p, npcName);
                                return true;
                            }
                        case 3:

                        case 4:

                        case 5:
                            if (args[0].equals("변경")) {
                                int key = Integer.parseInt(args[2]);
                                int price = Integer.parseInt(args[3]);
                                String itemCode = args[4];
                                shop.addShopItem(npcName, key, price, itemCode, p);
                                return true;
                            }
                    }
                }
            }
        }
        return true;
    }

}
