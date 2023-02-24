package org.prgrms.kdt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.UUID;

public class JdbcCustomerRepository {

    public static final Logger logger = LoggerFactory.getLogger(JdbcCustomerRepository.class);
    public static void main(String[] args) throws SQLException {
       /* Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "test1234@#");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from customers");

            while (resultSet.next()){
                var name = resultSet.getString("name");
                var customer_id = UUID.nameUUIDFromBytes(resultSet.getBytes("customer_id"));
                logger.info("customer id -> {}, name -> {}", customer_id, name);
            }

        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//            throw throwables;
            logger.error("Got error while closing connection", throwables);
        }finally {
            try {
                if (connection != null) connection.close();
                if (statement != null) connection.close();
                if (resultSet != null) resultSet.close();
            }catch (SQLException exception){
                logger.error("Got error while closing connection", exception);
            }
        }*/
//        try with resource 구문, AutoCloseable 을 구현하고 있는 객체가 와야 함.
        try(
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "test1234@#");
            var statement = connection.createStatement();
            var resultSet = statement.executeQuery("select * from customers");
        ){
            while (resultSet.next()){
                var name = resultSet.getString("name");
                var customerId = UUID.nameUUIDFromBytes(resultSet.getBytes("customer_id"));
                logger.info("customer id -> {}, name -> {}", customerId, name);
            }
        }catch(SQLException throwable){
            logger.error("Got error while closing connection", throwable);
        }
    }
}
