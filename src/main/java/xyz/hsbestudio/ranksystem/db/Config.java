package xyz.hsbestudio.ranksystem.db;

import xyz.hsbestudio.ranksystem.RankSystem;

public class Config {
    protected static final String HOST = RankSystem.getInstance().getConfig().getString("db.host");
    protected static final String PORT = RankSystem.getInstance().getConfig().getString("db.port");
    protected static final String DATABASE_NAME = RankSystem.getInstance().getConfig().getString("db.dbName");

    protected static final String LOGIN = RankSystem.getInstance().getConfig().getString("db.login");
    protected static final String PASSWORD = RankSystem.getInstance().getConfig().getString("db.password");
}
