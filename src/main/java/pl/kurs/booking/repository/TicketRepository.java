package pl.kurs.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.kurs.booking.model.Ticket;
import pl.kurs.booking.model.projection.TicketProjection;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    List<Ticket> findAllByIdIn(List<Integer> list);

    @Query("SELECT t.id as id , t.name as name, t.surname as surname FROM Ticket t")
    Page<TicketProjection> findAllProjection(Pageable pageable);
}
