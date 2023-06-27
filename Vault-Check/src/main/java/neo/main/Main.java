package neo.main;

import neo.command.Commands;
import neo.data.DataManager;
import neo.event.EventListener;
import neo.service.MoneyService;
import net.milkbowl.vault.economy.Economy;
import net.starly.money.Money;
import net.starly.money.support.PlayerEconomyProvider;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static DataManager data;
    public Economy economy;

    @Override
    public void onEnable() {
        if (!setupEconomy()) {
            System.out.println(ChatColor.RED + "Vault 플러그인이 설치되어 있지 않아, 종료합니다.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        Plugin plugin = getServer().getPluginManager().getPlugin("ST-Money");
        if(!(plugin instanceof Money)){
            System.out.println("ST-Money 플러그인이 설치되어있지 않아 종료합니다.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        Money money = (Money) plugin;
        Economy economy = new PlayerEconomyProvider(money);
//        economy.depositPlayer("test", 1);
//        economy.withdrawPlayer("test", 1);

        data = new DataManager(this);
        MoneyService moneyService = new MoneyService(this);
        getServer().getPluginManager().registerEvents(new EventListener(moneyService), this);
        getCommand("수표").setExecutor(new Commands(moneyService));
    }

    @Override
    public void onDisable() {

    }

    public static DataManager getData() {
        return data;
    }

    private boolean setupEconomy() {
        if (this.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        } else {
            RegisteredServiceProvider<Economy> rsp = this.getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp == null) {
                return false;
            } else {
                economy = (Economy)rsp.getProvider();
                return economy != null;
            }
        }
    }
}


