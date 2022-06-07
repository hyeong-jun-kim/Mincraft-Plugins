package neo.event;

import dev.sergiferry.playernpc.api.NPC;
import neo.shop.Shop;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.event.server.TabCompleteEvent;

public class EventListener implements Listener {
    boolean check = false;
    @EventHandler
    public void onNPCInteract(NPC.Events.Interact event){
        Player player = event.getPlayer();
        NPC npc = event.getNPC();
        NPC.Interact.ClickType clickType = event.getClickType();
    }
    @EventHandler
    public void onServerLoadEvent(ServerLoadEvent e){
        // NPC SHOP RELOAD
        Shop shop = new Shop();
        shop.reloadShop();
    }
}
