package org.prgrms.kdt.customer.service;

import org.prgrms.kdt.customer.domain.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    void createCustomers(List<Customer> customers);
    List<Customer> getAllCustomers();

    Customer createCustomer(String email, String name);

    Optional<Customer> getCustomer(UUID customerId);
}
