package pl.kurs.dictionary.model.command;

import jakarta.validation.constraints.NotNull;

public record UpdateDictionaryCommand(@NotNull String currentName, @NotNull String newName) {
}
