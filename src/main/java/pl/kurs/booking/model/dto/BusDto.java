package pl.kurs.booking.model.dto;

import pl.kurs.booking.model.Bus;


public record BusDto(int id) {
    public static BusDto fromBus(Bus bus) {
        return new BusDto(bus.getId());
    }
}
