package br.com.lorenzo.gestify.controller;

import br.com.lorenzo.gestify.dto.LoginRequestDTO;
import br.com.lorenzo.gestify.dto.RegisterRequestDTO;
import br.com.lorenzo.gestify.enuns.Role;
import br.com.lorenzo.gestify.model.User;
import br.com.lorenzo.gestify.repository.UserRepository;
import br.com.lorenzo.gestify.service.AuthService;
import br.com.lorenzo.gestify.service.TokenService;
import br.com.lorenzo.gestify.utils.Utils;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    TokenService tokenService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private Utils utils;

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
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequestDTO login) {


        UsernamePasswordAuthenticationToken authenticationToken = 
            new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authenticate);
        User user = (User) authenticate.getPrincipal();

        String token = tokenService.generateToken(user);

        ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .path("/")
                .maxAge(30 * 24 * 60 * 60)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(utils.getDefaultUserResponseMap(user));
    }

}