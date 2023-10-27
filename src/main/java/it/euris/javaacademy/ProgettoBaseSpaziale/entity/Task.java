package it.euris.javaacademy.ProgettoBaseSpaziale.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.euris.javaacademy.ProgettoBaseSpaziale.converter.LocalEntity;
import it.euris.javaacademy.ProgettoBaseSpaziale.converter.TrelloEntity;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.TaskDTO;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.archetype.Model;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.enums.Priorita;
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
public class Task implements Model, LocalEntity, UpdateTime {

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

    @Column(name = "trello_id")
    private String trelloId;

     LocalDateTime lastUpdate;


    @Column(name = "trello_list_id")
    private String trelloListId;
    @Override
    public TaskDTO toDto() {
        return TaskDTO.builder()
                .idTask(idTask)
                .tabella(tabella)
                .taskName(taskName)
                .priorita(prioritaToString(priorita))
                .descrizione(descrizione)
                .dataScadenza(localDateTimeToString(dataScadenza))
                .lastUpdate(checkLastUpdate())
                .trelloId(trelloId)
                .build();
    }

    @Override
    public Card toTrelloEntity() {
        TrelloLabel trelloLabel = TrelloLabel.builder().name(
                prioritaToLabel()
        ).build();

        return Card.builder()
                .localId(String.valueOf(idTask))
                .name(taskName)
                .id(trelloId)
                .due(String.valueOf(dataScadenza))
                .idList(trelloListId)
                .dateLastActivity(String.valueOf(lastUpdate))
                .desc(descrizione)
                .labels(List.of(trelloLabel))
                .build();
    }

    @Override
    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    @Override
    public LocalDateTime checkLastUpdate() {
        LocalDateTime taskLastUpdate = lastUpdate;
        Card card = toTrelloEntity();
        LocalDateTime cardLastUpdate = (card == null) ? null : card.toLocalEntity().getLastUpdate();
        if (cardLastUpdate==null) {
            return taskLastUpdate;
        } else if (taskLastUpdate.isAfter(cardLastUpdate)) {
            return taskLastUpdate;
        } else {
            return cardLastUpdate;
        }
    }


    private String prioritaToLabel() {
        if(null == priorita) {
            return null;
        }
        switch (priorita){
            case BASSA -> {
                return "Priorita bassa";
            }
            case ALTA -> {
                return "Priorita alta";
            }
            case MEDIA -> {
                return "Priorita media";
            }
            case DESIDERATA -> {
                return "Desiderata";
            }
            default -> {
                return null;
            }
        }
    }

}
