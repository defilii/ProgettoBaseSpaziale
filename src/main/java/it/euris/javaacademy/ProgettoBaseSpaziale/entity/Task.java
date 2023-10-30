package it.euris.javaacademy.ProgettoBaseSpaziale.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.euris.javaacademy.ProgettoBaseSpaziale.converter.LocalEntity;
import it.euris.javaacademy.ProgettoBaseSpaziale.converter.TrelloEntity;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.TaskDTO;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.archetype.Model;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.archetype.ModelToPreInsert;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.archetype.PreInsert;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.enums.Priorita;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.pre_insert.TaskInsert;
import it.euris.javaacademy.ProgettoBaseSpaziale.trello.Card;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static it.euris.javaacademy.ProgettoBaseSpaziale.utils.Converter.localDateTimeToString;
import static it.euris.javaacademy.ProgettoBaseSpaziale.utils.Converter.prioritaToString;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "task")
public class Task implements Model, LocalEntity, ModelToPreInsert {

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

    @Column(name = "last_update", nullable=false)
    @Builder.Default
    private LocalDateTime lastUpdate = LocalDateTime.now();

    @Column(name = "trello_id")
    private String trelloId;
    @Override
    public TaskDTO toDto() {
        return TaskDTO.builder()
                .idTask(idTask)
                .tabella(tabella)
                .taskName(taskName)
                .priorita(prioritaToString(priorita))
                .descrizione(descrizione)
                .dataScadenza(localDateTimeToString(dataScadenza))
                .lastUpdate(localDateTimeToString(lastUpdate))
                .build();
    }

    @Override
    public Card toTrelloEntity() {
        return Card.builder().build();
    }

    @Override
    public TaskInsert toPreInsert() {
        return TaskInsert.builder()
                .id(idTask)
                .taskName(taskName)
                .descrizione(descrizione)
                .tabella(tabella.toPreInsert())
                .build();
    }
}
