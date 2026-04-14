package com.eljumillano.loop.repository.postgres;

import org.springframework.data.jpa.repository.JpaRepository;
import com.eljumillano.loop.model.User;
import com.eljumillano.loop.model.enums.Role;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByPin(Integer pin);
    
    List<User> findByRole(Role role);
    
    List<User> findBySucursalId(Long sucursalId);
    
    List<User> findByDeliveryIdIsNotNull();
}
