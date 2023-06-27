package neo.event;

import de.tr7zw.nbtapi.NBTItem;
import neo.data.DataManager;
import neo.main.Main;
import neo.service.MoneyService;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EventListener implements Listener {
    private MoneyService moneyService;
    private DataManager data;

    public EventListener(MoneyService moneyService){
        this.moneyService = moneyService;
        data = Main.getData();
    }

    @EventHandler
    public void onPlayerRightClickEvent(PlayerInteractEvent e){
        if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
            Player p = e.getPlayer();
            ItemStack mainHandItem = p.getInventory().getItemInMainHand();

            if(mainHandItem.getType() == Material.AIR || mainHandItem.getItemMeta().getDisplayName() == null)
                return;

            // 수표 우클릭 했을경우
            if(mainHandItem.getItemMeta().getDisplayName().contains(ChatColor.translateAlternateColorCodes('&', "&a[ &f수표 &a] &e"))){
                NBTItem nbtItem = new NBTItem(mainHandItem);
                String uuid = nbtItem.getString("uuid");
//                p.sendMessage("수표 인식됨");
//                p.sendMessage(uuid);
                if(!data.getFile().contains("check." + uuid + ".name"))
                    return;

                int amount = data.getFile().getInt("check." + uuid + ".amount");
                String pubUserName = data.getFile().getString("check." + uuid + ".name");
                if(pubUserName == null)
                    return;

//                // 1. 자신의 수표인지 확인 => 아니면 밴
//                if(!Objects.equals(pubUserName, p.getName())){
//                    LocalDate now = LocalDate.now();
//                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
//                    String timestamp = now.format(formatter);
//
//                    // 수표를 발행한 사람 밴
//                    Bukkit.getBanList(BanList.Type.NAME).addBan(pubUserName, "다른 사람에게 수표를 양도함", null, "cosole");
//                    data.getFile().set("banList." + pubUserName + ".reason", "다른 사람에게 수표를 양도하였습니다.");
//                    data.getFile().set("banList." + pubUserName + ".amount", amount);
//                    data.getFile().set("banList." + pubUserName + ".time", timestamp);
//                    data.saveConfig();
//                    return;
//                }

                // 이미 사용한 수표인지 확인 => 이미 사용했으면 밴
                if(data.getFile().getBoolean("check." + uuid + ".used")){
                    LocalDate now = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                    String timestamp = now.format(formatter);

                    // 수표를 발행한 사람 밴
                    // 다른사람이 수표를 사용했을 경우
                    String reason = "";
                    if(!p.getName().equalsIgnoreCase(pubUserName)){
                        reason = "내가 사용했던 수표를 다른 사람이 다시 사용했습니다 (아이템 복사 의심)";
                    }else{ // 내가 수표를 사용했을 경우
                        reason = "사용했던 수표를 다시 사용했습니다 (아이템 복사 의심)";
                    }

                    p.kickPlayer(reason);
                    Bukkit.getBanList(BanList.Type.NAME).addBan(pubUserName, reason, null, "cosole");
                    data.getFile().set("banList." + pubUserName + ".reason", reason);
                    data.getFile().set("banList." + pubUserName + ".amount", amount);
                    data.getFile().set("banList." + pubUserName + ".time", timestamp);
                    data.saveConfig();
                    return;
                }

                // 돈 발급
                moneyService.deposit(p, amount);
                p.getInventory().remove(mainHandItem);

                // 사용한 수표로 저장
                data.getFile().set("check." + uuid + ".used", true);
                data.saveConfig();
            }
        }
    }
}
