package neo.feature;

import neo.data.AreaData;
import neo.data.DataManager;
import neo.main.Main;
import neo.util.EventUtil;
import neo.util.Util;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class War {
    private static Main plugin = (Main) JavaPlugin.getProvidingPlugin(Main.class);
    private BukkitTask readyTask = null;
    private BukkitTask warTask = null;

    // TODO 시간 수정해야 함
    final static int READY_TIME = 5 * 1; // 5분
    final static int WAR_TIME = 10 * 1; // 25분

    static DataManager data = Main.getData();
    static FileConfiguration file = Main.getData().getFile();
    public static HashMap<String, AreaData> areaMap = Main.getAreaMap();
    public static HashMap<String, String> warMap = Main.getWarMap();

    Player p;
    Util util;
    Main main;

    public War(Main main, Player p) {
        this.main = main;
        this.p = p;
        util = new Util(p);
    }

    public void declare(String captainName) {
        String targetPirateName = util.findPirateNameByCaptain(captainName);
        if (targetPirateName == null)
            return;
        if (!util.checkCaptain(p.getName()))
            return;
        if (!checkPirateCreatedByAreaName(p.getName()))
            return;
        if (!checkPiratePlayerNum(captainName))
            return;

        String myPirateName = util.findPirateNameByCaptain(p.getName());
        if (myPirateName.equals(targetPirateName)) {
            p.sendMessage(ChatColor.RED + "자기 자신의 해적단한테는 전쟁 선포를 못합니다");
            return;
        }

        if (warMap.containsKey(myPirateName)) {
            p.sendMessage(ChatColor.RED + "전쟁은 하나의 해적단만 선포가 가능합니다.");
        } else if (warMap.containsKey(captainName)) {
            p.sendMessage(ChatColor.RED + "해당 해적단은 현재 전쟁중이므로 선포가 불가능합니다.");
        }
        startWar(myPirateName, targetPirateName);
    }

    private boolean checkPirateName(String name) {
        if (file.get("pirates") == null)
            return false;
        if (file.contains("pirates." + name)) {
            return true;
        } else {
            p.sendMessage(ChatColor.RED + "존재하지 않는 해적단 이름입니다.");
            return false;
        }
    }

    // 영토 생성 후 해적단을 생성했는지 확인하는 메서드
    private boolean checkPirateCreatedByAreaName(String areaName) {
        long count = file.getConfigurationSection("pirates").getKeys(false)
                .stream().filter(key -> file.getString("pirates." + key + ".captain").equals(areaName))
                .count();
        if (count == 0) {
            p.sendMessage(ChatColor.RED + "해적단을 생성하신 뒤에 위 명령어를 입력해 주세요");
            return false;
        } else {
            return true;
        }
    }

    private boolean checkPiratePlayerNum(String captain) {
        AreaData areaData = areaMap.get(captain);
        if (areaData != null) {
            int x1 = areaData.x1;
            int x2 = areaData.x2;
            int z1 = areaData.z1;
            int z2 = areaData.z2;

            double count = Bukkit.getOnlinePlayers().stream()
                    .filter(p -> (x1 <= p.getLocation().getX() && x2 >= p.getLocation().getX())
                            && (z1 <= p.getLocation().getZ() && z2 >= p.getLocation().getZ()))
                    .count();
            // TODO count 숫자 4로 수정하기
            if (count >= 1) {
                return true;
            } else {
                p.sendMessage(ChatColor.RED + "상대방의 영토에 4명이상의 플레이어가 접속해있지 않아서, 전쟁 선포가 불가능합니다.");
                return false;
            }
        }
        return false;
    }


    // 전쟁 시작
    private void startWar(String myPirateaName, String targetPirateName) {
        sendMessagePiratePlayer(myPirateaName, ChatColor.GREEN + targetPirateName + ChatColor.GREEN + "에게 전쟁을 선포하였습니다." +
                " 5분뒤에 전쟁이 시작됩니다.");
        sendMessagePiratePlayer(targetPirateName, ChatColor.GREEN + targetPirateName + ChatColor.GREEN + "이 전쟁을 선포하였습니다." +
                " 5분뒤에 전쟁이 시작됩니다.");
        BossBar bossBar = Bukkit.createBossBar("전쟁 준비", BarColor.YELLOW, BarStyle.SEGMENTED_10);

        // 전쟁 준비 타이머
        if (readyTask == null) {
            readyTask = new BukkitRunnable() {
                int readyTime = READY_TIME;

                @Override
                public void run() {
                    if (readyTime == READY_TIME) {
                        warMap.put(myPirateaName, targetPirateName);
                        warMap.put(targetPirateName, myPirateaName);
                    }
                    if ((readyTime -= 1) == 0) {
                        this.cancel();

                        // 전쟁 시작 타이머
                        if(warTask ==  null) {
                            warTask = new BukkitRunnable() {
                                int warTime = WAR_TIME;

                                @Override
                                public void run() {
                                    if (warTime == WAR_TIME) {
                                        bossBar.setColor(BarColor.GREEN);
                                        bossBar.setTitle("전쟁 시작");
                                        sendMessagePiratePlayer(myPirateaName, ChatColor.YELLOW + targetPirateName + "와의 전쟁이 시작되었습니다!");
                                        sendMessagePiratePlayer(targetPirateName, ChatColor.YELLOW + myPirateaName + "와의 전쟁이 시작되었습니다!");
                                    }
                                    if ((warTime -= 1) == 0 ||
                                            (!warMap.containsKey(myPirateaName) && !warMap.containsKey(targetPirateName))) {
                                        this.cancel();
                                        warMap.remove(myPirateaName);
                                        warMap.remove(targetPirateName);
                                        bossBar.removeAll();
                                    } else {
                                        bossBar.setProgress(warTime / (double) WAR_TIME);
                                    }
                                }
                            }.runTaskTimer(main, 0, 20);
                        }

                    } else {
                        bossBar.setProgress(readyTime / (double) READY_TIME);
                    }
                }
            }.runTaskTimer(main, 0, 20);

            bossBar.setVisible(true);
            EventUtil.addBossBar(bossBar, myPirateaName);
            EventUtil.addBossBar(bossBar, targetPirateName);
        }
    }

    // 해적단 선원들 가져오기
    private Set<Player> getPiratePlayers(String pirateName) {
        Set<String> playersNameSet = new HashSet<>();
        Set<Player> piratePlayers = new HashSet<>();
        // 선장 가져오기
        String captain = file.getString("pirates." + pirateName + ".captain");
        playersNameSet.add(captain);

        // 선원 가져오기
        if (file.get("pirates." + pirateName + ".member") != null) {
            file.getConfigurationSection("pirates." + pirateName + ".member").getKeys(false)
                    .stream().forEach(name -> playersNameSet.add(name));
        }
        for (String name : playersNameSet) {
            Player player = Bukkit.getPlayer(name);
            if (player.isOnline()) {
                piratePlayers.add(player);
            }
        }
        return piratePlayers;
    }

    private void sendMessagePiratePlayer(String pirateName, String message) {
        for (Player p : getPiratePlayers(pirateName)) {
            p.sendMessage(message);
        }
    }
}
