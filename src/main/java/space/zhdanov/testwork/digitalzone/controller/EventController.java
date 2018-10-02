package space.zhdanov.testwork.digitalzone.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import space.zhdanov.testwork.digitalzone.entity.Event;
import space.zhdanov.testwork.digitalzone.entity.ResponseAggregationEvents;
import space.zhdanov.testwork.digitalzone.service.EventService;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/event")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseAggregationEvents> addEvent(@RequestBody Mono<Event> event) {
        return eventService.addEvent(
                event.map(it-> {
                    it.setTimestamp(new Date());
                    return it;
                }))
                .then(eventService.getAggregationDataForDay());
    }

    @GetMapping()
    public Mono<ResponseAggregationEvents> getStatisticForPeriod(@RequestParam @DateTimeFormat(pattern="dd.MM.yyyy") Date begin,
                                                                 @RequestParam @DateTimeFormat(pattern="dd.MM.yyyy") Date end) {
        return eventService.getAggregationDataForPeriod(begin, end);
    }
}
