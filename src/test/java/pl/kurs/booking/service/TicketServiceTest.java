package pl.kurs.booking.service;

import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.kurs.booking.exceptions.BusCapacityException;
import pl.kurs.booking.exceptions.TicketNotFoundException;
import pl.kurs.booking.model.Bus;
import pl.kurs.booking.model.Ticket;
import pl.kurs.booking.model.command.CreateTicketCommand;
import pl.kurs.booking.model.command.UpdateTicketCommand;
import pl.kurs.booking.model.projection.TicketProjection;
import pl.kurs.booking.repository.BusRepository;
import pl.kurs.booking.repository.TicketRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TicketServiceTest {
    private TicketService ticketService;

    @Mock
    private BusRepository busRepository;

    @Mock
    private TicketRepository ticketRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        ticketService = new TicketService(busRepository, ticketRepository);
    }

    @Test
    public void shouldGetTicket() {
        Ticket ticket = new Ticket("TEST", "TEST");
        when(ticketRepository.findById(ticket.getId())).thenReturn(Optional.of(ticket));

        Ticket ticket1 = ticketService.getTicket(ticket.getId());

        verify(ticketRepository, times(1)).findById(ticket.getId());
        assertEquals(ticket.getName(), ticket1.getName());
        assertEquals(ticket.getId(), ticket1.getId());
    }

    @Test
    public void shouldThrowExceptionWhenGetTicket() {
        int ticketId = 1;
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

        Assertions.assertThrows(TicketNotFoundException.class, () -> ticketService.getTicket(ticketId));
    }

    @Test
    public void shouldGetTickets() {
        TicketProjection ticketProjection = mock(TicketProjection.class);
        Pageable pageable = mock(Pageable.class);

        when(ticketProjection.getName()).thenReturn("TEST");
        when(ticketProjection.getSurname()).thenReturn("TEST");

        List<TicketProjection> expected = List.of(ticketProjection);

        when(ticketRepository.findAllProjection(pageable)).thenReturn(new PageImpl<>(expected, PageRequest.of(0, expected.size()), expected.size()));

        Page<TicketProjection> returnedTickets = ticketService.getTickets(pageable);

        verify(ticketRepository, times(1)).findAllProjection(pageable);
        assertEquals(expected.size(), returnedTickets.getTotalElements());
        assertEquals(expected.get(0), returnedTickets.getContent().get(0));
    }

    @Test
    public void shouldAddTicket() {
        Bus bus = new Bus(10);
        CreateTicketCommand command = new CreateTicketCommand("TEST", "TEST");

        when(busRepository.findByIdWithLock(bus.getId())).thenReturn(Optional.of(bus));

        ticketService.addTicket(bus.getId(), command);

        ArgumentCaptor<Bus> captor = ArgumentCaptor.forClass(Bus.class);
        verify(busRepository, times(1)).saveAndFlush(captor.capture());
        assertEquals(bus, captor.getValue());
    }

    @Test
    public void shouldThrowBusCapacityExceptionWhenAddTicket() {
        Bus bus = mock(Bus.class);
        CreateTicketCommand command = new CreateTicketCommand("TEST", "TEST");
        when(bus.bookTicket(any(Ticket.class))).thenReturn(false);

        when(busRepository.findByIdWithLock(bus.getId())).thenReturn(Optional.of(bus));

        assertThrows(BusCapacityException.class, () -> ticketService.addTicket(bus.getId(), command));
    }


    @Test
    public void shouldRemoveTicket() {
        Ticket ticket = mock(Ticket.class);
        Bus bus = mock(Bus.class);
        when(ticket.getBus()).thenReturn(bus);

        when(ticketRepository.findById(ticket.getId())).thenReturn(Optional.of(ticket));

        ticketService.removeTicket(ticket.getId());


        ArgumentCaptor<Ticket> captor = ArgumentCaptor.forClass(Ticket.class);
        verify(ticketRepository, times(1)).delete(captor.capture());
        assertEquals(ticket, captor.getValue());
    }

    @Test
    public void shouldUpdateTicket() {
        Ticket ticket = new Ticket("TEST", "TEST");
        UpdateTicketCommand command = new UpdateTicketCommand(ticket.getId(), "TEST1", "TEST1");


        when(ticketRepository.findById(ticket.getId())).thenReturn(Optional.of(ticket));
        when(ticketRepository.saveAndFlush(any(Ticket.class))).thenReturn(ticket);

        Ticket updated = ticketService.updateTicket(ticket.getId(), command);

        ArgumentCaptor<Ticket> captor = ArgumentCaptor.forClass(Ticket.class);
        verify(ticketRepository, times(1)).saveAndFlush(captor.capture());
        assertEquals(command.name(), updated.getName());
        assertEquals(command.surname(), updated.getSurname());
    }


    @Test
    public void shouldRetryWhenUpdateTicket() {
        Ticket ticket = new Ticket("TEST", "TEST");
        UpdateTicketCommand command = new UpdateTicketCommand(ticket.getId(), "TEST1", "TEST1");

        when(ticketRepository.findById(ticket.getId())).thenReturn(Optional.of(ticket));
        when(ticketRepository.saveAndFlush(ticket)).thenThrow(new OptimisticLockException())
                .thenThrow(new OptimisticLockException())
                .thenReturn(ticket);

        Ticket updated = ticketService.updateTicket(ticket.getId(), command);

        verify(ticketRepository, times(3)).saveAndFlush(ticket);
        assertEquals(command.name(), updated.getName());
        assertEquals(command.surname(), updated.getSurname());

    }
}