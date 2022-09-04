package xyz.hsbestudio.mineice.command;

import com.google.common.collect.Lists;
import xyz.hsbestudio.mineice.db.Database;
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
        if (args.length == 0) {
            sender.sendMessage(ChatColor.DARK_RED + "Для команды надо передать параметр:"
                    + ChatColor.DARK_BLUE + " никнейм в дискорде");
            return;
        }

        if (!Objects.equals(args[0], "") && !Objects.equals(args[0], null)) {
            database.addDiscordNickname(sender.getName(), args[0]);
            sender.sendMessage(ChatColor.DARK_GREEN + "Дискорд успешно добавлен");
            return;
        }

        sender.sendMessage(ChatColor.DARK_RED + "Такая команда не найдена.\nИспользуйте " +
                ChatColor.DARK_BLUE + "/discord 'Ваш никнейм в дискорде'" +
                ChatColor.DARK_RED + " для того, чтобы узнать ваш ранг на сервере");
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return Lists.newArrayList(COMMAND_NAME);
        }
        return Lists.newArrayList();
    }
}
