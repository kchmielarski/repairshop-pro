package com.wsiiz.repairshop.enterprise.employee;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
//@AllArgsConstructor
public enum EmployeeSkill {
   TEAMWORK, COMMUNICATION, RESPONSIBILITY, PROBLEMSOLVING, TIMEMANAGMENT;
}
