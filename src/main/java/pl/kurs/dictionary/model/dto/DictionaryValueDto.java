package pl.kurs.dictionary.model.dto;

import pl.kurs.dictionary.model.DictionaryValue;

public record DictionaryValueDto(int id, String dictionaryName, String value) {
    public static DictionaryValueDto fromDictionaryValue(DictionaryValue dictionaryValue) {
        return new DictionaryValueDto(dictionaryValue.getId(), dictionaryValue.getDictionary().getName(), dictionaryValue.getValue());
    }
}
