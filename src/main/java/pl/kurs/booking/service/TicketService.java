package pl.kurs.booking.service;

import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.booking.model.command.CreateTicketCommand;
import pl.kurs.booking.model.command.UpdateTicketCommand;
import pl.kurs.booking.exceptions.BusCapacityException;
import pl.kurs.booking.exceptions.TicketConflictException;
import pl.kurs.booking.exceptions.TicketNotFoundException;
import pl.kurs.booking.model.Bus;
import pl.kurs.booking.model.Ticket;
import pl.kurs.booking.model.projection.TicketProjection;
import pl.kurs.booking.repository.BusRepository;
import pl.kurs.booking.repository.TicketRepository;


@Service
@RequiredArgsConstructor
public class TicketService {
    private final static int RETRY_NUMBER = 3;
    private final BusRepository busRepository;
    private final TicketRepository ticketRepository;

    @Transactional
    public Bus addTicket(int busId, CreateTicketCommand command) {
        Bus bus = busRepository.findByIdWithLock(busId).orElseThrow(TicketNotFoundException::new);
        Ticket ticket = new Ticket(command.name(), command.surname());
        if (bus.bookTicket(ticket)) {
            return busRepository.saveAndFlush(bus);
        } else throw new BusCapacityException();
    }

    @Transactional(readOnly = true)
    public Ticket getTicket(int ticketId) {
        return ticketRepository.findById(ticketId).orElseThrow(TicketNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Page<TicketProjection> getTickets(Pageable pageable) {
        return ticketRepository.findAllProjection(pageable);
    }

    @Transactional
    public Ticket updateTicket(int ticketId, UpdateTicketCommand command) {
        validTicketId(ticketId, command);
        int count = 0;
        while (count < RETRY_NUMBER) {
            try {
                Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(TicketNotFoundException::new);
                setTicket(ticket, command);
                return ticketRepository.saveAndFlush(ticket);
            } catch (OptimisticLockException e) {
                count++;
            }
        }
        throw new IllegalArgumentException();
    }

    @Transactional
    public void removeTicket(int ticketId) {
        Ticket ticket = getTicket(ticketId);
        ticket.removeTicket();
        ticketRepository.delete(ticket);
    }


    private void validTicketId(int id, UpdateTicketCommand command) {
        if (id != command.id()) {
            throw new TicketConflictException();
        }
    }

    private void setTicket(Ticket ticketToBeChanged, UpdateTicketCommand command) {
        ticketToBeChanged.setName(command.name());
        ticketToBeChanged.setSurname(command.surname());
    }

}
