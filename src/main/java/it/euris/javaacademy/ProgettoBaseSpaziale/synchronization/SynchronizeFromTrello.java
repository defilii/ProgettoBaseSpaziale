package it.euris.javaacademy.ProgettoBaseSpaziale.synchronization;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.*;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.InvalidKeyOrToken;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.*;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.*;
import it.euris.javaacademy.ProgettoBaseSpaziale.trello.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static it.euris.javaacademy.ProgettoBaseSpaziale.utils.Converter.stringToLocalDateTime;

@AllArgsConstructor
@Service
public class SynchronizeFromTrello {

    TabellaService tabellaService;
    TaskService taskService;

    CheckmarkService checkmarkService;
    ChecklistService checklistService;
    ChecklistRepository checklistrepository;
    CheckmarkRepository checkmarkRepository;

    TaskRepository taskRepository;
    TabellaRepository tabellaRepository;
    ApiKeyService apiKeyService;

    PriorityService priorityService;
    PriorityRepository priorityRepository;

    UserService userService;
    UserRepository userRepository;

    CommentoService commentoService;
    CommentoRepository commentoRepository;

    List<ListTrello> allList;
    List<Card> allCard;
    List<TrelloChecklist> allTrelloChecklist;
    List<CheckItem> allCheckitems;
    List<Tabella> allTabella;
    List<Task> allTasks;
    List<Checklist> allChecklist;
    List<Checkmark> allCheckmark;
    List<Priority> allPriority;
    List<TrelloLabel> allLabel;
    List<Members> allMembers;
    List<User> allUser;
    List<Commento> allComments;
    List<TrelloAction> allTrelloActions;

    public void updateAllTaskAndTabella() {
        try {
            updateList();
        } catch (InvalidKeyOrToken e) {
            throw new RuntimeException(e.getMessage());
        }
        String idBoardToSet = allList.stream().map(ListTrello::getIdBoard).findAny().orElse(null);

        allList.stream()
                .forEach(trelloList ->
                {
                    System.out.println("prima di all");
                    if (allTabella.stream()
                            .map(tabella -> tabella.getTrelloId())
                            .collect(Collectors.toList())
                            .contains(trelloList.getId())) {

                        Tabella tabellaDb = tabellaRepository.findByTrelloId(trelloList.getId());
                        if (compareDate(trelloList, tabellaDb)
                        ) {
                            System.out.println("fatto update");
                            Tabella tabellaToUpdate =
                                    trelloList.toLocalEntity();
                            tabellaToUpdate.setId(tabellaRepository
                                    .findByTrelloId(trelloList.getId()).getId());
                            tabellaToUpdate.setTrelloBoardId(idBoardToSet);
                            tabellaToUpdate.setLastUpdate(getMostRescentLocalDateTime(trelloList));
                            tabellaService.update(tabellaToUpdate);

                        }
                    } else {
                        System.out.println("fatto insert");

                        Tabella tabellaToInsert = trelloList.toLocalEntity();
                        tabellaToInsert.setTrelloBoardId(idBoardToSet);
                        tabellaService.insert(tabellaToInsert);

                    }
                });


        allLabel.stream().
                forEach(label ->
                {
                    if (allPriority.stream()
                            .map(Priority::getTrelloId)
                            .toList()
                            .contains(label.getId())) {
                        updateLabel(label);
                    } else {
                        insertLabel(label);
                    }
                });

        allMembers.stream().
                forEach(member ->
                {
                    if (allUser.stream()
                            .map(User::getTrelloId)
                            .toList()
                            .contains(member.getId())) {
                        updateMember(member);
                    } else {
                        insertMember(member);
                    }
                });

        allCard.stream().
                forEach(card ->
                {
                    if (allTasks.stream()
                            .map(Task::getTrelloId)
                            .toList()
                            .contains(card.getId())

                    )
                        updateCard(card);
                    else {
                        insertCard(card);
                    }
                });

        deleteFromDatabase();
    }

    private boolean compareDate(ListTrello trelloList, Tabella tabellaDb) {
        return getMostRescentLocalDateTime(trelloList)
                .isAfter(tabellaDb.getLastUpdate());
    }

    private LocalDateTime getMostRescentLocalDateTime(ListTrello trelloList) {
        return trelloList.toLocalEntity().getTasks().stream()
                .map(Task::getLastUpdate)
                .max(LocalDateTime::compareTo)
                .orElse(LocalDateTime.now().minusYears(1L));
    }


