package pl.kurs.dictionary.model.dto;

import pl.kurs.dictionary.model.Dictionary;

public record DictionaryDto(long id, String name, long valuesCount) {
    public static DictionaryDto fromDictionary(Dictionary dictionary) {
        return new DictionaryDto(dictionary.getId(), dictionary.getName(), dictionary.getDictionaryValuesSet().size());
    }
}
