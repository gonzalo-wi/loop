package com.eljumillano.loop.controller;

import com.eljumillano.loop.dtos.user.LoginDto;
import com.eljumillano.loop.dtos.user.LoginResponseDto;
import com.eljumillano.loop.dtos.user.RegisterDto;
import com.eljumillano.loop.dtos.user.UserDto;
import com.eljumillano.loop.model.enums.Role;
import com.eljumillano.loop.service.iservice.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loop/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * Registrar un nuevo usuario
     * POST /api/users/register
     */
    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody RegisterDto registerDto) {
        UserDto userDto = userService.registerUser(registerDto);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    /**
     * Login de usuario
     * POST /api/users/login
     * Puede ser por username/password o por PIN
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginDto loginDto) {
        LoginResponseDto response = userService.login(loginDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Obtener todos los usuarios
     * GET /api/users
     */
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Obtener usuario por ID
     * GET /api/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto userDto = userService.getUserById(id);
        return ResponseEntity.ok(userDto);
    }

    /**
     * Obtener usuarios por rol
     * GET /api/users/role/{role}
     */
    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserDto>> getUsersByRole(@PathVariable Role role) {
        List<UserDto> users = userService.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }

    /**
     * Obtener usuarios por sucursal
     * GET /api/users/sucursal/{sucursalId}
     */
    @GetMapping("/sucursal/{sucursalId}")
    public ResponseEntity<List<UserDto>> getUsersBySucursal(@PathVariable Long sucursalId) {
        List<UserDto> users = userService.getUsersBySucursal(sucursalId);
        return ResponseEntity.ok(users);
    }

    /**
     * Obtener todos los usuarios de reparto (con deliveryId)
     * GET /api/users/delivery
     */
    @GetMapping("/delivery")
    public ResponseEntity<List<UserDto>> getDeliveryUsers() {
        List<UserDto> users = userService.getDeliveryUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Actualizar usuario
     * PUT /api/users/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @Valid @RequestBody RegisterDto registerDto) {
        UserDto userDto = userService.updateUser(id, registerDto);
        return ResponseEntity.ok(userDto);
    }

    /**
     * Eliminar usuario
     * DELETE /api/users/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Activar usuario
     * PATCH /api/users/{id}/activate
     */
    @PatchMapping("/{id}/activate")
    public ResponseEntity<UserDto> activateUser(@PathVariable Long id) {
        UserDto userDto = userService.activateUser(id);
        return ResponseEntity.ok(userDto);
    }

    /**
     * Desactivar usuario
     * PATCH /api/users/{id}/deactivate
     */
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<UserDto> deactivateUser(@PathVariable Long id) {
        UserDto userDto = userService.deactivateUser(id);
        return ResponseEntity.ok(userDto);
    }
}
