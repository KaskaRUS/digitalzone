package space.zhdanov.testwork.digitalzone;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import space.zhdanov.testwork.digitalzone.entity.Event;
import space.zhdanov.testwork.digitalzone.entity.ResponseAggregationEvents;
import space.zhdanov.testwork.digitalzone.repository.EventRepository;
import space.zhdanov.testwork.digitalzone.service.EventService;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EventTests {

    private static final int TIME_1_DAY = 1000 * 60 * 60 * 24;
    private static final int TIME_2_DAY = 1000 * 60 * 60 * 48;
    private static final int TIME_3_DAY = 1000 * 60 * 60 * 72;
    private static final int TIME_4_DAY = 1000 * 60 * 60 * 96;
    private static final int TIME_5_DAY = 1000 * 60 * 60 * 120;
    @Autowired
    private EventService eventService;

    @Autowired
    private EventRepository eventRepository;

    @Test
    public void addEventTest() {
        Mono<Event> newEvent = eventService.addEvent(Mono.just(
                Event.builder()
                        .PageUUID(UUID.randomUUID())
                        .UserUUID(UUID.randomUUID())
                        .build()
        ));

        Mono<Boolean> result = eventRepository.existsById(newEvent.map(Event::get_id));
        Assert.assertTrue(result.block());
    }

    @Test
    public void getStatisticForDayTest() {


        UUID firstUser = UUID.randomUUID();
        UUID secondUser = UUID.randomUUID();
        UUID thirdUser = UUID.randomUUID();

        UUID firstPage = UUID.randomUUID();

        Date today = new Date();
        Date yesterday = new Date(today.getTime() - TIME_1_DAY);

        Mono[] events = new Mono[] {
                Mono.just(Event.builder().PageUUID(firstPage).UserUUID(firstUser).timestamp(today).build()),
                Mono.just(Event.builder().PageUUID(firstPage).UserUUID(secondUser).timestamp(today).build()),
                Mono.just(Event.builder().PageUUID(firstPage).UserUUID(thirdUser).timestamp(today).build()),
                Mono.just(Event.builder().PageUUID(firstPage).UserUUID(firstUser).timestamp(today).build()),
                Mono.just(Event.builder().PageUUID(firstPage).UserUUID(firstUser).timestamp(yesterday).build())
        };

        for (Mono event: events) {
            eventService.addEvent(event).block();
        }

        Mono<ResponseAggregationEvents> responseMono = eventService.getAggregationDataForDay();
        ResponseAggregationEvents response = responseMono.block();

        Assert.assertEquals(response.getAmountAllVisits().intValue(), 4);
        Assert.assertEquals(response.getAmountUniqueUser().intValue(), 3);
    }

    @Test
    public void getStatisticForWeek() {
        UUID firstUser = UUID.randomUUID();
        UUID secondUser = UUID.randomUUID();
        UUID thirdUser = UUID.randomUUID();

        UUID page1 = UUID.randomUUID();
        UUID page2 = UUID.randomUUID();
        UUID page3 = UUID.randomUUID();
        UUID page4 = UUID.randomUUID();
        UUID page5 = UUID.randomUUID();
        UUID page6 = UUID.randomUUID();
        UUID page7 = UUID.randomUUID();
        UUID page8 = UUID.randomUUID();
        UUID page9 = UUID.randomUUID();
        UUID page10 = UUID.randomUUID();


        Date today = new Date();
        Date _1dayAgo = new Date(today.getTime() - TIME_1_DAY);
        Date _2dayAgo = new Date(today.getTime() - TIME_2_DAY);
        Date _3dayAgo = new Date(today.getTime() - TIME_3_DAY);
        Date _4dayAgo = new Date(today.getTime() - TIME_4_DAY);
        Date _5dayAgo = new Date(today.getTime() - TIME_5_DAY);

        Mono[] events = new Mono[] {
                // first user see 10 page for 5 days
                Mono.just(Event.builder().PageUUID(page1).UserUUID(firstUser).timestamp(_2dayAgo).build()),
                Mono.just(Event.builder().PageUUID(page2).UserUUID(firstUser).timestamp(_3dayAgo).build()),
                Mono.just(Event.builder().PageUUID(page3).UserUUID(firstUser).timestamp(_4dayAgo).build()),
                Mono.just(Event.builder().PageUUID(page4).UserUUID(firstUser).timestamp(_4dayAgo).build()),
                Mono.just(Event.builder().PageUUID(page5).UserUUID(firstUser).timestamp(_3dayAgo).build()),
                Mono.just(Event.builder().PageUUID(page6).UserUUID(firstUser).timestamp(_2dayAgo).build()),
                Mono.just(Event.builder().PageUUID(page7).UserUUID(firstUser).timestamp(_2dayAgo).build()),
                Mono.just(Event.builder().PageUUID(page8).UserUUID(firstUser).timestamp(_1dayAgo).build()),
                Mono.just(Event.builder().PageUUID(page9).UserUUID(firstUser).timestamp(_1dayAgo).build()),
                Mono.just(Event.builder().PageUUID(page10).UserUUID(firstUser).timestamp(today).build()),
                // second user see 10 page for 6 days
                Mono.just(Event.builder().PageUUID(page1).UserUUID(secondUser).timestamp(_5dayAgo).build()),
                Mono.just(Event.builder().PageUUID(page2).UserUUID(secondUser).timestamp(_5dayAgo).build()),
                Mono.just(Event.builder().PageUUID(page3).UserUUID(secondUser).timestamp(_4dayAgo).build()),
                Mono.just(Event.builder().PageUUID(page4).UserUUID(secondUser).timestamp(_4dayAgo).build()),
                Mono.just(Event.builder().PageUUID(page5).UserUUID(secondUser).timestamp(_3dayAgo).build()),
                Mono.just(Event.builder().PageUUID(page6).UserUUID(secondUser).timestamp(_2dayAgo).build()),
                Mono.just(Event.builder().PageUUID(page7).UserUUID(secondUser).timestamp(_2dayAgo).build()),
                Mono.just(Event.builder().PageUUID(page8).UserUUID(secondUser).timestamp(_1dayAgo).build()),
                Mono.just(Event.builder().PageUUID(page9).UserUUID(secondUser).timestamp(_1dayAgo).build()),
                Mono.just(Event.builder().PageUUID(page10).UserUUID(secondUser).timestamp(today).build()),
                // third user see 8 page for 5 days
                Mono.just(Event.builder().PageUUID(page1).UserUUID(thirdUser).timestamp(_2dayAgo).build()),
                Mono.just(Event.builder().PageUUID(page1).UserUUID(thirdUser).timestamp(_3dayAgo).build()),
                Mono.just(Event.builder().PageUUID(page2).UserUUID(thirdUser).timestamp(_4dayAgo).build()),
                Mono.just(Event.builder().PageUUID(page2).UserUUID(thirdUser).timestamp(_4dayAgo).build()),
                Mono.just(Event.builder().PageUUID(page5).UserUUID(thirdUser).timestamp(_3dayAgo).build()),
                Mono.just(Event.builder().PageUUID(page6).UserUUID(thirdUser).timestamp(_2dayAgo).build()),
                Mono.just(Event.builder().PageUUID(page7).UserUUID(thirdUser).timestamp(_2dayAgo).build()),
                Mono.just(Event.builder().PageUUID(page8).UserUUID(thirdUser).timestamp(_1dayAgo).build()),
                Mono.just(Event.builder().PageUUID(page9).UserUUID(thirdUser).timestamp(_1dayAgo).build()),
                Mono.just(Event.builder().PageUUID(page10).UserUUID(thirdUser).timestamp(today).build())
        };

        for (Mono event: events) {
            eventService.addEvent(event).block();
        }

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, 1);

        Date end = calendar.getTime();

        calendar.add(Calendar.DAY_OF_MONTH, -5);
        Date begin = calendar.getTime();


        Mono<ResponseAggregationEvents> responseMono = eventService.getAggregationDataForPeriod(begin, end);
        ResponseAggregationEvents response = responseMono.block();

        Assert.assertEquals(response.getAmountAllVisits().intValue(), 28);
        Assert.assertEquals(response.getAmountUniqueUser().intValue(), 3);
        Assert.assertEquals(response.getAmountRegularUser().intValue(), 1);
    }

    @After
    public void deleteAllFromCollection() {
        eventRepository.deleteAll().block();
    }
}
