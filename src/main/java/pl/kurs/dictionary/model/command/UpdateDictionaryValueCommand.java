package pl.kurs.dictionary.model.command;

import jakarta.validation.constraints.NotNull;

public record UpdateDictionaryValueCommand(@NotNull String currentValue, @NotNull String newValue) {
}
