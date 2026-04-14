package com.eljumillano.loop.service;

import com.eljumillano.loop.dtos.user.LoginDto;
import com.eljumillano.loop.dtos.user.LoginResponseDto;
import com.eljumillano.loop.dtos.user.RegisterDto;
import com.eljumillano.loop.dtos.user.UserDto;
import com.eljumillano.loop.exception.ResourceNotFoundException;
import com.eljumillano.loop.mapper.UserMapper;
import com.eljumillano.loop.model.Sucursal;
import com.eljumillano.loop.model.User;
import com.eljumillano.loop.model.enums.Role;
import com.eljumillano.loop.repository.postgres.SucursalRepository;
import com.eljumillano.loop.repository.postgres.UserRepository;
import com.eljumillano.loop.service.iservice.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SucursalRepository sucursalRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDto registerUser(RegisterDto registerDto) {
        // Validar que el username no exista si se proporciona
        if (registerDto.getUsername() != null && userRepository.findByUsername(registerDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("El username ya está en uso");
        }

        // Validar sucursal
        Sucursal sucursal = sucursalRepository.findById(registerDto.getSucursalId())
                .orElseThrow(() -> new ResourceNotFoundException("Sucursal no encontrada"));

        // Validar que si es DELIVERY, debe tener deliveryId
        if (registerDto.getRole() == Role.DELIVERY && registerDto.getDeliveryId() == null) {
            throw new IllegalArgumentException("Los usuarios de tipo DELIVERY deben tener un deliveryId");
        }

        // Si tiene deliveryId, el rol debe ser DELIVERY
        if (registerDto.getDeliveryId() != null && registerDto.getRole() != Role.DELIVERY) {
            registerDto.setRole(Role.DELIVERY);
        }

        // Validaciones según el tipo de usuario
        boolean isMobileUser = registerDto.getRole() == Role.DELIVERY || 
                               registerDto.getRole() == Role.CONTROLLER || 
                               registerDto.getRole() == Role.PICKER;

        if (isMobileUser) {
            // Usuarios móviles requieren PIN
            if (registerDto.getPin() == null) {
                throw new IllegalArgumentException("Los usuarios móviles (DELIVERY, CONTROLLER, PICKER) requieren PIN");
            }
        } else {
            // Usuarios web (ADMIN, USER) requieren username y password
            if (registerDto.getUsername() == null || registerDto.getUsername().trim().isEmpty()) {
                throw new IllegalArgumentException("Los usuarios web (ADMIN, USER) requieren username");
            }
            if (registerDto.getPassword() == null || registerDto.getPassword().trim().isEmpty()) {
                throw new IllegalArgumentException("Los usuarios web (ADMIN, USER) requieren password");
            }
        }

        User user = userMapper.toEntity(registerDto);
        user.setSucursal(sucursal);
        
        // Hashear la contraseña si existe (usuarios web)
        if (registerDto.getPassword() != null && !registerDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        }
        
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponseDto login(LoginDto loginDto) {
        User user = null;

        // Login por PIN
        if (loginDto.getPin() != null) {
            user = userRepository.findByPin(loginDto.getPin())
                    .orElseThrow(() -> new ResourceNotFoundException("PIN incorrecto"));
        }
        // Login por username y password
        else if (loginDto.getUsername() != null && loginDto.getPassword() != null) {
            user = userRepository.findByUsername(loginDto.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
            
            // Verificar contraseña hasheada
            if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
                throw new IllegalArgumentException("Contraseña incorrecta");
            }
        } else {
            throw new IllegalArgumentException("Debe proporcionar username/password o PIN");
        }

        // Verificar que el usuario esté activo
        if (!user.isActive()) {
            throw new IllegalArgumentException("Usuario inactivo");
        }

        return userMapper.toLoginResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsersByRole(Role role) {
        return userRepository.findByRole(role).stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsersBySucursal(Long sucursalId) {
        return userRepository.findBySucursalId(sucursalId).stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getDeliveryUsers() {
        return userRepository.findByDeliveryIdIsNotNull().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto updateUser(Long id, RegisterDto registerDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        // Validar username único si se está cambiando
        if (registerDto.getUsername() != null && 
            !registerDto.getUsername().equals(user.getUsername()) &&
            userRepository.findByUsername(registerDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("El username ya está en uso");
        }

        // Validaciones según el tipo de usuario
        boolean isMobileUser = registerDto.getRole() == Role.DELIVERY || 
                               registerDto.getRole() == Role.CONTROLLER || 
                               registerDto.getRole() == Role.PICKER;

        if (isMobileUser) {
            // Usuarios móviles requieren PIN
            if (registerDto.getPin() == null) {
                throw new IllegalArgumentException("Los usuarios móviles (DELIVERY, CONTROLLER, PICKER) requieren PIN");
            }
        } else {
            // Usuarios web (ADMIN, USER) requieren username y password
            String username = registerDto.getUsername() != null ? registerDto.getUsername() : user.getUsername();
            if (username == null || username.trim().isEmpty()) {
                throw new IllegalArgumentException("Los usuarios web (ADMIN, USER) requieren username");
            }
        }

        // Actualizar campos
        user.setName(registerDto.getName());
        user.setLastName(registerDto.getLastName());
        if (registerDto.getUsername() != null) {
            user.setUsername(registerDto.getUsername());
        }
        if (registerDto.getPassword() != null && !registerDto.getPassword().isEmpty()) {
            // Hashear la nueva contraseña
            user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        }
        user.setPin(registerDto.getPin());
        user.setRole(registerDto.getRole());
        user.setDeliveryId(registerDto.getDeliveryId());

        // Actualizar sucursal si cambió
        if (!user.getSucursal().getId().equals(registerDto.getSucursalId())) {
            Sucursal sucursal = sucursalRepository.findById(registerDto.getSucursalId())
                    .orElseThrow(() -> new ResourceNotFoundException("Sucursal no encontrada"));
            user.setSucursal(sucursal);
        }

        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public UserDto activateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        user.setActive(true);
        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }

    @Override
    @Transactional
    public UserDto deactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        user.setActive(false);
        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }
}
