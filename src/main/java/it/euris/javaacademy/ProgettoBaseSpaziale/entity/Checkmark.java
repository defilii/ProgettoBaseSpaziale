package it.euris.javaacademy.ProgettoBaseSpaziale.entity;

import jakarta.persistence.*;
import lombok.*;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "checkmark")
public class Checkmark {

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
}
