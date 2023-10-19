package it.euris.javaacademy.ProgettoBaseSpaziale.dto;

import it.euris.javaacademy.ProgettoBaseSpaziale.dto.archetype.Dto;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.archetype.Model;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Task;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.TaskHasUser;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.User;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.key.TaskHasUserKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.*;

import static it.euris.javaacademy.ProgettoBaseSpaziale.utils.Converter.stringToInteger;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskHasUserDTO implements Dto {


    private String user;
    private String task;

    @Override
    public TaskHasUser toModel() {
        return TaskHasUser.builder()
                .taskHasUserKey(new TaskHasUserKey(stringToInteger(user),stringToInteger(task)))
                .user(User.builder().idUser(stringToInteger(user)).build())
                .task(Task.builder().idTask(stringToInteger(task)).build())
                .build();
    }
}
