package com.wsiiz.repairshop.customerfile.domain.customer;

import static com.wsiiz.repairshop.foundation.ui.i18n.I18nHandler.i18nHandler;

import com.vaadin.flow.data.binder.ValidationResult;
import com.wsiiz.repairshop.foundation.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.internal.constraintvalidators.hv.pl.PESELValidator;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@CustomerConstraint
public class Customer extends BaseEntity {

  String firstName;
  String lastName;
  String pesel;
  @Email
  String email;
  String phone;
  @Past
  LocalDate dateOfBirth;
  String occupation;
  @Enumerated(EnumType.STRING)
  CustomerRole role;
  boolean important;

  public static ValidationResult peselValidation(String pesel) {

    if(pesel == null || pesel.isBlank()) {
      return ValidationResult.ok();
    }

    PESELValidator pw = new PESELValidator();

    pw.initialize(null);

    if(pw.isValid(pesel, null)) {
      return ValidationResult.ok();
    }

    return ValidationResult.error(i18nHandler.getMessage("com.wsiiz.repairshop.customerfile.ui.customer.CustomerView.invalidPesel"));
  }

}
