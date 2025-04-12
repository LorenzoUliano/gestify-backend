package br.com.lorenzo.gestify.repository;

import br.com.lorenzo.gestify.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    // Busca usuário por email (para login)
    Optional<User> findByEmail(String email);

    // Verifica se email já está cadastrado
    boolean existsByEmail(String email);
}