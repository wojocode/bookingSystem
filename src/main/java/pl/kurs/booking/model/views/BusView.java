package pl.kurs.booking.model.views;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

@Getter
@Setter
@Entity
@Immutable
@NoArgsConstructor
@EqualsAndHashCode
public class BusView {
    @Id
    private int id;
    private int capacity;
    private long numberOfTickets;

    public BusView(int capacity, long numberOfTickets) {
        this.capacity = capacity;
        this.numberOfTickets = numberOfTickets;
    }
}
