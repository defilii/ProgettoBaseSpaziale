package it.euris.javaacademy.ProgettoBaseSpaziale.dto;

import it.euris.javaacademy.ProgettoBaseSpaziale.dto.archetype.Dto;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Checklist;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Checkmark;
import lombok.*;

import java.time.LocalDateTime;

import static it.euris.javaacademy.ProgettoBaseSpaziale.utils.Converter.stringToLocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckmarkDTO implements Dto {

    private Integer idCheckmark;

    private String descrizione;

    private Boolean isItDone;
    private String trelloId;

    private Checklist checklist;
    private String lastUpdate;
    @Override
    public Checkmark toModel() {
        return Checkmark.builder()
                .idCheckmark(idCheckmark)
                .descrizione(descrizione)
                .isItDone(isItDone)
                .checklist(checklist)
                .lastUpdate(stringToLocalDateTime(lastUpdate))
                .trelloId(trelloId)

                .build();
    }
}
