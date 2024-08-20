package pl.kurs.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.booking.model.command.CreateBusCommand;
import pl.kurs.booking.model.Bus;
import pl.kurs.booking.model.Ticket;
import pl.kurs.booking.model.views.BusView;
import pl.kurs.booking.repository.BusRepository;
import pl.kurs.booking.repository.BusViewRepository;
import pl.kurs.booking.repository.TicketRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BusService {
    private static final int PAGE_SIZE = 5;
    private final BusRepository busRepository;
    private final BusViewRepository busViewRepository;
    private final TicketRepository ticketRepository;


    @Transactional
    public Bus addBus(CreateBusCommand command) {
        Bus bus = new Bus(command.capacity());
        return busRepository.saveAndFlush(bus);
    }

    @Transactional
    public void removeBus(int busId) {
        Bus bus = busRepository.findById(busId).orElseThrow(IllegalArgumentException::new);
        busRepository.delete(bus);
    }

    @Transactional(readOnly = true)
    public Page<Bus> findAll(Pageable pageable) {
        return busRepository.findAllBy(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Bus> findAllWithTicketsFull(Pageable pageable) {
        Page<Bus> allBuses = busRepository.findAllBy(pageable);
        List<Integer> ids = allBuses.stream().map(Bus::getId).toList();
        List<Ticket> allByIdIn = ticketRepository.findAllByIdIn(ids);
        allBuses.forEach(bus -> bus.setTickets(extractTicket(allByIdIn, bus.getId())));
        return allBuses;
    }

    @Transactional(readOnly = true)
    public Page<BusView> findAllWithTicketsLight(Pageable pageable) {
        return busViewRepository.findAll(pageable);
    }

    private Set<Ticket> extractTicket(List<Ticket> tickets, int id) {
        return tickets.stream().filter(ticket -> ticket.getBus().getId() == id).collect(Collectors.toSet());
    }

}
