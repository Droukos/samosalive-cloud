package com.droukos.aedservice.service.validator.aed_event;

import com.droukos.aedservice.environment.dto.client.aed_event.AedEventDtoSearch;
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
        return AedEventDtoSearch.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        rejectIfEmptyOrWhitespace(errors, OCCURRENCE_TYPE, OCCURRENCE_TYPE_EMPTY.getShortWarning());

        AedEventDtoSearch type = (AedEventDtoSearch) o;
        if (!isNumber(type.getOccurrenceType()))
            errors.rejectValue(OCCURRENCE_TYPE, OCCURRENCE_TYPE_INVALID.getShortWarning());
        if (isNumber(type.getOccurrenceType())) {
            int typeInt = type.getOccurrenceType();
            if (typeInt < 0 || typeInt > 3)
                errors.rejectValue(OCCURRENCE_TYPE, OCCURRENCE_TYPE_INVALID.getShortWarning());
        }
    }

    public boolean isNumber(Integer strNum) {
        if (strNum.getClass().equals(Integer.class)) {
            return true;
        }
        else if (strNum.getClass().equals(String.class)){
            return false;
        }
        else{
            return false;
        }
        //return Pattern.compile("-?\\d+(\\.\\d+)?").matcher(strNum).matches();
    }
}
