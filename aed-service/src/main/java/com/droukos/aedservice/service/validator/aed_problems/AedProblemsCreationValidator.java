package com.droukos.aedservice.service.validator.aed_problems;

import com.droukos.aedservice.environment.dto.client.aed_problems.AedProblemsDtoCreate;
import com.droukos.aedservice.service.aed_problem.AedProblemsCreation;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

import static com.droukos.aedservice.environment.constants.Fields.*;
import static com.droukos.aedservice.environment.enums.Warnings.*;
import static org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace;

public class AedProblemsCreationValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return AedProblemsCreation.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        rejectIfEmptyOrWhitespace(errors, PROBLEMS_TITLE, PROBLEMS_TITLE_EMPTY.getShortWarning());
        rejectIfEmptyOrWhitespace(errors, PROBLEMS_INFO, PROBLEMS_INFO_EMPTY.getShortWarning());

        AedProblemsDtoCreate aedProblemsDtoCreate = (AedProblemsDtoCreate) o;
        if ((isNumeric(aedProblemsDtoCreate.getProblemsTitle()) || aedProblemsDtoCreate.getProblemsTitle().length()>50) || ((AedProblemsDtoCreate) o).getProblemsTitle()==null)
            errors.rejectValue(PROBLEMS_TITLE, PROBLEMS_TITLE_INVALID.getShortWarning());
        if (aedProblemsDtoCreate.getInformation()==null || aedProblemsDtoCreate.getInformation().length()>500)
            errors.rejectValue(PROBLEMS_INFO, PROBLEMS_INFO_LENGTH.getShortWarning());
        if (isNumeric(aedProblemsDtoCreate.getInformation()))//||isSpecials(news.getContent()))
            errors.rejectValue(PROBLEMS_INFO, PROBLEMS_INFO_INVALID.getShortWarning());
    }

    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return Pattern.compile("-?\\d+(\\.\\d+)?").matcher(strNum).matches();
    }
}
