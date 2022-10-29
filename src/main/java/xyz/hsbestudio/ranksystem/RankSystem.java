package xyz.hsbestudio.ranksystem;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.checkerframework.checker.units.qual.A;
import xyz.hsbestudio.ranksystem.command.DiscordCommand;
import xyz.hsbestudio.ranksystem.command.RankCommand;
import xyz.hsbestudio.ranksystem.db.Database;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class RankSystem extends JavaPlugin {
    public static final Logger LOGGER = Logger.getLogger("RankSystem");
    private static RankSystem instance;

    Annoyer discordAnnoyer;

    private void onRegisterCommands() {
        new RankCommand();
        new DiscordCommand(RankSystem.getInstance().getConfig().getString("discordLinkMessage"));
    }

    public Annoyer getDiscordAnnoyer() {
        return discordAnnoyer;
    }

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        Database database = new Database();
        String message = getConfig().getString("discordAnnoyMessage");
        if (message == null) {
            LOGGER.warning("'discordAnnoyMessage' not found in config!");
        } else {
            TextComponent discordText = Component.text(message, NamedTextColor.GOLD);
            discordAnnoyer = new Annoyer(discordText);
        }

        onRegisterCommands();

        Bukkit.getPluginManager().registerEvents(new EventListener(), this);
        LOGGER.info("RankSystem plugin is working!");
    }

    @Override
    public void onDisable() {
        if (discordAnnoyer != null) {
            try {
                discordAnnoyer.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        LOGGER.info("RankSystem plugin is disabled!");
    }

    public static RankSystem getInstance() {
        return instance;
    }
}
