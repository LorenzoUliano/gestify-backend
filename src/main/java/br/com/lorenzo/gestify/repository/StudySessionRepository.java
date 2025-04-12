package br.com.lorenzo.gestify.repository;

import br.com.lorenzo.gestify.model.StudySession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StudySessionRepository extends JpaRepository<StudySession, UUID> {

    // Busca sessões de estudo por usuário e matéria
    List<StudySession> findByUserIdAndSubjectId(UUID userId, UUID subjectId);

    // Soma do tempo total de estudo (em minutos) de um usuário
    @Query("SELECT SUM(TIMESTAMPDIFF('minute', s.startTime, s.endTime)) FROM StudySession s WHERE s.user.id = :userId")
    Integer findTotalStudyMinutesByUser(UUID userId);
}