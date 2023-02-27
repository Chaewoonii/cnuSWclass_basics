-- docker run --name kdt-mysql -e MYSQL_PORT_HOST=% -e MYSQL_ROOT_PASSWORD=test1234@# -p 3306:3306 -d mysql:8
-- docker exec -it kdt-mysql mysql -u root -p
-- test1234@#

CREATE DATABASE order_mgmt;
use order_mgmt;
CREATE TABLE customers(
                          customer_id BINARY(16) PRIMARY KEY,
                          name VARCHAR(20) NOT NULL,
                          email VARCHAR(50) NOT NULL,
                          last_login_at DATETIME DEFAULT NULL,
                          created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),
                          CONSTRAINT unq_user_email UNIQUE (email)
);

INSERT INTO customers(customer_id, name, email)
VALUES (UUID_TO_BIN(UUID()), 'tester00', 'test00@gmail.com');
INSERT INTO customers(customer_id, name, email)
VALUES (UUID_TO_BIN(UUID()), 'tester01', 'test01@gmail.com');
INSERT INTO customers(customer_id, name, email)
VALUES (UUID_TO_BIN(UUID()), 'tester02', 'test02@gmail.com');