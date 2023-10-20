package it.euris.javaacademy.ProgettoBaseSpaziale.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.ChecklistDTO;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.archetype.Model;
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
@Table(name = "checklist")
public class Checklist implements Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer idChecklist;

    @Column(name = "nome", nullable=false)
    private String nome;

    @ManyToOne
    @JoinColumn(name="id_task", nullable=false)
    private Task task;

    @OneToMany(mappedBy = "checklist", fetch = FetchType.EAGER)
    @JsonIgnore
    @Builder.Default
    private List<Checkmark> checklist = new ArrayList<>();

    @Override
    public ChecklistDTO toDto() {
        return ChecklistDTO.builder()
                .idChecklist(idChecklist)
                .nome(nome)
                .task(task)
                .build();
    }
}
