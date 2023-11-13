package it.euris.javaacademy.ProgettoBaseSpaziale.repositoy;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Commento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentoRepository extends JpaRepository<Commento, Integer> {

    @Query("SELECT s FROM Commento s WHERE s.trelloId = :idToLook")
    Commento findByTrelloId(@Param("idToLook") String idToLook);
}
