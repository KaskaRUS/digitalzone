package space.zhdanov.testwork.digitalzone.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import space.zhdanov.testwork.digitalzone.entity.Event;
import space.zhdanov.testwork.digitalzone.entity.ResponseAggregationEvents;
import space.zhdanov.testwork.digitalzone.repository.EventRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

@Service
public class EventService {

    private EventRepository eventRepository;
    private ReactiveMongoTemplate mongoTemplate;

    @Autowired
    public EventService(EventRepository eventRepository, ReactiveMongoTemplate mongoTemplate) {
        this.eventRepository = eventRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public Mono<Event> addEvent(Mono<Event> event) {
        return mongoTemplate.insert(event);
    }

    public Mono<ResponseAggregationEvents> getAggregationDataForDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date today = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date tomorrow = calendar.getTime();

        GroupOperation group = group("UserUUID").count().as("countUsers");
        MatchOperation match = match(Criteria.where("timestamp").gte(today).lt(tomorrow));
        Aggregation aggregation = newAggregation(match, group);
        Flux<GroupEvent> eventFlux = mongoTemplate.aggregate(aggregation, Event.class, GroupEvent.class);
        return eventFlux.reduce(ResponseAggregationEvents.builder().amountAllVisits(0).amountUniqueUser(0).build(),
                (a, b) -> {
                    a.setAmountAllVisits(a.getAmountAllVisits() + b.countUsers);
                    a.setAmountUniqueUser(a.getAmountUniqueUser() + 1);
                    return a;
                });
    }

    public Mono<ResponseAggregationEvents> getAggregationDataForPeriod(Date begin, Date end) {
        MatchOperation match = match(Criteria.where("timestamp").gte(begin).lt(end));
        GroupOperation group = group("UserUUID", "PageUUID").count().as("amountVisits");
        GroupOperation group2 = group("UserUUID").count().as("amountUniqueVisits")
                .sum("amountVisits").as("amountAllVisits");
        Aggregation aggregation = newAggregation(match, group, group2);
        Flux<GroupVisit> eventFlux = mongoTemplate.aggregate(aggregation, Event.class, GroupVisit.class);

        return eventFlux
                .reduce(ResponseAggregationEvents.builder().amountAllVisits(0).amountUniqueUser(0).amountRegularUser(0).build(),
                        (a, b) -> {
                            a.setAmountAllVisits(a.getAmountAllVisits() + b.getAmountAllVisits());
                            a.setAmountUniqueUser(a.getAmountUniqueUser() + 1);
                            if (b.getAmountUniqueVisits() >= 10)
                                a.setAmountRegularUser(a.getAmountRegularUser() + 1);
                            return a;
                        });
    }

    @Data
    @AllArgsConstructor
    private class GroupEvent {

        private UUID userUUID;
        private Integer countUsers;
    }

    @Data
    @AllArgsConstructor
    private class GroupVisit {

        private UUID userUUID;
        private Integer amountUniqueVisits;
        private Integer amountAllVisits;
    }

}
