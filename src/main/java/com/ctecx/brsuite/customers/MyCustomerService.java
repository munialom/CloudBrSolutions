package com.ctecx.brsuite.customers;

import com.ctecx.brsuite.util.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MyCustomerService {
    private final CustomerRepository customerRepository;
    private Customer defaultCustomer;
    private static final Long DEFAULT_CUSTOMER_ID = 1L;

    @Autowired
    public MyCustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        loadDefaultCustomer();
    }

    private void loadDefaultCustomer() {
        try {
            this.defaultCustomer = customerRepository.findById(DEFAULT_CUSTOMER_ID)
                    .orElseThrow(() -> new ResourceNotFoundException("Default customer not found with id: " + DEFAULT_CUSTOMER_ID));
        } catch (Exception e) {
            log.error("Failed to load default customer: ", e);
            // Create a new default customer if not found
            this.defaultCustomer = new Customer();
            this.defaultCustomer.setCustid(DEFAULT_CUSTOMER_ID);
            // Set other default properties as needed
        }
    }

    public Customer getDefaultCustomer() {
        return defaultCustomer;
    }

    // Method to manually refresh the default customer if needed
    public void refreshDefaultCustomer() {
        loadDefaultCustomer();
    }
}