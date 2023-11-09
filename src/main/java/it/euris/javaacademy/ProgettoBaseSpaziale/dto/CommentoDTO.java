package it.euris.javaacademy.ProgettoBaseSpaziale.dto;

import it.euris.javaacademy.ProgettoBaseSpaziale.dto.archetype.Dto;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Commento;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Task;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.User;
import lombok.*;

import java.time.LocalDateTime;

import static it.euris.javaacademy.ProgettoBaseSpaziale.utils.Converter.stringToInteger;
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
    private String taskName;
    private String taskId;
    private String userId;
    private String userName;
    private String trelloId;

    @Override
    public Commento toModel() {
        return Commento.builder()
                .idCommento(idCommento)
                .commento(commento)
                .dataCommento(dataCommento == null ? stringToLocalDateTime(dataCommento = LocalDateTime.now()
                        .toString()) : stringToLocalDateTime(dataCommento))
                .task(Task.builder().idTask(stringToInteger(taskId)).build())
                .user(User.builder().idUser(stringToInteger(userId)).build())
                .trelloId(trelloId)
                .build();
    }
}