    private void updateList() throws InvalidKeyOrToken {
        TrelloCalls client = new TrelloCalls(apiKeyService);
        allList = client.allTrelloListFromJsonListWithReturn();
        allCard = allList.stream()
                .map(listTrello -> {
                    try {
                        return client.cardsFromJsonListId(listTrello.getId());
                    } catch (InvalidKeyOrToken e) {
                        throw new RuntimeException(e);
                    }
                })
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        allLabel = client.getAllTrelloLabels();
        allMembers = client.getAllMembers();
        allTrelloChecklist = allCard.stream()
                .map(Card::getTrelloChecklists)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        allCheckitems = allTrelloChecklist.stream()
                .map(TrelloChecklist::getCheckItems)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        allTabella = tabellaService.findAll();
        allTasks = taskService.findAll();
        allComments = commentoService.findAll();
        allChecklist = checklistService.findAll();
        allCheckmark = checkmarkService.findAll();
        allPriority = priorityService.findAll();
        allUser = userRepository.findAll();
    }

    private void deleteFromDatabase() {
        allCheckmark.stream()
                .forEach(checkmark ->
                {
                    if (checkmark.getTrelloId() != null && !allCheckitems.stream()
                            .map(checkitem -> checkitem.getIdCheckItem())
                            .toList().contains(checkmark.getTrelloId())) {
                        checkmarkService.deleteById(checkmark.getIdCheckmark());
                    }
                });
        allChecklist.stream()
                .forEach(checklist ->
                {
                    if (checklist.getTrelloId() != null && !allTrelloChecklist.stream()
                            .map(trelloChecklist -> trelloChecklist.getId())
                            .collect(Collectors.toList()).contains(checklist.getTrelloId())) {
                        checklistService.deleteById(checklist.getIdChecklist());
                    }
                });
        allTasks.stream()
                .forEach(task ->
                {
                    if (task.getTrelloId() != null && !allCard.stream()
                            .map(card -> card.getId())
                            .collect(Collectors.toList()).contains(task.getTrelloId())) {
                        taskService.deleteById(task.getIdTask());
                    }
                });

        allTabella.stream()
                .forEach(tabella ->
                {
                    if (tabella.getTrelloId() != null && !allList.stream()
                            .map(listTrello -> listTrello.getId())
                            .collect(Collectors.toList()).contains(tabella.getTrelloId())) {
                        tabellaService.deleteById(tabella.getId());
                    }
                });

    }

    private Task updateCard(Card card) {
        Task dbTask = taskRepository.findByTrelloId(card.getId());
        if (dbTask.getLastUpdate().isBefore(stringToLocalDateTime(card.toLocalEntity().getLastUpdate().toString()))) {
            Task newTask = card.toLocalEntity();
            newTask.setIdTask(taskRepository
                    .findByTrelloId(card.getId()).getIdTask());
            newTask.setTabella(tabellaRepository.findByTrelloId(card.getIdList()));
            Task updatedTask = taskService.update(newTask);
            insertChecklist(card, updatedTask);
            insertMemberTocard(card, updatedTask);
            insertLabelToCard(card, updatedTask);
            System.out.println("fatto update");
            if (!card.getTrelloActions().isEmpty()) {
                insertComment(card, updatedTask);
            }
            return updatedTask;
        } else {
            return dbTask;
        }
    }

    private Task insertCard(Card card) {
        Task newTask = card.toLocalEntity();
        newTask.setTabella(tabellaRepository.findByTrelloId(card.getIdList()));
        Task insertedTask = taskService.insert(newTask);
        insertChecklist(card, insertedTask);
        insertMemberTocard(card, insertedTask);
        insertLabelToCard(card, insertedTask);
        insertComment(card, insertedTask);
        System.out.println("fatto insert");
        return insertedTask;
    }

    private void insertLabelToCard(Card card, Task insertedTask) {
        List<String> idLabels = card.getIdLabels();
        for (String idLabel : idLabels) {
            Priority matchingPriority = priorityRepository.findByTrelloId(idLabel);
            matchingPriority.addTask(insertedTask);
            insertedTask.addPriority(matchingPriority);
        }

    }

