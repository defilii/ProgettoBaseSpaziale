package it.euris.javaacademy.ProgettoBaseSpaziale.entity;

import it.euris.javaacademy.ProgettoBaseSpaziale.converter.LocalEntity;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.PriorityDTO;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.archetype.Model;
import it.euris.javaacademy.ProgettoBaseSpaziale.trello.TrelloLabel;
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
@ToString
@Table(name = "priority")
public class Priority implements Model, LocalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "color")
    private String color;

    @Column(name = "trello_id")
    private String trelloId;

    @Column(name = "name")
    private String name;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy = "priorities")
    @Builder.Default
    private List<Task> tasks = new ArrayList<>();

    @Override
    public TrelloLabel toTrelloEntity() {
        return TrelloLabel.builder()
                .color(color)
                .id(trelloId)
                .name(name)
                .build();
    }

    @Override
    public PriorityDTO toDto() {
        return PriorityDTO.builder()
                .idPriority(id)
                .color(color)
                .name(name)
                .trelloId(trelloId)
                .build();
    }

    public void addTask (Task task){
        tasks.add(task);
    }
}
