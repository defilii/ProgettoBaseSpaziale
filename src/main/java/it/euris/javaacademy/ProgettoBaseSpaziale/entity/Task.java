package it.euris.javaacademy.ProgettoBaseSpaziale.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.TaskDTO;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.archetype.Dto;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.archetype.Model;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.enums.Priorita;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static it.euris.javaacademy.ProgettoBaseSpaziale.utils.Converter.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "task")
public class Task implements Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer idTask;

    @Column(name = "task_name", nullable=false)
    private String taskName;

    @Column(name = "priorita", nullable=true)
    @Enumerated(EnumType.STRING)
    private Priorita priorita;

    @Column(name = "descrizione", nullable=true)
    private String descrizione;

    @Column(name = "data_scadenza", nullable=true)
    private LocalDateTime dataScadenza;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="id_tabella", nullable=false)
    private Tabella tabella;

    @OneToMany(mappedBy = "task", fetch = FetchType.EAGER)
    @JsonIgnore
    @Builder.Default
    private List<Commento> commenti = new ArrayList<>();

    @OneToMany(mappedBy = "task")
    @JsonIgnore
    @Builder.Default
    private List<TaskHasUser> usersTask = new ArrayList<>();

    @OneToMany(mappedBy = "task", fetch = FetchType.EAGER)
    @JsonIgnore
    @Builder.Default
    private List<Checklist> checklist = new ArrayList<>();

    @Override
    public TaskDTO toDto() {
        return TaskDTO.builder()
                .idTask(idTask)
                .tabella(tabella)
                .taskName(taskName)
                .priorita(prioritaToString(priorita))
                .descrizione(descrizione)
                .dataScadenza(localDateTimeToString(dataScadenza))
                .build();
    }
}
