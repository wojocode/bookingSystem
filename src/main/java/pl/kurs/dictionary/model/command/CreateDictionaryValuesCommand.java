package pl.kurs.dictionary.model.command;


import java.util.List;

public record CreateDictionaryValuesCommand(List<String> valuesList) {
}
