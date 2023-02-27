package org.prgrms.kdt.customer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CustomerJDBCRepository implements CustomerRepository {

    private static final Logger logger = LoggerFactory.getLogger(CustomerJDBCRepository.class);
    private final DataSource dataSource;

    public CustomerJDBCRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public Customer insult(Customer customer) {
        return null;
    }

    @Override
    public Customer update(Customer customer) {
        return null;
    }

    @Override
    public List<Customer> findAll() {
        List<Customer> allCustomers = new ArrayList<>();
        try(
                var connection = dataSource.getConnection();
                var statement = connection.prepareStatement("SELECT * FROM customers");
                var resultSet = statement.executeQuery()
        ){
            while (resultSet.next()){
                var customerName = resultSet.getString("name");
                var email = resultSet.getString("email");
                var customerId = UUID.nameUUIDFromBytes(resultSet.getBytes("customer_id"));
                var lastLoginAt = resultSet.getTimestamp("last_login_at") != null ?
                        resultSet.getTimestamp("last_login_at").toLocalDateTime() : null;
                var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();

                allCustomers.add(new Customer(customerId, customerName, email, lastLoginAt, createdAt));

            }
        }catch(SQLException throwable){
            logger.error("Got error while closing connection", throwable);
            throw new RuntimeException(throwable);
        }

        return allCustomers;
    }

    @Override
    public Optional<Customer> findById(UUID customerId) {
        return Optional.empty();
    }

    @Override
    public Optional<Customer> findByName(UUID customerId) {
        return Optional.empty();
    }

    @Override
    public Optional<Customer> findByEmail(UUID customerId) {
        return Optional.empty();
    }

    @Override
    public void deleteAll() {

    }
}
