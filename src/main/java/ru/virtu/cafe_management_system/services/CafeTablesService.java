package ru.virtu.cafe_management_system.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.virtu.cafe_management_system.dao.CafeTableDAO;
import ru.virtu.cafe_management_system.models.CafeTable;
import ru.virtu.cafe_management_system.repositories.CafeTableRepository;

import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class CafeTablesService {

    private final CafeTableRepository cafeTableRepository;
    private final CafeTableDAO cafeTableDAO;

    @Autowired
    public CafeTablesService(CafeTableRepository cafeTableRepository, CafeTableDAO cafeTableDAO) {
        this.cafeTableRepository = cafeTableRepository;
        this.cafeTableDAO = cafeTableDAO;
    }

    public List<CafeTable> findAll() {
        return cafeTableRepository.findAll();
    }

    public List<CafeTable> findByCafeId(Long cafeId) {
        return cafeTableDAO.showByCafeId(cafeId);
    }

    public CafeTable findOne(Long id) {
        Optional<CafeTable> foundCafe = cafeTableRepository.findById(id);
        return foundCafe.orElse(null);
    }

    @Transactional
    public void save(CafeTable cafe) {
        cafeTableRepository.save(cafe);
    }

    @Transactional
    public void update(Long id, CafeTable updatedCafe) {
        updatedCafe.setId(id);
        cafeTableRepository.save(updatedCafe);
    }

    @Transactional
    public void delete(Long id) {
        cafeTableRepository.deleteById(id);
    }
}
