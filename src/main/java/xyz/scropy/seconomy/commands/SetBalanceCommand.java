package xyz.scropy.seconomy.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import xyz.scropy.seconomy.Placeholder;
import xyz.scropy.seconomy.SEconomyPlugin;
import xyz.scropy.seconomy.StringUtils;
import xyz.scropy.seconomy.user.User;

import java.util.List;

public class SetBalanceCommand implements CommandExecutor, TabCompleter {

    private final SEconomyPlugin plugin;

    public SetBalanceCommand(SEconomyPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("seconomy.setbalance")) {
            sender.sendMessage(StringUtils.format(plugin.getConfig().getString("messages.no-permission"), Placeholder.builder().build()));
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(StringUtils.format(plugin.getConfig().getString("messages.not-enough-arguments"), Placeholder.builder().build()));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(StringUtils.format(plugin.getConfig().getString("messages.invalid-argument"), Placeholder.builder().build()));
            return true;
        }

        double amount;
        try {
            amount = Double.parseDouble(args[1]);
        } catch (NumberFormatException exception) {
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessage(StringUtils.format(plugin.getConfig().getString("messages.invalid-argument"), Placeholder.builder().build()));
                return true;
            }
            sender.sendMessage(StringUtils.format(plugin.getConfig().getString("messages.invalid-argument"), Placeholder.builder().build()));
            return true;
        }

        if(amount < 0) {
            sender.sendMessage(StringUtils.format(plugin.getConfig().getString("messages.cannot-negative-value"), Placeholder.builder().build()));
            return true;
        }

        sender.sendMessage(StringUtils.format(plugin.getConfig().getString("messages.set-balance"), Placeholder.builder()
                .apply("%player%", target.getName())
                .apply("%amount%", StringUtils.moneyFormat(amount)).build()));

        User user = plugin.getUserManager().getUser(target.getUniqueId());
        user.setMoney(amount);
        user.setChanged(true);
        plugin.getDatabaseManager().getUserRepository().save(user);

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return plugin.getServer().getOnlinePlayers().stream().map(player -> player.getName()).toList();
    }
}
