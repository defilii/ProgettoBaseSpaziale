package it.euris.javaacademy.ProgettoBaseSpaziale.repositoy;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.TaskHasUser;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.key.TaskHasUserKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskHasUserRepository extends JpaRepository<TaskHasUser, TaskHasUserKey> {
}
