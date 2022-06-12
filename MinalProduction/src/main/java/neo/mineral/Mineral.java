package neo.mineral;

import neo.data.DataManager;
import neo.main.Main;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

class Node {
    int a;
    int b;
    int c;
    int key;

    Node(int a, int b, int c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
    Node(int a, int b, int c, int key) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.key = key;
    }
    public int getA() {
        return a;
    }
    public int getB() {
        return b;
    }
    public int getC() {
        return c;
    }
    public int getKey() {
        return key;
    }
    public void setKey(int key) {
        this.key = key;
    }
}
public class Mineral {
    static DataManager data = Main.getData();
    World world = Bukkit.getWorld("world");
    static int[] dx = {-1,1,0,0,0,0};
    static int[] dy = {0,0,-1,1,0,0};
    static int[] dz = {0,0,0,0,-1,1};
    // 지정된 장소에 광물 생성
    public void createMinal(int x, int y, int z, Player p){
        int key = 1;
        Location loc = new Location(world, x, y, z);
        if(data.getFile().contains("mineral")){ // 처음 생성이 아닐때
            ConfigurationSection section = data.getFile().getConfigurationSection("mineral");
            // 같은 위치가 있는지 체크
            for(String k: section.getKeys(false)){
                Location dataLoc = data.getFile().getLocation("mineral."+ k +".location");
                if(dataLoc == loc){ // 이미 현재 위치가 저장되어 있다면
                    key = Integer.parseInt(k) + 1;
                    break;
                }
                key++;
            }
            String mineralName = getNearMineral(loc);
            if(mineralName == null || mineralName == Material.AIR.toString()){
                mineralName = MineralScheduler.getRandomBlock().toString();
            }
            data.getFile().set("mineral." + key + ".location", loc);
            data.getFile().set("mineral." + key + ".mineral", mineralName);
            world.getBlockAt(loc).setType(Material.getMaterial(mineralName));
            data.saveConfig();
        }else{ // 처음 생성일 때
            String mineralName = MineralScheduler.getRandomBlock().toString();
            data.getFile().set("mineral." + key + ".location", loc);
            data.getFile().set("mineral." + key + ".mineral", mineralName);
            world.getBlockAt(loc).setType(Material.getMaterial(mineralName));
            data.saveConfig();
        }
        p.sendMessage(ChatColor.GREEN + "x: " + x + " y " + y + " z " + z + "위치에 광물 자동 생성이 설정이 완료되었습니다.");
    }

    // 인접한 블록에 설정된 광물이 있는지 확인. 있으면 광물이름 반환
    public static String getNearMineral(Location loc){
        World world = Bukkit.getWorld("world");
        boolean check = true;
        ArrayList<Node> blockArray = new ArrayList<>();
        HashMap<Location, String> keyMap = new HashMap<>();
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();
        ArrayList<Location> locArray = new ArrayList<>();
        ConfigurationSection section = data.getFile().getConfigurationSection("mineral");
        for(String key: section.getKeys(false)){
            Location dataLoc = data.getFile().getLocation("mineral."+ key +".location");
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
                // 인접한 곳에 data.yml 블록 세팅이 되어있는지 확인
                int key = getPlacedBlockKey(a,b,c);
                if(key != 0){
                    String mineralName = data.getFile().getString("mineral."+ key +".mineral");
                    return mineralName;
                }
            }
        }
        // 없으면 null 반환
        return null;
    }

    // 인접한 블록에 광물이 모두 캐졌는지 확인
    public static boolean checkBlockAllBreak(Location loc){
        World world = Bukkit.getWorld("world");
        boolean check = true;
        ArrayList<Node> blockArray = new ArrayList<>();
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();
        blockArray.add(new Node(x, y, z));
        ArrayList<Location> locArray = new ArrayList<>();
        ConfigurationSection section = data.getFile().getConfigurationSection("mineral");
        for(String key: section.getKeys(false)){
            Location dataLoc = data.getFile().getLocation("mineral."+ key +".location");
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
                 if(checkPlacedBlock(locArray, a, b, c)){
                    // 이미 방문한 적이 있는지 체크
                    for(int j = 0; j < blockArray.size(); j++){
                        Node BlockNode = blockArray.get(j);
                        if(a == BlockNode.getA() && b == BlockNode.getB() && c == BlockNode.getC()){
                            continue loop2;
                        }
                    }
                    Block m = world.getBlockAt(a, b, c);
                    if(m.getType() != Material.AIR){
                        return false;
                    }
                    q.offer(new Node(a, b, c));
                    blockArray.add(new Node(a, b, c));
                }
            }
        }
        return check;
    }
    // data.yml에 저장되어 있는 위치에 블록이 있는지 확인해주는 메서드
    public static boolean checkPlacedBlock(ArrayList<Location> locArray, int x, int y, int z){
        boolean check = false;
        for(int i = 0; i < locArray.size(); i++){
            Location loc = locArray.get(i);
            if(loc == null)
                return false;
            int a = loc.getBlockX();
            int b = loc.getBlockY();
            int c = loc.getBlockZ();
            if(a == x && b == y && c == z){
                check = true;
                break;
            }
        }
        return check;
    }
    // data.yml에 저장되어 있는 위치에 블록이 있으면 idx 반환
    public static int getPlacedBlockKey(int x, int y, int z) {
        boolean check = false;
        ConfigurationSection section = data.getFile().getConfigurationSection("mineral");
        if(section == null)
            return 0;
        for (String key : section.getKeys(false)) {
            Location loc = data.getFile().getLocation("mineral." + key + ".location");
            if(loc != null){
                if (loc.getBlockX() == x && loc.getBlockY() == y && loc.getBlockZ() == z) {
                    return Integer.parseInt(key);
                }
            }
        }
        return 0;
    }
    // data.yml 광물 초기화
    public static void allDeleteData(Player p){
        data.getFile().set("mineral", null);
        data.saveConfig();
        p.sendMessage(ChatColor.GREEN + "정상적으로 광물 설정 초기화를 완료 했습니다.");
    }
}
