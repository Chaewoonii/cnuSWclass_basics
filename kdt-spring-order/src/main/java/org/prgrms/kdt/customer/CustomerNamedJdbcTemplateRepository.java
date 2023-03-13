package org.prgrms.kdt.customer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.nio.ByteBuffer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

//JdbcTemplate을 이용
@Repository
@Primary
public class CustomerNamedJdbcTemplateRepository implements CustomerRepository {

    private static final Logger logger = LoggerFactory.getLogger(CustomerNamedJdbcTemplateRepository.class);

    //sql 파라미터 ?를 인덱스 기반(1, 2, 3) 에서 이름기반으로 설정할 수 있도록 하는 jdbc Template
    //NamedParameterJdbcTemplate 이 JdbcTemplate 을 들고있고 Named~ 가 이름 설정할 수 있도록 함.
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

//    private final PlatformTransactionManager transactionManager;

//    transactionManager를 간단하게 쓸 수 있도록 하는 template
//    private final TransactionTemplate transactionTemplate;
//     >>>>@Transactional 로 관리.

    public CustomerNamedJdbcTemplateRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    static UUID toUUID(byte[] bytes) {
        //nameUUIDFromBytes 가 UUID 3이기 때문에 아래와 같이 변경.
        var byteBuffer = ByteBuffer.wrap(bytes);
        return new UUID(byteBuffer.getLong(), byteBuffer.getLong()); //64비트씩 쪼개서 가져와 넣어준다.
    }

    private static final RowMapper<Customer> customerRowMapper = (resultSet, i) -> {
        var customerName = resultSet.getString("name");
        var customerEmail = resultSet.getString("email");
        var customerId = toUUID(resultSet.getBytes("customer_id"));
        var lastLoginAt = resultSet.getTimestamp("last_login_at") != null ?
                resultSet.getTimestamp("last_login_at").toLocalDateTime() : null;
        var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
        return new Customer(customerId, customerName, customerEmail, lastLoginAt, createdAt);
    };

    private Map<String, Object> toParamMap(Customer customer){
        return new HashMap<>(){{
            put("customerId", customer.getCustomerId().toString().getBytes());
            put("name", customer.getName());
            put("email", customer.getEmail());
            put("createdAt",Timestamp.valueOf(customer.getCreatedAt()));
            put("lastLoginAt", customer.getLastLoginAt() != null ? Timestamp.valueOf(customer.getLastLoginAt()) : null);
        }};
    }

    private void mapToCustomer(List<Customer> allCustomers, ResultSet resultSet) throws SQLException {
        var customerName = resultSet.getString("name");
        var email = resultSet.getString("email");
        var customerId = toUUID(resultSet.getBytes("customer_id"));
        var lastLoginAt = resultSet.getTimestamp("last_login_at") != null ?
                resultSet.getTimestamp("last_login_at").toLocalDateTime() : null;
        var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
        allCustomers.add(new Customer(customerId, customerName, email, lastLoginAt, createdAt));
    }


    @Override
    public Customer insert(Customer customer) {
/*        var paramMap = new HashMap<String, Object>(){{
            put("customerId", customer.getCustomerId().toString().getBytes());
            put("name", customer.getName());
            put("email", customer.getEmail());
            put("createdAt",Timestamp.valueOf(customer.getCreatedAt()));
            put("lastLoginAt", customer.getLastLoginAt() != null ? Timestamp.valueOf(customer.getLastLoginAt()) : null);
        }};*/
        var update = namedParameterJdbcTemplate
                .update("INSERT INTO customers(customer_id, name, email, created_at) VALUES (UUID_TO_BIN(:customerId), :name, :email, :createdAt)",
                toParamMap(customer));
        if (update != 1) {
            throw new RuntimeException("Nothing was inserted");
        }
        return customer;

    }

    @Override
    public Customer update(Customer customer) {
        /*var paramMap = new HashMap<String, Object>(){{
            put("customerId", customer.getCustomerId().toString().getBytes());
            put("name", customer.getName());
            put("email", customer.getEmail());
            put("createdAt",Timestamp.valueOf(customer.getCreatedAt()));
            put("lastLoginAt", customer.getLastLoginAt() != null ? Timestamp.valueOf(customer.getLastLoginAt()) : null);
        }};*/
        var update = namedParameterJdbcTemplate
                .update("UPDATE customers SET name = :name, email = :email, created_at = :createdAt, last_login_at = :lastLoginAt WHERE customer_id = UUID_TO_BIN(:customerId)",
                        toParamMap(customer));

        if (update != 1) {
            throw new RuntimeException("Nothing was updated");
        }
        return customer;
    }

    @Override
    public int count() {
        return namedParameterJdbcTemplate.queryForObject("select count(*) from customers",
                Collections.emptyMap(),
                Integer.class);
    }

    @Override
    public void deleteAll() {
//        namedParameterJdbcTemplate.update("DELETE FROM customers", Collections.emptyMap());
        namedParameterJdbcTemplate.getJdbcTemplate().update("DELETE FROM customers");
    }

    @Override
    public List<Customer> findAll() {
        return namedParameterJdbcTemplate.query("SELECT * FROM customers", customerRowMapper);
    }

    @Override
    public Optional<Customer> findById(UUID customerId) {
        try {
            return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject("select * from customers where customer_id = UUID_TO_BIN(:customerId)",
                    Collections.singletonMap("customerId", customerId.toString().getBytes()),
                    customerRowMapper));
        } catch (EmptyResultDataAccessException e) {
            logger.error("Got empty result", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Customer> findByName(String name) {
        try {
            return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject("select * from customers where name = :name",
                    Collections.singletonMap("name", name),
                    customerRowMapper));
        } catch (EmptyResultDataAccessException e) {
            logger.error("Got empty result", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        try {
            return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject("select * from customers where email = :email",
                    Collections.singletonMap("email", email),
                    customerRowMapper));
        } catch (EmptyResultDataAccessException e) {
            logger.error("Got empty result", e);
            return Optional.empty();
        }
    }


    //Transaction test
    //TransactionManager 이용.
   /* public void testTransaction(Customer customer){
        var transaction = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try{
            namedParameterJdbcTemplate.update("UPDATE customers SET name=:name WHERE customer_id = UUID_TO_BIN(:customerId)", toParamMap(customer));
            namedParameterJdbcTemplate.update("UPDATE customers SET email=:email WHERE customer_id = UUID_TO_BIN(:customerId)", toParamMap(customer));

            //에러가 발생하지 않고 모두 성공했을 때 commit
            transactionManager.commit(transaction);
        }catch (DataAccessException e){
            logger.error("Got error", e);

            //에러 발생했을 경우 rollback
            transactionManager.rollback(transaction);
        }
    }*/

    //Transaction Template 이용
/*    public void testTransaction(Customer customer){
        transactionTemplate.execute(new TransactionCallbackWithoutResult(){
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                namedParameterJdbcTemplate.update("UPDATE customers SET name=:name WHERE customer_id = UUID_TO_BIN(:customerId)", toParamMap(customer));
                namedParameterJdbcTemplate.update("UPDATE customers SET email=:email WHERE customer_id = UUID_TO_BIN(:customerId)", toParamMap(customer));
            }
        });*/

    public void testTransaction(Customer customer){
        namedParameterJdbcTemplate.update("UPDATE customers SET name=:name WHERE customer_id = UUID_TO_BIN(:customerId)", toParamMap(customer));
        namedParameterJdbcTemplate.update("UPDATE customers SET email=:email WHERE customer_id = UUID_TO_BIN(:customerId)", toParamMap(customer));
    }

}
