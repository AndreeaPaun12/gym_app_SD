package com.example.gym_app.dto;

import com.example.gym_app.model.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String email;
    private String username;
    private String password;

}