package pl.kurs.dictionary.model.dto;

import pl.kurs.dictionary.model.Dictionary;

public record DictionaryDto(int id, String name) {
    public static DictionaryDto fromDictionary(Dictionary dictionary) {
        return new DictionaryDto(dictionary.getId(), dictionary.getName());
    }
}
