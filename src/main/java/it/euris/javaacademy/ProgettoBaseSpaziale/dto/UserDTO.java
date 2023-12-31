package it.euris.javaacademy.ProgettoBaseSpaziale.dto;

import it.euris.javaacademy.ProgettoBaseSpaziale.dto.archetype.Dto;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.User;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Dto {
    private Integer idUser;

    private String username;

    private String email;

   // private Task task;


    @Override
    public User toModel() {
        return User.builder()
                .idUser(idUser)
                .username(username)
                .email(email)
                .build();
    }
}
