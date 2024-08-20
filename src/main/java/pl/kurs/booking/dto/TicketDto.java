package pl.kurs.booking.dto;

import pl.kurs.booking.model.Ticket;

public record TicketDto(int id, String name, String surname) {
    public static TicketDto fromTicket(Ticket ticket) {
        return new TicketDto(ticket.getId(), ticket.getName(), ticket.getSurname());
    }
}
