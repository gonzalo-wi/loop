package com.eljumillano.loop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eljumillano.loop.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
}
