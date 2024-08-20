package pl.kurs.booking.dto;

import pl.kurs.booking.model.Bus;
import pl.kurs.booking.model.Ticket;

import java.util.List;
import java.util.stream.Collectors;


public record BusDto(int id) {
    public static BusDto fromBus(Bus bus) {
        return new BusDto(bus.getId());
    }
}
