package com.wsiiz.repairshop.enterprise.employee;

import com.wsiiz.repairshop.foundation.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ManyToAny;

import java.time.LocalDate;
@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor
@AllArgsConstructor

public class Employee extends BaseEntity {
    String firstName;
    String lastName;
    LocalDate employmentStartDate;
    Long employeeId;
    @Enumerated(EnumType.STRING)
    EmploymentPosition employmentPosition;
    @ManyToAny
    BranchEmployeeConnection branchEmployeeConnection;
    @OneToMany(cascade = CascadeType.ALL)
    EmployeeSkill employeeSkill;
}
