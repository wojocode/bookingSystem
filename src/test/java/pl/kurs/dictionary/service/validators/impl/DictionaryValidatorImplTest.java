package pl.kurs.dictionary.service.validators.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.kurs.dictionary.exceptions.DictionaryConflictException;

import static org.junit.jupiter.api.Assertions.*;

class DictionaryValidatorImplTest {
    private DictionaryValidatorImpl dictionaryValidator;

    @BeforeEach
    public void init() {
        dictionaryValidator = new DictionaryValidatorImpl();
    }

    @Test
    public void shouldValidateWhenNamesAreTheSame() {
        assertTrue(dictionaryValidator.validate("TEST", "TEST"));
    }

    @Test
    public void shouldThrowDictionaryConflictExceptionWhenNamesAreTheSame() {
        assertThrows(DictionaryConflictException.class, () -> dictionaryValidator.validate("TEST", "TEST1"));
    }


}