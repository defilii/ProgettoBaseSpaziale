package it.euris.javaacademy.ProgettoBaseSpaziale.repositoy;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Checkmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CheckmarkRepository extends JpaRepository<Checkmark, Integer> {

    @Query("SELECT s FROM Checkmark s WHERE s.trelloId = :idToLook")
    Checkmark findByTrelloId(@Param("idToLook") String idToLook);
}
