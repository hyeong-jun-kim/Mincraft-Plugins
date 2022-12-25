package neo.feature;

import eu.endercentral.crazy_advancements.*;
import eu.endercentral.crazy_advancements.manager.AdvancementManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CustomAdvencement {
    public void sendMsgToPlayer(Player p, String iconItem, String s_title, String s_content){
        Material material = findByItemName(iconItem);
        if(material == null){
            p.sendMessage(ChatColor.RED + "아이템 이름을 다시 확인해 주시기 바랍니다.");
            return;
        }
        ItemStack icon = new ItemStack(material);
        JSONMessage title = new JSONMessage(s_title);
        JSONMessage content = new JSONMessage(s_content);
        AdvancementDisplay.AdvancementFrame frame = AdvancementDisplay.AdvancementFrame.GOAL;
        AdvancementVisibility visibility = new AdvancementVisibility() {
            @Override
            public boolean isVisible(Player player, Advancement advancement) {
                if(advancement.isGranted(player))
                    return true;
                Advancement parent = advancement.getParent();
                return parent == null || parent.isGranted(player);
            }
        };

        AdvancementDisplay display = new AdvancementDisplay(icon, title, content, frame, true, true, visibility);
        Advancement rootAdvancement = new Advancement(null, new NameKey("your_namespace", "advancement_name"), display);

//        AdvancementManager manager = CrazyAdvancements.getNewAdvancementManager();
//        manager.addPlayer(p);
//        manager.addAdvancement(rootAdvancement);
    }

    public Material findByItemName(String itemName){
        for(Material m: Material.values()){
            if(m.toString().equals(itemName)){
                return m;
            }
        }
        return null;
    }
}
