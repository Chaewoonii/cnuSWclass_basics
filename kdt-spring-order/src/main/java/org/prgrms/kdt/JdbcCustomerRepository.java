package org.prgrms.kdt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcCustomerRepository {

    public static final Logger logger = LoggerFactory.getLogger(JdbcCustomerRepository.class);
    public static void main(String[] args) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "test1234@#");
            statement = connection.createStatement();
            var resultset = statement.executeQuery("select * from customers");
        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//            throw throwables;
            logger.error("Got error while closing connection", throwables);
        }finally {
            try {
                if (connection != null) connection.close();
                if (statement != null) connection.close();
            }catch (SQLException exception){
                logger.error("Got error while closing connection", exception);
            }
        }
    }
}
