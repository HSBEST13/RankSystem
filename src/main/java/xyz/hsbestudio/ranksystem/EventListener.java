package xyz.hsbestudio.ranksystem;

import org.bukkit.attribute.Attribute;
import xyz.hsbestudio.ranksystem.db.Database;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EventListener implements Listener {
    private final Database database = new Database();
    private final Map<String, Long> lastMessageTime = new HashMap<>();
    private final Map<String, Long> lastKillEntityTine = new HashMap<>();

    private String getNickname(PlayerEvent event) {
        return event.getPlayer().getName();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        String nickname = getNickname(event);

        if (!database.isPlayerInDatabase(nickname))
            database.createPlayer(nickname, event.getPlayer().getUniqueId());
        if(!database.isRegistered(uuid)) {
            RankSystem.getInstance().discordAnnoyer.annoyPlayer(event.getPlayer());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        lastMessageTime.remove(getNickname(event));
    }

    @EventHandler
    public void onSendMessage(AsyncChatEvent event) {
        String nickname = getNickname(event);
        long nowTime = new Date().getTime();

        if (lastMessageTime.containsKey(nickname)) {
            if ((nowTime - lastMessageTime.get(nickname)) / 1000 / 60 >= 5) {
                database.addChatActivity(nickname);
                lastMessageTime.put(nickname, nowTime);
            }
        } else {
            database.addChatActivity(nickname);
            lastMessageTime.put(nickname, nowTime);
        }
    }

    @EventHandler
    public void onKillEntity(EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null) {
            return;
        }
        String nickname = event.getEntity().getKiller().getName();
        long nowTime = new Date().getTime();

        if (lastKillEntityTine.containsKey(nickname)) {
            if ((nowTime - lastKillEntityTine.get(nickname)) / 1000 >= 10) {
                database.addGameActivity(nickname, event.getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                lastKillEntityTine.put(nickname, nowTime);
            }
        } else {
            database.addGameActivity(nickname, event.getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
            lastKillEntityTine.put(nickname, nowTime);
        }
    }
}