    private Priority insertLabel(TrelloLabel label) {
        Priority newPriority = label.toLocalEntity();
        Priority insertedPriority = priorityService.insert(newPriority);
        return insertedPriority;
    }

    private Priority updateLabel(TrelloLabel label) {
        Priority newPriority = label.toLocalEntity();
        newPriority.setId(priorityRepository
                .findByTrelloId(label.getId()).getId());
        Priority insertedPriority = priorityService.update(newPriority);
        return insertedPriority;
    }

    private void insertMemberTocard(Card card, Task insertedTask) {
        List<String> idMembers = card.getIdMembers();
        for (String idMember : idMembers) {
            User matchingUser = userRepository.findByTrelloId(idMember);
            matchingUser.addTask(insertedTask);
            insertedTask.addUser(matchingUser);
        }
    }

    private User insertMember(Members user) {
        User newUser = user.toLocalEntity();
        User insertedUser = userService.insert(newUser);
        return insertedUser;
    }

    private User updateMember(Members members) {
        User newUser = members.toLocalEntity();
        newUser.setIdUser(userRepository
                .findByTrelloId(members.getId()).getIdUser());
        User insertedPriority = userService.update(newUser);
        return insertedPriority;
    }

    private void insertChecklist(Card card, Task updatedTask) {
        List<TrelloChecklist> newChecklists = card.getTrelloChecklists();
        for (TrelloChecklist trelloChecklist :
                newChecklists) {
            Checklist checklistToInsert = trelloChecklist.toLocalEntity();
            checklistToInsert.setTask(updatedTask);
            if (allChecklist.stream()
                    .map(checklist -> checklist.getTrelloId())
                    .collect(Collectors.toList())
                    .contains(trelloChecklist.getId())) {
                Checklist dbChecklist = checklistrepository.findByTrelloId(trelloChecklist.getId());
                if (dbChecklist.getLastUpdate().isBefore(trelloChecklist.toLocalEntity().getLastUpdate())) {
                    checklistToInsert.setIdChecklist(checklistrepository
                            .findByTrelloId(trelloChecklist.getId()).getIdChecklist());
                    Checklist updatedChecklist = checklistService.update(checklistToInsert);
                }

//                insertCheckmark(trelloChecklist,updatedChecklist);
            } else {
                Checklist insertedChecklist = checklistService.insert(checklistToInsert);

//                insertCheckmark(trelloChecklist,insertedChecklist);
            }
        }
    }


    private void insertComment(Card card, Task updatedTask) {
        List<TrelloAction> newComments = card.getTrelloActions();
        for (TrelloAction trelloComment :
                newComments) {
            Commento commentToInsert = trelloComment.toLocalEntity();
            commentToInsert.setTask(updatedTask);
            commentToInsert.setUser(userRepository.findByTrelloId(trelloComment.getIdMemberCreator()));
            if (allComments.stream()
                    .map(Commento::getTrelloId)
                    .toList()
                    .contains(trelloComment.getId())) {
                Commento dbCommento = commentoRepository.findByTrelloId(trelloComment.getId());
                if (dbCommento.getLastUpdate().isBefore(trelloComment.toLocalEntity().getLastUpdate())) {
                    commentToInsert.setIdCommento(commentoRepository
                            .findByTrelloId(trelloComment.getId()).getIdCommento());

                    Commento updatedChecklist = commentoService.update(commentToInsert);
                }
            } else {
                Commento insertedChecklist = commentoService.insert(commentToInsert);
            }
        }
    }

    private void insertCheckmark(TrelloChecklist trelloChecklist, Checklist insertedChecklist) {
        List<CheckItem> checkitems = trelloChecklist.getCheckItems();
        for (CheckItem checkitem :
                checkitems) {
            Checkmark checkmarkToInsert = checkitem.toLocalEntity();
            checkmarkToInsert.setChecklist(insertedChecklist);
            if (allCheckmark.stream()
                    .map(checkmark -> checkmark.getTrelloId())
                    .collect(Collectors.toList())
                    .contains(checkitem.getIdCheckItem())) {

                checkmarkToInsert.setIdCheckmark(checkmarkRepository
                        .findByTrelloId(checkitem.getIdCheckItem()).getIdCheckmark());
                Checkmark updatedCheckmark = checkmarkService.update(checkmarkToInsert);

            } else {
                Checkmark insertedCheckmark = checkmarkService.insert(checkmarkToInsert);
            }
        }

    }

}
