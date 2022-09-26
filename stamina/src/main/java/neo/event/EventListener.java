package neo.event;

import neo.feature.Thirsty;
import neo.main.Main;
import neo.feature.Stamina;
import neo.feature.StaminaBoard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.*;

import java.util.HashMap;

import static org.bukkit.event.player.PlayerAnimationType.ARM_SWING;

public class EventListener implements Listener {
    HashMap<OfflinePlayer, StaminaBoard> staminaBoards = Main.getStaminaBoards();
    HashMap<String, Stamina> staminas = new HashMap<String, Stamina>();
    HashMap<String, Thirsty> thirsty_map = new HashMap<String, Thirsty>();
//    HashMap<Player, Double> staminaCoolDowns = new HashMap<>();

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e){
        Player p = e.getPlayer();
        String name = p.getName();
        // 스태미나
        if(!staminas.containsKey(name)){
            staminas.put(name, new Stamina(p));
        }else{
            Stamina stamina = staminas.get(p.getName());
            double leftStaminaCoolDown = stamina.getCoolDown();
            staminas.put(name, new Stamina(p, leftStaminaCoolDown));
        }

        if(!thirsty_map.containsKey(name)){
            thirsty_map.put(name, new Thirsty(p));
        }else{
            Thirsty thirsty = thirsty_map.get(p.getName());
            double thirstyCoolDown = thirsty.getCoolDown();
            thirsty_map.put(name, new Thirsty(p, thirstyCoolDown));
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

        if(thirsty_map.containsKey(name)){
            Thirsty thirsty = thirsty_map.get(name);
            thirsty.cancelScheduler();
        }
    }
    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent e){
        Player p = e.getPlayer();
        String name = p.getName();

        Thirsty thirsty = thirsty_map.get(name);
        thirstyNullCheckAndRun(p, thirsty);
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

    // 리스폰 이벤트
    @EventHandler
    public void playerRespawnEvent(PlayerRespawnEvent e){
        Player p = e.getPlayer();
        String name = p.getName();

        if(staminas.containsKey(name)){
            Stamina stamina = staminas.get(name);
            stamina.setCoolDownMax();
        }

        if(thirsty_map.containsKey(name)){
            Thirsty thirsty = thirsty_map.get(name);
            thirsty.setCoolDownMax();
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

    // 증류수 마실 때 이벤트
    @EventHandler
    public void onPlayerDrinkWaterEvent(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
            ItemStack itemStack = p.getInventory().getItemInMainHand();
            ItemMeta itemMeta = itemStack.getItemMeta();
            if(itemMeta == null)
                return;
            if(itemMeta.getDisplayName().equals(ChatColor.AQUA + "증류수")){
                if(thirsty_map.containsKey(p.getName())){
                    p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "증류수를 마셨습니다.");
                    Thirsty thirsty = thirsty_map.get(p.getName());
                    thirsty.drinkWater();
                    p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                }
            }
        }

    }

    public Stamina staminaNullCheck(Player p, Stamina stamina){
        if(stamina == null){
            stamina = new Stamina(p);
            staminas.put(p.getName(), stamina);
        }
        return stamina;
    }

    public void thirstyNullCheckAndRun(Player p, Thirsty thirsty){
        if(thirsty == null){
            thirsty = new Thirsty(p);
            thirsty_map.put(p.getName(), thirsty);
        }
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
