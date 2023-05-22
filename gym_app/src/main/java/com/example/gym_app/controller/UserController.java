package com.example.gym_app.controller;

import com.example.gym_app.dto.AccountDTO;
import com.example.gym_app.dto.UserEditDTO;
import com.example.gym_app.model.Account;
import com.example.gym_app.model.User;
import com.example.gym_app.model.UserRole;
import com.example.gym_app.security.JwtRequest;
import com.example.gym_app.security.JwtResponse;
import com.example.gym_app.security.JwtUtility;
import com.example.gym_app.service.UserDetailsService;
import com.example.gym_app.service.UserService;
import com.example.gym_app.security.SecurityConfiguration;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@CrossOrigin
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtility jwtUtility;
    private final UserDetailsService userDetailsService;

    @PostMapping("/signup")
    @CrossOrigin
    public ResponseEntity<?> signup(@RequestBody AccountDTO user) {
        if (this.userService.userByUsernameExists(user.getUsername()) || this.userService.userByEmailExists(user.getEmail())) {
            throw new RuntimeException("Username or email address already in use.");
        }
        Account client = this.userService.register(user);
        return new ResponseEntity<Account>(client, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @CrossOrigin
    public String logInUser(@RequestParam String username) {
        User userByUsername = this.userService.findUserByUsername(username);
        if (userByUsername.getRole().equals(UserRole.CLIENT)) {
            return "CLIENT";
        } else if (userByUsername.getRole().equals(UserRole.TRAINER)) {
            return "TRAINER";
        } else if (userByUsername.getRole().equals(UserRole.ADMIN)) {
            return "ADMIN";
        }
        return null;
    }

    @PostMapping("/authenticate")
    @CrossOrigin
    public JwtResponse authenticate(@RequestBody JwtRequest jwtRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUsername(),
                            jwtRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
        final UserDetails userDetails
                = userDetailsService.loadUserByUsername(jwtRequest.getUsername());

        final String token =
                jwtUtility.generateToken(userDetails);
        return new JwtResponse(token);
    }

    @GetMapping("/account-details")
    @CrossOrigin
    public Account showAccountDetails(@RequestParam String username) {
        return this.userService.findAccountByUsername(username);
    }

    @DeleteMapping("/delete-account")
    @CrossOrigin
    public ResponseEntity<?> deleteAccount(@RequestParam String username) {
        if (!this.userService.userByUsernameExists(username)) {
            return new ResponseEntity<>(username, HttpStatus.NOT_FOUND);
        }
        this.userService.delete(username);
        return new ResponseEntity<>(username, HttpStatus.OK);
    }

    @PutMapping("/edit-account")
    @CrossOrigin
    public ResponseEntity<?> editAccount(@RequestBody UserEditDTO userEditDTO) {
        if(!this.userService.userByUsernameExists(userEditDTO.getUsername())) {
            return new ResponseEntity<>(userEditDTO.getUsername(), HttpStatus.NOT_FOUND);
        }
        Account client = this.userService.edit(userEditDTO);
        return new ResponseEntity<Account>(client, HttpStatus.CREATED);
    }
}

