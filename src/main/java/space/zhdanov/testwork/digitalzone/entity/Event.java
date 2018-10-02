package space.zhdanov.testwork.digitalzone.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
@Document
@NoArgsConstructor
@AllArgsConstructor
public class Event implements Serializable {

    @Id
    private String _id;

    private UUID UserUUID;
    private UUID PageUUID;
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+3", pattern = "dd.MM.yyyy HH:mm:ss")
    private Date timestamp;
}
