package com.playernguyen.pndb.sql;

import com.playernguyen.pndb.sql.adapt.DatabaseAdaptor;
import com.playernguyen.pndb.sql.adapt.DatabaseAdaptorBuilder;
import com.playernguyen.pndb.sql.hoster.DatabaseHoster;
import com.playernguyen.pndb.sql.hoster.DatabaseHosterBuilder;
import com.playernguyen.pndb.sql.mysql.DatabaseHosterMySQL;
import com.playernguyen.pndb.sql.mysql.DatabaseOptionsMySQL;
import com.playernguyen.pndb.sql.query.*;
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

    List<String> randomProductList = Arrays.asList("car",
            "rover",
            "computer",
            "intel core i3",
            "intel 14ghz",
            "range rover"
            , "iPhone",
            "iPad",
            "Airpods Pro"
    );

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
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS `product` (" +
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
    public void criteriaBuilderTest() {
        // AND logic
        // In pair
        Assertions.assertEquals("name=? AND uuid=?", new CriteriaBuilder().and(CriteriaField.equal("name"), CriteriaField.equal("uuid")).build());
        // In triage
        Assertions.assertEquals("uuid<? AND password>? AND address=?",
                new CriteriaBuilder().and(CriteriaField.lessThan("uuid"), CriteriaField.moreThan("password"), CriteriaField.equal("address")).build());

        // OR logic
        // Assertions.assertEquals("uuid=? OR username=?", new CriteriaBuilder().or("uuid", "username").build());

    }

    @Test
    public void selectAProduct() throws SQLException {
        try (PreparedStatement statement = new DatabaseQueryBuilder(hoster)
                .selectAll("product")
                .criteria(CriteriaBuilder.newInstance().and(CriteriaField.moreThan("id"), CriteriaField.lessThanOrEqual("id")))
                .buildStatement(1, 5)) {
            ResultSet resultSet = statement.executeQuery();
            // iterate
            while (resultSet.next()) {
                System.out.println(resultSet.getObject("product_name"));
            }
        }
    }

    @Test
    public void createOrder() throws SQLException {
        ResultSet resultSet = DatabaseQueryBuilder
                .newInstance(hoster)
                .select("product")
                .criteria(
                        CriteriaBuilder.newInstance().newField(CriteriaField.lessThanOrEqual("id"))
                )
                .order(
                        OrderField.createField("id", OrderFieldType.DESC)
                )
                .buildStatement(9)
                .executeQuery();

        while (resultSet.next()) {
            System.out.println(resultSet.getObject(1));
            System.out.println(resultSet.getObject(2));
        }
    }

    @Test
    public void queryBuilderTest() throws SQLException {
        Assertions.assertEquals("SELECT * FROM product WHERE id=?",
                DatabaseQueryBuilder
                        .newInstance(hoster)
                        .select("product")
                        .criteria(CriteriaBuilder.newInstance().newField(CriteriaField.equal("id")))
                        .build()
        );

        Assertions.assertEquals("SELECT * FROM product WHERE id>=? AND id<=?",
                DatabaseQueryBuilder
                        .newInstance(hoster)
                        .select("product")
                        .criteria(CriteriaBuilder.newInstance().and(CriteriaField.moreThanOrEqual("id"), CriteriaField.lessThanOrEqual("id")))
                        .build()
        );

    }

    @Test
    public void combineQueryAndSQL() throws SQLException {
        PreparedStatement preparedStatement = DatabaseQueryBuilder
                .newInstance(hoster)
                .select("product")
                .criteria(CriteriaBuilder.newInstance().and(CriteriaField.moreThanOrEqual("id"), CriteriaField.lessThanOrEqual("id")))
                .buildStatement(1, 9);
        ResultSet resultSet = preparedStatement.executeQuery();

        System.out.print("-------\n");
        int i = 0;
        while (resultSet.next()) {
            System.out.printf("%s\t%20s\t%10s%n",
                    resultSet.getObject("id"),
                    resultSet.getObject("product_name"),
                    resultSet.getObject("price"));
            i++;
        }

        Assertions.assertEquals(9, i);
    }

    @Test
    public void insertData() throws SQLException {
        DatabaseQueryBuilder.newInstance(hoster)
                .insert("product")
                .values("product_name", "price")
                .execUpdate("Lamborghini", "120000");

        ResultSet resultSet = DatabaseQueryBuilder.newInstance(hoster)
                .select("product")
                .criteria(CriteriaBuilder.newInstance().newField(
                        CriteriaField.equal("product_name")
                )).execQuery("Lamborghini");
        while (resultSet.next()) {
            System.out.println(resultSet.getObject(2));
        }
    }

    @Test
    public void onCallWhileIterating() throws Exception {
        // Manual way
        Connection connection = hoster.connection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `product`");
        // preparedStatement.setObject(1, "Lamborghini");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            System.out.println(resultSet.getObject("id") + " ~ " + resultSet.getObject("product_name"));
        }

        // pndb way
        DatabaseQueryBuilder.newInstance(hoster)
                .select("product")
                .callIterateQuery(
                        item -> {
                            System.out.println(
                                    item.getObject("id") + " ~ " + item.getObject("product_name")
                            );
                        },
                        "Lamborghini"
                );
    }

}
