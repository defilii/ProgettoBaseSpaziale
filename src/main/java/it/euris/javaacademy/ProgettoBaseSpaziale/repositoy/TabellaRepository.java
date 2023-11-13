package it.euris.javaacademy.ProgettoBaseSpaziale.repositoy;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Tabella;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TabellaRepository extends JpaRepository<Tabella, Integer> {
    @Query("SELECT s FROM Tabella s WHERE s.trelloId = :idToLook")
    Tabella findByTrelloId(@Param("idToLook") String idToLook);
}
