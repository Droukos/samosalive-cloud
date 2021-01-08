package com.droukos.newsservice.service.validator;

import com.droukos.newsservice.environment.dto.client.NewsDtoSearch;
import com.droukos.newsservice.model.news.News;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.droukos.newsservice.environment.constants.Fields.NEWS_TITLE;
import static com.droukos.newsservice.environment.enums.Warnings.*;
import static org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace;

public class NewsTitleValidator implements Validator {

    @Value("${news.content.maxlength}")
    private int MAX_LENGTH;

    @Override
    public boolean supports(Class<?> aClass) {
        return News.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        //rejectIfEmptyOrWhitespace(errors, NEWS_TITLE, NEWS_TITLE_EMPTY.getShortWarning());
        List<Integer> l = new ArrayList<>();
        l.add(-1);
        NewsDtoSearch newsDtoSearch = (NewsDtoSearch) o;
        if (newsDtoSearch.getNewsTitle().equals("")){
            if((newsDtoSearch.getSearchTag()==l)){
                errors.rejectValue(NEWS_TITLE, NEWS_TITLE_INVALID.getShortWarning());
            }
        }
        //if((newsDtoSearch.getSearchTag()<=0 || (newsDtoSearch.getSearchTag()>=4))){
        //    errors.rejectValue(NEWS_TITLE, NEWS_TITLE_INVALID.getShortWarning());
        //}
        //if (news.getContent()!=null||news.getContent().length()>MAX_LENGTH)
        //    errors.rejectValue(NEWS_CONTENT, NEWS_CONTENT_LENGTH.getShortWarning());
    }

    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return Pattern.compile("-?\\d+(\\.\\d+)?").matcher(strNum).matches();
    }
}
