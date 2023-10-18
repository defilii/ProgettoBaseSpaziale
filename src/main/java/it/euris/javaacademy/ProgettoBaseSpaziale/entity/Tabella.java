package it.euris.javaacademy.ProgettoBaseSpaziale.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "tabella")
public class Tabella {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nome", nullable=false)
    private String nome;

    @OneToMany(mappedBy = "tabella", fetch = FetchType.EAGER)
    @JsonIgnore
    @Builder.Default
    private List<Task> tasks = new ArrayList<Task>();

}
