package br.com.lorenzo.gestify.controller;

import br.com.lorenzo.gestify.dto.LoginRequestDTO;
import br.com.lorenzo.gestify.dto.RegisterRequestDTO;
import br.com.lorenzo.gestify.enuns.Role;
import br.com.lorenzo.gestify.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register/user")
    public void registerUser(
        @RequestBody RegisterRequestDTO registerRequestDTO
    ) {
        authService.registerUser(registerRequestDTO.getEmail(), registerRequestDTO.getPassword(), registerRequestDTO.getName(), Role.USER);
    }

    @PostMapping("/register/teacher")
    public void registerTeacher(
        @RequestParam String email,
        @RequestParam String password,
        @RequestParam String name
    ) {
        authService.registerUser(email, password, name, Role.TEACHER);
    }

    @PostMapping("/login")
    public String login(
        @RequestParam LoginRequestDTO loginRequestDTO
    ) {
        return authService.loginUser(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());
    }
}