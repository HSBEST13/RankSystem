package xyz.hsbestudio.ranksystem.command;

import com.google.common.collect.Lists;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import xyz.hsbestudio.ranksystem.RankSystem;
import xyz.hsbestudio.ranksystem.db.Database;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

public class DiscordCommand extends BaseCommand {
    private static final String COMMAND_NAME = "discord";
    String message;

    private final Database database = new Database();

    public DiscordCommand(String message) {
        super(COMMAND_NAME);
        RankSystem.LOGGER.info(message);
        this.message = message;
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player player) {
            RankSystem.getInstance().getDiscordAnnoyer().disannoyPlayer((Player) sender);
            var nickname = player.getName();
            UUID uuid = player.getUniqueId();
            if (database.isRegistered(uuid)) {
                //FIXME: add message with choose
                sender.sendMessage("Вы уже зарегистрированы в дискорде!");
                return;
            }
            var msg = message;
            var hash = database.generateDiscordHash(nickname);

            int first = msg.indexOf('[');
            int last = msg.indexOf(']') + 1;
            var button = msg.substring(first, last);
            //button.replace('<', '[');
            //button.replace('>', ']');

            var array = new String[]{msg.substring(0, first), msg.substring(last)};

            RankSystem.LOGGER.info("button : "+button);
            RankSystem.LOGGER.info("Array is : "+array.toString());

            Style style = Style.style(NamedTextColor.GOLD, TextDecoration.UNDERLINED);
            sender.sendMessage(Component.text()
                    .append(Component.text(array[0], NamedTextColor.DARK_GREEN))
                    .append(Component.text(button, style).clickEvent(ClickEvent.copyToClipboard(hash)))
                    .append(Component.text(array[1], NamedTextColor.DARK_GREEN))
                    .build());
        }
    }

//    @Override
//    public void execute(CommandSender sender, String label, String[] args) {
//        if (Objects.equals(sender.getName(), "CONSOLE")) {
//            sender.sendMessage(ChatColor.DARK_RED + "Server could not use this command");
//            return;
//        }
//
//        if (args.length == 0) {
//            int id = database.getDiscordId(sender.getName());
//
//            sender.sendMessage(ChatColor.DARK_GREEN + "Your discord id is " +
//                    ChatColor.DARK_BLUE + id +
//                    ChatColor.DARK_GREEN + ". Please enter this id in discord bot");
//            return;
//        }
//
//        sender.sendMessage(ChatColor.DARK_RED + "No such command was found.\nYou can use " +
//                ChatColor.DARK_BLUE + "/discord " +
//                ChatColor.DARK_RED + " to get your discord id for bot");
//    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return Lists.newArrayList(COMMAND_NAME);
        }
        return Lists.newArrayList();
    }
}
