package neo.feature;

import neo.config.StaminaConfig;
import neo.config.ThirstyConfig;
import neo.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public class Thirsty{
    final double THIRSTY_MAX = ThirstyConfig.THIRSTY_MAX; // 갈증 총 시간
    final double WATER_HEAL = ThirstyConfig.WATER_HEAL; // 증류수 차는 칸수
    final double DAMAGE = ThirstyConfig.DAMAGE; // 체력 다는 횟수
    Player p;
    private Long currentTime;
    private BukkitTask bukkitTask;
    private Main plugin = Main.getPlugin();
    private long lastEventTime = 0L;
    private double thirstyCoolDown;
    private BukkitScheduler scheduler= Bukkit.getScheduler();
    private ThirstyBoard thirstyBoard;

    public Thirsty(Player p){
        this.p = p;
        thirstyBoard = new ThirstyBoard(p, this);
        thirstyCoolDown = THIRSTY_MAX;
        runScheduler();
    }

    public Thirsty(Player p, double thirstyCoolDown){
        this.p = p;
        thirstyBoard = new ThirstyBoard(p, this);
        this.thirstyCoolDown = thirstyCoolDown;
        runScheduler();
    }
    public void setCoolDownMax(){
        this.thirstyCoolDown = THIRSTY_MAX;
    }

    public void drinkWater(){
        this.thirstyCoolDown += WATER_HEAL;
    }

    public double getCoolDown(){
        return thirstyCoolDown;
    }

    public void cancelScheduler() {
        bukkitTask.cancel();
    }

    public void runScheduler() {
        bukkitTask = scheduler.runTaskTimer(plugin, () -> {
            currentTime = System.currentTimeMillis();
            Double lastTime = (currentTime - lastEventTime) / 1000d;

            // 느려짐, 어지로움 적용
            if(thirstyCoolDown < 1){
                PotionEffect SlowEffect = new PotionEffect(PotionEffectType.SLOW, 20, 2);
                PotionEffect DizzyEffect = new PotionEffect(PotionEffectType.CONFUSION, 20, 2);

                p.addPotionEffect(SlowEffect);
                p.addPotionEffect(DizzyEffect);
            }

            if(thirstyCoolDown >= 0){
                if(thirstyCoolDown - 1 <= 0){
                    thirstyCoolDown = 0;
                    p.damage(DAMAGE);
                }else{
                    thirstyCoolDown -= 1;
                }
            }
            thirstyBoard.setScore(thirstyCoolDown);
        }, 0L, 20L);
    }
}
