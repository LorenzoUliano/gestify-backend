package br.com.lorenzo.gestify.repository;

import br.com.lorenzo.gestify.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, UUID> {

    // Lista matérias de um usuário
    List<Subject> findByUserId(UUID userId);

    // Verifica se matéria já existe para o usuário
    boolean existsByNameAndUserId(String name, UUID userId);
}