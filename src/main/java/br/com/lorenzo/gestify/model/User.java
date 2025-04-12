package br.com.lorenzo.gestify.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String name;
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user")
    private List<StudySession> studySessions = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Achievement> achievements = new ArrayList<>();
}