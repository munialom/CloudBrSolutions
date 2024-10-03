package com.ctecx.brsuite.customers;

import com.ctecx.brsuite.util.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public List<Customer> searchCustomers(String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            return customerRepository.findByCustomernameContainingIgnoreCase(keyword);
        }
        return customerRepository.findAll();
    }

    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Long id, Customer customerDetails) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        customer.setCustomername(customerDetails.getCustomername());
        customer.setCustomeraddress(customerDetails.getCustomeraddress());
        customer.setCustomercontact(customerDetails.getCustomercontact());
        customer.setCreditterm(customerDetails.getCreditterm());
        customer.setTaxPin(customerDetails.getTaxPin());
        customer.setCreditlimit(customerDetails.getCreditlimit());
        customer.setEmail(customerDetails.getEmail());
        customer.setCustomerType(customerDetails.getCustomerType());

        return customerRepository.save(customer);
    }

    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        customerRepository.delete(customer);
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
    }
}