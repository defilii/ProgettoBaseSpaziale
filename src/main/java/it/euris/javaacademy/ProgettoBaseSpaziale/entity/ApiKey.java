package it.euris.javaacademy.ProgettoBaseSpaziale.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "api_key")
public class ApiKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer idApikey;

    @Column(name = "apikey", nullable = false)
    private String key;

    @Column(name = "token", nullable=false)
    private String token;

    @Column(name = "data_inserimento", nullable=false)
    @Builder.Default
    private LocalDateTime dataInserimento = LocalDateTime.now();
}
