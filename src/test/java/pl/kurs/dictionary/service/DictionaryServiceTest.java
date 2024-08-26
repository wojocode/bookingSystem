package pl.kurs.dictionary.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import pl.kurs.dictionary.exceptions.DictionaryNotFoundException;
import pl.kurs.dictionary.model.Dictionary;
import pl.kurs.dictionary.model.DictionaryValue;
import pl.kurs.dictionary.model.command.CreateDictionaryCommand;
import pl.kurs.dictionary.model.command.CreateDictionaryValuesCommand;
import pl.kurs.dictionary.model.command.UpdateDictionaryCommand;
import pl.kurs.dictionary.model.command.UpdateDictionaryValueCommand;
import pl.kurs.dictionary.repository.DictionaryRepository;
import pl.kurs.dictionary.repository.DictionaryValueRepository;
import pl.kurs.dictionary.service.validators.interfaces.Validator;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DictionaryServiceTest {

    private DictionaryService dictionaryService;
    @Mock
    private DictionaryRepository dictionaryRepository;

    @Mock
    private DictionaryValueRepository dictionaryValueRepository;

    @Mock
    private Validator validator;


    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        dictionaryService = new DictionaryService(dictionaryRepository, dictionaryValueRepository, validator);
    }


    @Test
    public void shouldAddDictionary() {
        CreateDictionaryCommand createDictionaryCommand = new CreateDictionaryCommand("TEST");
        dictionaryService.addDictionary(createDictionaryCommand);
        ArgumentCaptor<Dictionary> captor = ArgumentCaptor.forClass(Dictionary.class);
        verify(dictionaryRepository, times(1)).saveAndFlush(captor.capture());
        Dictionary captured = captor.getValue();
        Assertions.assertEquals(createDictionaryCommand.name(), captured.getName());
    }

    @Test
    public void shouldAddDictionaryValues() {
        String name = "TEST";
        List<String> test = List.of("TEST1", "TEST2");
        CreateDictionaryValuesCommand command = new CreateDictionaryValuesCommand(test);

        when(dictionaryRepository.findByName(name)).thenReturn(Optional.of(new Dictionary(name)));

        dictionaryService.addDictionaryValues(name, command);

        ArgumentCaptor<Dictionary> captor = ArgumentCaptor.forClass(Dictionary.class);
        verify(dictionaryRepository, times(1)).findByName(name);
        verify(dictionaryRepository, times(1)).saveAndFlush(captor.capture());
        Assertions.assertEquals(name, captor.getValue().getName());
    }

    @Test
    public void shouldThrowNotFoundWhenAddDictionaryValues() {
        String dictionaryName = "test";
        CreateDictionaryValuesCommand command = new CreateDictionaryValuesCommand(List.of("value1", "value2"));
        when(dictionaryRepository.findByName(dictionaryName)).thenReturn(Optional.empty());

        Assertions.assertThrows(DictionaryNotFoundException.class, () -> dictionaryService.addDictionaryValues(dictionaryName, command));
    }

    @Test
    public void shouldGetDictionary() {
        String name = "TEST1";
        Dictionary dictionary = new Dictionary(name);
        Mockito.when(dictionaryRepository.findByName(name)).thenReturn(Optional.of(dictionary));
        dictionaryService.getDictionary(name);

        verify(dictionaryRepository, times(1)).findByName(name);
    }

    @Test
    public void shouldThrowNotFoundExceptionWhenGetDictionary() {
        String name = "TEST1";
        Mockito.when(dictionaryRepository.findByName(name)).thenReturn(Optional.empty());
        Assertions.assertThrows(DictionaryNotFoundException.class, () -> dictionaryService.getDictionary(name));
    }


    @Test
    public void shouldRemoveDictionary() {
        Dictionary dictionary = new Dictionary("TEST");
        dictionaryService.removeDictionary(dictionary.getName());

        verify(dictionaryRepository, times(1)).deleteByName(dictionary.getName());
    }

    @Test
    public void shouldRemoveDictionaryValue() {
        Dictionary dictionary = new Dictionary("TEST");
        DictionaryValue dictionaryValue = new DictionaryValue("TEST", dictionary);

        when(dictionaryRepository.findByName(dictionary.getName())).thenReturn(Optional.of(dictionary));
        when(dictionaryValueRepository.findByDictionaryNameAndValue(dictionary.getName(), dictionaryValue.getValue())).thenReturn(Optional.of(dictionaryValue));

        dictionaryService.removeDictionaryValue(dictionary.getName(), dictionaryValue.getValue());
        verify(dictionaryValueRepository, times(1)).delete(dictionaryValue);
    }

    @Test
    public void shouldUpdateDictionary() {
        Dictionary dictionary = new Dictionary("TEST");
        UpdateDictionaryCommand command = new UpdateDictionaryCommand("TEST", "TEST1");

        when(dictionaryRepository.findByName(dictionary.getName())).thenReturn(Optional.of(dictionary));

        dictionaryService.updateDictionary(dictionary.getName(), command);

        ArgumentCaptor<Dictionary> captor = ArgumentCaptor.forClass(Dictionary.class);
        verify(dictionaryRepository, times(1)).saveAndFlush(captor.capture());
        Assertions.assertEquals(command.newName(), captor.getValue().getName());
    }

    @Test
    public void shouldUpdateDictionaryValue() {
        Dictionary dictionary = new Dictionary("TEST");
        DictionaryValue dictionaryValue = new DictionaryValue("TEST", dictionary);
        UpdateDictionaryValueCommand command = new UpdateDictionaryValueCommand("TEST", "TEST1");

        when(dictionaryValueRepository.findByDictionaryNameAndValue(dictionary.getName(), dictionaryValue.getValue())).thenReturn(Optional.of(dictionaryValue));

        dictionaryService.updateDictionaryValue(dictionary.getName(), dictionaryValue.getValue(), command);

        ArgumentCaptor<DictionaryValue> captor = ArgumentCaptor.forClass(DictionaryValue.class);
        verify(dictionaryValueRepository, times(1)).saveAndFlush(captor.capture());
        Assertions.assertEquals(command.newValue(), captor.getValue().getValue());
    }


}