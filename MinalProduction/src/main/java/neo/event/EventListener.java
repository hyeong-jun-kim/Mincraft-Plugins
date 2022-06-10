package neo.event;

import neo.data.DataManager;
import neo.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;

public class EventListener implements Listener {
    DataManager data = Main.getData();
    Main plugin = Main.getPlugin();

    // 리로딩 될 때 광물 생성 체크 스케쥴러 생성
    public void onReloadEvent(ServerLoadEvent e) {

    }

    // 설정된 위치의 블록을 캐면 광물 생성 스케쥴러 실행
    public void onBlockBreakEvent(BlockBreakEvent e) {
        Location loc = e.getBlock().getLocation();
        ConfigurationSection section = data.getFile().getConfigurationSection("mineral");
        for (String key : section.getKeys(false)) {
            Location dataLoc = data.getFile().getLocation("mineral." + key + ".location");
            if (loc == dataLoc) {

            }
        }
    }

    // 5초 뒤에 랜덤 광물 생성
    public void runRadomMineralTask(Location loc) {
        Material block = getBlock();
        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskLater(plugin, () -> {

        }, 20L * 5L);
    }

    // 블록 확률 map에 넣기
    public Material getBlock() {
        /*   <광물 확률표>
                돌 (40)
                석탄 (10)
                청금석 (10)
                레드스톤 (10)
                철 (10)
                금 (10)
                다이아 (7)
                고대잔해 (3)
         */
        int b[] = new int[8];
        ConfigurationSection section = data.getFile().getConfigurationSection("blockRate");
        int i = 0;
        for (String block : section.getKeys(false)) {
            int percent = data.getFile().getInt("blockRate." + block);
            b[i++] = percent;
        }
        int random = (int) (Math.random() * (getSum(b, 7))) + 1;
        Material block = Material.STONE;
        if (1 <= random && random <= getSum(b, 0)) { // 돌
            block = Material.STONE;
        } else if (getSum(b, 0) <= random && random <= getSum(b, 1)) { // 석탄
            block = Material.COAL_BLOCK;
        } else if (getSum(b, 1) <= random && random <= getSum(b, 2)) { // 청금석
            block = Material.LAPIS_BLOCK;
        } else if (getSum(b, 2) <= random && random <= getSum(b, 3)) { // 레드스톤
            block = Material.REDSTONE_BLOCK;
        } else if (getSum(b, 3) <= random && random <= getSum(b, 4)) { // 철
            block = Material.IRON_BLOCK;
        } else if (getSum(b, 4) <= random && random <= getSum(b, 5)) { // 금
            block = Material.GOLD_BLOCK;
        } else if (getSum(b, 5) <= random && random <= getSum(b, 6)) { // 다이아
            block = Material.DIAMOND_BLOCK;
        } else if (getSum(b, 6) <= random && random <= getSum(b, 7)) { // 고대 잔해
            block = Material.ANCIENT_DEBRIS;
        }
        return block;
    }

    public int getSum(int b[], int idx) {
        int sum = 0;
        for (int i = 0; i <= idx; i++) {
            sum += b[i];
        }
        return sum;
    }
}
