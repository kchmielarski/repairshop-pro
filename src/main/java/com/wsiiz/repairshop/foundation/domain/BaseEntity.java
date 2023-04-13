package com.wsiiz.repairshop.foundation.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;

@MappedSuperclass
@Data
@EqualsAndHashCode(of = "id")
public abstract class BaseEntity {

  @Id
  @GeneratedValue
  protected Long id;

  @Version
  protected int version;

}
