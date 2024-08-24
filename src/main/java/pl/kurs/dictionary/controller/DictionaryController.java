package pl.kurs.dictionary.controller;

import lombok.RequiredArgsConstructor;
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
import pl.kurs.dictionary.model.dto.DictionaryDto;
import pl.kurs.dictionary.service.DictionaryService;

@RestController
@RequestMapping("api/dictionary")
@RequiredArgsConstructor
public class DictionaryController {
    private final DictionaryService dictionaryService;

    @GetMapping("/{dictionaryName}")
    public ResponseEntity<DictionaryDto> getDictionary(@PathVariable String dictionaryName) {
        Dictionary saved = dictionaryService.getDictionary(dictionaryName);
        return ResponseEntity.ok(DictionaryDto.fromDictionary(saved));
    }

    @PostMapping
    public ResponseEntity<DictionaryDto> createDictionary(@RequestBody CreateDictionaryCommand command) {
        Dictionary saved = dictionaryService.createDictionary(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(DictionaryDto.fromDictionary(saved));
    }

    @DeleteMapping("/{dictionaryName}")
    public ResponseEntity<Void> deleteDictionary(@PathVariable String dictionaryName) {
        dictionaryService.removeDictionary(dictionaryName);
        return ResponseEntity.noContent().build();
    }

}
