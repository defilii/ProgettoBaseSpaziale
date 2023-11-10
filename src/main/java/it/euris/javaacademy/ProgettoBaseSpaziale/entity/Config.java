package it.euris.javaacademy.ProgettoBaseSpaziale.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "config")
public class Config {

    @Id
    @Column(name = "action", nullable = false)
    private String action;

    @Column(name = "url", nullable = false)
    private String url;

}
