package com.droukos.authservice.service.auth;

import com.droukos.authservice.environment.dto.NewAccTokenData;
import com.droukos.authservice.environment.dto.NewRefTokenData;
import com.droukos.authservice.environment.dto.RequesterRefTokenData;
import com.droukos.authservice.environment.dto.server.auth.login.LoginResponse;
import com.droukos.authservice.environment.dto.server.auth.token.NewAccessTokenResponse;
import com.droukos.authservice.environment.security.TokenService;
import com.droukos.authservice.model.factories.user.res.tokens.UserFactoryAllTokens;
import com.droukos.authservice.model.factories.user.res.tokens.UserFactoryAndroidTokens;
import com.droukos.authservice.model.factories.user.res.tokens.UserFactoryIosTokens;
import com.droukos.authservice.model.factories.user.res.tokens.UserFactoryWebTokens;
import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.repo.UserRepository;
import com.droukos.authservice.util.RolesUtil;
import com.droukos.authservice.util.SecurityUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuple4;

import static com.droukos.authservice.environment.constants.Platforms.IOS;
import static com.droukos.authservice.environment.constants.Platforms.WEB;
import static com.droukos.authservice.util.factories.HttpExceptionFactory.badRequest;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Service
@AllArgsConstructor
public class TokensService {

  private final UserRepository userRepository;
  private final TokenService tokenService;

  public Mono<NewAccTokenData> genNewAccTokenToUser(UserRes user) {
    return tokenService.genNewAccessToken(user);
  }

  public Mono<NewRefTokenData> genNewRefTokenToUser(UserRes user) {
    return tokenService.genNewRefreshToken(user);
  }

  public boolean isValidAdmin(SecurityContext context) {
    return RolesUtil.roleChangeValidAdmins(SecurityUtil.getRequesterRoles(context));
  }

    public Mono<Tuple2<UserRes, RequesterRefTokenData>> validateUserRefreshToken
            (Tuple2<UserRes, RequesterRefTokenData> tuple2) {


        return tokenService.validateRefreshToken(tuple2)
                .then(Mono.zip(
                        Mono.just(tuple2.getT1()),
                        Mono.just(tuple2.getT2())
                ));
    }

  public Mono<Tuple3<UserRes, NewAccTokenData, NewRefTokenData>> saveUserToMongoDb
          (Tuple3<UserRes, NewAccTokenData, NewRefTokenData> tuple3) {


    return userRepository.save(tuple3.getT1())
            .then(Mono.zip(
                    Mono.just(tuple3.getT1()),
                    Mono.just(tuple3.getT2()),
                    Mono.just(tuple3.getT3()))
            );
  }

    public Mono<Tuple2<UserRes, NewAccTokenData>> saveUserToMongoDb
            (Tuple2<UserRes, NewAccTokenData> tuple2) {


        return userRepository.save(tuple2.getT1())
                .then(Mono.zip(
                        Mono.just(tuple2.getT1()),
                        Mono.just(tuple2.getT2())
                ));
    }

    public Mono<Tuple3<UserRes, NewAccTokenData, NewRefTokenData>> itsSameDeleteAllTokensAndAddNew
            (Tuple4<UserRes, SecurityContext, NewAccTokenData, NewRefTokenData> tuple4) {


        return Mono.zip(
                Mono.just(
                        switch (SecurityUtil.getRequesterDevice(tuple4.getT2())) {
                            case IOS -> UserFactoryIosTokens.updateIosTokensDeleteOthers(tuple4.getT1(), tuple4.getT3(), tuple4.getT4());
                            case WEB -> UserFactoryWebTokens.updateWebTokensDeleteOthers(tuple4.getT1(), tuple4.getT3(), tuple4.getT4());
                            default -> UserFactoryAndroidTokens.updateAndroidTokensDeleteOthers(tuple4.getT1(), tuple4.getT3(), tuple4.getT4());
                        }),
                Mono.just(tuple4.getT3()),
                Mono.just(tuple4.getT4())
        );
    }


  public Mono<Tuple3<UserRes, NewAccTokenData, NewRefTokenData>> notSameDeleteAllTokens
          (Tuple4<UserRes, SecurityContext, NewAccTokenData, NewRefTokenData> tuple4) {


    return isValidAdmin(tuple4.getT2())
            ? Mono.zip(
                    Mono.just(UserFactoryAllTokens.deleteAllTokens(tuple4.getT1())),
                    Mono.just(tuple4.getT3()),
                    Mono.just(tuple4.getT4()))
            : Mono.error(badRequest("Forbidden action"));
  }

  public Mono<Tuple3<UserRes, NewAccTokenData, NewRefTokenData>> saveNewAccessTokenIdToRedis
          (Tuple3<UserRes, NewAccTokenData, NewRefTokenData> tuple3) {


    return tokenService.redisSetUserToken(tuple3.getT1(), tuple3.getT2())
            .then(Mono.zip(
                    Mono.just(tuple3.getT1()),
                    Mono.just(tuple3.getT2()),
                    Mono.just(tuple3.getT3())
            ));
  }

