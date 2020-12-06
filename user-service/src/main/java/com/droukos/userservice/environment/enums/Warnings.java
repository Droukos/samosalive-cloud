package com.droukos.userservice.environment.enums;

import static com.droukos.userservice.environment.constants.FieldNames.*;
import static com.droukos.userservice.environment.constants.Fields.*;

public enum Warnings {

    EMPTY(".empty"," field can't be empty."),
    INVALID(".invalid", ""),
    INVALID_LENGTH(".invalid.length", ""),
    TAKEN(".taken", ""),
    NOTMATCH(".notmatch", ""),
    NOTEXISTS(".notexists", ""),
    NOTNULL(".null", " field can't be null"),
    MAXSIZE(".maxsize", " exceeds max size"),
    SAME(".same", " is the same"),
    LAST(".last", " is the last one"),

    USERNAME_INVALID(USERNAME + INVALID.getShortWarning(), "Username must be 3-20 letters \nand of pattern a-zA-Z_-"),
    USERNAME_TAKEN(USERNAME + TAKEN.getShortWarning(), "Username given is already taken."),
    USERNAME_NOTEXISTS(USERNAME + NOTEXISTS.getShortWarning(), "Username does not exists."),
    USERNAME_INVALID_LENGTH(USERNAME + INVALID_LENGTH.getShortWarning(), "Username length must be under 20 letters"),
    USERNAME_EMPTY(USERNAME + EMPTY.getShortWarning(), "Username" + EMPTY.getWarning()),
    USERNAME_NULL(USERNAME + NOTNULL.getShortWarning(), "Username" + NOTNULL.getWarning()),

    PASSWORD_EMPTY(PASSWORD + EMPTY.getShortWarning(), "Password can't be empty"),
    PASSWORD_INVALID_LENGTH(PASSWORD + INVALID_LENGTH.getShortWarning(), "Password must be under 120 letters"),
    PASSWORD_NULL(PASSWORD + NOTNULL.getShortWarning(), "Password" + NOTNULL.getWarning()),

    PASSWORD_NOMATCH_ERROR(PASSWORD_CONFRIMED + NOTMATCH.getShortWarning(),"Password and Confirmation Password do not match."),
    PASSWORD_CONFIRMED_EMPTY(PASSWORD_CONFRIMED + EMPTY.getShortWarning(), "Password confirmed can't be empty"),
    PASSWORD_CONFIRMED_NULL(PASSWORD_CONFRIMED + NOTNULL.getShortWarning(), "Password confirmed"+ NOTNULL.getWarning()),

    PASSWORD_NEW_EMPTY(F_PASSNEW + EMPTY.getShortWarning(), "New password can't be empty"),
    PASSWORD_NEW_INVALID(F_PASSNEW + INVALID.getShortWarning(), "New password is invalid"),
    PASSWORD_NEW_CONFIRMED_EMPTY(F_PASSNEWC + EMPTY.getShortWarning(), "New Password Corfirmation can't be empty"),
    PASSWORD_NEW_CONFIRMED_NOMATCH(F_PASSNEWC + NOTMATCH.getShortWarning(), "New Password and New Password Confrimation do not match"),
    PASSWORD_PASSWORD_NEW_SAME(F_PASSNEW + SAME.getShortWarning(), "New password is the same as old password"),

    EMAIL_INVALID(EMAIL + INVALID.getShortWarning(), "Invalid email given."),
    EMAIL_TAKEN(EMAIL + TAKEN.getShortWarning(), "Email given is already taken."),
    EMAIL_INVALID_LENGTH(EMAIL + INVALID_LENGTH.getShortWarning(), "Email must be under 120 letters"),
    EMAIL_EMPTY(EMAIL + EMPTY.getShortWarning(), "Email can't be empty"),
    EMAIL_NULL(EMAIL + NOTNULL.getShortWarning(), "Email" + NOTNULL.getWarning()),

    NAME_INVALID(NAME + INVALID.getShortWarning(), "Name is invalid"),
    NAME_INVALID_LENGTH(NAME + INVALID_LENGTH.getShortWarning(), "Name's length is invalid"),
    NAME_EMPTY(NAME + EMPTY.getShortWarning(), "Name can't be empty"),
    NAME_NULL(NAME + NOTNULL.getShortWarning(), "Name" + NOTNULL.getWarning()),

    SURNAME_INVALID(SURNAME + INVALID.getShortWarning(), "Surname is invalid"),
    SURNAME_INVALID_LENGTH(SURNAME + INVALID_LENGTH.getShortWarning(), "Surname's length is invalid"),
    SURNAME_EMPTY(SURNAME + EMPTY.getShortWarning(), "Surname can't be empty"),
    SURNAME_NULL(SURNAME + NOTNULL.getShortWarning(), "Surname" + NOTNULL.getWarning()),

