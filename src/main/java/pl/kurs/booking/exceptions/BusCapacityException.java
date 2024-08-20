package pl.kurs.booking.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.CONFLICT)
public class BusCapacityException extends RuntimeException {
    public BusCapacityException() {
    }

    public BusCapacityException(String message) {
        super(message);
    }
}
