package xyz.hsbestudio.mineice;

import xyz.hsbestudio.mineice.command.DiscordCommand;
import xyz.hsbestudio.mineice.command.RankCommand;
import xyz.hsbestudio.mineice.db.Database;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class MineIceRank extends JavaPlugin {
    private static final Logger LOGGER = Logger.getLogger("MineIceRank");
    private static MineIceRank instance;

    private void onRegisterCommands() {
        new RankCommand();
        new DiscordCommand();
    }

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        Database database = new Database();
        String tableName = MineIceRank.getInstance().getConfig().getString("tableName");

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

    public static MineIceRank getInstance() {
        return instance;
    }
}
