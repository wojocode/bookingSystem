package pl.kurs.booking.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class TicketConflictException extends RuntimeException {
    public TicketConflictException() {
    }

    public TicketConflictException(String message) {
        super(message);
    }
}
