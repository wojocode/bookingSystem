package pl.kurs.booking.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import pl.kurs.booking.model.Bus;
import pl.kurs.booking.model.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    List<Ticket> findAllByIdIn(List<Integer> list);
}
