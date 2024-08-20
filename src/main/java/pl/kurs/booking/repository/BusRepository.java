package pl.kurs.booking.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.kurs.booking.model.Bus;

import java.util.List;
import java.util.Optional;

public interface BusRepository extends JpaRepository<Bus, Integer> {
    @Query("SELECT b FROM Bus b LEFT JOIN FETCH b.tickets WHERE b.id = :id")
    Optional<Bus> findById(@Param("id") int id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM Bus b LEFT JOIN FETCH b.tickets WHERE b.id = :id")
    Optional<Bus> findByIdWithLock(@Param("id") int id);

    Page<Bus> findAllBy(Pageable pageable);

}
