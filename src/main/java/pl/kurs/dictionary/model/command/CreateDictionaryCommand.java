package pl.kurs.dictionary.model.command;

import jakarta.validation.constraints.NotNull;

public record CreateDictionaryCommand(@NotNull String name) {
}
