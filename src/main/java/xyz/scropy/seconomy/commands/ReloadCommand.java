package xyz.scropy.seconomy.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.scropy.seconomy.Placeholder;
import xyz.scropy.seconomy.SEconomyPlugin;
import xyz.scropy.seconomy.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ReloadCommand implements CommandExecutor, TabCompleter {

    private final SEconomyPlugin plugin;

    public ReloadCommand(SEconomyPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length < 1 || !"reload".equals(args[0])) return true;

        if(!sender.hasPermission("seconomy.reload")) {
            sender.sendMessage(StringUtils.format(plugin.getConfig().getString("messages.no-permission"), Placeholder.builder().build()));
            return true;
        }

        sender.sendMessage(StringUtils.format(plugin.getConfig().getString("messages.reloaded-config"), Placeholder.builder().build()));
        plugin.getConfig().reload();

        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return Collections.singletonList("reload");
    }
}
