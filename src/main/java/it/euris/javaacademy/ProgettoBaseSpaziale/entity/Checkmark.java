package it.euris.javaacademy.ProgettoBaseSpaziale.entity;

import it.euris.javaacademy.ProgettoBaseSpaziale.dto.CheckmarkDTO;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.archetype.Model;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import static it.euris.javaacademy.ProgettoBaseSpaziale.utils.Converter.localDateTimeToString;


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

    @Column(name = "last_update", nullable=false)
    @Builder.Default
    private LocalDateTime lastUpdate = LocalDateTime.now();

    @Column(name = "trello_id")
    private String trelloId;
    @Override
    public CheckmarkDTO toDto() {
        return CheckmarkDTO.builder()
                .idCheckmark(idCheckmark)
                .descrizione(descrizione)
                .isItDone(isItDone)
                .checklist(checklist)
                .lastUpdate(localDateTimeToString(lastUpdate))
                .build();
    }
}
