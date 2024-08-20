package pl.kurs.booking.command;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateBusCommand(@NotNull @Positive Integer capacity) {
}
