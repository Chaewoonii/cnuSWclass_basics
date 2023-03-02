CREATE DATABASE order_mgmt;
USE order_mgmt;
CREATE TABLE customers(
                          customer_id BINARY(16) PRIMARY KEY,
                          name VARCHAR(20) NOT NULL,
                          email VARCHAR(50) NOT NULL,
                          last_login_at DATETIME(6) DEFAULT NULL,
                          created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
                          CONSTRAINT unq_user_email UNIQUE (email)
);

CREATE FUNCTION UUID_TO_BIN(uuid VARCHAR(36))
    RETURNS BINARY(16) DETERMINISTIC
BEGIN
RETURN UNHEX(CONCAT(REPLACE(uuid, '-', '')));
END;

CREATE FUNCTION BIN_TO_UUID(bin BINARY(16))
    RETURNS VARCHAR(36) DETERMINISTIC
BEGIN
  DECLARE hex VARCHAR(32);
  SET hex = HEX(bin);
RETURN LOWER(CONCAT(LEFT(hex, 8), '-', MID(hex, 9, 4), '-', MID(hex, 13, 4), '-', MID(hex, 17, 4), '-', RIGHT(hex, 12)));
END;