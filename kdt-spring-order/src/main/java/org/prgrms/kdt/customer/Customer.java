package org.prgrms.kdt.customer;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

public class Customer {

    //final: 한 번 만들어지면 바뀌지 않는 속성인지 아닌지 잘 생각.
    private final UUID customerId;
    private String name;
    private final String email;
    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;

    private static void validName(String name) {
        if (name.isBlank()){
            throw new RuntimeException("Name should not be blank");
        }
    }

    public Customer(UUID customerId, String name, String email, LocalDateTime lastLoginAt) {
        validName(name);
        this.name = name;
        this.customerId = customerId;
        this.email = email;
        this.lastLoginAt = lastLoginAt;
    }

    public Customer(UUID customerId, String name, String email,
                    LocalDateTime lastLoginAt, LocalDateTime createdAt) {
        validName(name);
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.lastLoginAt = lastLoginAt;
        this.createdAt = createdAt;
    }

    //Setter: 신중하게 생성
    public void changeName(String name){
        //.isBlank(): 없거나 띄어쓰기, 공백만 있으면 true 반환.
        validName(name);
        this.name = name;
    }

    public void login(){
        this.lastLoginAt = LocalDateTime.now();
    }

    //Getter: 전부 생성
    public UUID getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
