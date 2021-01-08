package com.droukos.newsservice.environment.dto.client;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@ToString
@NoArgsConstructor @AllArgsConstructor
@Getter
public class NewsDtoCreate {
    private String username;
    private String newsTitle;
    private String content;
    private List<Integer> tag;
}
