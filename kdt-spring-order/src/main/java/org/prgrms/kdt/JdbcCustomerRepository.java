package org.prgrms.kdt;

import org.prgrms.kdt.customer.domain.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JdbcCustomerRepository {

    public static final Logger logger = LoggerFactory.getLogger(JdbcCustomerRepository.class);
    private final String SELECT_BY_NAME_SQL = "SELECT * FROM customers WHERE name = ?";
    private final String SELECT_ALL_SQL = "SELECT * FROM customers";
    private final String INSERT_SQL = "INSERT INTO customers(customer_id, name, email) VALUES (UUID_TO_BIN(?), ?, ?)";
    private final String DELETE_ALL_SQL = "DELETE FROM customers";
    private final String UPDATE_SQL = "UPDATE customers SET name = ? WHERE customer_id = UUID_TO_BIN(?)";

    public void transactionTest(Customer customer){
        String updateNameSql = "UPDATE customers SET name = ? WHERE customer_id = UUID_TO_BIN(?)";
        String updateEmailSql = "UPDATE customers SET email = ? WHERE customer_id = UUID_TO_BIN(?)";

        Connection connection = null;
        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "test1234@#");

            try (

                    var updateNameSatement = connection.prepareStatement(updateNameSql);
                    var updateEmailSatement = connection.prepareStatement(updateEmailSql);
            ){
                connection.setAutoCommit(false); //트랜잭션 묶음 처리

                updateNameSatement.setString(1, customer.getName());
                updateNameSatement.setBytes(2, customer.getCustomerId().toString().getBytes());
                updateNameSatement.executeUpdate();

                updateEmailSatement.setString(1, customer.getEmail());
                updateEmailSatement.setBytes(2, customer.getCustomerId().toString().getBytes());
                updateEmailSatement.executeUpdate();

                connection.setAutoCommit(true); // 트랜잭션이 끝나면 commit
            }
        } catch (SQLException exception) {
            if (connection != null){
                try{
                    connection.rollback(); //예외 발생 시 rollback 하고 connection 종료.
                    connection.close();
                }catch (SQLException throwable){
                    logger.error("Got error while closing connection", throwable);
                    throw new RuntimeException(exception);
                }
            }
            logger.error("Got error while closing connection", exception);
            throw new RuntimeException(exception);
        }


    }
    static UUID toUUID(byte[] bytes){
        //nameUUIDFromBytes 가 UUID 3이기 때문에 아래와 같이 변경.
        var byteBuffer = ByteBuffer.wrap(bytes);
        return new UUID(byteBuffer.getLong(), byteBuffer.getLong()); //64비트씩 쪼개서 가져와 넣어준다.
    }

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
        /*
        var SELECT_SQL = "SELECT * FROM customers WHERE name = %s".formatted(name);
        List<String> names = new ArrayList<>();
        try(
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "test1234@#");
            var statement = connection.createStatement(SELECT_SQL);
            var resultSet = statement.executeQuery(SELECT_SQL);
        ){
            while (resultSet.next()){
                var customerName = resultSet.getString("name");
                var customerId = UUID.nameUUIDFromBytes(resultSet.getBytes("customer_id"));
                var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
                logger.info("customer id -> {}, customerName -> {}, createdAt -> {}", customerId, customerName, createdAt);
                names.add(customerName);
            }
        }catch(SQLException throwable){
            logger.error("Got error while closing connection", throwable);
        }*/

    //        prepared statement
