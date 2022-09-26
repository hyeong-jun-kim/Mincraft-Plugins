package neo.main;

import neo.data.DataManager;
import neo.event.EventListener;
import neo.feature.StaminaBoard;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main extends JavaPlugin {
    public static DataManager data;
    public static Main plugin;
    public static HashMap<OfflinePlayer, StaminaBoard> staminaBoards;

    @Override
    public void onEnable() {
        plugin = this;
        data = new DataManager(this);
        staminaBoards = new HashMap<>();
        initRecipeBottle();
        getServer().getPluginManager().registerEvents(new EventListener(), this);
//        getCommand("커맨드").setExecutor(new Commands());
    }

    @Override
    public void onDisable() {

    }

    public void initRecipeBottle() {
        ItemStack resultPotion = new ItemStack(Material.POTION);
        ItemMeta itemMeta = resultPotion.getItemMeta();
        PotionData resultPotionData = new PotionData(PotionType.WATER, false, false);
        ((PotionMeta) itemMeta).setBasePotionData(resultPotionData);
        itemMeta.setDisplayName(ChatColor.AQUA + "증류수");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GOLD + "갈증을 해소시켜 주는 증류수이다.");
        itemMeta.setLore(lore);
        resultPotion.setItemMeta(itemMeta);

        ItemStack waterPotion = new ItemStack(Material.POTION);
        ItemMeta waterPotionItemMeta = waterPotion.getItemMeta();
        PotionData potiondata = new PotionData(PotionType.WATER, false, false);
        ((PotionMeta) waterPotionItemMeta).setBasePotionData(potiondata);
        waterPotion.setItemMeta(waterPotionItemMeta);

        RecipeChoice recipeChoice = new RecipeChoice.ExactChoice(waterPotion);
        NamespacedKey namespacedKey = new NamespacedKey(this, NamespacedKey.MINECRAFT);
        FurnaceRecipe recipe = new FurnaceRecipe(namespacedKey, resultPotion, recipeChoice, 0f, 100);
        getServer().addRecipe(recipe);
    }

    public static Main getPlugin() {
        return plugin;
    }

    public static DataManager getData() {
        return data;
    }

    public static HashMap<OfflinePlayer, StaminaBoard> getStaminaBoards() {
        return staminaBoards;
    }
}

