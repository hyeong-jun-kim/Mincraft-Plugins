package neo.stamina;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class StaminaBoard {
    public Scoreboard board;
    public Objective o;
    public Score score;

    private Player p;

    StaminaBoard(Player p, Scoreboard board, Objective o, Score score){
        this.p = p;
        this.board = board;
        this.o = o;
        this.score = score;
    }
}
