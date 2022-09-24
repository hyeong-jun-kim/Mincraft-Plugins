package neo.event;

import neo.main.Main;
import neo.stamina.StaminaBoard;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.scoreboard.*;

import java.util.HashMap;
import java.util.Set;
import java.util.Stack;

public class EventListener implements Listener {
    HashMap<OfflinePlayer, StaminaBoard> staminaBoards = Main.getStaminaBoards();

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e){
        Player p = e.getPlayer();
        addStaminaBoard(p);
    }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent e){
        Player p = e.getPlayer();
        String name = p.getName();
        Scoreboard board = p.getScoreboard();

        // TODO 남은 시간으로 네모 칸 반환해주는 메서드 만들기
        Score score = board.getObjective(name + ".stamina").getScore("스태미나: ■■");
        Set<Score> scores = board.getScores(name + ".stamina");


        score.setScore(score.getScore() + 1);
        p.setScoreboard(board);
    }

    public void addStaminaBoard(Player p){
        String name = p.getName();
        Scoreboard board = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
        Objective o = board.registerNewObjective(name + ".stamina", Criteria.DUMMY, "stamina");
        o.setDisplaySlot(DisplaySlot.SIDEBAR);
        Score score = board.getObjective(name+ ".stamina").getScore("stamina: ■■■■■");
        p.setScoreboard(board);
    }

}
