package pl.kurs.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.dictionary.exceptions.DictionaryNotFoundException;
import pl.kurs.dictionary.model.Dictionary;
import pl.kurs.dictionary.model.command.CreateDictionaryCommand;
import pl.kurs.dictionary.repository.DictionaryRepository;
import pl.kurs.dictionary.repository.DictionaryValueRepository;

@Service
@RequiredArgsConstructor
public class DictionaryService {
    private final DictionaryRepository dictionaryRepository;
    private final DictionaryValueRepository dictionaryValueRepository;


    @Transactional(readOnly = true)
    public Dictionary getDictionary(String name) {
        return dictionaryRepository.findByName(name).orElseThrow(DictionaryNotFoundException::new);
    }

    @Transactional
    public Dictionary createDictionary(CreateDictionaryCommand command) {
        return dictionaryRepository.saveAndFlush(new Dictionary(command.name()));
    }

    @Transactional
    public void removeDictionary(String dictionaryName) {
        dictionaryRepository.deleteByName(dictionaryName);
    }
}
