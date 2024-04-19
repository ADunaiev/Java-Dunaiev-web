package step.learning.dal.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import step.learning.dal.dto.CartItem;
import step.learning.dal.dto.Product;
import step.learning.dal.dto.User;
import step.learning.services.db.DbService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@Singleton
public class CartDao {
    private final DbService dbService;

    @Inject
    public CartDao(DbService dbService) {
        this.dbService = dbService;
    }

    public List<CartItem> getCart() {

        List<CartItem> result = new ArrayList<>();

        String sql = "SELECT * FROM carts_details" +
                     " LEFT JOIN carts ON carts_details.cart_id = carts.cart_id" +
                     " WHERE carts.cart_status = 0";

        try(Statement statement = dbService.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                result.add(new CartItem(resultSet));
            }
         }
        catch (SQLException ex) {
            System.err.println(ex.getMessage());
            System.out.println(sql);
        }

        return result;
        /*
        return Arrays.asList( new CartItem[]{
                new CartItem(UUID.randomUUID(), UUID.randomUUID(), 1),
                new CartItem(UUID.randomUUID(), UUID.randomUUID(), 2),
                new CartItem(UUID.randomUUID(), UUID.randomUUID(), 3),
        });*/
    }

    public void add(String userId, String productId, int cnt) {
        // Шукаємо чи є користувача відкритий кошик, якщо не має то створюємо
        // перевіраємо чи є кошику такий товар, якщо є, то збільшуємо кількість,
        // якщо немає то створюємо (додаємо)

        String sql = String.format("SELECT cart_id FROM carts WHERE cart_user = '%s' AND cart_status=0",
                                    userId);
        try(Statement statement = dbService.getConnection().createStatement()) {
            String cartId = null;
            ResultSet res = statement.executeQuery(sql);

            if(res.next()) { // є відкритий кошик
               cartId = res.getString(1);
            }
            else {   // немає відкритого кошика
                cartId = UUID.randomUUID().toString();

                sql = String.format(
                "INSERT INTO carts (cart_id, cart_user, cart_date, cart_status) VALUES(" +
                        "'%s', '%s', CURRENT_TIMESTAMP, 0)",
                        cartId, userId);
                statement.executeUpdate(sql);
            }
            // cartId - id кошику чи старого чи нового
            // перевіряємо чи є у кошику такий товар
            sql = String.format("SELECT cart_dt_cnt FROM carts_details WHERE cart_id = '%s' AND product_id = '%s'",
                    cartId, productId);

            res = statement.executeQuery(sql);

            if(res.next()) { // є такий товар
                cnt += res.getInt(1);
                sql = String.format("UPDATE carts_details SET cart_dt_cnt = %d WHERE cart_id = '%s' AND " +
                        "product_id = '%s'", cnt, cartId, productId);
            }
            else {
                sql = String.format("INSERT INTO carts_details (cart_dt_id, cart_id, product_id, cart_dt_cnt) VALUES " +
                        "(UUID(), '%s', '%s', %d )", cartId, productId, cnt);
            }
            statement.executeUpdate(sql);
        }
        catch (SQLException ex) {
            System.err.println(ex.getMessage());
            System.out.println(sql);

        }
    }
}
