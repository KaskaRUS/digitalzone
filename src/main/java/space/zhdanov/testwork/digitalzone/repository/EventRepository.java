package space.zhdanov.testwork.digitalzone.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import space.zhdanov.testwork.digitalzone.entity.Event;

public interface EventRepository extends ReactiveMongoRepository<Event, String> {
}
