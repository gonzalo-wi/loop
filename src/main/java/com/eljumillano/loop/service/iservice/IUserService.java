package com.eljumillano.loop.service.iservice;

import com.eljumillano.loop.dtos.user.LoginDto;
import com.eljumillano.loop.dtos.user.LoginResponseDto;
import com.eljumillano.loop.dtos.user.RegisterDto;
import com.eljumillano.loop.dtos.user.UserDto;
import com.eljumillano.loop.model.enums.Role;

import java.util.List;

public interface IUserService {
    
    UserDto registerUser(RegisterDto registerDto);
    
    LoginResponseDto login(LoginDto loginDto);
    
    UserDto getUserById(Long id);
    
    List<UserDto> getAllUsers();
    
    List<UserDto> getUsersByRole(Role role);
    
    List<UserDto> getUsersBySucursal(Long sucursalId);
    
    List<UserDto> getDeliveryUsers();
    
    UserDto updateUser(Long id, RegisterDto registerDto);
    
    void deleteUser(Long id);
    
    UserDto activateUser(Long id);
    
    UserDto deactivateUser(Long id);
}