    AVATAR_INVALID(AVATAR + INVALID.getShortWarning(), "Avatar is invalid"),
    AVATAR_MAXSIZE(AVATAR + MAXSIZE.getShortWarning(), "Avatar exceeds max size"),
    DESCRIPTION_INVALID (DESCRIPTION + INVALID.getShortWarning(), "Description is invalid"),
    DESCRIPTION_INVALID_LENGTH(DESCRIPTION + INVALID_LENGTH.getShortWarning(), "Description's length is invalid"),

    COUNTRYISO_INVALID(COUNTRYISO + INVALID.getShortWarning(), "Country code is invalid"),
    STATE_INVALID(STATE + INVALID.getShortWarning(), "State is invalid"),
    STATE_INVALID_LENGTH(STATE + INVALID_LENGTH.getShortWarning(), "State's length is invalid"),
    CITY_INVALID(CITY + INVALID.getShortWarning(), "City is invalid"),
    CITY_INVALID_LENGTH(CITY + INVALID_LENGTH.getShortWarning(), "City's length is invalid"),

    UPDATEDROLE_EMPTY(F_UPDATEDROLE + EMPTY.getShortWarning(), "Role can't be empty"),
    UPDATEDROLE_INVALID(F_UPDATEDROLE + INVALID.getShortWarning(), "Role is invalid"),
    UPDATEDROLE_EXISTS(F_UPDATEDROLE + SAME.getShortWarning(), "Role already exists"),
    UPDATEDROLE_NOTEXISTS(F_UPDATEDROLE+NOTEXISTS.getShortWarning(), "Role does not exists on user"),
    UPDATEDROLE_LAST(F_UPDATEDROLE+LAST.getShortWarning(), "Role is the last one"),

    NEWMEMAIL_EMPTY(F_NEWEMAIL + EMPTY.getShortWarning(), "New email can't be empty"),
    NEWEMAIL_INVALID(F_NEWEMAIL + INVALID.getShortWarning(), "New email is invalid"),
    NEWEMAIL_SAME(F_NEWEMAIL + SAME.getShortWarning(), "New email is the same as previous"),

    PRIVY_EMPTY(EMPTY.getShortWarning(), " can't be empty"),
    PRIVY_INVALID_TYPE(INVALID.getShortWarning(), " type is invalid"),
    PRIVY_INVALID_USER(INVALID.getShortWarning(), " a username is invalid"),
    PRIVY_INVALID_LENGTH(MAXSIZE.getShortWarning(), " exceeded max size"),

    DEVICE_INVALID(F_DEVICE + INVALID.getShortWarning(), "Device is invalid"),

    UNAUTHORIZED("@:warnings:unauthorized", "Unauthorized action"),
    TOKEN_DIFFERENT_DEVICE("token.different.device", "Token from different device"),
    TOKEN_EXPIRED("token.expired", "Token has expired"),
    TOKEN_INVALID("token.invalid", "Token is invalid"),
    USERID_NOTEXISTS("@:warnings:userid_not_exists", "UserID not exists"),
    USER_CREDENTIALS_INVALID("user.credentials.invalid", "Invalid user's credentials."),

    OCCURRENCE_TYPE_EMPTY(OCCURRENCE_TYPE + EMPTY.getShortWarning(), "Not valid occurrence type"),
    OCCURRENCE_TYPE_INVALID(OCCURRENCE_TYPE + INVALID.getShortWarning(), "Not valid occurrence type"),
    OCCURRENCE_COMMENT_INVALID(OCCURRENCE_COMMENT+ INVALID.getShortWarning(), "Wrong comment/spam"),

    NEWS_TITLE_EMPTY(NEWS_TITLE + EMPTY.getShortWarning(), "Not given news title"),
    NEWS_TITLE_INVALID(NEWS_TITLE + INVALID.getShortWarning(), "Not valid news title"),
    NEWS_INVALID(NEWS + INVALID.getShortWarning(), "Not valid news"),

    NEWS_CONTENT_LENGTH(NEWS_CONTENT +INVALID.getShortWarning(), "Wrong content length"),
    NEWS_CONTENT_EMPTY(NEWS_CONTENT + EMPTY.getShortWarning(), "Empty content"),
    NEWS_CONTENT_INVALID(NEWS_CONTENT + INVALID.getShortWarning(), "Wrong comment/spam");


    private final String shortWarning;
    private final String warning;

    public String getShortWarning() {
        return shortWarning;
    }
    public String getWarning(){
        return warning;
    }

    Warnings(String shortWarning, String warning){
        this.shortWarning = shortWarning;
        this.warning = warning;

    }
}
