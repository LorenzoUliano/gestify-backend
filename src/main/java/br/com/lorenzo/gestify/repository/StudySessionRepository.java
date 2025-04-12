package br.com.lorenzo.gestify.repository;

import br.com.lorenzo.gestify.model.StudySession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StudySessionRepository extends JpaRepository<StudySession, UUID> {

    // Busca sessões de estudo por usuário e matéria
    List<StudySession> findByUserIdAndSubjectId(UUID userId, UUID subjectId);

    // Soma do tempo total de estudo (em minutos) de um usuário
    @Query(value = """
        SELECT COALESCE(SUM(
            EXTRACT(EPOCH FROM (s.end_time - s.start_time)) / 60
        ), 0) 
        FROM study_session s 
        WHERE s.user_id = :userId
        """, nativeQuery = true)
    Integer findTotalStudyMinutesByUser(@Param("userId") UUID userId);
}