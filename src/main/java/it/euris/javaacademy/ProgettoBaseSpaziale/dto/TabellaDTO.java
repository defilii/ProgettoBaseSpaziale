package it.euris.javaacademy.ProgettoBaseSpaziale.dto;


import it.euris.javaacademy.ProgettoBaseSpaziale.dto.archetype.Dto;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.archetype.Model;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Tabella;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TabellaDTO implements Dto {
    private Integer id;

    private String nome;

    @Override
    public Tabella toModel() {
        return Tabella.builder()
                .id(id)
                .nome(nome)
                .build();
    }
}
