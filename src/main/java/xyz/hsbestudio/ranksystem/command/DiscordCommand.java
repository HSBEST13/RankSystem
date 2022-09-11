package xyz.hsbestudio.ranksystem.command;

import com.google.common.collect.Lists;
import xyz.hsbestudio.ranksystem.db.Database;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Objects;

public class DiscordCommand extends BaseCommand {
    private static final String COMMAND_NAME = "discord";

    private final Database database = new Database();

    public DiscordCommand() {
        super(COMMAND_NAME);
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (Objects.equals(sender.getName(), "CONSOLE")) {
            sender.sendMessage(ChatColor.DARK_RED + "Server could not use this command");
            return;
        }

        if (args.length == 0) {
            int id = database.getId(sender.getName());

            sender.sendMessage(ChatColor.DARK_GREEN + "Your discord id is " +
                    ChatColor.DARK_BLUE + id +
                    ChatColor.DARK_GREEN + ". Please enter this id in discord bot");
            return;
        }

        sender.sendMessage(ChatColor.DARK_RED + "No such command was found.\nYou can use " +
                ChatColor.DARK_BLUE + "/discord " +
                ChatColor.DARK_RED + " to get your discord id for bot");
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return Lists.newArrayList(COMMAND_NAME);
        }
        return Lists.newArrayList();
    }
}
