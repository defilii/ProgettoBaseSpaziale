package it.euris.javaacademy.ProgettoBaseSpaziale.dto;

import it.euris.javaacademy.ProgettoBaseSpaziale.dto.archetype.Dto;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Priority;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PriorityDTO implements Dto {
    private Integer idPriority;
    private String name;
    private String color;
    private String trelloId;

    @Override
    public Priority toModel() {
        return Priority.builder()
                .id(idPriority)
                .color(color)
                .trelloId(trelloId)
                .name(name)
                .build();
    }
}
