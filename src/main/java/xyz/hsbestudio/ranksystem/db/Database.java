package xyz.hsbestudio.ranksystem.db;


import xyz.hsbestudio.ranksystem.RankSystem;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;

public class Database extends Config {

    private static final Logger LOGGER = Logger.getLogger(RankSystem.class.getName());

    private String tableName;
    Connection connection;

    public Database() {
        //boolean isNaveDb = RankSystem.getInstance().getConfig().getBoolean("hasDb");
        tableName = RankSystem.getInstance().getConfig().getString("tableName");
        String request = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "nickname VARCHAR(32) NOT NULL PRIMARY KEY, uuid VARCHAR(128) NOT NULL, ds_id VARCHAR(70), chat_activity DOUBLE, game_activity DOUBLE)";

        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(request);
            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private Connection getConnection() throws SQLException, ClassNotFoundException {
        String connectionData = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE_NAME;
        Class.forName("com.mysql.jdbc.Driver");

        connection = DriverManager.getConnection(connectionData, LOGIN, PASSWORD);

        return connection;
    }

    public String getDiscordId(String nickname) {
        ResultSet resultSet;
        String request = "SELECT ds_id FROM " + tableName + " WHERE nickname = ?";

        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(request);
            preparedStatement.setString(1, nickname);
            resultSet = preparedStatement.executeQuery();

            return resultSet.getString("ds_id");
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private String hashString(String string) {

        byte[] hash = new byte[0];
        try {
            hash = MessageDigest.getInstance("SHA-256").digest(string.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('x');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public boolean isRegistered(UUID uuid) {
        ResultSet resultSet;
        String request = "SELECT ds_id FROM " + tableName + " WHERE uuid = ?";

        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(request);
            preparedStatement.setString(1, uuid.toString());
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String id = resultSet.getString("ds_id");
                if(!Objects.equals(id, null)) {
                    return id.startsWith("id_");
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public String generateDiscordHash(String nickname) {
        String request = "UPDATE players SET ds_id = ? WHERE nickname = ?";
        String hash = hashString(nickname);
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(request);
            preparedStatement.setString(1, hash);
            preparedStatement.setString(2, nickname);
            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return hash;
    }


    public void createPlayer(String nickname, UUID uuid) {
        String request = "INSERT INTO " + tableName + " (nickname, uuid, chat_activity, game_activity) VALUES (?, ?, 0, 0)";

        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(request);
            preparedStatement.setString(1, nickname);
            preparedStatement.setString(2, uuid.toString());
            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isPlayerInDatabase(String nickname) {
        ResultSet resultSet;
        String request = "SELECT nickname FROM " + tableName + " WHERE nickname = ?";

        try {
            try(PreparedStatement preparedStatement = getConnection().prepareStatement(request)) {
                preparedStatement.setString(1, nickname);
                resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    return Objects.equals(resultSet.getString("nickname"), nickname);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public void addGameActivity(String nickname, Double value) {
        String request = "UPDATE " + tableName + " SET game_activity = game_activity + ? WHERE nickname = ?";

        try {
            try(PreparedStatement preparedStatement = getConnection().prepareStatement(request)) {
                preparedStatement.setDouble(1, value / 20);
                preparedStatement.setString(2, nickname);
                preparedStatement.executeUpdate();
            }
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
