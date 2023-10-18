package it.euris.javaacademy.ProgettoBaseSpaziale.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.UserDTO;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.archetype.Dto;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.archetype.Model;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User implements Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer idUser;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", nullable = false)
    private String email;

    @ManyToOne
    @JoinColumn(name="id_task", nullable=true)
    private Task task;

    @OneToMany(mappedBy = "commento", fetch = FetchType.EAGER)
    @JsonIgnore
    @Builder.Default
    private List<Commento> checklist = new ArrayList<>();

    @Override
    public UserDTO toDto() {
        return UserDTO.builder()
                .idUser(idUser)
                .username(username)
                .email(email)
                .task(task)
                .build();
    }
}
