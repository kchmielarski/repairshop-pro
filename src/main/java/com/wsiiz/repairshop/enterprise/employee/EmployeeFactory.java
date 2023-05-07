package com.wsiiz.repairshop.enterprise.employee;

import com.wsiiz.repairshop.foundation.domain.AbstractFactory;
import org.springframework.stereotype.Component;

@Component
public abstract class EmployeeFactory implements AbstractFactory<Employee> {

    @Override
    public Employee create (){
        return new Employee();
   }
}
