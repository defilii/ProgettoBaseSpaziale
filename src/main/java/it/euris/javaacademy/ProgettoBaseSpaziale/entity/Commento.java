package it.euris.javaacademy.ProgettoBaseSpaziale.entity;

import it.euris.javaacademy.ProgettoBaseSpaziale.dto.CommentoDTO;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.archetype.Dto;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.archetype.Model;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.enums.Priorita;
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
@Table(name = "commento")
public class Commento implements Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer idCommento;

    @Column(name = "commento", nullable=false)
    private String commento;

    @Column(name = "data_commento", nullable=false)
    private LocalDateTime dataCommento;

    @ManyToOne
    @JoinColumn(name="id_task", nullable=false)
    private Task task;

    @ManyToOne
    @JoinColumn(name="id_user", nullable=false)
    private User user;

    @Override
    public CommentoDTO toDto() {
        return CommentoDTO.builder()
                .idCommento(idCommento)
                .commento(commento)
                .dataCommento(localDateTimeToString(dataCommento))
                .task(task)
                .user(user)
                .build();
    }
}
