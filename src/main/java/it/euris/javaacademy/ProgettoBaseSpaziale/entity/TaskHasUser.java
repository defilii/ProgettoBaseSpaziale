package it.euris.javaacademy.ProgettoBaseSpaziale.entity;

import it.euris.javaacademy.ProgettoBaseSpaziale.dto.TaskHasUserDTO;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.archetype.Dto;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.archetype.Model;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.key.TaskHasUserKey;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "task_has_user")
public class TaskHasUser implements Model {
    @EmbeddedId
    TaskHasUserKey taskHasUserKey;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("taskId")
    @JoinColumn(name = "task_id")
    Task task;

    @Override
    public TaskHasUserDTO toDto() {
        return TaskHasUserDTO.builder()
                .task(task == null ? null : task.getIdTask().toString())
                .user(user == null ? null : user.getIdUser().toString())
                .build();
    }
}
