package neo.feature;

import neo.config.StaminaConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class StaminaBoard implements Board {
    private Stamina stamina;
    private String prevStaminaScore = " ";
    private Scoreboard board;
    private Objective o;
    private Score score;

    private Player p;

    StaminaBoard(Player p, Stamina stamina) {
        this.p = p;
        this.stamina = stamina;
        addBoard(p);
    }

    public void addBoard(Player p) {
        String name = p.getName();
        Scoreboard board = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
        Objective o = board.registerNewObjective(name + ".status", Criteria.DUMMY, ChatColor.GOLD + "상태 창");
        o.setDisplaySlot(DisplaySlot.SIDEBAR);
        score = board.getObjective(name + ".status").getScore("스태미나");
        p.setScoreboard(board);
    }

    public void setScore(Double staminaCoolDown) {
        double staminaValue = StaminaConfig.STAMINA_MAX / 10;
        String name = p.getName();
        board = p.getScoreboard();


        StringBuilder sb = new StringBuilder("□□□□□□□□□□");
        int cnt = 0;
        double tmp = 0;

        for (int i = 0; i < 10; i++) {
            if (staminaCoolDown >= tmp && staminaCoolDown < tmp + staminaValue) {
                break;
            }
            cnt++;
            tmp += staminaValue;
        }

        for (int i = 0; i < cnt; i++) {
            sb.setCharAt(i, '■');
        }

        board.resetScores(prevStaminaScore);
        Score score = board.getObjective(name + ".status").getScore("스태미나: " + ChatColor.GREEN + sb.toString());
        score.setScore(staminaCoolDown.intValue());
        prevStaminaScore = "스태미나: " + ChatColor.GREEN + sb.toString();
    }
}