package com.wsiiz.repairshop.security.domain.useraccount;

import com.wsiiz.repairshop.foundation.domain.AbstractFactory;
import org.springframework.stereotype.Component;

@Component
public class UserAccountFactory implements AbstractFactory<UserAccount> {

  @Override
  public UserAccount create() {
    return new UserAccount();
  }

}
