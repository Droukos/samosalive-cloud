package com.droukos.authservice.environment.dto.server.responses;

import com.droukos.authservice.environment.constants.StatusCodes;
import com.droukos.authservice.environment.dto.server.ApiResponse;
import com.droukos.authservice.model.user.UserRes;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import static com.droukos.authservice.environment.enums.Warnings.*;
import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;

public class BadResponses {

    public Mono<ServerResponse> badResponse(Object obj) {
        return badRequest().body(BodyInserters.fromValue(obj));
    }

    public Mono<ServerResponse> invalidCredentials() {
        return badRequest().body(BodyInserters.fromValue(
                new ApiResponse(StatusCodes.BAD_REQUEST, USER_CREDENTIALS_INVALID.getWarning(), USER_CREDENTIALS_INVALID.getShortWarning())));
    }

    public Mono<UserRes> userNotExist() {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, USERID_NOTEXISTS.getShortWarning());
    }

    public Mono<ServerResponse> useridNotExist() {
        return badRequest().body(BodyInserters.fromValue(
                new ApiResponse(StatusCodes.BAD_REQUEST, USERID_NOTEXISTS.getWarning(), USERID_NOTEXISTS.getShortWarning())));
    }

    public Mono<UserRes> unauthorized() {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    public Mono<UserRes> emailTaken() {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, EMAIL_TAKEN.getShortWarning());
    }

    public Mono<ServerResponse> invalidToken() {
        return badRequest().body(BodyInserters.fromValue(
                new ApiResponse(StatusCodes.UNAUTHORIZED, TOKEN_INVALID.getWarning(), TOKEN_INVALID.getShortWarning())));
    }

    public Mono<UserRes> usernameTaken() {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, USERNAME_TAKEN.getShortWarning());
    }

    public Mono<ServerResponse> commentNotValid(){
        return badRequest().body(BodyInserters.fromValue(
                new ApiResponse(StatusCodes.BAD_REQUEST, OCCURRENCE_COMMENT_INVALID.getWarning(), OCCURRENCE_COMMENT_INVALID.getShortWarning())));
    }

    public Mono<ServerResponse> invalidNews(){
        return badRequest().body(BodyInserters.fromValue(
                new ApiResponse(StatusCodes.BAD_REQUEST, NEWS_INVALID.getWarning(), NEWS_INVALID.getShortWarning())));
    }
}
