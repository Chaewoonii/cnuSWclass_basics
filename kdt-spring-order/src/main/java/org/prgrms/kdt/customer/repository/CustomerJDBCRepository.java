package org.prgrms.kdt.customer.repository;

import org.prgrms.kdt.customer.domain.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.nio.ByteBuffer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

//@Repository
public class CustomerJDBCRepository implements CustomerRepository {

    private static final Logger logger = LoggerFactory.getLogger(CustomerJDBCRepository.class);
    private final DataSource dataSource;

    public CustomerJDBCRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    static UUID toUUID(byte[] bytes){
        //nameUUIDFromBytes 가 UUID 3이기 때문에 아래와 같이 변경.
        var byteBuffer = ByteBuffer.wrap(bytes);
        return new UUID(byteBuffer.getLong(), byteBuffer.getLong()); //64비트씩 쪼개서 가져와 넣어준다.
    }


    private static void mapToCustomer(List<Customer> allCustomers, ResultSet resultSet) throws SQLException {
        var customerName = resultSet.getString("name");
        var email  =resultSet.getString("email");
        var customerId = toUUID(resultSet.getBytes("customer_id"));
        var lastLoginAt = resultSet.getTimestamp("last_login_at") != null?
                resultSet.getTimestamp("last_login_at").toLocalDateTime() : null;
        var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
        allCustomers.add(new Customer(customerId, customerName, email, lastLoginAt, createdAt));
    }


    @Override
    public Customer insert(Customer customer) {
        List<String> names = new ArrayList<>();
        try (
                var connection = dataSource.getConnection();
                var statement = connection.prepareStatement("INSERT INTO customers(customer_id, name, email, created_at) VALUES (UUID_TO_BIN(?), ?, ?, ?)");
        ){
            statement.setBytes(1, customer.getCustomerId().toString().getBytes());
            statement.setString(2, customer.getName());
            statement.setString(3, customer.getEmail());
            statement.setTimestamp(4, Timestamp.valueOf(customer.getCreatedAt()));
            var executeUpdate = statement.executeUpdate();
            if (executeUpdate != 1){
                throw new RuntimeException("Nothing was inserted");
            }
            return customer;
        }catch (SQLException throwable){
            logger.error("Got error while closing connection", throwable);
        }
        return customer;
    }

    @Override
    public Customer update(Customer customer) {
        try (
                var connection = dataSource.getConnection();
                var statement = connection.prepareStatement("UPDATE customers SET name = ?, email = ?, last_login_at = ? WHERE customer_id = UUID_TO_BIN(?)");
        ){
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getEmail());
            statement.setTimestamp(3, customer.getLastLoginAt() != null ? Timestamp.valueOf(customer.getLastLoginAt()) : null);
            statement.setBytes(4, customer.getCustomerId().toString().getBytes());
            var executeUpdate = statement.executeUpdate();
            if (executeUpdate != 1){
                throw new RuntimeException("Nothing was updated");
            }
            return customer;
        }catch (SQLException throwable){
            logger.error("Got error while closing connection", throwable);
            throw new RuntimeException(throwable);
        }
    }

    @Override
    public int count() {
        return 0;
    }

    public int deleteAllCustomers(){
        try (
                var connection = dataSource.getConnection();
                var statement = connection.prepareStatement("DELETE FROM customers");
        ){
            return statement.executeUpdate();
        }catch (SQLException throwable){
            logger.error("Got error while closing connection", throwable);
        }
        return 0;
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
                mapToCustomer(allCustomers, resultSet);

            }
        }catch(SQLException throwable){
            logger.error("Got error while closing connection", throwable);
            throw new RuntimeException(throwable);
        }

        return allCustomers;
    }

    @Override
    public Optional<Customer> findById(UUID customerId) {
        List<Customer> allCustomers = new ArrayList<>();
        try(
                var connection = dataSource.getConnection();
                var statement = connection.prepareStatement("SELECT * FROM customers WHERE customer_id = UUID_TO_BIN(?)");
        ){
            statement.setBytes(1, customerId.toString().getBytes());

            try(var resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    mapToCustomer(allCustomers, resultSet);
                }
            }
        }catch(SQLException throwable){
            logger.error("Got error while closing connection", throwable);
        }

        return allCustomers.stream().findFirst();
    }

    @Override
    public Optional<Customer> findByName(String name) {
        List<Customer> allCustomers = new ArrayList<>();
        try(
                var connection = dataSource.getConnection();
                var statement = connection.prepareStatement("SELECT * FROM customers WHERE name = ?");
        ){
            statement.setString(1, name);

            try(var resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    mapToCustomer(allCustomers, resultSet);
                }
            }
        }catch(SQLException throwable){
            logger.error("Got error while closing connection", throwable);
        }

        return allCustomers.stream().findFirst();
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        List<Customer> allCustomers = new ArrayList<>();
        try(
                var connection = dataSource.getConnection();
                var statement = connection.prepareStatement("SELECT * FROM customers WHERE email = ?");
        ){
            statement.setString(1, email);

            try(var resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    mapToCustomer(allCustomers, resultSet);
                }
            }
        }catch(SQLException throwable){
            logger.error("Got error while closing connection", throwable);
        }

        return allCustomers.stream().findFirst();
    }

    @Override
    public void deleteAll() {
        try (
                var connection = dataSource.getConnection();
                var statement = connection.prepareStatement("DELETE FROM customers");
        ){
            statement.executeUpdate();
        }catch (SQLException throwable){
            logger.error("Got error while closing connection", throwable);
            throw new RuntimeException(throwable);
        }
    }
}
