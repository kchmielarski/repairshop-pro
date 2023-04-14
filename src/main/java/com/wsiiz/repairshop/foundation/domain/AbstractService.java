package com.wsiiz.repairshop.foundation.domain;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AbstractService<E extends BaseEntity> {

  JpaRepository<E, Long> getRepository();

  JpaSpecificationExecutor<E> getSpecificationExecutor();

  default Optional<E> get(Long id) {
    return getRepository().findById(id);
  }

  default E add(E entity) {
    return getRepository().save(entity);
  }

  default E change(E entity) {
    return getRepository().save(entity);
  }

  default void delete(Long id) {
    getRepository().deleteById(id);
  }

  default Page<E> list(Pageable pageable) {
    return getRepository().findAll(pageable);
  }

  default Page<E> list(Pageable pageable, Specification<E> filter) {
    return getSpecificationExecutor().findAll(filter, pageable);
  }

  default int count() {
    return (int) getRepository().count();
  }
}
