package com.eljumillano.loop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eljumillano.loop.model.Disposable;

public interface DisposableRepository extends JpaRepository<Disposable, Long> {
  
}
