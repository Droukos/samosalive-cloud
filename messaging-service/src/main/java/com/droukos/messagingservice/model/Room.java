package com.droukos.messagingservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@ToString
@Document
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Room {
    private String id;
    private List<String> users;
    private String nickname;
    private LocalDateTime created;
}
