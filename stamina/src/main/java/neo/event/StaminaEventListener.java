package neo.event;

import neo.main.Main;
import neo.stamina.Stamina;
import neo.stamina.StaminaBoard;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.scoreboard.*;

import java.util.HashMap;

import static org.bukkit.event.player.PlayerAnimationType.ARM_SWING;

public class StaminaEventListener implements Listener {
    HashMap<OfflinePlayer, StaminaBoard> staminaBoards = Main.getStaminaBoards();
    HashMap<String, Stamina> staminas = new HashMap<String, Stamina>();
//    HashMap<Player, Double> staminaCoolDowns = new HashMap<>();

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e){
        Player p = e.getPlayer();
        String name = p.getName();
        if(!staminas.containsKey(name)){
            staminas.put(name, new Stamina(p));
        }else{
            Stamina stamina = staminas.get(p.getName());
            double leftStaminaCoolDown = stamina.getCoolDown();
            staminas.put(name, new Stamina(p, leftStaminaCoolDown));
        }
    }

    @EventHandler
    public void onPlayerExistEvent(PlayerQuitEvent e){
        Player p = e.getPlayer();
        String name = p.getName();

        if(staminas.containsKey(name)){
            Stamina stamina = staminas.get(name);
            stamina.cancelScheduler();
        }
    }

    //왼쪽 버튼 누를 때 이벤트
    @EventHandler
    public void playerAnimationEvent(PlayerAnimationEvent e){
        if(e.getAnimationType() == ARM_SWING){
            Player p = e.getPlayer();
            String name = p.getName();

            Stamina stamina = staminas.get(name);

            stamina = staminaNullCheck(p, stamina);

            Long lastTime = stamina.getLastEventTime();
            Long currentTime = System.currentTimeMillis();
            Double leftTime = (currentTime - lastTime) / 1000d;
            if(leftTime <= 0.5){ // 0.5 초 이내에 클릭
                Double staminaCoolDown = stamina.getCoolDown();
                if(staminaCoolDown > 0){
                    if((staminaCoolDown) - leftTime < 0){
                        stamina.setCoolDown(0d);
                    }else{
                        stamina.setCoolDown(staminaCoolDown - leftTime);
                    }
                }
            }
            stamina.setLastEventTime(currentTime);
        }
    }

    // 달릴 때 이벤트
    @EventHandler
    public void playerRunEvent(PlayerMoveEvent e){
        Player p = e.getPlayer();
        String name = p.getName();
        if(p.isSprinting()){
            Stamina stamina = staminas.get(name);
            stamina = staminaNullCheck(p, stamina);
            Long currentTime = System.currentTimeMillis();

            stamina.setLastEventTime(currentTime);
        }
    }

    public Stamina staminaNullCheck(Player p, Stamina stamina){
        if(stamina == null){
            stamina = new Stamina(p);
            staminas.put(p.getName(), stamina);
        }
        return stamina;
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
