package com.droukos.newsservice.environment.dto.client;

import lombok.*;

@ToString
@NoArgsConstructor @AllArgsConstructor
@Getter
public class NewsDtoCreate {
    private String userid;
    private String username;
    private String newsTitle;
    private String content;

}
