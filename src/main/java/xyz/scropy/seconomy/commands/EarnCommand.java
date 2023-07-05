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
import xyz.scropy.seconomy.managers.CooldownProvider;
import xyz.scropy.seconomy.user.User;

import java.time.Duration;
import java.util.List;
import java.util.Random;

public class EarnCommand implements CommandExecutor, TabCompleter {

    private final SEconomyPlugin plugin;
    private final CooldownProvider cooldownProvider;
    private final Random random = new Random();

    public EarnCommand(SEconomyPlugin plugin) {
        this.plugin = plugin;
        cooldownProvider = new CooldownProvider(Duration.ofSeconds(plugin.getConfig().getInt("cooldown-in-seconds")));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(StringUtils.format(plugin.getConfig().getString("messages.only-player"), Placeholder.builder().build()));
            return true;
        }

        if(cooldownProvider.isOnCooldown(player.getUniqueId())) {
            StringUtils.sendMessage(player, "cooldown");
            return true;
        }

        cooldownProvider.apply(player.getUniqueId());

        int amount = random.nextInt(1, 6);
        StringUtils.sendMessage(player, "earned-money", Placeholder.builder().apply("%amount%", StringUtils.moneyFormat(amount)));

        User user = plugin.getUserManager().getUser(player.getUniqueId());
        user.setMoney(user.getMoney() + amount);
        user.setChanged(true);
        plugin.getDatabaseManager().getUserRepository().save(user);
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return plugin.getServer().getOnlinePlayers().stream().map(player -> player.getName()).toList();
    }
}
