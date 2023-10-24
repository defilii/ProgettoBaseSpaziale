package it.euris.javaacademy.ProgettoBaseSpaziale.utils;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.*;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.enums.Priorita;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.key.TaskHasUserKey;

import java.time.LocalDateTime;
import java.util.List;

public class TestUtils {

    public static Task getTask(Integer id) {
        return Task.builder()
                .idTask(id)
                .taskName("Test name")
                .priorita(Priorita.ALTA)
                .descrizione("Test descrizione")
                .dataScadenza(LocalDateTime.now().plusDays(3))
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
                .fullName("Test email")
                .build();
    }

    public static TaskHasUser getTaskHasUserSingleId(User user) {
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

    public static TaskHasUser getTaskHasUserSingleId(Task task) {
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

    public static TaskHasUser getTaskHasUserId(Integer idTask, Integer idUser) {

        return TaskHasUser.builder()
                .taskHasUserKey(new TaskHasUserKey(idUser,idTask))
                .task(Task.builder().idTask(idTask).build())
                .user(User.builder().idUser(idUser).build())
                .build();
    }

    public static TaskHasUser getTaskHasUserSingleId(Integer id) {
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
                .tasks(List.of(Task.builder().idTask(1).descrizione("test").build()))
                .nome("Test nome")
                .build();
    }

    public static Tabella getTabellaTaskMediaPriorita(Integer id) {
        return Tabella.builder()
                .id(id)
                .tasks(List.of(Task.builder().idTask(1).priorita(Priorita.MEDIA).descrizione("test").build()))
                .nome("Test nome")
                .build();
    }



    public static Tabella getTabellaTaskPrioritaDesiderata(Integer id) {
        return Tabella.builder()
                .id(id)
                .tasks(List.of(Task.builder().idTask(1).priorita(Priorita.DESIDERATA).descrizione("test").build()))
                .nome("Test nome")
                .build();
    }

    public static Tabella getTabellaTaskBassaPriorita(Integer id) {
        return Tabella.builder()
                .id(id)
                .tasks(List.of(Task.builder().idTask(1).priorita(Priorita.BASSA).descrizione("test").build()))
                .nome("Test nome")
                .build();
    }

    public static Tabella getTabellaExpire( Integer days) {
        return Tabella.builder()
                .id(1)
                .tasks(List.of(Task.builder().idTask(1).priorita(Priorita.BASSA)
                        .dataScadenza(LocalDateTime.now().plusDays(1)).build()))
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


    public static Commento getCommentoUserTask(Integer id, Integer idTask, Integer idUser) {

        return Commento
                .builder()
                .idCommento(id)
                .dataCommento(LocalDateTime.now())
                .task(Task.builder().idTask(idTask).build())
                .user(User.builder().idUser(idUser).build())
                .build();
    }
}
