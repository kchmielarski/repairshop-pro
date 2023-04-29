package com.wsiiz.repairshop.security.domain.useraccount;

import com.wsiiz.repairshop.foundation.domain.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class UserAccount extends BaseEntity {

  String userName;
  String login;
  @Enumerated(EnumType.STRING)
  UserType type;
  LocalDate validFrom;
  LocalDate validTo;
  LocalDateTime registrationTime;
  LocalDateTime recentLoginTime;
  boolean active;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "user_account_id")
  List<UserRole> roles;

  public UserAccount() {
    this.registrationTime = LocalDateTime.now();
    this.roles = new ArrayList<>();
  }
}
