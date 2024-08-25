package pl.kurs.dictionary.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DictionaryValueConflictException extends RuntimeException {
    public DictionaryValueConflictException() {
    }

    public DictionaryValueConflictException(String message) {
        super(message);
    }
}
