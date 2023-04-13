package com.wsiiz.repairshop.security.domain.useraccount;

import com.wsiiz.repairshop.foundation.domain.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class UserRole extends BaseEntity {

  Long roleId;
  Boolean active;
}
