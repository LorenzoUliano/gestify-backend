package br.com.lorenzo.gestify.repository;


import br.com.lorenzo.gestify.model.Token;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Transactional
    @Modifying
    void deleteByToken(String token);

    Token findTokenByToken(String token);

}
