package xyz.hsbestudio.mineice.db;

import xyz.hsbestudio.mineice.MineIceRank;

public class Config {
    protected static final String HOST = MineIceRank.getInstance().getConfig().getString("db.host");
    protected static final String PORT = MineIceRank.getInstance().getConfig().getString("db.port");
    protected static final String DATABASE_NAME = MineIceRank.getInstance().getConfig().getString("db.dbName");

    protected static final String LOGIN = MineIceRank.getInstance().getConfig().getString("db.login");
    protected static final String PASSWORD = MineIceRank.getInstance().getConfig().getString("db.password");
}
