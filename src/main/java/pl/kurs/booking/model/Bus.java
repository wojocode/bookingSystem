package pl.kurs.booking.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@ToString(exclude = "tickets")
public class Bus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int capacity;

    @OneToMany(mappedBy = "bus", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private Set<Ticket> tickets = new HashSet<>();

    public Bus(int capacity) {
        this.capacity = capacity;
    }

    public boolean bookTicket(Ticket ticket) {
        if (this.capacity <= this.tickets.size()) return false;
        else {
            this.getTickets().add(ticket);
            ticket.setBus(this);
            return true;
        }
    }


}
