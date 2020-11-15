package com.droukos.newsservice.environment.dto.client;

import lombok.*;

@ToString
@NoArgsConstructor @AllArgsConstructor
@Getter
public class NewsDtoCreate {
    private String username;
    private String newsTitle;
    private String content;
    private String uploadedTime;
}
