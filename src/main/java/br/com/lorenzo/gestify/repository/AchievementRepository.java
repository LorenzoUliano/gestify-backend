package br.com.lorenzo.gestify.repository;

import br.com.lorenzo.gestify.model.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, UUID> {

    // Busca conquistas de um usuário
    List<Achievement> findByUserId(UUID userId);

    // Verifica se conquista já foi desbloqueada
    boolean existsByTitleAndUserId(String title, UUID userId);
}