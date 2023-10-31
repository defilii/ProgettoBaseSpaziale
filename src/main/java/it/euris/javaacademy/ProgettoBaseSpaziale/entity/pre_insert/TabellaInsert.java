package it.euris.javaacademy.ProgettoBaseSpaziale.entity.pre_insert;

import it.euris.javaacademy.ProgettoBaseSpaziale.dto.archetype.PreInsert;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Tabella;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TabellaInsert implements PreInsert {
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
