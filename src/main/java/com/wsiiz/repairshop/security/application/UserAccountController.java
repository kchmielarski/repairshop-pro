package com.wsiiz.repairshop.security.application;

import com.wsiiz.repairshop.security.domain.useraccount.UserAccount;
import com.wsiiz.repairshop.security.domain.useraccount.UserAccountRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/security/user-accounts")
public class UserAccountController {

  @Autowired
  UserAccountRepository userAccountRepository;

  @GetMapping
  public ResponseEntity<List<UserAccount>> getMany() {
    return ResponseEntity.ok(userAccountRepository.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserAccount> getOne(@PathVariable("id") Long id) {

    return userAccountRepository.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<UserAccount> addNew(@RequestBody UserAccount userAccount) {
    return ResponseEntity.created(null).body(userAccountRepository.save(userAccount));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<UserAccount> remove(@PathVariable("id") Long id) {

    Optional<UserAccount> userAccount = userAccountRepository.findById(id);

    if (!userAccount.isPresent()) {
      return ResponseEntity.notFound().build();
    }

    userAccountRepository.deleteById(id);
    return ResponseEntity.ok(userAccount.get());
  }
}
