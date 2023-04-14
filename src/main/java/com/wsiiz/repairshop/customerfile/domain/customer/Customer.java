package com.wsiiz.repairshop.customerfile.domain.customer;

import com.wsiiz.repairshop.foundation.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class Customer extends BaseEntity {

  String firstName;
  String lastName;
  @Email
  String email;
  String phone;
  LocalDate dateOfBirth;
  String occupation;
  @Enumerated(EnumType.STRING)
  CustomerRole role;
  boolean important;
}
