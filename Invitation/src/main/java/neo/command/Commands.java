package neo.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("cmd에서는 이 작업을 수행하지 못합니다.");
            return true;
        } else {
            Player p = (Player) sender;
            switch (args.length) {
                case 2:
                    // 초대권 생성
                    if (args[0].equals("생성")) {
                        if (p.isOp()) {
                            if (p.getInventory().firstEmpty() == -1) {
                                p.sendMessage(ChatColor.RED + "인벤토리 슬롯을 비워주세요!");
                                return true;
                            }
                            int count = Integer.parseInt(args[1]);
                            ItemStack invitation = createInvitation(count);
                            p.getInventory().addItem(invitation);
                            p.sendMessage(ChatColor.GREEN + "초대권을 발급 받았습니다!");
                            return true;
                        } else {
                            p.sendMessage(ChatColor.RED + "OP만 사용 가능합니다.");
                        }
                    }
                    // 초대권 사용
                    if (args[0].equals("사용")) {
                        ItemStack invitation = p.getInventory().getItemInMainHand();
                        int count = invitation.getAmount();
                        ItemMeta itemMeta = invitation.getItemMeta();
                        if (itemMeta != null && itemMeta.getDisplayName().equals(ChatColor.GOLD + "초대권")) {
                            //boolean check = false;
                            String nickName = args[1];
                            // 화이트 리스트에 추가됐는지 체크
                            for(OfflinePlayer op: Bukkit.getWhitelistedPlayers()){
                                if(op.getName().equals(nickName)){
                                    p.sendMessage(ChatColor.RED + "이미 화이트리스트에 등록된 유저입니다!");
                                    return true;
                                }
                            }
                            // 화이트 리스트 명령어로 추가하기
                            ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                            String command = "whitelist add " + nickName;
                            Bukkit.dispatchCommand(console, command);
                            /*
                            for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
                                if(offlinePlayer.getName() != null) {
                                    if (offlinePlayer.getName().equals(nickName)) {
                                        check = true;
                                        offlinePlayer.setWhitelisted(true);
                                    }
                                }
                            }
                            if (!check) {
                                p.sendMessage(ChatColor.RED + "존재하지 않는 유저 닉네임입니다.");
                                return true;
                            }*/
                            p.getInventory().remove(invitation);
                            p.getInventory().setItemInMainHand(createInvitation(count - 1));
                            p.sendMessage(ChatColor.GOLD + nickName + ChatColor.GREEN + "님이 화이트 리스트로 추가되셨습니다!");
                            return true;
                        } else {
                            p.sendMessage(ChatColor.RED + "초대권을 들고 명령어를 입력해주세요!");
                            return true;
                        }
                    }
            }
        }
        return true;
    }

    // 초대권 생성
    public ItemStack createInvitation(int count) {
        ItemStack invitation = new ItemStack(Material.PAPER, count);
        ItemMeta itemMeta = invitation.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GOLD + "초대권");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GREEN + "화이트 리스트를 추가할 수 있는 초대권입니다.");
        lore.add(ChatColor.WHITE + "사용 방법: " + ChatColor.AQUA + "/초대권 사용 [닉네임]");
        itemMeta.setLore(lore);
        invitation.setItemMeta(itemMeta);
        return invitation;
    }
}
