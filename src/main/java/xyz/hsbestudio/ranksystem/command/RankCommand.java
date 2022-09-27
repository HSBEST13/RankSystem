package xyz.hsbestudio.ranksystem.command;

import com.google.common.collect.Lists;
import xyz.hsbestudio.ranksystem.RankSystem;
import xyz.hsbestudio.ranksystem.db.Database;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Objects;

public class RankCommand extends BaseCommand {
    private static final String COMMAND_NAME = "rank";

    private final Database database = new Database();

    private static final double chatActivityRatio = RankSystem.getInstance().getConfig().getDouble("chatActivityRatio");
    private static final double gameActivityRatio = RankSystem.getInstance().getConfig().getDouble("gameActivityRatio");

    public RankCommand() {
        super(COMMAND_NAME);
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (Objects.equals(sender.getName(), "CONSOLE")) {
            sender.sendMessage(ChatColor.DARK_RED + "Server could not use this command");
            return;
        }

        if (args.length == 0) {
            double activity = Math.log(database.chatActivity(sender.getName()) * chatActivityRatio +
                    database.getGameActivity(sender.getName()) * gameActivityRatio) / Math.log(2);
            sender.sendMessage(ChatColor.DARK_GREEN + "Now your rank is: " +
                    ChatColor.DARK_PURPLE + (int)Math.floor(activity));
            return;
        }

        sender.sendMessage(ChatColor.DARK_RED + "No such command was found.\nUse " +
                ChatColor.DARK_BLUE + "/rank" +
                ChatColor.DARK_RED + " to recognize your rank on server");
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return Lists.newArrayList(COMMAND_NAME);
        }
        return Lists.newArrayList();
    }
}
