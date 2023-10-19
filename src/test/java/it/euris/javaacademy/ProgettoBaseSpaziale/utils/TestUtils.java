package it.euris.javaacademy.ProgettoBaseSpaziale.utils;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.*;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.enums.Priorita;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.key.TaskHasUserKey;

import java.time.LocalDateTime;

public class TestUtils {

    public static Task getTask(Integer id) {
        return Task.builder()
                .idTask(id)
                .taskName("Test name")
                .priorita(Priorita.ALTA)
                .descrizione("Test descrizione")
                .dataScadenza(LocalDateTime.now().minusDays(3))
                .tabella(getTabella(id))
                .build();
    }

    public static Task getTaskThatExpiresInMinusDays(Integer id, Integer days) {
        return Task.builder()
                .idTask(id)
                .taskName("Test name")
                .priorita(Priorita.ALTA)
                .descrizione("Test descrizione")
                .dataScadenza(LocalDateTime.now().minusDays(days))
                .tabella(getTabella(id))
                .build();
    }

    public static User getUser(Integer id) {
        return User.builder()
                .idUser(id)
                .username("Test username")
                .email("Test email")
                .build();
    }

    public static TaskHasUser getTaskHasUser(User user) {
        Task task = getTask(1);

        return TaskHasUser.builder()
                .task(task)
                .user(user)
                .taskHasUserKey(TaskHasUserKey.builder()
                        .taskId(task.getIdTask())
                        .userId(user.getIdUser())
                        .build())
                .build();
    }

    public static TaskHasUser getTaskHasUser(Task task) {
        User user = getUser(1);

        return TaskHasUser.builder()
                .task(task)
                .user(user)
                .taskHasUserKey(TaskHasUserKey.builder()
                        .taskId(task.getIdTask())
                        .userId(user.getIdUser())
                        .build())
                .build();
    }

    public static TaskHasUser getTaskHasUser(Integer id) {
        User user = getUser(id);
        Task task = getTask(id);

        return TaskHasUser.builder()
                .task(task)
                .user(user)
                .taskHasUserKey(TaskHasUserKey.builder()
                        .taskId(task.getIdTask())
                        .userId(user.getIdUser())
                        .build())
                .build();
    }

    public static TaskHasUser getTaskHasUserId(Integer idTask, Integer idUser) {

        return TaskHasUser.builder()
                .taskHasUserKey(new TaskHasUserKey(idUser,idTask))
                .task(Task.builder().build())
                .user(User.builder().build())
                .build();
    }

    public static TaskHasUser getTaskHasUser(Integer id) {
        User user = getUser(id);
        Task task = getTask(id);

        return TaskHasUser.builder()
                .task(task)
                .user(user)
                .taskHasUserKey(TaskHasUserKey.builder()
                        .taskId(task.getIdTask())
                        .userId(user.getIdUser())
                        .build())
                .build();
    }

    public static Tabella getTabella(Integer id) {
        return Tabella.builder()
                .id(id)
                .nome("Test nome")
                .build();
    }

    public static Checklist getCheckList(Integer id) {

        return Checklist
                .builder()
                .idChecklist(id)
                .nome("test nome")
                .task(getTask(id))
                .build();


    }


    public static Checkmark getCheckmark(Integer id) {

        return Checkmark
                .builder()
                .idCheckmark(id)
                .checklist(Checklist.builder().build())
                .descrizione("test descrizione")
                .build();

    }


    public static Commento getCommento(Integer id) {

        return Commento
                .builder()
                .idCommento(id)
                .dataCommento(LocalDateTime.now())
                .task(getTask(id))
                .user(getUser(id))
                .build();
    }

}
