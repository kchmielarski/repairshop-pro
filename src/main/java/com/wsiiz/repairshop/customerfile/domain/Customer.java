package com.wsiiz.repairshop.customerfile.domain;

import com.wsiiz.repairshop.foundation.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class Customer extends BaseEntity {

    private String firstName;
    private String lastName;
    @Email
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private String occupation;
    private String role;
    private boolean important;
}
