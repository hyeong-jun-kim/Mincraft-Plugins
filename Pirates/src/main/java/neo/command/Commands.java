package neo.command;

import neo.feature.Captain;
import neo.feature.Sailor;
import neo.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
    Main main;
    public Commands(Main main){
        this.main = main;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("cmd에서는 이 작업을 수행하지 못합니다.");
            return true;
        } else {
            if (label.equals("해적단")) {
                Player p = (Player) sender;
                switch (args.length) {
                    case 0:
                        p.sendMessage(ChatColor.YELLOW + "/해적단 초대 (닉네임)" + ChatColor.WHITE + "- 자신의 영토에 (닉네임)을 초대합니다.\n" +
                                ChatColor.YELLOW + "/해적단 이름 (이름) " + ChatColor.WHITE + "- 해적단 이름을 설정합니다. ( 최대 7글자 )\n" +
                                ChatColor.YELLOW + "/해적단 추방 (닉네임) " + ChatColor.WHITE + "- (닉네임)을 영토에서 추방합니다.\n" +
                                ChatColor.YELLOW + "/해적단 정보 " + ChatColor.WHITE + "- 자신의 해적단 인원 목록을 출력합니다.\n" +
                                ChatColor.YELLOW + "/해적단 탈퇴 " + ChatColor.WHITE + "- 해적단에서 탈퇴합니다.\n" +
                                ChatColor.YELLOW + "/해적단 목록 " + ChatColor.WHITE + "- 해적단 목록을 출력합니다.\n" +
                                ChatColor.YELLOW + "/해적단 채팅 " + ChatColor.WHITE + "- 해적단 채팅을 on/off 합니다.");
                        break;
                    case 1:
                        Sailor sailor = new Sailor(main, p);
                        if(args[0].equals("정보")){
                            sailor.printPirateInfo();
                        }else if(args[0].equals("탈퇴")) {
                            sailor.exitPirate();
                        }else if(args[0].equals("목록")){
                            sailor.printCaptainAndPirateName();
                        }else if(args[0].equals("채팅")){
                            sailor.joinChatChannels(p);
                        }
                        break;
                    case 2:
                        if (args[0].equals("초대") || args[0].equals("이름") || args[0].equals("추방")) {
                            Captain captain = new Captain(p);

                            if(args[0].equals("이름")){
                                String pirateName = args[1];
                                captain.create(pirateName);
                            }else if(args[0].equals("초대")) {
                                String targetName = args[1];
                                captain.invitePlayer(targetName);
                            }else if(args[0].equals("추방")){
                                String targetName = args[1];
                                captain.kickPlayer(targetName);
                            }
                        }
                        break;
                }
            }
        }
        return true;
    }

}
