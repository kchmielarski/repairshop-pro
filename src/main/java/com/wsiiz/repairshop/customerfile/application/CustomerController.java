package com.wsiiz.repairshop.customerfile.application;

import com.wsiiz.repairshop.customerfile.domain.Customer;
import com.wsiiz.repairshop.customerfile.domain.CustomerRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customerfile/customers")
public class CustomerController {

  @Autowired
  CustomerRepository customerRepository;

  @GetMapping
  public ResponseEntity<List<Customer>> getMany() {
    return ResponseEntity.ok(customerRepository.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Customer> getOne(@PathVariable("id") Long id) {

    return customerRepository.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<Customer> addNew(@RequestBody Customer customer) {
    return ResponseEntity.created(null).body(customerRepository.save(customer));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Customer> remove(@PathVariable("id") Long id) {

    Optional<Customer> customer = customerRepository.findById(id);

    if (!customer.isPresent()) {
      return ResponseEntity.notFound().build();
    }
    customerRepository.deleteById(id);
    return ResponseEntity.ok(customer.get());
  }
}
