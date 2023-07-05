package xyz.scropy.seconomy;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class StringUtils {

    private final static LegacyComponentSerializer legacyComponentSerializer = LegacyComponentSerializer.legacyAmpersand();
    private final static DecimalFormat numberFormat = new DecimalFormat("#.00");

    public static void sendMessage(Player player, String section) {
        sendMessage(player, section, Placeholder.builder());
    }

    public static void sendMessage(Player player, String section, Placeholder.PlaceholderBuilder placeholderBuilder) {
        String message = SEconomyPlugin.getInstance().getConfig().getString("messages." + section);
        if(message == null) return;
        Component component = StringUtils.formatComponent(message,
                placeholderBuilder.apply("%player_name%", player.getName()).build());
        SEconomyPlugin.getInstance().getAdventure().player(player).sendMessage(component);
    }

    public static String numberFormat(int number) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        formatter.setDecimalFormatSymbols(symbols);
        return formatter.format(number);
    }


    public static String moneyFormat(double number) {
        if(number == 0) return "0.0";
        return numberFormat.format(number);
    }

    public static Component formatComponent(String string, List<Placeholder> placeholders) {
        string = processPlaceholders(string, placeholders);
        return legacyComponentSerializer.deserialize(string).decoration(TextDecoration.ITALIC, false);
    }

    public static List<Component> formatComponent(List<String> list, List<Placeholder> placeholders) {
        return list.stream().map(string -> formatComponent(string, placeholders)).collect(Collectors.toList());
    }

    public static String format(String string, List<Placeholder> placeholders) {
        return ChatColor.translateAlternateColorCodes('&', processPlaceholders(string, placeholders));
    }

    public static List<String> format(List<String> list, List<Placeholder> placeholders) {
        return list.stream().map(string -> format(string, placeholders)).collect(Collectors.toList());
    }

    public static String processPlaceholders(String content, List<Placeholder> placeholders) {
        for (Placeholder placeholder : placeholders) {
            content = content.replace(placeholder.getKey(), placeholder.getValue());
        }
        return content;
    }

    public static String colorize(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
