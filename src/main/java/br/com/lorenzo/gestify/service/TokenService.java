package br.com.lorenzo.gestify.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import br.com.lorenzo.gestify.model.Token;
import br.com.lorenzo.gestify.model.User;
import br.com.lorenzo.gestify.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
@Service
public class TokenService {

    @Value("${gestify.secret}")
    private String secretWord;
    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public String generateToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(secretWord);

        String token = JWT.create()
                .withIssuer("gestify-api")
                .withSubject(user.getUsername())
                .withClaim("id", user.getId().toString())
                .withExpiresAt(LocalDateTime.now().plusSeconds(86400).toInstant(ZoneOffset.of("-03:00")))
                .sign(algorithm);

        Token tokenObj = new Token();
        tokenObj.setToken(token);
        tokenObj.setUser(user);
        tokenObj.setCreateDate(LocalDateTime.now());
        tokenObj.setExpirationDate(LocalDateTime.from(LocalDateTime.now().plusSeconds(86400)));
        tokenRepository.save(tokenObj);

        return token;
    }

    public String validateToken(String token, HttpServletRequest request) {
        Algorithm algorithm = Algorithm.HMAC256(secretWord);
        try {
            Token tokenDB = tokenRepository.findTokenByToken(token);

            if (tokenDB == null) {
                request.getSession().invalidate();
                throw new IllegalArgumentException("Token not found or invalid");
            }

            return JWT.require(algorithm)
                    .withIssuer("gestify-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (Exception exception) {
            request.getSession().invalidate();
            return null;
        }
    }

    public void invalidateToken(String token){
        tokenRepository.deleteByToken(token);
    }
}
