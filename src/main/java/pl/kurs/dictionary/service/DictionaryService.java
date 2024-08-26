package pl.kurs.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.dictionary.exceptions.DictionaryNotFoundException;
import pl.kurs.dictionary.exceptions.DictionaryValueNotFoundException;
import pl.kurs.dictionary.model.command.UpdateDictionaryValueCommand;
import pl.kurs.dictionary.service.validators.interfaces.Validator;
import pl.kurs.dictionary.model.Dictionary;
import pl.kurs.dictionary.model.DictionaryValue;
import pl.kurs.dictionary.model.command.CreateDictionaryCommand;
import pl.kurs.dictionary.model.command.CreateDictionaryValuesCommand;
import pl.kurs.dictionary.model.command.UpdateDictionaryCommand;
import pl.kurs.dictionary.model.dto.DictionaryDto;
import pl.kurs.dictionary.repository.DictionaryRepository;
import pl.kurs.dictionary.repository.DictionaryValueRepository;

@Service
@RequiredArgsConstructor
public class DictionaryService {
    private final DictionaryRepository dictionaryRepository;
    private final DictionaryValueRepository dictionaryValueRepository;
    private final Validator validator;

    @Transactional
    public Dictionary addDictionary(CreateDictionaryCommand command) {
        return dictionaryRepository.saveAndFlush(new Dictionary(command.name()));
    }

    @Transactional
    @CacheEvict(cacheNames = "dictionary", key = "#dictionaryName")
    public Dictionary addDictionaryValues(String dictionaryName, CreateDictionaryValuesCommand command) {
        Dictionary dictionary = dictionaryRepository.findByName(dictionaryName).orElseThrow(DictionaryNotFoundException::new);
        command.valuesList().forEach(dictionary::addValue);
        return dictionaryRepository.saveAndFlush(dictionary);
    }


    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "dictionary", key = "#name")
    public Dictionary getDictionary(String name) {
        return dictionaryRepository.findByName(name).orElseThrow(DictionaryNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Page<DictionaryDto> getDictionaries(Pageable pageable) {
        return dictionaryRepository.findAllWithValues(pageable);
    }

    @Transactional
    @CacheEvict(cacheNames = "dictionary", key = "#dictionaryName")
    public Dictionary updateDictionary(String dictionaryName, UpdateDictionaryCommand command) {
        validator.validate(dictionaryName, command.currentName());
        Dictionary dictionary = dictionaryRepository.findByName(dictionaryName).orElseThrow(DictionaryNotFoundException::new);
        dictionary.setName(command.newName());
        return dictionaryRepository.saveAndFlush(dictionary);
    }

    @Transactional
    @CacheEvict(cacheNames = "dictionary", key = "#dictionaryName")
    public DictionaryValue updateDictionaryValue(String dictionaryName, String dictionaryValueName, UpdateDictionaryValueCommand command) {
        validator.validate(dictionaryValueName, command.currentValue());
        DictionaryValue dictionaryValue = dictionaryValueRepository.findByDictionaryNameAndValue(dictionaryName, dictionaryValueName).orElseThrow(DictionaryValueNotFoundException::new);
        dictionaryValue.setValue(command.newValue());
        return dictionaryValueRepository.saveAndFlush(dictionaryValue);
    }

    @Transactional
    @CacheEvict(cacheNames = "dictionary", key = "#dictionaryName")
    public void removeDictionary(String dictionaryName) {
        dictionaryRepository.deleteByName(dictionaryName);
    }

    @Transactional
    @CacheEvict(cacheNames = "dictionary", key = "#dictionaryName")
    public void removeDictionaryValue(String dictionaryName, String dictionaryValueName) {
        DictionaryValue dictionaryValue = dictionaryValueRepository.findByDictionaryNameAndValue(dictionaryName, dictionaryValueName).orElseThrow(DictionaryValueNotFoundException::new);
        Dictionary dictionary = getDictionary(dictionaryName);
        dictionary.removeValue(dictionaryValue);
        dictionaryValueRepository.delete(dictionaryValue);
    }


}
