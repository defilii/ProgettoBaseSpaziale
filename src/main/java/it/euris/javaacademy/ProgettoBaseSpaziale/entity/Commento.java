package it.euris.javaacademy.ProgettoBaseSpaziale.entity;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.enums.Priorita;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "commento")
public class Commento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer idCommento;

    @Column(name = "commento", nullable=false)
    private String commento;

    @Column(name = "data_commento", nullable=false)
    private LocalDateTime data_commento;

    @ManyToOne
    @JoinColumn(name="id_task", nullable=false)
    private Task task;

    @ManyToOne
    @JoinColumn(name="id_user", nullable=false)
    private User user;
}
