package neo.command;

import neo.shop.Shop;
import org.bukkit.ChatColor;
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
                    String npcName = null;
                    if (args.length > 0)
                        npcName = args[1];
                    switch (args.length) {
                        case 0:
                            p.sendMessage(ChatColor.GOLD + "------------------------------------------------");
                            p.sendMessage(ChatColor.AQUA + "/상점 생성 [이름] " +ChatColor.GREEN + "- 해당 위치로 이름이 설정된 NPC가 생성됩니다");
                            p.sendMessage(ChatColor.AQUA + "/상점 변경 [이름] [1~5] [가격] [아이템코드] " +ChatColor.GREEN + "- 손에 들고있는 아이템을 이름이 같은 npc에 상점에 추가합니다");
                            p.sendMessage(ChatColor.AQUA + "/상점 삭제 [이름] " +ChatColor.GREEN + "- 상점을 삭제합니다");
                            p.sendMessage(ChatColor.AQUA + "/상점 가격 [이름] [1~5] [가격] " +ChatColor.GREEN + "- 구매시 상점의 오르는 가격을 설정합니다");
                            p.sendMessage(ChatColor.GOLD + "------------------------------------------------");
                            return true;
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
                            if (args[0].equals("가격")) {
                                int key = Integer.parseInt(args[2]);
                                int price = Integer.parseInt(args[3]);
                                shop.setAddPrice(npcName, key, price, p);
                                return true;
                            }
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
