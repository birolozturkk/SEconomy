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

import java.util.List;

public class BalanceCommand implements CommandExecutor, TabCompleter {

    private final SEconomyPlugin plugin;

    public BalanceCommand(SEconomyPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(StringUtils.format(plugin.getConfig().getString("messages.not-enough-arguments"), Placeholder.builder().build()));
                return true;
            }
            StringUtils.sendMessage(player, "your-balance", Placeholder.builder()
                    .apply("%balance%", StringUtils.moneyFormat(plugin.getUserManager().getUser(player.getUniqueId()).getMoney())));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(StringUtils.format(plugin.getConfig().getString("messages.invalid-argument"), Placeholder.builder().build()));
            return true;
        }
        sender.sendMessage(StringUtils.format(plugin.getConfig().getString("messages.balance"), Placeholder.builder()
                .apply("%player%", target.getName())
                .apply("%balance%", StringUtils.moneyFormat(plugin.getUserManager().getUser(target.getUniqueId()).getMoney())).build()));
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return plugin.getServer().getOnlinePlayers().stream().map(player -> player.getName()).toList();
    }
}
