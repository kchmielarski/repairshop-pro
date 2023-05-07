package com.wsiiz.repairshop.enterprise.branch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BranchRepository extends JpaRepository<Branch, Long>, JpaSpecificationExecutor<Branch> {

    @Query
    List<Branch> findByCity ();  // nie można wpisać argumentów: String, city
    List<Branch> findByBranchId (); // nie można wpisać argumentów: int, branchId


}
