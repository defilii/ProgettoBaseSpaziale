package it.euris.javaacademy.ProgettoBaseSpaziale.repositoy;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PriorityRepository extends JpaRepository<Priority, Integer> {

    @Query("SELECT s FROM Priority s WHERE s.trelloId = :idToLook")
    Priority findByTrelloId(@Param("idToLook") String idToLook);

    Priority findByNameIgnoreCase(String name);
}
