package step.learning.dal.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.dal.dto.Product;
import step.learning.dal.dto.User;
import step.learning.services.db.DbService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Singleton
public class ProductDao {

    private  final DbService dbService;


    @Inject
    public ProductDao(DbService dbService) {
        this.dbService = dbService;
    }

    public List<Product> getList(int skip, int take) {
        // skip, take - сснова пагінації - поділу на сторінки
        List<Product> result = new ArrayList<>();
        String sql = String.format("SELECT * FROM Products LIMIT %d, %d", skip, take);

        try(Statement statement = dbService.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()) {
                result.add(new Product(resultSet) );
            }
        }
        catch (SQLException ex) {
            System.err.println(ex.getMessage());
            System.out.println(sql);
        }
        return result;
    }

    public boolean add( Product product ) {
        if( product == null ) return false ;
        if( product.getId() == null ) product.setId( UUID.randomUUID() );

        String sql = "INSERT INTO Products" +
                "(product_id,product_name,product_price,product_description,product_image ) " +
                "VALUES(?,?,?,?,?)";
        try( PreparedStatement prep = dbService.getConnection().prepareStatement(sql) ) {
            prep.setString( 1, product.getId().toString() );   // у JDBC відлік від 1
            prep.setString( 2, product.getName() );
            prep.setDouble( 3, product.getPrice() );
            prep.setString( 4, product.getDescription() );
            prep.setString( 5, product.getImage() );
            prep.executeUpdate();
            return true;
        }
        catch (SQLException ex) {
            System.err.println( ex.getMessage() );
            System.out.println( sql );
            return false ;
        }
    }

    public Product getProductById(String productId) {
        Product product = new Product();

        String sql = String.format("SELECT * FROM products WHERE product_id = '%s'", productId);

        try(Statement statement = dbService.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                product = new Product(resultSet);
            }
        }
        catch (SQLException ex) {
            System.err.println(ex.getMessage());
            System.out.println(sql);
        }

        return product;
    }
}
