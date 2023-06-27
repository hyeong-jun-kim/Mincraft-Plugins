package neo.service;

import de.tr7zw.nbtapi.NBTItem;
import neo.data.DataManager;
import neo.entity.Check;
import neo.main.Main;
import neo.util.TextUtil;
import net.kyori.adventure.text.Component;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;

public class MoneyService {
    private Main main;
    private Economy economy;
    private DataManager data;

    public MoneyService(Main main){
        this.main = main;
        economy = main.economy;
        data = Main.getData();
    }

    // 수표 생성
    public void createCheck(Player p, int amount){
        // 최소 금액, 최대 금액 확인
        if(data.getFile().get("checkPrice.min") == null || data.getFile().get("checkPrice.max") == null){
            p.sendMessage(ChatColor.RED + "수표 플러그인을 사용하려면 먼저 최소 금액, 최대 금액 설정이 필요합니다.");
            return;
        }

        if(amount > data.getFile().getInt("checkPrice.max")){
            p.sendMessage(ChatColor.RED + "수표로 발행 가능한 최대 금액을 초과했습니다.");
            return;
        }

        if(amount <= 0 || amount < data.getFile().getInt("checkPrice.min")){
            p.sendMessage(ChatColor.RED + "수표로 발행 가능한 최소 금액 미만인 금액입니다.");
            return;
        }

        // 인벤토리 공간 체크
        if(p.getInventory().firstEmpty() == -1){
            p.sendMessage(ChatColor.RED + "인벤토리를 비우고 다시 시도해주시길 바랍니다.");
            return;
        }

        // 돈 있는지 체크
        if(economy.getBalance(p.getName()) < amount){
            p.sendMessage(ChatColor.RED + "수표를 발행할 충분한 돈이 없습니다.");
            return;
        }

        String playerName = p.getName();
        String uuid = UUID.randomUUID().toString();
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        String timestamp = now.format(formatter);

        // 수표 ItemStack 생성
        ItemStack checkItemStack = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = checkItemStack.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&a[&f!&a] &f우클릭 시 돈이 지급됩니다 !"));
        itemMeta.setLore(lore);
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a[ &f수표 &a] &e" + amount +"원"));
        checkItemStack.setItemMeta(itemMeta);

        NBTItem nbtItem = new NBTItem(checkItemStack);
        nbtItem.setString("uuid", uuid);
        p.getInventory().addItem(nbtItem.getItem());

        // data에 추가
        data.getFile().set("check." + uuid + ".name", playerName);
        data.getFile().set("check." + uuid + ".used", false);
        data.getFile().set("check." + uuid + ".amount", amount);
        data.getFile().set("check." + uuid + ".time", timestamp);
        data.saveConfig();

        economy.withdrawPlayer(p, amount);
        p.sendMessage(ChatColor.YELLOW + "수표가 성공적으로 발급되었습니다.");
    }

    /**
     * OP 전용
     */
    // 수표 최소금액
    public void setCheckMinPrice(Player p, int amount){
        if(amount <= 0){
            p.sendMessage(ChatColor.RED + "올바른 숫자를 입력해주시기 바랍니다.");
            return;
        }

        data.getFile().set("checkPrice.min", amount);
        data.saveConfig();
        p.sendMessage(ChatColor.GREEN + "" + amount + "만큼 수표 최소금액 설정이 완료되었습니다.");
    }

    // 수표 최대금액
    public void setCheckMaxPrice(Player p, int amount){
        if(amount <= 0){
            p.sendMessage(ChatColor.RED + "올바른 숫자를 입력해주시기 바랍니다.");
            return;
        }

        data.getFile().set("checkPrice.max", amount);
        data.saveConfig();
        p.sendMessage(ChatColor.GREEN + "" + amount + "만큼 수표 최대 금액 설정이 완료되었습니다.");
    }

    /**
     * 돈 입금 테스트용
     */
    public void deposit(Player p, int amount){
        economy.depositPlayer(p, amount);
        p.sendMessage(ChatColor.GREEN + "" + amount + "원을 받았습니다.");
    }

    public void withDraw(Player p, int amount){
        economy.withdrawPlayer(p, amount);
        p.sendMessage(ChatColor.GREEN + "" + amount + "원만큼 출금이 완료되었습니다.");
    }
}
