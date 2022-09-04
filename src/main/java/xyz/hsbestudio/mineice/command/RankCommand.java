package xyz.hsbestudio.mineice.command;

import com.google.common.collect.Lists;
import xyz.hsbestudio.mineice.MineIceRank;
import xyz.hsbestudio.mineice.db.Database;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class RankCommand extends BaseCommand {
    private static final String COMMAND_NAME = "rank";

    private final Database database = new Database();

    private static final double chatActivityRatio = MineIceRank.getInstance().getConfig().getDouble("chatActivityRatio");
    private static final double gameActivityRatio = MineIceRank.getInstance().getConfig().getDouble("gameActivityRatio");

    public RankCommand() {
        super(COMMAND_NAME);
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (args.length == 0) {
            double gameActivity = database.getGameActivity(sender.getName());
            int chatActivity = database.chatActivity(sender.getName());
            double activity = Math.log(chatActivity * chatActivityRatio +
                    gameActivity * gameActivityRatio) / Math.log(5);

            sender.sendMessage(ChatColor.DARK_GREEN + "На данный момент ваш ранг: " +
                    ChatColor.DARK_PURPLE + (activity <= 12 ? Math.round(activity) : 12));
            return;
        }

        sender.sendMessage(ChatColor.DARK_RED + "Такая команда не найдена.\nИспользуйте " +
                ChatColor.DARK_BLUE + "/rank" +
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
