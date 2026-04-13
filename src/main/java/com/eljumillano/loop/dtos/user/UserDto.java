package com.eljumillano.loop.dtos.user;

import com.eljumillano.loop.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String lastName;
    private String username;
    private Integer pin;
    private Long sucursalId;
    private String sucursalName;
    private Role role;
    private Long deliveryId;
    private boolean active;
}
