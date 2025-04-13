package br.com.lorenzo.gestify.utils;

import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import br.com.lorenzo.gestify.model.User;
import br.com.lorenzo.gestify.repository.UserRepository;

@Component
public class Utils {

    @Autowired
    private UserRepository userRepository;


    public static final java.util.List<String> PUBLIC_ROUTES = java.util.List.of(
        "/auth/login",
        "/auth/register/**"
);

    public LinkedHashMap<String, Object> getDefaultUserResponseMap(User user) {
        LinkedHashMap<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("userRole", user.getRole());
        responseMap.put("name", user.getName());
        responseMap.put("email", user.getEmail());

        return responseMap;
    }

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }


    public User getUserAuthenticated() {
        Object principal = getAuthentication().getPrincipal();

        if (principal instanceof User) {
            return (User) principal;
        } else if (principal instanceof String) {
            String email = (String) principal;
            return userRepository.findByEmail(email).orElse(null);
        }

        throw new IllegalStateException("Erro ao buscar usuário: " + principal.getClass());
    }

    private User findUserByName(String name) {
        return userRepository.findUserByEmail(name);
    }
}
