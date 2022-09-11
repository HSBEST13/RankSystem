package xyz.hsbestudio.ranksystem;

import xyz.hsbestudio.ranksystem.command.DiscordCommand;
import xyz.hsbestudio.ranksystem.command.RankCommand;
import xyz.hsbestudio.ranksystem.db.Database;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class RankSystem extends JavaPlugin {
    private static final Logger LOGGER = Logger.getLogger("RankSystem");
    private static RankSystem instance;

    private void onRegisterCommands() {
        new RankCommand();
        new DiscordCommand();
    }

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        Database database = new Database();
        String tableName = RankSystem.getInstance().getConfig().getString("tableName");

        try {
            database.createTable(tableName);
        } catch (RuntimeException e) {
            LOGGER.info("Table '" + tableName + "' already created");
        }

        onRegisterCommands();

        Bukkit.getPluginManager().registerEvents(new EventListener(), this);
        LOGGER.info("MineIce Discord Plugin is working!");
    }

    @Override
    public void onDisable() {
        LOGGER.info("MineIce Discord Plugin disabled!");
    }

    public static RankSystem getInstance() {
        return instance;
    }
}
