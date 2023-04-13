package com.wsiiz.repairshop.foundation.domain;

public interface AbstractFactory<E extends BaseEntity> {

  E create();

}
