package it.euris.javaacademy.ProgettoBaseSpaziale.controller;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Task;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.*;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.*;
import it.euris.javaacademy.ProgettoBaseSpaziale.synchronization.SynchronizeFromTrello;
import it.euris.javaacademy.ProgettoBaseSpaziale.trello.Card;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@Data
@RequestMapping("/synchronizeFromTrello")
public class SynchronizationController {

    SynchronizeFromTrello synchronizeFromTrello;

    @PutMapping("/synchronize")
    private void insertsSmooth() {
        synchronizeFromTrello.updateAllTaskAndTabella();
    }
}