package neo.event;

import dev.sergiferry.playernpc.api.NPC;
import neo.data.DataManager;
import neo.main.Main;
import neo.shop.Shop;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Map;

public class EventListener implements Listener {
    DataManager data = Main.getData();
    boolean check = false;

    @EventHandler
    public void onNPCInteract(NPC.Events.Interact event) {
        Player p = event.getPlayer();
        NPC npc = event.getNPC();
        if (event.getClickType() == NPC.Interact.ClickType.RIGHT_CLICK) {
            ConfigurationSection section = data.getFile().getConfigurationSection("npc");
            for (String key : section.getKeys(false)) {
                if (npc.getCode().equals("npcshop.global_" + key)) {
                    p.sendMessage("아야");
                    String npcName = npc.getCode().replace("npcshop.global_","");
                    Inventory gui = getNpcGUI(npcName, p);
                    if(gui == null)
                        return;
                    p.openInventory(gui);
                }
            }
        }
    }
    // 상점 NPC GUI 클릭 이벤트
    @EventHandler
    public void onPlayerInventoryClicKEvent(InventoryClickEvent e) {
        String title = e.getView().getTitle();
        String shopTitle = title.substring(0, 2);
        if (shopTitle.equals("상점")) {
            Player p = (Player) e.getWhoClicked();
            // 공기
            ItemStack AIR = new ItemStack(Material.AIR);
            //유리
            ItemStack GLASS = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem() == AIR || e.getCurrentItem() == GLASS)
                return;
            if (e.getClick() == ClickType.RIGHT) {
                String npcName = title.replace("상점_", "");
                Inventory inventory = p.getInventory();
                int key = e.getSlot() - 10;
                if (data.getFile().contains("npc." + npcName + ".shop." + key)) {
                    ItemStack shopItem = data.getFile().getItemStack("npc." + npcName + ".shop." + key + ".shopItem");
                    ItemStack buyItem = data.getFile().getItemStack("npc." + npcName + ".shop." + key + ".buyItem");
                    int addPrice = data.getFile().getInt("npc." + npcName + ".shop." + key + ".buyItem");
                    int buyCount = data.getFile().getInt("npc." + npcName + ".shop." + key + ".buyCount");
                    addPrice = buyItem.getAmount() + buyCount * addPrice;
                    if (inventory.containsAtLeast(buyItem, addPrice)) {
                        if(inventory.firstEmpty() == -1){ // 인벤토리 꽉찼을 경우
                            p.sendMessage(ChatColor.RED + "인벤토리를 한칸이상 비우고 구매해주세요!");
                            return;
                        }
                        consumeItem(p, addPrice, buyItem); // 아이템 소비
                        inventory.addItem(shopItem);
                        data.getFile().set("npc." + npcName + ".shop." + key + ".buyCount", ++buyCount);
                        data.saveConfig();
                        p.sendMessage(ChatColor.GREEN +  "물건을 구매하셨습니다!");
                        return;
                    } else {
                        p.sendMessage(ChatColor.RED + "물건을 사기위한 아이템의 갯수가 부족합니다!");
                        return;
                    }
                }
            }

        }
    }

    // 서버 reload 될 때 NPC 불러오기
    @EventHandler
    public void onServerLoadEvent(ServerLoadEvent e) {
        // NPC SHOP RELOAD
        Shop shop = new Shop();
        shop.reloadShop();
    }

    // GUI 창 가져오기
    public Inventory getNpcGUI(String npcName, Player p){
        ConfigurationSection section = data.getFile().getConfigurationSection("npc."+npcName + ".shop");
        if(section == null){ // 아이템이 상점에 등록 안 됐을 때
            return null;
        }
        ItemStack[] shopItems = new ItemStack[5];
        // 아이템들 가져오기
        for(String key: section.getKeys(false)){
            int key_int = Integer.parseInt(key);
            shopItems[key_int-1] =  data.getFile().getItemStack("npc." + npcName + ".shop." + key + ".shopItem");
        }
        // 기본 GUI 세팅
        Inventory gui = Bukkit.getServer().createInventory(p, 27, "상점_" + npcName);
        ItemStack glass = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
        ItemMeta itemMeta = glass.getItemMeta();
        itemMeta.setDisplayName("-");
        glass.setItemMeta(itemMeta);
        // 유리창 세팅
        gui.setItem(0, glass);
        gui.setItem(1, glass);
        gui.setItem(7, glass);
        gui.setItem(8, glass);
        gui.setItem(9, glass);
        gui.setItem(17, glass);
        gui.setItem(18, glass);
        gui.setItem(19, glass);
        gui.setItem(25, glass);
        gui.setItem(26, glass);
        // 아이템 세팅
        for(int i = 0; i <shopItems.length; i++){
            if(shopItems[i] != null){
                check = true;
                gui.setItem(i+11, shopItems[i]);
            }
        }
        return gui;
    }
    // 인벤토리에 있는 아이템 제거하기
    public boolean consumeItem(Player player, int count, ItemStack itemstack) {
        String displayName = itemstack.getItemMeta().getDisplayName();
        Material material = itemstack.getType();
        Map<Integer, ? extends ItemStack> ammo = player.getInventory().all(material);

        int found = 0;
        for (ItemStack stack : ammo.values()){
            if(stack.getItemMeta().getDisplayName().equals(displayName)){
                found += stack.getAmount();
            }
        }
        if (count > found)
            return false;

        for (Integer index : ammo.keySet()) {
            ItemStack stack = ammo.get(index);

            int removed = Math.min(count, stack.getAmount());
            count -= removed;

            if (stack.getAmount() == removed)
                player.getInventory().setItem(index, null);
            else
                stack.setAmount(stack.getAmount() - removed);

            if (count <= 0)
                break;
        }

        player.updateInventory();
        return true;
    }
}
