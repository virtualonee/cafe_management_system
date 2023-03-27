package ru.virtu.cafe_management_system.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.virtu.cafe_management_system.dao.BookingDAO;
import ru.virtu.cafe_management_system.models.Booking;
import ru.virtu.cafe_management_system.repositories.BookingRepository;
import ru.virtu.cafe_management_system.repositories.CafeTableRepository;

import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class BookingsService {

    private final BookingRepository bookingRepository;
    private final BookingDAO bookingDAO;
    private final CafeTablesService cafeTablesService;

    @Autowired
    public BookingsService(BookingRepository bookingRepository, BookingDAO bookingDAO, CafeTablesService cafeTablesService) {
        this.bookingRepository = bookingRepository;
        this.bookingDAO = bookingDAO;
        this.cafeTablesService = cafeTablesService;

    }

    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    public List<Booking> findByCafeId(Long cafeId) {
        return bookingDAO.showByCafeId(cafeId);
    }

    public Booking findOne(Long id) {
        Optional<Booking> foundCafe = bookingRepository.findById(id);
        return foundCafe.orElse(null);
    }

    @Transactional
    public void save(Booking cafe) {
        bookingRepository.save(cafe);
    }

    @Transactional
    public void update(Long id, Booking updatedCafe) {
        updatedCafe.setId(id);
        bookingRepository.save(updatedCafe);
    }

    @Transactional
    public void delete(Long id) {
        Booking booking = bookingRepository.getById(id);
        cafeTablesService.unbookingTable(booking.getTableNumber());

        bookingRepository.deleteById(id);
    }
}
