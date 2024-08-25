package pl.kurs.dictionary.service.validators.impl;

import org.springframework.stereotype.Component;
import pl.kurs.dictionary.exceptions.DictionaryConflictException;
import pl.kurs.dictionary.service.validators.interfaces.Validator;

@Component
public class DictionaryValidatorImpl implements Validator {

    @Override
    public boolean validate(String dictionaryName, String nameFromCommand) {
        if (!dictionaryName.equals(nameFromCommand)) {
            throw new DictionaryConflictException();
        }
        return true;
    }
}
