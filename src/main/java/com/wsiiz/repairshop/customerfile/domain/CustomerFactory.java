package com.wsiiz.repairshop.customerfile.domain;

import com.wsiiz.repairshop.foundation.domain.AbstractFactory;
import org.springframework.stereotype.Component;

@Component
public class CustomerFactory implements AbstractFactory<Customer> {

  @Override
  public Customer create() {
    return new Customer();
  }
}
