package it.euris.javaacademy.ProgettoBaseSpaziale.dto;


import it.euris.javaacademy.ProgettoBaseSpaziale.dto.archetype.Dto;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Tabella;
import lombok.*;

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
    private String trelloBoardId;
    @Override
    public Tabella toModel() {

        return Tabella.builder()
                .id(id)
                .nome(nome)
                .trelloId(trelloId)
                .trelloBoardId(trelloBoardId)
                .build();
    }
}
