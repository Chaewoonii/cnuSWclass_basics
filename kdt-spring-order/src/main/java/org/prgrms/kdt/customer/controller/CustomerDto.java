package org.prgrms.kdt.customer.controller;

import org.prgrms.kdt.customer.domain.Customer;

import java.time.LocalDateTime;
import java.util.UUID;


//customer 생성 시의 validation은 dto에 있어야 함
public record CustomerDto(UUID customerId, String name, String email,
                          LocalDateTime lastLoginAt, LocalDateTime createdAt) {

    static CustomerDto of(Customer customer){
        return new CustomerDto(customer.getCustomerId(),
                customer.getName(),
                customer.getEmail(),
                customer.getLastLoginAt(),
                customer.getCreatedAt());
    }

    static Customer to(CustomerDto dto){
        return new Customer(dto.customerId(),
                dto.name(),
                dto.email(),
                dto.lastLoginAt(),
                dto.createdAt());
    }
}
