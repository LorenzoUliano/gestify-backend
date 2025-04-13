package br.com.lorenzo.gestify.service;

import br.com.lorenzo.gestify.enuns.Role;
import br.com.lorenzo.gestify.model.User;
import br.com.lorenzo.gestify.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;


    public void registerUser(String email, String password, String name, Role role) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email já cadastrado");
        }
        
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);
        user.setRole(role);
        user.setCreatedAt(LocalDateTime.now());
        
        userRepository.save(user);
    }

}