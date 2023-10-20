package it.euris.javaacademy.ProgettoBaseSpaziale.entity;

import it.euris.javaacademy.ProgettoBaseSpaziale.dto.CheckmarkDTO;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.archetype.Model;
import jakarta.persistence.*;
import lombok.*;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "checkmark")
public class Checkmark implements Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer idCheckmark;

    @Column(name = "descrizione", nullable = false)
    private String descrizione;

    @Column(name = "isItDone", nullable=false)
    @Builder.Default
    private Boolean isItDone = false;

    @ManyToOne
    @JoinColumn(name = "id_checklist", nullable = false)
    private Checklist checklist;

    @Override
    public CheckmarkDTO toDto() {
        return CheckmarkDTO.builder()
                .idCheckmark(idCheckmark)
                .descrizione(descrizione)
                .isItDone(isItDone)
                .checklist(checklist)
                .build();
    }
}
