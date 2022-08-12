package neo.mineral;

import neo.data.DataManager;
import neo.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;


public class MineralScheduler {
    static int[] dx = {-1,1,0,0,0,0};
    static int[] dy = {0,0,-1,1,0,0};
    static int[] dz = {0,0,0,0,-1,1};
    static World world = Bukkit.getWorld("world");
    static DataManager data = Main.getData();
    static Main plugin = Main.getPlugin();

    // 5초 뒤에 랜덤 광물 생성
    public static void runRadomMineralTask(Location loc, int key) {
        Material mineral = getRandomBlock();
        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskLater(plugin, () -> {
            ArrayList<Node> blocksArray = getJoinBlocks(loc, key);
            for(int i = 0; i < blocksArray.size(); i++){
                Node node = blocksArray.get(i);
                int k = node.getKey();
                Location blockLoc = new Location(world, node.getA(), node.getB(), node.getC());
                world.getBlockAt(blockLoc).setType(mineral);
                data.getFile().set("mineral." + k + ".mineral", mineral.toString());
                data.saveConfig();
            }
        }, 20L * 5L);
    }
    // 인접한 좌표 주소 가져오기
    private static ArrayList<Node> getJoinBlocks(Location loc, int key){
        ArrayList<Node> joinBlockArray = new ArrayList<>();
        boolean check = true;
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();
        joinBlockArray.add(new Node(x, y, z, key));
        ArrayList<Location> locArray = new ArrayList<>();
        ConfigurationSection section = data.getFile().getConfigurationSection("mineral");
        for(String k: section.getKeys(false)){
            Location dataLoc = data.getFile().getLocation("mineral."+ k +".location");
            locArray.add(dataLoc);
        }
        Queue<Node> q = new LinkedList<>();
        q.offer(new Node(x, y, z));
        loop:
        while(!q.isEmpty()){
            Node node = q.poll();
            x = node.getA();
            y = node.getB();
            z = node.getC();
            loop2:
            for(int i = 0; i < 6; i++){
                int a = x + dx[i];
                int b = y + dy[i];
                int c = z + dz[i];
                // data.yml에 지정된 위치에 블록이 있는지 체크
                if(Mineral.checkPlacedBlock(locArray, a, b, c)){
                    int k = Mineral.getPlacedBlockKey(a, b, c); // 위치 key값 가져오기
                    if(k == 0) continue;
                    // 이미 방문한 적이 있는지 체크
                    for(int j = 0; j < joinBlockArray.size(); j++){
                        Node BlockNode = joinBlockArray.get(j);
                        if(a == BlockNode.getA() && b == BlockNode.getB() && c == BlockNode.getC()){
                            continue loop2;
                        }
                    }
                    Node blockNode = new Node(a,b,c);
                    blockNode.setKey(k);
                    joinBlockArray.add(blockNode);
                    q.offer(blockNode);
                }
            }
        }
        return joinBlockArray;
    }

    // 블록 확률 map에 넣기
    public static Material getRandomBlock() {
        /*   <광물 확률표>
                돌 (40)
                석탄 (10)
                청금석 (10)
                레드스톤 (10)
                철 (10)
                금 (10)
                다이아 (7)
                옵시디언 (2)
                고대잔해 (1)
         */
        int b[] = new int[9];
        ConfigurationSection section = data.getFile().getConfigurationSection("blockRate");
        int i = 0;
        for (String block : section.getKeys(false)) {
            int percent = data.getFile().getInt("blockRate." + block);
            b[i++] = percent;
        }
        int sum = getSum(b, 8);
        int random = (int) (Math.random() * (getSum(b, 8))) + 1;
        Material block = Material.STONE;
        if (1 <= random && random <= getSum(b, 0)) { // 돌
            block = Material.STONE;
        } else if (getSum(b, 0) <= random && random <= getSum(b, 1)) { // 석탄
            block = Material.DEEPSLATE_COAL_ORE;
        } else if (getSum(b, 1) <= random && random <= getSum(b, 2)) { // 청금석
            block = Material.LAPIS_ORE;
        } else if (getSum(b, 2) <= random && random <= getSum(b, 3)) { // 레드스톤
            block = Material.REDSTONE_ORE;
        } else if (getSum(b, 3) <= random && random <= getSum(b, 4)) { // 철
            block = Material.IRON_ORE;
        } else if (getSum(b, 4) <= random && random <= getSum(b, 5)) { // 금
            block = Material.GOLD_ORE;
        } else if (getSum(b, 5) <= random && random <= getSum(b, 6)) { // 다이아
            block = Material.DIAMOND_ORE;
        } else if (getSum(b, 6) <= random && random <= getSum(b, 7)) { // 옵시디언
            block = Material.OBSIDIAN;
        } else if (getSum(b, 7) <= random && random <= getSum(b, 8)) { // 고대 잔해
            block = Material.ANCIENT_DEBRIS;
        }
        return block;
    }

    private static int getSum(int b[], int idx) {
        int sum = 0;
        for (int i = 0; i <= idx; i++) {
            sum += b[i];
        }
        return sum;
    }
}
