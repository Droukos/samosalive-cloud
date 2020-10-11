package com.droukos.aedservice.service.validator.aed_event;

import com.droukos.aedservice.environment.dto.client.aed_event.AedEventDtoCreate;
import com.droukos.aedservice.model.aed_event.AedEvent;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

import static com.droukos.aedservice.environment.constants.Fields.OCCURRENCE_COMMENT;
import static com.droukos.aedservice.environment.enums.Warnings.OCCURRENCE_COMMENT_INVALID;


public class AedCreationValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return AedEvent.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        AedEventDtoCreate event = (AedEventDtoCreate) o;
        if(isNumeric(event.getComment())&&event.getComment().length()>3)
            errors.rejectValue(OCCURRENCE_COMMENT, OCCURRENCE_COMMENT_INVALID.getShortWarning());
        //if(isSpecials(event.getComment()))
        //    errors.rejectValue(OCCURRENCE_COMMENT, OCCURRENCE_COMMENT_INVALID.getShortWarning());
    }

    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return Pattern.compile("-?\\d+(\\.\\d+)?").matcher(strNum).matches();
    }
    public boolean isSpecials(String strSp) {
        if(strSp == null){
            return  false;
        }
        return Pattern.compile("^[^<>{}\"/|;:.,~!?@#$%^=&*\\]\\\\()\\[¿§«»ω⊙¤°℃℉€¥£¢¡®©0-9_+]*$").matcher(strSp).matches();
    }
}
