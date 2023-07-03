package com.elalfee;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SpringBootApplication
@RestController
@RequestMapping("api/v1/customers")
public class Main {

    private final CustomerRepository customerRepository;

    public Main(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public static void main(String[] args){
        SpringApplication.run(Main.class, args);
    }

    @GetMapping
    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    static record NewCustomerRequest(String email, String name, Integer age){}

    @PostMapping
    public ResponseEntity<Customer> addCustomer(@RequestBody NewCustomerRequest request){
        Customer customer= new Customer();
        customer.setName(request.name);
        customer.setEmail(request.email);
        customer.setAge(request.age);
        customerRepository.save(customer);

        return ResponseEntity.ok(customer);
    }

    @DeleteMapping("{id}")
    public void deleteCustomer(@PathVariable("id") Integer id){
        customerRepository.deleteById(id);
    }

    static record CustomerRequest(String email, String name, Integer age){}

    @PutMapping("{id}")
    public ResponseEntity<Customer> updateCustomer(@RequestBody CustomerRequest request, @PathVariable("id") Integer id){
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + id));

        customer.setName(request.name);
        customer.setEmail(request.email);
        customer.setAge(request.age);

        customerRepository.save(customer);
        return ResponseEntity.ok(customer);
    }

}
