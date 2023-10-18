package it.euris.javaacademy.ProgettoBaseSpaziale.repositoy;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Tabella;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TabellaRepository extends JpaRepository<Tabella, Integer> {
}
