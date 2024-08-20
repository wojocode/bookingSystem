package pl.kurs.booking.dto;

import pl.kurs.booking.model.Bus;

import java.util.List;
import java.util.stream.Collectors;

public record BusDtoWithTickets(int id, List<TicketDto> ticketDtos) {

    public static BusDtoWithTickets fromBus(Bus bus) {
        List<TicketDto> ticketDtos = bus.getTickets().stream()
                .map(TicketDto::fromTicket)
                .collect(Collectors.toList());
        return new BusDtoWithTickets(bus.getId(), ticketDtos);
    }

}
