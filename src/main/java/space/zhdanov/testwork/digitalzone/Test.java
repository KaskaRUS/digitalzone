package space.zhdanov.testwork.digitalzone;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import space.zhdanov.testwork.digitalzone.entity.Event;

import java.util.Date;
import java.util.UUID;

public class Test {

    public static void main(String[] args) {
        Event event = Event.builder().PageUUID(UUID.randomUUID()).UserUUID(UUID.randomUUID()).timestamp(new Date()).build();

        ObjectMapper ob = new ObjectMapper();

        try {
            System.out.println(ob.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
