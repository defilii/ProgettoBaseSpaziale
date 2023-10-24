package it.euris.javaacademy.ProgettoBaseSpaziale.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.euris.javaacademy.ProgettoBaseSpaziale.converter.LocalEntity;
import it.euris.javaacademy.ProgettoBaseSpaziale.converter.TrelloEntity;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.UserDTO;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.archetype.Model;
import it.euris.javaacademy.ProgettoBaseSpaziale.trello.Members;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User implements Model, LocalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer idUser;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "trello_id")
    private String trelloId;

    @Column(name = "email", nullable = false)
    private String fullName;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    @Builder.Default
    private List<TaskHasUser> usersTask = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    @Builder.Default
    private List<Commento> commenti = new ArrayList<>();

    @Override
    public UserDTO toDto() {
        return UserDTO.builder()
                .idUser(idUser)
                .username(username)
                .fullName(fullName)
                .build();
    }

    @Override
    public Members toTrelloEntity() {
        return Members.builder()
                .fullName(fullName)
                .username(username)
                .localId(String.valueOf(idUser))
                .build();
    }
}
