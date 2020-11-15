package com.droukos.aedservice.service.validator.aed_event;

import com.droukos.aedservice.environment.dto.client.aed_event.AedEventDtoCreate;
import com.droukos.aedservice.model.aed_event.AedEvent;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

import static com.droukos.aedservice.environment.constants.Fields.OCCURRENCE_COMMENT;
import static com.droukos.aedservice.environment.constants.Fields.OCCURRENCE_TYPE;
import static com.droukos.aedservice.environment.enums.Warnings.*;


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
        if(isNumber(event.getOccurrenceType())){
            if(event.getOccurrenceType()<1 || event.getOccurrenceType()>3)
            errors.rejectValue(OCCURRENCE_TYPE, OCCURRENCE_TYPE_INVALID.getShortWarning());}
        else{
            errors.rejectValue(OCCURRENCE_TYPE,OCCURRENCE_TYPE_EMPTY.getShortWarning());
        }
        if(isNumber(event.getStatus())){
            if(event.getStatus()<1 || event.getStatus()>3)
                errors.rejectValue(OCCURRENCE_COMMENT, OCCURRENCE_COMMENT_INVALID.getShortWarning());}
    }

    public boolean isNumber(Integer strNum) {
        if(strNum==null){
            return false;
        }
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
