package com.eljumillano.loop.repository.postgres;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eljumillano.loop.model.Control;

public interface ControlRepository extends JpaRepository<Control, Long> {
    
}
