package pl.kurs.booking.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Version;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@ToString(exclude = "bus")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String surname;

    @ManyToOne
    @JoinColumn(name = "bus_id")
    private Bus bus;

    @Version
    private long version;

    public Ticket(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public void removeTicket() {
        bus.getTickets().remove(this);
        bus = null;
    }
}
