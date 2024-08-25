package pl.kurs.dictionary.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DictionaryConflictException extends RuntimeException {
    public DictionaryConflictException() {
    }

    public DictionaryConflictException(String message) {
        super(message);
    }
}
