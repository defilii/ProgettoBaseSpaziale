package it.euris.javaacademy.ProgettoBaseSpaziale.repositoy;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Checklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChecklistRepository extends JpaRepository<Checklist, Integer> {

    @Query("SELECT s FROM Checklist s WHERE s.trelloId = :idToLook")
    Checklist findByTrelloId(@Param("idToLook") String idToLook);
}