//          - Auto Closeable 이 쓰이면 안됨. -> resultSet 을 따로 빼준다.
//          - statement.setString(순번, 변수)로 파라미터 ? 의 순서를 정해준다.

    public List<String> findNames(String name){
        List<String> names = new ArrayList<>();
        try(
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "test1234@#");
                var statement = connection.prepareStatement(SELECT_BY_NAME_SQL);
        ){
            statement.setString(1, name);
            logger.info("statement -> {}", statement);
            try(var resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    var customerName = resultSet.getString("name");
//                    var customerId = UUID.nameUUIDFromBytes(resultSet.getBytes("customer_id"));
                    var customerId = toUUID(resultSet.getBytes("customer_id"));
                    var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
                    logger.info("customer id -> {}, customerName -> {}, createdAt -> {}", customerId, customerName, createdAt);
                    names.add(customerName);
                }
            }
        }catch(SQLException throwable){
            logger.error("Got error while closing connection", throwable);
        }

        return names;
    }

    public List<String> findAllName(){
        List<String> names = new ArrayList<>();
        try(
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "test1234@#");
                var statement = connection.prepareStatement(SELECT_ALL_SQL);
        ){
            try(var resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    var customerName = resultSet.getString("name");
                    var customerId = UUID.nameUUIDFromBytes(resultSet.getBytes("customer_id"));
                    var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
//                    logger.info("customer id -> {}, customerName -> {}, createdAt -> {}", customerId, customerName, createdAt);
                    names.add(customerName);
                }
            }
        }catch(SQLException throwable){
            logger.error("Got error while closing connection", throwable);
        }

        return names;
    }

    public List<UUID> findAllIds(){
        List<UUID> uuids = new ArrayList<>();
        try(
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "test1234@#");
                var statement = connection.prepareStatement(SELECT_ALL_SQL);
        ){
            try(var resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    var customerId = toUUID(resultSet.getBytes("customer_id"));
//                    logger.info("customer id -> {}, customerName -> {}, createdAt -> {}", customerId, customerName, createdAt);
                    uuids.add(customerId);
                }
            }
        }catch(SQLException throwable){
            logger.error("Got error while closing connection", throwable);
        }

        return uuids;
    }

    public int instertCustomer(UUID customerId, String name, String email){
        List<String> names = new ArrayList<>();
        try (
                var connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "test1234@#");
                var statement = connection.prepareStatement(INSERT_SQL);
                ){
            statement.setBytes(1, customerId.toString().getBytes());
            statement.setString(2, name);
            statement.setString(3, email);
            return statement.executeUpdate();
        }catch (SQLException throwable){
            logger.error("Got error while closing connection", throwable);
        }
        return 0;
    }

    public int updateCustomerName(UUID customerId, String name){
        try (
                var connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "test1234@#");
                var statement = connection.prepareStatement(UPDATE_SQL);
        ){
            statement.setString(1, name);
            statement.setBytes(2, customerId.toString().getBytes());
            return statement.executeUpdate();
        }catch (SQLException throwable){
            logger.error("Got error while closing connection", throwable);
        }
        return 0;
    }
    public int deleteAllCustomers(){
        try (
                var connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "test1234@#");
                var statement = connection.prepareStatement(DELETE_ALL_SQL);
        ){
            return statement.executeUpdate();
        }catch (SQLException throwable){
            logger.error("Got error while closing connection", throwable);
        }
        return 0;
    }


    public static void main(String[] args) {
        var customerRepository = new JdbcCustomerRepository();
/*//        var names = new JdbcCustomerRepository().findNames("tester01' OR 'a'='a");
        //SQL INJECTION: 전제 유저 정보 출력됨. ->방지: prepared statement
         매번 실행할 때마다 쿼리 분석, 컴파일, 실행 세 단계를 모두 거침
        prepared statement
            - SQL 문 고정(Dynamic Query x)
            - 처음 한 번만 세 단계로 실행되고 이후엔 캐시에 담아서 실행됨. 즉, 처음의 쿼리문이 고정딤. SQL injection 방지.
            - 처음 한 번만 세 단계로 실행되기 때문에 성능상으로도 이점이 있음.
            - tester01 이후의 OR 절이 시행되지 않는다. "tester01' OR 'a'='a" 라는 이름의 유저를 찾음.

//        var names = new JdbcCustomerRepository().findNames("tester01");
//        names.forEach(v -> logger.info("Found name: {}", v));

        var customerRepository = new JdbcCustomerRepository();

//        delete
        var count = customerRepository.deleteAllCustomers();
        logger.info("deleted count -> {}", count);

//        insert
        customerRepository.instertCustomer(UUID.randomUUID(), "new-user", "new_user@gmail.com");

        var customerId = UUID.randomUUID();
        logger.info("created Id -> {}", customerId); //2fcd04d9-3ddb-490a-972b-26ba692fe1af
        logger.info("created UUID version -> {}", customerId.version()); // 4
        customerRepository.instertCustomer(customerId, "new-user02", "new_user02@gmail.com");

//        select
//        customerRepository.findAllName().forEach(v -> logger.info("Found name: {}", v));
//        update
//        customerRepository.updateCustomerName(customer2, "updated-user2");


        customerRepository.findAllIds().forEach(v -> logger.info("Found customerId: {} and version: {}", v, v.version())); //849cbced-943f-3068-bc58-9a550aa41a18, 3
//        SELECT BIN_TO_UUID(customer_id) from customers; #2fcd04d9-3ddb-490a-972b-26ba692fe1af
//        값은 맞게 들어갔는데 조회가 이상하게 됨.
//        만들땐 UUID 4, 조회할땐 UUID 3이라 값이 다름.*/
        /*
        - transaction 원자성 test. 기존에 존재하는 중복된 email 정보를 넣어준다. (update 하려는 email 이 기존에 존재하는 다른 user 의 email 과 중복됨.)
        - user의 이름이 update-user로 변경되면 안됨.
        - name과 email을 update하는 것이 하나의 transaction이기 때문
        >> 이름이 update됨. 하나의 transaction으로 처리되지 않았다.

        - connection.setAutoCommit(false), 트랜잭션 종료 후 connection.setAutoCommit(true)
        - 예외 부분에 connection.rollback(); connection.close();
        */
        customerRepository.transactionTest(new Customer(UUID.fromString("952e2962-26c2-4489-b407-40dfe339bb3b"), "update-user", "new_user@gmail.com", LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS)));

    }
}
