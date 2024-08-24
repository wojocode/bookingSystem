package pl.kurs.booking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kurs.booking.model.command.UpdateTicketCommand;
import pl.kurs.booking.model.dto.TicketDto;
import pl.kurs.booking.model.Ticket;
import pl.kurs.booking.model.projection.TicketProjection;
import pl.kurs.booking.service.TicketService;

@RestController
@RequestMapping("api/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;

    @GetMapping("/{id}")
    public ResponseEntity<TicketDto> getTicket(@PathVariable int id) {
        Ticket ticket = ticketService.getTicket(id);
        return ResponseEntity.ok(TicketDto.fromTicket(ticket));
    }

    @GetMapping
    public ResponseEntity<Page<TicketProjection>> getTickets(@PageableDefault(size = 3) Pageable pageable) {
        Page<TicketProjection> tickets = ticketService.getTickets(pageable);
        return ResponseEntity.ok(tickets);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> returnTicket(@PathVariable int id) {
        ticketService.removeTicket(id);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<TicketDto> updateTicket(@PathVariable int id, @RequestBody @Valid UpdateTicketCommand command) {
        Ticket saved = ticketService.updateTicket(id, command);
        return ResponseEntity.ok(TicketDto.fromTicket(saved));
    }
}
