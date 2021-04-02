package com.playernguyen.pndb.sql;

import com.playernguyen.pndb.sql.adapt.DatabaseAdaptor;
import com.playernguyen.pndb.sql.adapt.DatabaseAdaptorBuilder;
import com.playernguyen.pndb.sql.hoster.DatabaseHoster;
import com.playernguyen.pndb.sql.hoster.DatabaseHosterBuilder;
import com.playernguyen.pndb.sql.mysql.DatabaseHosterMySQL;
import com.playernguyen.pndb.sql.mysql.DatabaseOptionsMySQL;
import com.playernguyen.pndb.sql.query.DatabaseQueryBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SQLDatabaseTest {

    DatabaseOptions options = new DatabaseOptionsMySQL(
            "localhost",
            "3306",
            "root",
            "123",
            "opteco",
            "useSSL=false"
    );
    DatabaseHoster hoster = new DatabaseHosterMySQL(options);

    List<String> randomProductList = Arrays.asList("car", "rover", "computer", "intel core i3", "intel 14ghz", "range rover"
            , "iPhone", "iPad", "Airpods Pro");

    @Test
    public void openConnectionTest() throws SQLException {
        try (Connection connection = hoster.connection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `opteco`");
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();
            Assertions.assertEquals(1, resultSet.getObject(1));
        }
    }

    @Test
    public void createNewTable() throws SQLException {

        try (Connection connection = hoster.connection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `product` (" +
                    "`id` INTEGER(11) NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "`product_name` VARCHAR(255) NOT NULL, " +
                    "`price` VARCHAR(255) NOT NULL" +
                    ")");

            preparedStatement.executeUpdate();

            // generate data set
            for (int i = 0; i < 10; i++) {
                PreparedStatement preparedStatement1 = connection.prepareStatement("INSERT INTO `product` (`product_name`, `price`) VALUES(?, ?)");
                preparedStatement1.setString(1, randomProductList.get(new Random().nextInt(randomProductList.size())));
                preparedStatement1.setInt(2, new Random().nextInt(10000000));
                Assertions.assertEquals(1, preparedStatement1.executeUpdate());

            }
        }
    }

    @Test
    public void createHostByBuilder() throws SQLException {
        DatabaseHoster databaseHoster = DatabaseHosterBuilder.newInstance().options(options).buildMySQL();
        DatabaseAdaptor adaptor = DatabaseAdaptorBuilder.newInstance().hoster(databaseHoster).build();

        try (PreparedStatement preparedStatement = adaptor.prepareStatement("SELECT * FROM `product` WHERE product_name=?", "car")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                System.out.println("resultSet.getObject(1) = " + resultSet.getObject(1));
                System.out.println("resultSet.getObject(2) = " + resultSet.getObject(2));
            }
        }
    }

    @Test
    public void queryBuilderTest() throws SQLException {
        try {
            ResultSet resultSet = DatabaseQueryBuilder.newInstance(hoster)
                    .select()
                    .table("product")
                    .where(new DatabaseQueryBuilder.CriteriaBuilder()
                                .of("id")
                                .and()
                                .of("product_name")
                                .build()
                    )
                    .executeQuery("9", "car");

            while (resultSet.next()) {
                System.out.println(resultSet.getObject(1));
                System.out.println(resultSet.getObject(2));
                System.out.println(resultSet.getObject(3));
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

}
