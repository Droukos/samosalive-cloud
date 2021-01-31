package com.droukos.authservice.model.sub;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserSub {
    @Id
    private String id;
    private String entityId;
    private int type;
    private String title;
    private LocalDateTime subOn;
}
