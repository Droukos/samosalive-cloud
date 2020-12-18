package com.droukos.authservice.service.auth;

import com.droukos.authservice.environment.security.TokenService;
import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.repo.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class BanServices {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    public Mono<UserRes> saveUser(UserRes user) {
        return userRepository.save(user);
    }

    public Mono<Boolean> removeAllAccessTokens(UserRes user) {
        return tokenService.redisRemoveAllUserAccessTokens(user);
    }

}
