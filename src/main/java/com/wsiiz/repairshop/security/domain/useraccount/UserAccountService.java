package com.wsiiz.repairshop.security.domain.useraccount;

import com.wsiiz.repairshop.foundation.domain.AbstractService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAccountService implements AbstractService<UserAccount> {

  private final UserAccountRepository repository;

  @Override
  public JpaRepository<UserAccount, Long> getRepository() {
    return repository;
  }

  @Override
  public JpaSpecificationExecutor<UserAccount> getSpecificationExecutor() {
    return repository;
  }

  @Override
  public UserAccount add(UserAccount userAccount) {
    userAccount.registrationTime = LocalDateTime.now();
    return repository.save(userAccount);
  }
}
