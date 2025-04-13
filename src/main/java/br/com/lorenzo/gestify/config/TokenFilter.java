package br.com.lorenzo.gestify.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.lorenzo.gestify.model.Token;
import br.com.lorenzo.gestify.model.User;
import br.com.lorenzo.gestify.repository.TokenRepository;
import br.com.lorenzo.gestify.repository.UserRepository;
import br.com.lorenzo.gestify.service.TokenService;
import br.com.lorenzo.gestify.utils.Utils;

import java.io.IOException;
import java.util.Optional;

@Component
public class TokenFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();
        AntPathMatcher pathMatcher = new AntPathMatcher();
        String token = recoverToken(request);

        if (Utils.PUBLIC_ROUTES.stream().anyMatch(route -> pathMatcher.match(route, path))) {
                filterChain.doFilter(request, response);
                return;
        }

        var login = tokenService.validateToken(token, request);
        if (login != null) {
                Optional<User> userOpt = userRepository.findByEmail(login);
                if (userOpt.isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Usuário não encontrado para o token fornecido");
                    return;
                }
                UserDetails user = userOpt.get();

                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token expirado ou invalido");
                return;
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {


        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("token".equals(cookie.getName())) {
                    Token tokenBD = tokenRepository.findTokenByToken(cookie.getValue());
                    if(tokenBD != null){
                        return tokenBD.getToken();
                    }
                }
            }
            return null;
        }

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }

        return null;
    }



    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        if (SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
            return true;
        }

        return path.startsWith("/api/auth/login") || path.startsWith("/api/auth/register") || path.startsWith("/api/login/success");
    }

}
