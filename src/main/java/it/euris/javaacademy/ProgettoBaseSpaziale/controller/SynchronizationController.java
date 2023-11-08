package it.euris.javaacademy.ProgettoBaseSpaziale.controller;

import it.euris.javaacademy.ProgettoBaseSpaziale.synchronization.LocalDBCalls;
import it.euris.javaacademy.ProgettoBaseSpaziale.synchronization.SynchronizeFromTrello;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@Data
@Component
@RequestMapping("/synchronize")
public class SynchronizationController {

    SynchronizeFromTrello synchronizeFromTrello;

    LocalDBCalls synchronizeToTrello;
//    @Scheduled(cron = "3 * * ? * *")
    @PutMapping("/synchronizeToTrello")
    public void synchronize() {
        synchronizeToTrello.synchronize();
    }

//    @Scheduled(cron = "3 * * ? * *")
    @PutMapping("/synchronizeFromTrello")
    private void insertsSmooth() {
        synchronizeFromTrello.updateAllTaskAndTabella();
    }
}