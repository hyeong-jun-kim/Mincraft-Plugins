package neo.feature;

import neo.config.StaminaConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class ThirstyBoard implements Board{
    private Thirsty thirsty;
    private String prevThirstyScore = " ";
    private Scoreboard board;
    private Score score;
    private Player p;

    ThirstyBoard(Player p, Thirsty thirsty){
        this.p = p;
        this.thirsty = thirsty;
        board = p.getScoreboard();
        addBoard(p);
    }
    @Override
    public void addBoard(Player p) {
        String name = p.getName();
        score = board.getObjective(name + ".status").getScore("    갈증");
        p.setScoreboard(board);
    }

    @Override
    public void setScore(Double thirstyCoolDown) {
        double thirstyValue = thirsty.THIRSTY_MAX / 10;
        String name = p.getName();
        board = p.getScoreboard();


        StringBuilder sb = new StringBuilder("□□□□□□□□□□");
        int cnt = 0;
        double tmp = 0;

        for (int i = 0; i < 10; i++) {
            if (thirstyCoolDown >= tmp && thirstyCoolDown < tmp + thirstyValue) {
                break;
            }
            cnt++;
            tmp += thirstyValue;
        }

        for (int i = 0; i < cnt; i++) {
            sb.setCharAt(i, '■');
        }

        board.resetScores(prevThirstyScore);
        Score score = board.getObjective(name + ".status").getScore("    갈증: " + ChatColor.AQUA + sb.toString());
        score.setScore(thirstyCoolDown.intValue());
        prevThirstyScore = "    갈증: " + ChatColor.AQUA + sb.toString();
    }
}
