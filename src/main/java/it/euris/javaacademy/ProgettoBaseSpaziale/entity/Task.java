package it.euris.javaacademy.ProgettoBaseSpaziale.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.euris.javaacademy.ProgettoBaseSpaziale.converter.LocalEntity;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.TaskDTO;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.archetype.Model;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.archetype.ModelToPreInsert;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.pre_insert.TaskInsert;
import it.euris.javaacademy.ProgettoBaseSpaziale.trello.Card;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static it.euris.javaacademy.ProgettoBaseSpaziale.utils.Converter.localDateTimeToString;

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

    @Column(name = "task_name", nullable = false)
    private String taskName;

    @Column(name = "descrizione", nullable = true)
    private String descrizione;

    @Column(name = "data_scadenza", nullable = true)
    private LocalDateTime dataScadenza;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tabella", nullable = false)
    private Tabella tabella;

    @OneToMany(mappedBy = "task", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    private List<Commento> commenti = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "task_has_user",
            joinColumns = {@JoinColumn(name = "task_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    @Builder.Default
    private List<User> users = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "priority_tasks",
            joinColumns = {@JoinColumn(name = "task_id")},
            inverseJoinColumns = {@JoinColumn(name = "priority_id")})
    @Builder.Default
    private List<Priority> priorities = new ArrayList<>();

    @OneToMany(mappedBy = "task", fetch = FetchType.EAGER)
    @JsonIgnore
    @Builder.Default
    private List<Checklist> checklist = new ArrayList<>();

    @Column(name = "trello_id")
    private String trelloId;

    @Column(name = "last_update", nullable = false)
    @Builder.Default
    private LocalDateTime lastUpdate = LocalDateTime.now();


    @Column(name = "trello_list_id")
    private String trelloListId;

    @Override
    public TaskDTO toDto() {
        return TaskDTO.builder()
                .idTask(idTask)
                .tabella(tabella)
                .taskName(taskName)
                .descrizione(descrizione)
                .trelloId(trelloId)
                .trelloListId(trelloListId)
                .dataScadenza(localDateTimeToString(dataScadenza))
                .priorita(priorities.stream().map(Priority::getName).toList().toString())
                .lastUpdate(lastUpdate.toString())
                .build();
    }

    @Override
    public Card toTrelloEntity() {
        return Card.builder()
                .localId(String.valueOf(idTask))
                .name(taskName)
                .id(String.valueOf(trelloId))
                .due(String.valueOf(dataScadenza))
                .idList(trelloListId)
                .dateLastActivity(String.valueOf(lastUpdate))
                .desc(descrizione)
//                .labels(priorities.stream().map(Priority::toTrelloEntity).toList())
                .idLabels(priorities.stream().map(Priority::getTrelloId).toList())
                .build();
    }

    @Override
    public TaskInsert toPreInsert() {
        return TaskInsert.builder()
                .id(idTask)//
                .taskName(taskName)
                .descrizione(descrizione)
                .tabella(tabella.toPreInsert())
//                .trelloId(trelloId)
                .build();
    }


    public void addUser(User user) {
        users.add(user);
    }

    public void addPriority(Priority priority) {
        priorities.add(priority);

    }
}


