package pl.kurs.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.dictionary.exceptions.DictionaryNotFoundException;
import pl.kurs.dictionary.exceptions.DictionaryValueNotFoundException;
import pl.kurs.dictionary.model.Dictionary;
import pl.kurs.dictionary.model.DictionaryValue;
import pl.kurs.dictionary.model.command.CreateDictionaryCommand;
import pl.kurs.dictionary.model.command.CreateDictionaryValuesCommand;
import pl.kurs.dictionary.model.dto.DictionaryDto;
import pl.kurs.dictionary.repository.DictionaryRepository;
import pl.kurs.dictionary.repository.DictionaryValueRepository;

@Service
@RequiredArgsConstructor
public class DictionaryService {
    private final DictionaryRepository dictionaryRepository;
    private final DictionaryValueRepository dictionaryValueRepository;


    @Transactional
    public Dictionary addDictionary(CreateDictionaryCommand command) {
        return dictionaryRepository.saveAndFlush(new Dictionary(command.name()));
    }

    @Transactional
    public Dictionary addDictionaryValues(String dictionaryName, CreateDictionaryValuesCommand command) {
        Dictionary dictionary = dictionaryRepository.findByName(dictionaryName).orElseThrow(DictionaryNotFoundException::new);
        command.valuesList().forEach(dictionary::addValue);
        return dictionaryRepository.saveAndFlush(dictionary);
    }

    @Transactional(readOnly = true)
    public Dictionary getDictionary(String name) {
        return dictionaryRepository.findByName(name).orElseThrow(DictionaryNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Page<DictionaryDto> getDictionaries(Pageable pageable) {
        return dictionaryRepository.findAllWithValues(pageable);
    }

    @Transactional
    public void removeDictionary(String dictionaryName) {
        dictionaryRepository.deleteByName(dictionaryName);
    }

    @Transactional
    public void removeDictionaryValue(String dictionaryName, String dictionaryValueName) {
        DictionaryValue dictionaryValue = dictionaryValueRepository.findByDictionaryNameAndValue(dictionaryName, dictionaryValueName).orElseThrow(DictionaryValueNotFoundException::new);
        Dictionary dictionary = getDictionary(dictionaryName);
        dictionary.removeValue(dictionaryValue);
        dictionaryValueRepository.delete(dictionaryValue);
    }


}
