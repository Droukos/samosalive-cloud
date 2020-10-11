package com.droukos.aedservice.service.validator.aed_event;

import com.droukos.aedservice.model.aed_event.AedEvent;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

import static com.droukos.aedservice.environment.constants.Fields.OCCURRENCE_TYPE;
import static com.droukos.aedservice.environment.enums.Warnings.*;
import static org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace;

public class OccurrenceTypeValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return String.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        rejectIfEmptyOrWhitespace(errors, OCCURRENCE_TYPE, OCCURRENCE_TYPE_EMPTY.getShortWarning());

        String type = (String) o;
        if(!isNumeric(type))
            errors.rejectValue(OCCURRENCE_TYPE, OCCURRENCE_TYPE_INVALID.getShortWarning());
        if(isNumeric(type)){
            int typeInt = Integer.parseInt(type);
            if(typeInt < 0 || typeInt > 2)
                errors.rejectValue(OCCURRENCE_TYPE, OCCURRENCE_TYPE_INVALID.getShortWarning());
        }
    }

    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return Pattern.compile("-?\\d+(\\.\\d+)?").matcher(strNum).matches();
    }
}
