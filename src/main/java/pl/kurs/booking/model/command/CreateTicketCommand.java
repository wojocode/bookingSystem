package pl.kurs.booking.model.command;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateTicketCommand(@NotNull @NotEmpty String name,
                                  @NotNull @NotEmpty String surname) {
}
