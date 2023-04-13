package com.wsiiz.repairshop.customerfile.domain;

import com.wsiiz.repairshop.foundation.domain.AbstractService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService implements AbstractService<Customer> {

  private final CustomerRepository customerRepository;

  @Override
  public JpaRepository<Customer, Long> getRepository() {
    return customerRepository;
  }

  @Override
  public JpaSpecificationExecutor<Customer> getSpecificationExecutor() {
    return customerRepository;
  }
}
