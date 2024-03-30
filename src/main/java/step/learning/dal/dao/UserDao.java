package step.learning.dal.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.dal.dto.User;
import step.learning.services.db.DbService;

import java.sql.*;
import java.util.UUID;

@Singleton
public class UserDao {
    private  final DbService dbService;

    @Inject
    public UserDao(DbService dbService) {
        this.dbService = dbService;
    }

    public String generateToken(User user) {
        String sql = "INSERT INTO Tokens(token_id, user_id,  token_expires) VALUES(?, ?, ?)";
        try ( PreparedStatement prep = dbService.getConnection().prepareStatement(sql) ) {
            String token = UUID.randomUUID().toString();
            prep.setString(1, token);
            prep.setString(2, user.getId().toString());
            prep.setTimestamp(3, new Timestamp(new java.util.Date().getTime() + 60 * 15 * 1000 ) );
            prep.executeUpdate();
            return token;
        }
        catch (SQLException ex) {
            System.err.println(ex.getMessage());
            System.out.println(sql);
        }
        return null;
    }
    public boolean installTable() {
        String sql = "CREATE TABLE Users (" +
                "user_id        CHAR(36)        PRIMARY KEY DEFAULT(UUID())," +
                "user_name      VARCHAR(64)     NOT NULL," +
                "user_email     VARCHAR(128)    NOT NULL," +
                "user_avatar    VARCHAR(64)     NULL," +
                "user_salt      CHAR(32)        NOT NULL," +
                "user_dk        CHAR(32)        NOT NULL," +
                "user_created   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "user_deleted   DATETIME        NULL" +
                ") ENGINE = INNODB, DEFAULT CHARSET = utf8mb4";
        try (Statement statement = dbService.getConnection().createStatement()) {
            statement.executeUpdate(sql);
            return true;
        }
        catch (SQLException ex) {
            System.err.println(ex.getMessage());
            System.out.println(sql);
            return false;
        }
    }
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM users u WHERE u.user_email = ?";
        try (PreparedStatement prep = dbService.getConnection().prepareStatement(sql)) {
            prep.setString(1, email);
            ResultSet res = prep.executeQuery();
            if (res.next()) {
                return User.fromResultSet(res);
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.out.println(sql);
        }
        return null;
    }
    public User getUserByToken(String token) {
        String sql =    "SELECT * " +
                        "FROM Tokens t JOIN Users u ON t.user_id = u.user_id " +
                        "WHERE t.token_id = ? AND t.token_expires > CURRENT_TIMESTAMP " +
                        "LIMIT 1";
        try (PreparedStatement prep = dbService.getConnection().prepareStatement(sql)) {
            prep.setString(1, token);
            ResultSet res = prep.executeQuery();
            if (res.next()) {
                return User.fromResultSet(res);
            }
            else {
                System.out.println(res);
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.out.println(sql);

        }
        return null;
    }
    public boolean registerUser (User user) {
    if(user == null) return false;
    if(user.getId() == null) user.setId(UUID.randomUUID());

    String sql = "INSERT INTO Users " +
            "(user_id, user_name, user_email, user_avatar, user_salt, user_dk)" +
            "VALUES(?,?,?,?,?,?)";
    try(PreparedStatement prep = dbService.getConnection().prepareStatement(sql)) {
        prep.setString(1, user.getId().toString()); //JDBC відлік від 1
        prep.setString(2, user.getName());
        prep.setString(3, user.getEmail());
        prep.setString(4, user.getAvatar());
        prep.setString(5, user.getSalt());
        prep.setString(6, user.getDerivedKey());
        prep.executeUpdate();
        return true;
    }
    catch (SQLException ex) {
        System.err.println(ex.getMessage());
        System.out.println(sql);
        return false;
    }
}
}
