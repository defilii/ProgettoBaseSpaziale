package it.euris.javaacademy.ProgettoBaseSpaziale.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.euris.javaacademy.ProgettoBaseSpaziale.converter.LocalEntity;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.TabellaDTO;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.archetype.Model;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.archetype.ModelToPreInsert;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.pre_insert.TabellaInsert;
import it.euris.javaacademy.ProgettoBaseSpaziale.trello.ListTrello;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static it.euris.javaacademy.ProgettoBaseSpaziale.utils.Converter.localDateTimeToString;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "tabella")
public class Tabella implements Model, LocalEntity, ModelToPreInsert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @OneToMany(mappedBy = "tabella", fetch = FetchType.EAGER)
    @JsonIgnore
    @Builder.Default
    private List<Task> tasks = new ArrayList<Task>();

    @Column(name = "last_update", nullable = false)
    @Builder.Default
    private LocalDateTime lastUpdate = LocalDateTime.now();

    @Column(name = "trello_id")
    private String trelloId;


    @Column(name = "trello_board_id")
    private String trelloBoardId;

    @Override
    public TabellaDTO toDto() {
        return TabellaDTO.builder()
                .id(id)
                .nome(nome)
                .lastUpdate(localDateTimeToString(lastUpdate))
                .trelloId(trelloId)
                .build();
    }

    @Override
    public ListTrello toTrelloEntity() {
        return ListTrello.builder()
                .id(trelloId)
                .idBoard(trelloBoardId)
                .name(nome)
                .build();
    }

    @Override
    public TabellaInsert toPreInsert() {
        return TabellaInsert.builder()
                .id(id)
                .nome(nome)
                .build();
    }
}
