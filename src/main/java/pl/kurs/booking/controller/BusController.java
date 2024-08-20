package pl.kurs.booking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kurs.booking.model.command.CreateTicketCommand;
import pl.kurs.booking.model.dto.BusDto;
import pl.kurs.booking.model.command.CreateBusCommand;
import pl.kurs.booking.model.dto.BusDtoWithTickets;
import pl.kurs.booking.model.Bus;
import pl.kurs.booking.model.views.BusView;
import pl.kurs.booking.service.BusService;
import pl.kurs.booking.service.TicketService;

@RestController
@RequestMapping("api/buses")
@RequiredArgsConstructor
public class BusController {
    private final TicketService ticketService;
    private final BusService busService;

    @PostMapping
    public ResponseEntity<BusDto> addBus(@Valid @RequestBody CreateBusCommand command) {
        Bus saved = busService.addBus(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(BusDto.fromBus(saved));
    }

//    @GetMapping
//    public ResponseEntity<List<BusDto>> findAll(@RequestParam(required = false) Integer page) {
//        int pageNumber = (page != null && page >= 0) ? page : 0;
//        List<Bus> buses = busService.findAll(pageNumber);
//        return ResponseEntity.ok(buses.stream().map(BusDto::fromBus).collect(Collectors.toList()));
//    }

    @GetMapping
    public ResponseEntity<Page<BusDto>> findAll(@PageableDefault(size = 3) Pageable pageable) {
        Page<Bus> buses = busService.findAll(pageable);
        return ResponseEntity.ok(buses.map(BusDto::fromBus));
    }

    // using 2 query one for buses and one for their tickets
    @GetMapping("/ticketsFull")
    public ResponseEntity<Page<BusDtoWithTickets>> findAllWithTicketsFull(@PageableDefault(size = 3) Pageable pageable) {
        Page<Bus> buses = busService.findAllWithTicketsFull(pageable);
        return ResponseEntity.ok(buses.map(BusDtoWithTickets::fromBus));
    }

    // using BusView
    @GetMapping("/ticketsLight")
    public ResponseEntity<Page<BusView>> findAllWithTicketsLight(@PageableDefault(size = 3) Pageable pageable) {
        Page<BusView> buses = busService.findAllWithTicketsLight(pageable);
        return ResponseEntity.ok(buses);
    }


    @PostMapping("/{id}/ticket")
    public ResponseEntity<BusDto> buyTicket(@PathVariable int id, @RequestBody @Valid CreateTicketCommand command) {
        Bus saved = ticketService.addTicket(id, command);
        return ResponseEntity.ok(BusDto.fromBus(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeBus(@PathVariable int id) {
        busService.removeBus(id);
        return ResponseEntity.noContent().build();
    }

}
