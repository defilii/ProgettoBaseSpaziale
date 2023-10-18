package it.euris.javaacademy.ProgettoBaseSpaziale.repositoy;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Checkmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckmarkRepository extends JpaRepository<Checkmark, Integer> {
}
