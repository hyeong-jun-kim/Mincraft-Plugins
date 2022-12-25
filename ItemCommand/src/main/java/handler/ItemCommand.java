package handler;

import neo.data.DataManager;
import neo.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemCommand {
    private FileConfiguration file;
    private ConsoleCommandSender console;

    private Player p;

    private int key; // data.yml key

    private String itemName;
    private int itemCount;

    private String itemCommand;
    private String commonCommand;

    public ItemCommand(Player p){
        this.p = p;
        file = Main.getFileConfiguration();
        console = Bukkit.getServer().getConsoleSender();
    }

    // data.yml에 등록된 커맨드인지 확인
    // 등록된 커맨드면 안에있는 정보를 불러온다.
    public boolean isRegisterCommand(String cmd){
        file.getConfigurationSection("data").getKeys(false)
                .forEach(k -> {
                    if(cmd.equalsIgnoreCase(file.getString("data." + k + ".run_command"))){
                        getDataInfo(k);
                    }
                });

        if(itemName != null){
            return true;
        }else{
            return false;
        }
    }

    // 아이템이 존재하는지 확인
    public boolean isHaveItem(){
        for(ItemStack item: p.getInventory().getContents()){
            if(item == null)
                continue;

            ItemMeta itemMeta = item.getItemMeta();
            String displayName = itemMeta.getDisplayName();
            displayName = ChatColor.stripColor(displayName);
            if(itemMeta.getDisplayName() != null && displayName.equalsIgnoreCase(itemName)){
                // 갯수 확인
                if(itemCount <= item.getAmount()){
                    // 아이템 제거
                    item.setAmount(item.getAmount() - itemCount);
                    return true;
                }
            }
        }
        return false;
    }

    // 아이템이 있는 커맨드 실행 (OP 권한으로 실행)
    public void excuteItemCommand(){
//        p.sendMessage("아이템 커맨드 실행");
        if(!p.isOp()) {
            p.setOp(true);
            p.performCommand(itemCommand);
            p.setOp(false);
        }
    }

    // 아이템이 없는 커맨드 실행
    public void excuteCommonCommand(){
//        p.sendMessage("아이템 없을 때 커맨드 실행");
        if(commonCommand != null) {
            p.performCommand(commonCommand);
        }
    }

    // data.yml 정보  가져오기
    private void getDataInfo(String k){
        key = Integer.parseInt(k);
        itemName = file.getString("data." + k + ".item").toUpperCase();
        itemCount = file.getInt("data." + k + ".count");
        itemCommand = file.getString("data." + k + ".item_command");
        commonCommand = file.getString("data." + k + ".common_command");

        itemCommand = itemCommand.replace("%player", p.getName());
        commonCommand = commonCommand.replace("%player", p.getName());
    }
}
