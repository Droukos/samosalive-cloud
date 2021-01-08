package com.droukos.authservice.service.admin;

import com.droukos.authservice.config.redis.RedisConfigProperties;
import com.droukos.authservice.environment.dto.RequesterAccessTokenData;
import com.droukos.authservice.environment.dto.server.auth.login.LoginResponse;
import com.droukos.authservice.model.user.UserRes;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.droukos.authservice.environment.constants.Platforms.ANDROID;
import static com.droukos.authservice.environment.constants.Platforms.IOS;

@Service
@Slf4j
@AllArgsConstructor
public class AdminChannelService {
    private final RedisConfigProperties redisConfig;
    private final ReactiveRedisTemplate<String, LoginResponse> reactiveRedisTemplateAuth;

    public Flux<LoginResponse> listenToAuth(RequesterAccessTokenData requesterData) {
        String userCh = redisConfig.getAuthChPrefix() + requesterData.getUsername();
        String authChannel = switch (requesterData.getUserDevice()) {
            case ANDROID -> userCh + redisConfig.getAuthChAndroidPostfix();
            case IOS -> userCh + redisConfig.getAuthChIosPostfix();
            default -> userCh + redisConfig.getAuthChWebPostfix();
        };
        log.info("User: "+requesterData.getUsername()+" listens to: "+authChannel);

        return this.reactiveRedisTemplateAuth
                .listenToChannel(authChannel)
                .flatMap(reactiveSubMsg -> Mono.just(reactiveSubMsg.getMessage()));
    }

    public Mono<UserRes> publishUserAuthOnRedis(UserRes user) {
        LoginResponse userInfo = LoginResponse.build(user, "");
        String userCh = redisConfig.getAuthChPrefix() + user.getUser();
        String userAndroidCh = userCh + redisConfig.getAuthChAndroidPostfix();
        String userIosCh = userCh + redisConfig.getAuthChIosPostfix();
        String userWebCh = userCh + redisConfig.getAuthChWebPostfix();
        Mono<Long> androidAuthChannel = Mono.empty();
        Mono<Long> iosAuthChannel = Mono.empty();
        Mono<Long> webAuthChannel = Mono.empty();

        if (user.getAndroidJwtModel() != null) {
            androidAuthChannel = this.reactiveRedisTemplateAuth.convertAndSend(userAndroidCh, userInfo);
        }
        if (user.getIosJwtModel() != null) {
            iosAuthChannel = this.reactiveRedisTemplateAuth.convertAndSend(userIosCh, userInfo);
        }
        if (user.getWebJwtModel() != null) {
            webAuthChannel = this.reactiveRedisTemplateAuth.convertAndSend(userWebCh, userInfo);
        }
        return androidAuthChannel
                .then(iosAuthChannel)
                .then(webAuthChannel)
                .then(Mono.just(user));
    }


    //public Mono<Tuple2<AedEvent, AedDevice>> publishRescuerOnRedisEventSubChannel(Tuple3<AedEvent, AedDevice, UserRes> tuple3) {
    //    return this.reactiveRedisTemplateRescuer
    //            .convertAndSend(redisConfigProperties.getAedEventSingleChannelPrefix()
    //                            + tuple3.getT1().getId() + redisConfigProperties.getRescuerPostfix(),
    //                    RequestedPreviewRescuer.build(tuple3.getT3()))
    //            .then(Mono.zip(Mono.just(tuple3.getT1()), Mono.just(tuple3.getT2())));
    //}
//
    //public Flux<RequestedPreviewRescuer> fetchPublishedRescuerFromSingleEvent(String channel) {
    //    return this.reactiveRedisTemplateRescuer
    //            .listenToChannel(redisConfigProperties.getAedEventSingleChannelPrefix() +
    //                    channel + redisConfigProperties.getRescuerPostfix())
    //            .flatMap(reactiveSubMsg -> Mono.just(reactiveSubMsg.getMessage()));
    //}
}
