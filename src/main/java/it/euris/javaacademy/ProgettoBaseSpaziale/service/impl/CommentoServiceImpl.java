package it.euris.javaacademy.ProgettoBaseSpaziale.service.impl;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Commento;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.ForeignKeyIdMustNotBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustNotBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.CommentoRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.TaskRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.UserRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.CommentoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CommentoServiceImpl implements CommentoService {
    CommentoRepository commentoRepository;
    UserRepository userRepository;
    TaskRepository taskRepository;

    @Override
    public List<Commento> findAll() {
        return commentoRepository.findAll();
    }

    @Override
    public Commento insert(Commento commento) {
        if (commento.getIdCommento() != null && commento.getIdCommento() > 0) {
            throw new IdMustBeNullException();
        }
        if (taskRepository.findById(commento.getTask().getIdTask()).isEmpty()) {
            throw new ForeignKeyIdMustNotBeNullException();
        }
        if (userRepository.findById(commento.getUser().getIdUser()).isEmpty()) {
            throw new ForeignKeyIdMustNotBeNullException();
        }
        return commentoRepository.save(commento);
    }

    @Override
    public Commento update(Commento commento) {
        if (commento.getIdCommento() == null || commento.getIdCommento() == 0) {
            throw new IdMustNotBeNullException();
        }

        if (taskRepository.findById(commento.getTask().getIdTask()).isEmpty()) {
            throw new ForeignKeyIdMustNotBeNullException();
        }
        if (userRepository.findById(commento.getUser().getIdUser()).isEmpty()) {
            throw new ForeignKeyIdMustNotBeNullException();
        }
        return commentoRepository.save(commento);
    }

    @Override
    public Boolean deleteById(Integer idCommento) {
        commentoRepository.deleteById(idCommento);
        return commentoRepository.findById(idCommento).isEmpty();
    }

    @Override
    public Commento findById(Integer idCommento) {
        return commentoRepository.findById(idCommento).orElse(Commento.builder().build());
    }
}
