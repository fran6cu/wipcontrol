package com.backend.wipcontrol.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private Long id;

    private String username;

    private String email;

    private String avatar;

    private String rol;
}
