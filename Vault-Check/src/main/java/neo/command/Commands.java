package neo.command;

import neo.service.MoneyService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
    private MoneyService moneyService;

    public Commands(MoneyService moneyService) {
        this.moneyService = moneyService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("cmd에서는 이 작업을 수행하지 못합니다.");
            return true;
        } else {
            Player p = ((Player) sender).getPlayer();

            if (p.isOp()) {
                switch (args.length) {
                    case 2:
                        if (args[0].equals("최대금액")) {
                            int amount = Integer.parseInt(args[1]);
                            moneyService.setCheckMaxPrice(p, amount);
                        } else if (args[0].equals("최소금액")) {
                            int amount = Integer.parseInt(args[1]);
                            moneyService.setCheckMinPrice(p, amount);
                            break;
                        }
                }
            }

            switch (args.length) {
                case 2:
                    if (args[0].equals("발행")) {
                        int amount = Integer.parseInt(args[1]);
                        moneyService.createCheck(p, amount);
                    }
                    break;
            }
            return true;
        }
    }
}
