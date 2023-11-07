package it.euris.javaacademy.ProgettoBaseSpaziale.controller;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Priority;
import it.euris.javaacademy.ProgettoBaseSpaziale.synchronization.LocalDBCalls;
import it.euris.javaacademy.ProgettoBaseSpaziale.synchronization.SynchronizeFromTrello;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@Data
@RequestMapping("/synchronize")
public class SynchronizationController {

    SynchronizeFromTrello synchronizeFromTrello;

    LocalDBCalls synchronizeToTrello;

    @PutMapping("/synchronizeToTrello")
    public void synchronize() {
        synchronizeToTrello.synchronize();
    }

    @PutMapping("/test")
    public void testingMethods() {
        Priority priority = Priority.builder()
                .color("purple")
                .name("name")
                .build();

        synchronizeToTrello.postNewPriority(priority);

        priority.setName("new name");
        priority.setColor("black");
        synchronizeToTrello.updatePriority(priority);
    }

    @PutMapping("/synchronizeFromTrello")
    private void insertsSmooth() {
        synchronizeFromTrello.updateAllTaskAndTabella();
    }
}