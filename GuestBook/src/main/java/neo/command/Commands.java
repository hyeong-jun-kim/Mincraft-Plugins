package neo.command;

import neo.service.GuestBookService;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
    private GuestBookService guestBookService;

    public Commands(){
        this.guestBookService = new GuestBookService();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("cmd에서는 이 작업을 수행하지 못합니다.");
            return true;
        } else {
            Player p = ((Player) sender).getPlayer();
            if(args.length == 0){
                if(p.isOp()){
                    p.sendMessage(ChatColor.translateAlternateColorCodes('§', "§f§l[ §a§l방명록 §f§l] §f"));
                    p.sendMessage(ChatColor.BOLD + "OP 권한");
                    p.sendMessage(ChatColor.AQUA + "/방명록 목록 "+ ChatColor.WHITE + "- 방명록이 생성된 목록들을 나열합니다.");
                    p.sendMessage(ChatColor.AQUA + "/방명록 보기 [항목명] [페이지] " + ChatColor.WHITE + "- 해당 항목에 남겨진 글 리스트를 확인합니다. (페이지란 미 입력시 1페이지로 처리합니다.)");
                    p.sendMessage(ChatColor.AQUA + "/방명록 글삭제 [항목명] [페이지] " + ChatColor.WHITE +"- 해당 항목에서 선택한 번호의 글을 제거합니다.");
                    p.sendMessage(ChatColor.AQUA + "/방명록 생성 [항목명] " + ChatColor.WHITE + "- 새 항목을 생성합니다.");
                    p.sendMessage(ChatColor.AQUA + "/방명록 항목삭제 [항목명] " + ChatColor.WHITE + "- 해당 항목을 삭제합니다.");

                    p.sendMessage(ChatColor.BOLD + "유저 권한");
                    p.sendMessage(ChatColor.AQUA + "/방명록 작성 [항목명] [할말] " + ChatColor.WHITE + "- 해당 항목에 방명록을 작성합니다.");
                    p.sendMessage(ChatColor.AQUA + "/방명록 삭제 [항목명] " + ChatColor.WHITE + "- 자신이 작성한 해당 항목의 글을 제거합니다.");
                }else{
                    p.sendMessage(ChatColor.translateAlternateColorCodes('§', "§f§l[ §a§l방명록 §f§l] §f"));
                    p.sendMessage(ChatColor.BOLD + "유저 권한");
                    p.sendMessage("/방명록 작성 [항목명] [할말] " + "- 해당 항목에 방명록을 작성합니다.");
                    p.sendMessage("/방명록 삭제 [항목명] " + "- 자신이 작성한 해당 항목의 글을 제거합니다.");
                }

                return true;
            }


            // 유저권한
            switch (args.length) {
                case 2:
                    if (args[0].equalsIgnoreCase("삭제")) {
                        String name = args[1];
                        guestBookService.deleteMyGuestBook(p, name);
                    }
                    break;

                case 3:
                    if (args[0].equalsIgnoreCase("작성")) {
                        String name = args[1];
                        String content = args[2];
                        guestBookService.createGuestBook(p, name, content);
                    }
                    break;
            }

            if (p.isOp()) {
                switch (args.length) {
                    case 1:
                        if (args[0].equalsIgnoreCase("목록")) {
                            guestBookService.showBoardName(p);
                        }
                        break;

                    case 2:
                        if (args[0].equalsIgnoreCase("생성")) {
                            String name = args[1];
                            guestBookService.createBoard(p, name);
                        } else if (args[0].equalsIgnoreCase("항목삭제")) {
                            String name = args[1];
                            guestBookService.deleteBoard(p, name);
                        }else if(args[0].equalsIgnoreCase("보기")){
                            String name = args[1];
                            guestBookService.showGuestBook(p, name, 1);
                        }
                        break;

                    case 3:
                        if (args[0].equalsIgnoreCase("보기")) {
                            String name = args[1];
                            int page = Integer.parseInt(args[2]);
                            guestBookService.showGuestBook(p, name, page);
                        } else if (args[0].equalsIgnoreCase("글삭제")) {
                            String name = args[1];
                            int num = Integer.parseInt(args[2]);
                            guestBookService.deleteGuestBook(p, name, num);
                        }
                        break;
                }
            }
        }
        return true;
    }
}
