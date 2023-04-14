package com.wsiiz.repairshop.customerfile.application;

import com.wsiiz.repairshop.customerfile.domain.customer.Customer;
import com.wsiiz.repairshop.customerfile.domain.customer.CustomerRepository;
import com.wsiiz.repairshop.customerfile.domain.customer.CustomerService;
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
@RequestMapping("api/customerfile/customers")
public class CustomerController {

  @Autowired
  CustomerRepository repository;

  @Autowired
  CustomerService service;

  @GetMapping
  public ResponseEntity<List<Customer>> getMany() {
    return ResponseEntity.ok(repository.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Customer> getOne(@PathVariable("id") Long id) {

    return repository.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<Customer> addNew(@RequestBody Customer customer) {
    return ResponseEntity.created(null).body(service.add(customer));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Customer> remove(@PathVariable("id") Long id) {

    Optional<Customer> customer = repository.findById(id);

    if (!customer.isPresent()) {
      return ResponseEntity.notFound().build();
    }
    service.delete(id);
    return ResponseEntity.ok(customer.get());
  }
}
