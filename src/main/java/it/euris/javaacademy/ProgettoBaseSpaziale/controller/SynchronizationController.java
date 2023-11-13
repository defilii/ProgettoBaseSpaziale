package it.euris.javaacademy.ProgettoBaseSpaziale.controller;

import it.euris.javaacademy.ProgettoBaseSpaziale.service.ConfigService;
import it.euris.javaacademy.ProgettoBaseSpaziale.synchronization.LocalDBCalls;
import it.euris.javaacademy.ProgettoBaseSpaziale.synchronization.SynchronizeFromTrello;
import it.euris.javaacademy.ProgettoBaseSpaziale.synchronization.SynchronizeLabelsToCardsFromTrello;
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
    SynchronizeLabelsToCardsFromTrello synchronizeLabelsToCardsFromTrello;
    ConfigService configService;

    LocalDBCalls synchronizeToTrello;


    @Scheduled(cron = "0 */11 * * * ?")
    @PutMapping("/synchronizeToTrello")
    public void synchronizeToTrello() {
        synchronizeToTrello.synchronize();
    }

    @Scheduled(cron = "0 */3 * * * ?")
    @PutMapping("/synchronizeFromTrello")
    private void synchronizeFromTrello() {
        synchronizeFromTrello.updateAllTaskAndTabella();
        synchronizeLabelsToCardsFromTrello.updateLabelsToCard();
    }
}