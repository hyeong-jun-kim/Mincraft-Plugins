package neo.stamina;

import neo.config.StaminaConfig;
import neo.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;

public class Stamina {
    final Double STAMINA_MAX = StaminaConfig.STAMINA_MAX;
    Player p;
    private Long currentTime;
    private BukkitTask bukkitTask;

    private Main plugin = Main.getPlugin();
    private Long lastEventTime = 0L;
    private Double staminaCoolDown;
    private BukkitScheduler scheduler= Bukkit.getScheduler();
    private StaminaBoard staminaBoard;

    public Stamina(Player p){
        this.p = p;
        runStaminaScheduler();
        staminaBoard = new StaminaBoard(p, this);
        staminaCoolDown = STAMINA_MAX;
    }
    public Stamina(Player p, double staminaCoolDown){
        this.p = p;
        runStaminaScheduler();
        staminaBoard = new StaminaBoard(p, this);
        this.staminaCoolDown = staminaCoolDown;
    }

    public void setLastEventTime(Long lastEventTime) {
        this.lastEventTime = lastEventTime;
    }

    public void setStaminaCoolDown(Double staminaCoolDown){
        this.staminaCoolDown = staminaCoolDown;
        staminaBoard.setScore(staminaCoolDown);
    }

    public Long getLastEventTime(){
        return lastEventTime;
    }

    public Double getStaminaCoolDown(){
        return staminaCoolDown;
    }

    public void cancelStaminaScheduler(){
        bukkitTask.cancel();
    }

    // 아무것도 안할 때 스태미나 증가 스케쥴러
    // TODO 왼쪽 클릭 누를 때 스케쥴러 잠시 중지
    public void runStaminaScheduler(){
        bukkitTask = scheduler.runTaskTimer(plugin, () -> {
            currentTime = System.currentTimeMillis();
            Double lastTime = (currentTime - lastEventTime) / 1000d;

            // 느려짐 적용
            if(staminaCoolDown < 1){
                PotionEffect potionEffect = new PotionEffect(PotionEffectType.SLOW, 20, 1);
                p.addPotionEffect(potionEffect);
            }

            if(staminaCoolDown < 20 && lastTime >= StaminaConfig.RECOVERY_TIME){
                if(staminaCoolDown + 0.5 > 20){
                    staminaCoolDown = 20d;
                }else{
                    staminaCoolDown += 0.5;
                }
            }
            p.sendMessage(staminaCoolDown.toString());
            staminaBoard.setScore(staminaCoolDown);
        }, 0L, 10L);
    }
}
