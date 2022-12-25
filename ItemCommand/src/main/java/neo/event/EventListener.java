package neo.event;

import handler.ItemCommand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EventListener implements Listener {
    @EventHandler
    public void onPlayerCommandEvent(PlayerCommandPreprocessEvent e){
        Player p = e.getPlayer();
        ItemCommand itemCommand = new ItemCommand(p);
        String cmd = e.getMessage().replace("/","");

//        ItemStack item = new ItemStack(Material.SAND, 60);
//        ItemMeta itemMeta = item.getItemMeta();
//        itemMeta.setDisplayName(ChatColor.GREEN+ "테스트");
//        item.setItemMeta(itemMeta);
//        p.getInventory().setItemInMainHand(item);

        // 등록된 커맨드일 때 이벤트 처리
        if(itemCommand.isRegisterCommand(cmd)){
            e.setCancelled(true);

            if(itemCommand.isHaveItem()){
                itemCommand.excuteItemCommand();
            }else{
                itemCommand.excuteCommonCommand();
            }
        }
    }
}
