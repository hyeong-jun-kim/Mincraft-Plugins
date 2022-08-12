package neo.event;

import io.papermc.paper.event.player.AsyncChatEvent;
import neo.event.handler.CaptainEventHandler;
import neo.event.handler.ChattingHandler;
import neo.event.handler.SailorEventHandler;
import neo.main.Main;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class ChattingEventListener implements Listener {
    Main main;

    public ChattingEventListener(Main main){
        this.main = main;
    }

    @EventHandler
    public void onPirateJoinEvent(PlayerJoinEvent e){
        Player p = e.getPlayer();

        // 일반 채널 해쉬 값 넣어주기
        ArrayList<Player> players = new ArrayList<Player>();
        main.cM.joinChannel(p, "General");

        String pirateName = ChattingHandler.getPirateName(p);
        if(pirateName == null)
            return;
        // 탭 이름 설정
        String name = p.getName();
        p.playerListName(Component.text("[" + pirateName + "해적단]" + name));
    }

    @EventHandler
    public void onCaptionChattingEvent(AsyncChatEvent e){
        Player p = e.getPlayer();
        String pirateName = CaptainEventHandler.getPirateName(p);
        if(pirateName == null)
            return;
        e.renderer((source, sourceDisplayName, message, viewer) -> {
            Component name = sourceDisplayName;
            Component displayPrifix = Component.text("[" + pirateName + "해적단 선장] ")
                    .append(name.append(Component.text(" "))).append(message);
            return displayPrifix;
        });
    }

    @EventHandler
    public void onSailorChattingEvent(AsyncChatEvent e) {
        Player p = e.getPlayer();
        String pirateName = SailorEventHandler.getPirateName(p);
        if (pirateName == null)
            return;
        e.renderer((source, sourceDisplayName, message, viewer) -> {
            Component name = sourceDisplayName;
            Component displayPrifix = Component.text("[" + pirateName + "해적단] ")
                    .append(name.append(Component.text(" "))).append(message);
            return displayPrifix;
        });
    }

    // 채널에서 말하기
    @EventHandler
    public void onPlayerChannelChat(AsyncChatEvent e){
        Player p = e.getPlayer();
        e.recipients().clear();
        main.cM.getChannel(p).stream().forEach(
                player -> e.recipients().add(player)
        );
    }
}
