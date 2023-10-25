package it.euris.javaacademy.ProgettoBaseSpaziale.dto;


import it.euris.javaacademy.ProgettoBaseSpaziale.dto.archetype.Dto;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Tabella;
import lombok.*;

import java.time.LocalDateTime;

import static it.euris.javaacademy.ProgettoBaseSpaziale.utils.Converter.stringToLocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TabellaDTO implements Dto {
    private Integer id;
    private String trelloId;

    private String nome;
    private String lastUpdate;
    @Override
    public Tabella toModel() {
        return Tabella.builder()
                .id(id)
                .nome(nome)
                .lastUpdate(stringToLocalDateTime(lastUpdate))
                .trelloId(trelloId)

                .build();
    }
}
