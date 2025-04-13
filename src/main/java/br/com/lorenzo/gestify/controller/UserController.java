package br.com.lorenzo.gestify.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.lorenzo.gestify.model.User;
import br.com.lorenzo.gestify.repository.UserRepository;
import br.com.lorenzo.gestify.utils.Utils;


@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Utils utils;

    @GetMapping("/isAuthenticated")
    public ResponseEntity<Object> isAuthenticated() {
        try {
            User userAuth = utils.getUserAuthenticated();
            if (userAuth == null) {
                return ResponseEntity.ok().header(HttpHeaders.CONNECTION, "close").body("");
            }

            User authenticatedUser = userRepository.findById(userAuth.getId()).orElse(null);
            Map<String, Object> userResponse = utils.getDefaultUserResponseMap(authenticatedUser);
            return ResponseEntity.accepted().header(HttpHeaders.CONNECTION, "close").body(userResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header(HttpHeaders.CONNECTION, "close")
                    .body(Map.of(
                            "status", 500,
                            "title", "Internal Server Error",
                            "detail", e.getMessage(),
                            "instance", "/api/user/isAuthenticated"
                    ));
        }
    }
}
