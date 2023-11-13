package it.euris.javaacademy.ProgettoBaseSpaziale.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.euris.javaacademy.ProgettoBaseSpaziale.converter.LocalEntity;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.ChecklistDTO;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.archetype.Model;
import it.euris.javaacademy.ProgettoBaseSpaziale.trello.TrelloChecklist;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static it.euris.javaacademy.ProgettoBaseSpaziale.utils.Converter.localDateTimeToString;
import static it.euris.javaacademy.ProgettoBaseSpaziale.utils.Converter.numberToString;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
//@Data
@Table(name = "checklist")
public class Checklist implements Model, LocalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer idChecklist;

    @Column(name = "nome", nullable=false)
    private String nome;

    @ManyToOne /*(fetch = FetchType.EAGER)*/
    @JoinColumn(name="id_task", nullable=false)
//    @JsonIgnore
    private Task task;

    @OneToMany(mappedBy = "checklist", fetch = FetchType.EAGER)
    @JsonIgnore
    @Builder.Default
    private List<Checkmark> checklist = new ArrayList<>();

    @Column(name = "last_update", nullable=false)
    @Builder.Default
    private LocalDateTime lastUpdate = LocalDateTime.now();

    @Column(name = "trello_id")
    private String trelloId;
    @Override
    public ChecklistDTO toDto() {
        return ChecklistDTO.builder()
                .idChecklist(idChecklist)
                .nome(nome)
                .taskId(task == null ? null : numberToString(task.getIdTask()))
                .taskName(task == null ? null : task.getTaskName())
                .trelloId(trelloId)
                .lastUpdate(localDateTimeToString(lastUpdate))
                .build();
    }

    @Override
    public TrelloChecklist toTrelloEntity() {
        return TrelloChecklist.builder()
                .name(nome)
                .checkItems(checklist.stream().map(Checkmark::toTrelloEntity).toList())
                .build();
    }
}
