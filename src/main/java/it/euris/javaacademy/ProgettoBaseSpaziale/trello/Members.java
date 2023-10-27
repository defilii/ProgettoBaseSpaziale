package it.euris.javaacademy.ProgettoBaseSpaziale.trello;

import it.euris.javaacademy.ProgettoBaseSpaziale.converter.TrelloEntity;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.User;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Members implements TrelloEntity {

    private String id;
    private String fullName;
    private String username;
    @Override
    public User toLocalEntity() {
        return User.builder()
                .trelloId(id)
                .username(username)
                .fullName(fullName)
                .build();
    }
}
