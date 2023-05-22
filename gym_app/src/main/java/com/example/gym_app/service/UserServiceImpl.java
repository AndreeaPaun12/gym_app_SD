package com.example.gym_app.service;

import com.example.gym_app.dto.AccountDTO;
import com.example.gym_app.dto.UserEditDTO;
import com.example.gym_app.model.Account;
import com.example.gym_app.model.User;
import com.example.gym_app.model.UserRole;
import com.example.gym_app.repository.AccountRepository;
import com.example.gym_app.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;
    private final EmailService emailService;
    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AccountRepository accountRepository, ModelMapper modelMapper, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountRepository = accountRepository;
        this.modelMapper = modelMapper;
        this.emailService = emailService;
    }

    @Override
    public User findUserByUsername(String username) {
        Optional<User> byUsername = this.userRepository.findByUsername(username);
        if (byUsername.isPresent()) {
            return byUsername.get();
        } else {
            throw new IllegalStateException("No user found!");
        }
    }

    @Override
    public boolean userByUsernameExists(String username) {
        Optional<User> byUsername = this.userRepository.findByUsername(username);
        return byUsername.isPresent();
    }

    @Override
    public boolean userByEmailExists(String email) {
        Optional<User> byEmail = this.userRepository.findByEmail(email);
        return byEmail.isPresent();
    }

    @Override
    public Account register(AccountDTO user) {
        Account newAccount = this.modelMapper.map(user, Account.class);
        newAccount.setRole(UserRole.CLIENT);
        newAccount.setPassword(this.passwordEncoder.encode(user.getPassword()));
        emailService.sendEmail(newAccount.getEmail(), "Registered successfully!", "Have fun!");
        return accountRepository.save(newAccount);
    }

    @Override
    public Account findAccountByUsername(String username) {
        return this.accountRepository.findByUsername(username).orElseThrow();
    }

    @Override
    public void delete(String username) {
        Optional<User> user = this.userRepository.findByUsername(username);
        user.ifPresent(value -> this.userRepository.deleteById(value.getId()));
    }

    @Override
    public Account edit(UserEditDTO userEditDTO) {
        Optional<Account> user = this.accountRepository.findByUsername(userEditDTO.getUsername());
        if(user.isPresent()) {
            user.get().setFullName(userEditDTO.getFullName());
            user.get().setPhoneNumber(userEditDTO.getPhoneNumber());
            return accountRepository.save(user.get());
        }
        return null;
    }
    @EventListener
    public void seedUsers(ContextRefreshedEvent event) {
        //valid login user: root passw: rootroot
        List<User> seededUsers = new ArrayList<>();
        //simple user
        if (userRepository.count() == 0) {
            User user = new User();
            user.setRole(UserRole.CLIENT);
            seededUsers.add(user);
        }
    }
}
