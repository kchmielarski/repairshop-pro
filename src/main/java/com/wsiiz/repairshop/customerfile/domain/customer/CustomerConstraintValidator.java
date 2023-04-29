package com.wsiiz.repairshop.customerfile.domain.customer;

import static com.wsiiz.repairshop.customerfile.domain.customer.Customer.peselValidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.val;

public class CustomerConstraintValidator implements
    ConstraintValidator<CustomerConstraint, Customer> {

  @Override
  public boolean isValid(Customer customer, ConstraintValidatorContext context) {

    context.disableDefaultConstraintViolation();
    AtomicBoolean violationsDetected = new AtomicBoolean(false);

    if (customer.pesel != null) {
      val v = peselValidation(customer.pesel);
      if (v.isError()) {
        addViolation(context, violationsDetected,
            "{com.wsiiz.repairshop.customerfile.domain.customer.Customer.invalidPesel}");
      }
    }

    if (customer.dateOfBirth != null && customer.pesel != null) {
      val dateFromPesel = obtainDateFromPesel(customer.pesel);

      if (dateFromPesel != null && !dateFromPesel.isEqual(customer.dateOfBirth)) {
        addViolation(context, violationsDetected,
            "{com.wsiiz.repairshop.customerfile.domain.customer.Customer.peselDateDiffers}");
      }
    }

    return !violationsDetected.get();
  }

  private void addViolation(ConstraintValidatorContext context, AtomicBoolean violationsDetected, String messageKey) {
    context.buildConstraintViolationWithTemplate(messageKey).addConstraintViolation();
    violationsDetected.set(true);
  }

  public static LocalDate obtainDateFromPesel(String pesel) {
    if (pesel == null || pesel.length() < 6) {
      return null;
    }
    if (pesel.charAt(2) == '2') {
      pesel = "20" + pesel.substring(0, 2) + "1" + pesel.substring(3, 6);
    } else {
      pesel = "19" + pesel.substring(0, 6);
    }
    try {
      return LocalDate.parse(pesel, DateTimeFormatter.ofPattern("yyyyMMdd"));
    } catch (DateTimeParseException ex) {
      return null;
    }
  }

}
