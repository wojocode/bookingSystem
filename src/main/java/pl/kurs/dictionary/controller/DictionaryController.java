package pl.kurs.dictionary.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kurs.dictionary.model.Dictionary;
import pl.kurs.dictionary.model.command.CreateDictionaryCommand;
import pl.kurs.dictionary.model.command.CreateDictionaryValuesCommand;
import pl.kurs.dictionary.model.dto.DictionaryDto;
import pl.kurs.dictionary.service.DictionaryService;

@RestController
@RequestMapping("api/dictionary")
@RequiredArgsConstructor
public class DictionaryController {
    private final DictionaryService dictionaryService;

    @PostMapping
    public ResponseEntity<DictionaryDto> createDictionary(@RequestBody CreateDictionaryCommand command) {
        Dictionary saved = dictionaryService.addDictionary(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(DictionaryDto.fromDictionary(saved));
    }

    @PostMapping("/{dictionaryName}/dictionaryValues")
    public ResponseEntity<DictionaryDto> addDictionaryValues(@PathVariable String dictionaryName, @RequestBody CreateDictionaryValuesCommand command) {
        Dictionary saved = dictionaryService.addDictionaryValues(dictionaryName, command);
        return ResponseEntity.status(HttpStatus.CREATED).body(DictionaryDto.fromDictionary(saved));
    }

    @GetMapping("/{dictionaryName}")
    public ResponseEntity<DictionaryDto> getDictionary(@PathVariable String dictionaryName) {
        Dictionary saved = dictionaryService.getDictionary(dictionaryName);
        return ResponseEntity.ok(DictionaryDto.fromDictionary(saved));
    }

    @GetMapping
    public ResponseEntity<Page<DictionaryDto>> getDictionaries(Pageable pageable) {
        Page<DictionaryDto> dictionaries = dictionaryService.getDictionaries(pageable);
        return ResponseEntity.ok(dictionaries);
    }


    @DeleteMapping("/{dictionaryName}")
    public ResponseEntity<Void> deleteDictionary(@PathVariable String dictionaryName) {
        dictionaryService.removeDictionary(dictionaryName);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{dictionaryName}/values/{dictionaryValueName}")
    public ResponseEntity<Void> deleteDictionaryValue(@PathVariable String dictionaryName, @PathVariable String dictionaryValueName) {
        dictionaryService.removeDictionaryValue(dictionaryName, dictionaryValueName);
        return ResponseEntity.noContent().build();
    }


}
