package neo.event;

import dev.sergiferry.playernpc.api.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;

public class EventListener implements Listener {
    @EventHandler
    public void onNPCInteract(NPC.Events.Interact event){
        Player player = event.getPlayer();
        NPC npc = event.getNPC();
        NPC.Interact.ClickType clickType = event.getClickType();
    }
}
