package pl.kurs.booking.model.command;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UpdateTicketCommand(@NotNull Integer id,
                                  @NotNull @NotEmpty String name,
                                  @NotNull @NotEmpty String surname) {
}
