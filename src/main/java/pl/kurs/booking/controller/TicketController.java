package pl.kurs.booking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kurs.booking.command.UpdateTicketCommand;
import pl.kurs.booking.dto.TicketDto;
import pl.kurs.booking.model.Ticket;
import pl.kurs.booking.service.TicketService;

@RestController
@RequestMapping("api/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;

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
