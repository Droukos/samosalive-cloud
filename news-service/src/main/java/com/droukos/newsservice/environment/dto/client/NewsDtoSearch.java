package com.droukos.newsservice.environment.dto.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter @ToString
@AllArgsConstructor
@NoArgsConstructor
public class NewsDtoSearch {
    private String newsTitle;
    private Integer searchTag;
}
