package com.droukos.newsservice.model.factories;

import com.droukos.newsservice.environment.dto.client.NewsDtoCreate;
import com.droukos.newsservice.model.news.News;

public class NewsFactoryCreate {

    private NewsFactoryCreate(){}

    public static News newsCreate(NewsDtoCreate newsDtoCreate){
        return new News(null,
                newsDtoCreate.getUsername().toLowerCase(),
                newsDtoCreate.getUsername(),
                newsDtoCreate.getNewsTitle(),
                newsDtoCreate.getContent(),
                newsDtoCreate.getUploadedTime()
        );
    }
}
