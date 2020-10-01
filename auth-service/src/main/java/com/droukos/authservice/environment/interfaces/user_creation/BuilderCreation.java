package com.droukos.authservice.environment.interfaces.user_creation;

import com.droukos.authservice.model.user.UserRes;
import reactor.core.publisher.Mono;

public interface BuilderCreation {
    Mono<UserRes> buildMono();
    UserRes build();
}
