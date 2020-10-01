package com.droukos.authservice.environment.interfaces;

import com.droukos.authservice.model.user.UserRes;
import reactor.core.publisher.Mono;

public interface AuthResult {
    Mono<UserRes> checkDbTokenForUser();
}
