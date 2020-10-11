package com.droukos.aedservice.service.validator.aed_problems;

import com.droukos.aedservice.model.aed_problems.AedProblems;
import com.droukos.aedservice.model.aed_problems.AedProblems;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

import static com.droukos.aedservice.environment.constants.Fields.PROBLEMS_TITLE;
import static com.droukos.aedservice.environment.enums.Warnings.*;
import static org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace;

public class AedProblemsTitleValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return AedProblems.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        rejectIfEmptyOrWhitespace(errors, PROBLEMS_TITLE, PROBLEMS_TITLE_EMPTY.getShortWarning());

        AedProblems problems = (AedProblems) o;
        if (/*isNumeric(news.getNewsTitle()) || */((AedProblems) o).getProblemTitle()==null)
            errors.rejectValue(PROBLEMS_TITLE, PROBLEMS_TITLE_INVALID.getShortWarning());
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
