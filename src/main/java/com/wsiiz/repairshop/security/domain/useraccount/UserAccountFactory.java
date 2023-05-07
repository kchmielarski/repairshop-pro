package com.wsiiz.repairshop.security.domain.useraccount;

import java.time.LocalDateTime;

import com.wsiiz.repairshop.foundation.domain.AbstractFactory;
import org.springframework.stereotype.Component;

@Component
public class UserAccountFactory implements AbstractFactory<UserAccount> {

  @Override
  public UserAccount create() {
    UserAccount userAccount = new UserAccount();
    userAccount.setRegistrationTime(LocalDateTime.now());
    return userAccount;
  }
}
