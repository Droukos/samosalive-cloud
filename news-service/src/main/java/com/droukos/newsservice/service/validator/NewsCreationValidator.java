package com.droukos.newsservice.service.validator;


import com.droukos.newsservice.environment.dto.client.NewsDtoCreate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

import static com.droukos.newsservice.environment.constants.Fields.NEWS_CONTENT;
import static com.droukos.newsservice.environment.constants.Fields.NEWS_TITLE;
import static com.droukos.newsservice.environment.enums.Warnings.*;
import static org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace;

public class NewsCreationValidator implements Validator {

    @Value("${news.content.maxlength}")
    private int MAX_LENGTH;

    @Override
    public boolean supports(Class<?> aClass) {
        return NewsDtoCreate.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        rejectIfEmptyOrWhitespace(errors, NEWS_TITLE, NEWS_TITLE_EMPTY.getShortWarning());
        rejectIfEmptyOrWhitespace(errors, NEWS_CONTENT, NEWS_CONTENT_EMPTY.getShortWarning());

        NewsDtoCreate newsDtoCreate = (NewsDtoCreate) o;
        if ((isNumeric(newsDtoCreate.getNewsTitle()) || newsDtoCreate.getNewsTitle().length()>50) || ((NewsDtoCreate) o).getNewsTitle()==null)
            errors.rejectValue(NEWS_TITLE, NEWS_TITLE_INVALID.getShortWarning());
        if (newsDtoCreate.getContent()==null || newsDtoCreate.getContent().length()>500)
            errors.rejectValue(NEWS_CONTENT, NEWS_CONTENT_LENGTH.getShortWarning());
        if (isNumeric(newsDtoCreate.getContent()))//||isSpecials(news.getContent()))
            errors.rejectValue(NEWS_CONTENT, NEWS_CONTENT_INVALID.getShortWarning());
    }

    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return Pattern.compile("-?\\d+(\\.\\d+)?").matcher(strNum).matches();
    }

    /*public boolean isSpecials(String strSp) {
        if(strSp == null){
            return  false;
        }
        return Pattern.compile("^[^<>{}\"/|;:.,~!?@#$%^=&*\\]\\\\()\\[¿§«»ω⊙¤°℃℉€¥£¢¡®©0-9_+]*$").matcher(strSp).matches();
    }*/
}
