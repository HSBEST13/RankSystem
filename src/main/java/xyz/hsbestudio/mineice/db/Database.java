package xyz.hsbestudio.mineice.db;


import xyz.hsbestudio.mineice.MineIceRank;

import java.sql.*;
import java.util.Objects;
import java.util.logging.Logger;

public class Database extends Config {

    private static final Logger LOGGER = Logger.getLogger(MineIceRank.class.getName());
    Connection connection;

    public Connection getConnection() throws SQLException, ClassNotFoundException {
        String connectionData = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE_NAME;

        Class.forName("com.mysql.jdbc.Driver");

        connection = DriverManager.getConnection(connectionData, LOGIN, PASSWORD);

        return connection;
    }

    public void createTable(String tableName) {
        String request = "CREATE TABLE " + tableName + " (" +
                "nickname VARCHAR(30) PRIMARY KEY NOT NULL, ds_nickname VARCHAR(30), chat_activity INTEGER, game_activity DOUBLE)";

        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(request);
            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void createPlayer(String nickname) {
        String request = "INSERT INTO players (nickname, chat_activity, game_activity) VALUES (?, 0, 0)";


        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(request);
            preparedStatement.setString(1, nickname);
            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isPlayerInDatabase(String nickname) {
        ResultSet resultSet;
        String request = "SELECT nickname FROM players WHERE nickname = ?";

        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(request);
            preparedStatement.setString(1, nickname);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Objects.equals(resultSet.getString("nickname"), nickname);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public void addGameActivity(String nickname, Double value) {
        String request = "UPDATE players SET game_activity = game_activity + ? WHERE nickname = ?";

        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(request);
            preparedStatement.setDouble(1, value / 20);
            preparedStatement.setString(2, nickname);
            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void addChatActivity(String nickname) {
        String request = "UPDATE players SET chat_activity = chat_activity + 1 WHERE nickname = ?";

        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(request);
            preparedStatement.setString(1, nickname);
            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void addDiscordNickname(String nickname, String ds) {
        String request = "UPDATE players SET ds_nickname = ? WHERE nickname = ?";

        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(request);
            preparedStatement.setString(1, ds);
            preparedStatement.setString(2, nickname);
            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public int getGameActivity(String nickname) {
        ResultSet resultSet;
        String request = "SELECT game_activity FROM players WHERE nickname = ?";

        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(request);
            preparedStatement.setString(1, nickname);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("game_activity");
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public int chatActivity(String nickname) {
        ResultSet resultSet;
        String request = "SELECT chat_activity FROM players WHERE nickname = ?";

        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(request);
            preparedStatement.setString(1, nickname);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("chat_activity");
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
}
