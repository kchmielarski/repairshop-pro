package com.wsiiz.repairshop.enterprise.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends JpaRepository <Employee, Long>, JpaSpecificationExecutor<Employee>{
   @Query
    List<Employee> findByBranchId (@Param("branchId") Long branchId);


}

