package xyz.hsbestudio.ranksystem;

import com.mysql.cj.x.protobuf.MysqlxCrud;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.lang.reflect.Array;
import java.util.*;

public class Annoyer implements AutoCloseable {
    private TextComponent message;
    private int taskID = -1;
    private Server serv;
    List<UUID> players = new LinkedList<>();

    Annoyer(TextComponent component) {
        message = component;
        serv = RankSystem.getInstance().getServer();
        taskID = serv.getScheduler().scheduleSyncRepeatingTask(RankSystem.getInstance(), new Runnable() {
            @Override
            public void run() {
                Collection<Player> onlinePlayers = (Collection<Player>)serv.getOnlinePlayers();
                for (Player player : onlinePlayers) {
                    if (players.contains(player.getUniqueId())) {
                        annoy(player);
                    }
                }
            }

            void annoy(Player player) {
                RankSystem.getInstance().LOGGER.info("Annoyed " + player.getName());
                player.sendMessage(message);
            }
        }, 0, 20000);
    }

    public void annoyPlayer(Player player) {
        RankSystem.getInstance().getLogger().info("Annoying " + player.getName());
        UUID uuid = player.getUniqueId();
        if(players.contains(uuid)) return;

        players.add(uuid);
    }

    public void disannoyPlayer(Player player) {
        RankSystem.getInstance().getLogger().info("Disannoying " + player.getName());
        players.remove(player.getUniqueId());
    }

    @Override
    public void close() throws Exception {
        serv.getScheduler().cancelTask(taskID);
    }
}
