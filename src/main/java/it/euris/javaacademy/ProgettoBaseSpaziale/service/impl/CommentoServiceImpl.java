package it.euris.javaacademy.ProgettoBaseSpaziale.service.impl;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Commento;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.CommentoRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.CommentoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class CommentoServiceImpl implements CommentoService {
    CommentoRepository commentoRepository;
    @Override
    public List<Commento> findAll() {
        return null;
    }

    @Override
    public Commento insert(Commento commento) {
        return null;
    }

    @Override
    public Commento update(Commento commento) {
        return null;
    }

    @Override
    public Boolean deleteById(Integer idCommento) {
        return null;
    }

    @Override
    public Commento findById(Integer idCommento) {
        return null;
    }
}