    public Mono<Tuple2<UserRes, NewAccTokenData>> saveNewAccessTokenIdToRedis
            (Tuple2<UserRes, NewAccTokenData> tuple2) {


        return tokenService.redisSetUserToken(tuple2.getT1(), tuple2.getT2())
                .then(Mono.zip(
                        Mono.just(tuple2.getT1()),
                        Mono.just(tuple2.getT2())
                ));
    }


  public Mono<Tuple3<UserRes, NewAccTokenData, NewRefTokenData>> removeAllAccessTokenIdFromRedis
          (Tuple3<UserRes, NewAccTokenData, NewRefTokenData> tuple3) {


    return tokenService.redisRemoveAllUserAccessTokens(tuple3.getT1())
            .then(Mono.zip(
                    Mono.just(tuple3.getT1()),
                    Mono.just(NewAccTokenData.nullAccessToken()),
                    Mono.just(tuple3.getT3())
            ));
  }



  public Mono<Tuple3<UserRes, NewAccTokenData, NewRefTokenData>> checkCaseRefTokenIsExpiring
          (Tuple4<UserRes, RequesterRefTokenData, NewAccTokenData, NewRefTokenData> tuple4) {


    return Mono.zip(
            Mono.just(tuple4.getT1()),
            Mono.just(tuple4.getT3()),
            Mono.just(tokenService.isRefreshAboutToExp(tuple4.getT2())
                    ? tuple4.getT4()
                    : NewRefTokenData.nullRefreshToken())
    );
  }



  public Mono<Tuple3<UserRes, NewAccTokenData, NewRefTokenData>> updateUserWithAccTokenAndExpiringRefToken
          (Tuple3<UserRes, NewAccTokenData, NewRefTokenData> tuple3) {


    return Mono.zip(
            Mono.just(
                    tuple3.getT3().getTokenId() == null
                        ? updateUserNotExpiringRefToken(tuple3.getT1(), tuple3.getT2())
                        : updateUserExpiringRefToken(tuple3.getT1(), tuple3.getT2(), tuple3.getT3())
                    ),
            Mono.just(tuple3.getT2()),
            Mono.just(tuple3.getT3())
    );
  }

  public Mono<Tuple2<UserRes, NewAccTokenData>> updateUserAccessToken(Tuple2<UserRes, NewAccTokenData> tuple2) {
      return Mono.just(updateUserNotExpiringRefToken(tuple2.getT1(), tuple2.getT2()))
              .zipWith(Mono.just(tuple2.getT2()));
  }

  private UserRes updateUserNotExpiringRefToken(UserRes user, NewAccTokenData accessTokenData) {


      return switch (accessTokenData.getUserDevice()) {
          case IOS -> UserFactoryIosTokens.updateIosAccessTokenOnly(user, accessTokenData);
          case WEB -> UserFactoryWebTokens.updateWebAccessTokenOnly(user, accessTokenData);
          default -> UserFactoryAndroidTokens.updateAndroidAccessTokenOnly(user, accessTokenData);
      };
  }

  private UserRes updateUserExpiringRefToken(UserRes user, NewAccTokenData accessTokenData, NewRefTokenData refreshTokenData) {


      return switch (accessTokenData.getUserDevice()) {
          case IOS -> UserFactoryIosTokens.updateIosTokensOnly(user, accessTokenData, refreshTokenData);
          case WEB -> UserFactoryWebTokens.updateWebTokensOnly(user, accessTokenData, refreshTokenData);
          default -> UserFactoryAndroidTokens.updateAndroidTokensOnly(user, accessTokenData, refreshTokenData);
      };
  }


    public Mono<ServerResponse> setRefTokenOnHttpCookieAndAccessTokenOnBody
            (Tuple3<UserRes, NewAccTokenData, NewRefTokenData> tuple3) {


        return ok().cookie(tokenService.refreshHttpCookie(tuple3.getT3()))
                .body(fromValue(new NewAccessTokenResponse(tuple3.getT2().getToken())));
    }

    public Mono<ServerResponse> justMessageRevoked() {
        return ok().body(fromValue("all.tokens.revoked"));
    }

    public Mono<ServerResponse> newAccessToken(Tuple3<UserRes, NewAccTokenData, NewRefTokenData> tuple3) {

        return tuple3.getT3().getTokenId() == null
                ? ok().cookie(tokenService.refreshHttpCookie(tuple3.getT3()))
                .body(fromValue(new NewAccessTokenResponse(tuple3.getT2().getToken())))
                : ok().body(fromValue(new NewAccessTokenResponse(tuple3.getT2().getToken())));
    }

    public Mono<ServerResponse> buildUserData(Tuple2<UserRes, NewAccTokenData> tuple2) {

        return ok().body(fromValue(LoginResponse.build(tuple2.getT1(), tuple2.getT2().getToken())));
    }




}
