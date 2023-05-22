package com.example.gym_app.service;

import com.example.gym_app.dto.AccountDTO;
import com.example.gym_app.dto.UserDTO;
import com.example.gym_app.dto.UserEditDTO;
import com.example.gym_app.model.Account;
import com.example.gym_app.model.User;
import com.example.gym_app.model.UserRole;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User findUserByUsername(String username);

    boolean userByUsernameExists(String username);

    boolean userByEmailExists(String email);

    Account register(AccountDTO user);

    Account findAccountByUsername(String username);

    void delete(String username);

    Account edit(UserEditDTO userEditDTO);
}
