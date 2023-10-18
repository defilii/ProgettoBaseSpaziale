package it.euris.javaacademy.ProgettoBaseSpaziale.dto;

import it.euris.javaacademy.ProgettoBaseSpaziale.dto.archetype.Dto;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.archetype.Model;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Commento;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Task;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.User;
import lombok.*;


import static it.euris.javaacademy.ProgettoBaseSpaziale.utils.Converter.stringToLocalDateTime;
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentoDTO implements Dto {

    private Integer idCommento;
    private String commento;
    private String dataCommento;
    private Task task;
    private User user;

    @Override
    public Commento toModel() {
        return Commento.builder()
                .idCommento(idCommento)
                .commento(commento)
                .dataCommento(stringToLocalDateTime(dataCommento))
                .task(task)
                .user(user)
                .build();
    }
}
