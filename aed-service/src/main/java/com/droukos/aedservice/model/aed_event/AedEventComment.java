package com.droukos.aedservice.model.aed_event;

import com.droukos.aedservice.environment.dto.RequesterAccessTokenData;
import com.droukos.aedservice.environment.dto.client.aed_event.AedEventCommentDto;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import reactor.util.function.Tuple3;

import java.time.LocalDateTime;

@ToString
@Document
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AedEventComment {
    @Id
    private String id;
    @Indexed private String eventId;
    private String username;
    private String message;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime posted;
    @Transient
    private double allComments;

    public static AedEventComment build(Tuple3<AedEvent, AedEventCommentDto, RequesterAccessTokenData> tuple3) {
        return new AedEventComment(
                null,
                tuple3.getT1().getId(),
                tuple3.getT3().getUsername(),
                tuple3.getT2().getComment(),
                LocalDateTime.now(),
                tuple3.getT1().getCommsN()
        );
    }
}
