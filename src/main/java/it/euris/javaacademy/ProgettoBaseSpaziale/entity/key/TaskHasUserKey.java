package it.euris.javaacademy.ProgettoBaseSpaziale.entity.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class TaskHasUserKey implements Serializable {

    @Column(name = "user_id")
    Integer userId;
    @Column(name = "task_id")
    Integer taskId;
}
