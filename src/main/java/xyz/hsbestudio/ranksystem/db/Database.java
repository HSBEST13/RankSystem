package xyz.hsbestudio.ranksystem.db;


import xyz.hsbestudio.ranksystem.RankSystem;

import java.sql.*;
import java.util.Objects;
import java.util.logging.Logger;

public class Database extends Config {

    private static final Logger LOGGER = Logger.getLogger(RankSystem.class.getName());

    private String tableName;
    Connection connection;

//    public Database() {
//        boolean isNaveDb = RankSystem.getInstance().getConfig().getBoolean("hasDb");
//
//        if (!isNaveDb) {
//            String request = "CREATE DATABASE IF NOT EXISTS ?";
//            String connectionData = "jdbc:mysql://" + HOST + ":" + PORT;
//            try {
//                Connection fConnection = DriverManager.getConnection(connectionData, LOGIN, PASSWORD);
//                PreparedStatement preparedStatement = fConnection.prepareStatement(request);
//                preparedStatement.setString(1, DATABASE_NAME);
//                preparedStatement.executeUpdate();
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }

    public Connection getConnection() throws SQLException, ClassNotFoundException {
        String connectionData = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE_NAME;
        Class.forName("com.mysql.jdbc.Driver");

        connection = DriverManager.getConnection(connectionData, LOGIN, PASSWORD);

        return connection;
    }

    public void createTable(String _tableName) {

        tableName = _tableName;
        String request = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                " id INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL nickname VARCHAR(30) NOT NULL , ds_nickname VARCHAR(30), chat_activity INTEGER, game_activity DOUBLE)";

        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(request);
            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public int getId(String nickname) {
        ResultSet resultSet;
        String request = "SELECT id FROM " + tableName + " WHERE nickname = ?";

        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(request);
            preparedStatement.setString(1, nickname);
            resultSet = preparedStatement.executeQuery();

            return resultSet.getInt("id");
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void createPlayer(String nickname) {
        String request = "INSERT INTO " + tableName + " (nickname, chat_activity, game_activity) VALUES (?, 0, 0)";


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
        String request = "SELECT nickname FROM " + tableName + " WHERE nickname = ?";

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
        String request = "UPDATE " + tableName + " SET game_activity = game_activity + ? WHERE nickname = ?";

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
        String request = "UPDATE " + tableName + " SET chat_activity = chat_activity + 1 WHERE nickname = ?";

        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(request);
            preparedStatement.setString(1, nickname);
            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public int getGameActivity(String nickname) {
        ResultSet resultSet;
        String request = "SELECT game_activity FROM " + tableName + " WHERE nickname = ?";

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
        String request = "SELECT chat_activity FROM " + tableName + " WHERE nickname = ?";

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
