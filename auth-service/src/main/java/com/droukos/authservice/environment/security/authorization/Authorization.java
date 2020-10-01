package com.droukos.authservice.environment.security.authorization;

import com.droukos.authservice.environment.interfaces.AuthResult;
import com.droukos.authservice.environment.interfaces.core_services.SecRunByInfo;
import com.droukos.authservice.environment.interfaces.core_services.Service0Info;
import com.droukos.authservice.environment.interfaces.core_services.ServiceInfo;
import com.droukos.authservice.environment.security.JwtService;
import com.droukos.authservice.environment.security.TokenService;
import com.droukos.authservice.environment.services.LvL0_Services;
import com.droukos.authservice.model.user.UserRes;
import com.droukos.authservice.repo.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.droukos.authservice.environment.security.HttpExceptionFactory.badRequest;
import static com.droukos.authservice.environment.security.HttpExceptionFactory.unauthorized;
import static com.droukos.authservice.environment.services.GeneralSemantics.*;

@Component
@RequiredArgsConstructor
public class Authorization implements AuthResult {

  @NonNull private final TokenService tokenService;
  @NonNull private final UserRepository userRepository;
  @NonNull private final JwtService jwtService;

  @Value("${jwt.prefix.bearer}")
  private String tokenPrefix;

  private ServiceInfo serviceInfo;
  private String token;
  private String pathVarId;
  private List<String> roles;
  private String userId;

  public static List<String> getPathElements(RequestPath requestPath) {
    Predicate<PathContainer.Element> slashFilter = elem -> !elem.value().equals("/");
    return requestPath.pathWithinApplication().elements().stream()
        .filter(slashFilter)
        .map(PathContainer.Element::value)
        .collect(Collectors.toList());
  }

  public Authorization init(ServiceInfo serviceInfo, String token, RequestPath requestPath) {
    this.serviceInfo = serviceInfo;
    this.token = token;
    this.roles = jwtService.getRoles(token);
    this.userId = jwtService.getUserIdClaim(token);
    if (serviceInfo.getRunByInfo().getPutFromSameUserId()) {
      List<String> elements = getPathElements(requestPath);
      pathVarId = elements.get(elements.size() - 1);
    }
    return this;
  }

  public void run() {
    SecRunByInfo secRunByInfo = serviceInfo.getRunByInfo();
    if (secRunByInfo.getPutFromSameUserId()) {
      if (!pathVarId.equals(userId) && areRolesNotValid(roles, secRunByInfo)) throw unauthorized();
    } else {
      if (secRunByInfo.getCodes() != null && areRolesNotValid(roles, secRunByInfo))
        throw unauthorized();
    }
    if (serviceInfo.chkAccToken()) {
      checkDbTokenForUser();
    }
  }

  /*Refactor this!!!!*/
  public boolean areRolesNotValid(List<String> roles, SecRunByInfo secRunByInfo) {
    HashSet<String> validRolesSet = new HashSet<>();
    List<String> allLvL1 = new ArrayList<>();
    List<String> allLvL0 = new ArrayList<>();
    Function<String, String[]> splitBySplitter = code -> code.split(SPLITTER.getEscapedCode());
    Function<String, String[]> splitByBetween = code -> code.split(BETWEEN.getEscapedCode());

    Consumer<String> analyzeBetweenRoles =
        code -> {
          // 1st case like: 0.0_0.100, after split [0.0][0.100]
          // 2nd case like: 0.0_100, after split [0.0][100]

          String[] splitBetween = splitByBetween.apply(code);
          String[] splitRole1 = splitBySplitter.apply(splitBetween[0]);

          IntFunction<String> addRoleString = value -> splitRole1[0] + SPLITTER.getCode() + value;

          IntStream.range(
                  Integer.parseInt(splitRole1[1]),
                  (splitBetween[1].contains(SPLITTER.getCode()))
                      ? Integer.parseInt(splitBySplitter.apply(splitBetween[1])[1] + 1)
                      : Integer.parseInt(splitBetween[1]) + 1)
              .forEach(value -> validRolesSet.add(addRoleString.apply(value)));
        };

    Consumer<String> analyzeAnyRoles =
        code -> {
          String[] splitRole = splitBySplitter.apply(code);
          Predicate<String> isAnyCode = splintedRole -> splintedRole.equals(ANY.getCode());
          boolean isLvL0AnyCode = isAnyCode.test(splitRole[0]);
          boolean isLvL1AnyCode = isAnyCode.test(splitRole[1]);

          if (isLvL0AnyCode && isLvL1AnyCode) allLvL0.add(ANY.getCode());
          else if (isLvL0AnyCode) allLvL0.add(splitRole[1]);
          else if (isLvL1AnyCode) allLvL1.add(splitRole[0]);

          // System.out.println(allLvL1);
          // System.out.println(allLvL0);
        };

    Consumer<List<String>> buildValidRolesSet =
        codes ->
            codes.forEach(
                code -> {
                  if (code.contains(BETWEEN.getCode())) analyzeBetweenRoles.accept(code);
                  else if (code.contains(ANY.getCode())) analyzeAnyRoles.accept(code);
                  else validRolesSet.add(code);
                });

    Predicate<List<String>> lvl0AnyCheckNotValid =
        allLvL0List ->
            !allLvL0List.contains(ANY.getCode())
                && roles.stream()
                    .noneMatch(role -> splitBySplitter.apply(role)[1].equals(allLvL0.get(0)));
    Predicate<List<String>> lvl1AnyCheckNotValid =
        allLvL1List ->
            roles.stream()
                .noneMatch(
                    role -> {
                      String userLvL0Role = splitBySplitter.apply(role)[0];
                      return allLvL1.stream().anyMatch(lvl1Role -> lvl1Role.equals(userLvL0Role));
                    });

    Predicate<List<String>> isNotValidUser =
        userAllRoles -> {
          if (!allLvL0.isEmpty() && !lvl0AnyCheckNotValid.test(allLvL0)) return false;
          if (!allLvL1.isEmpty() && !lvl1AnyCheckNotValid.test(allLvL1)) return false;
          return userAllRoles.stream().noneMatch(validRolesSet::contains);
        };

    buildValidRolesSet.accept(secRunByInfo.getCodes());
    return isNotValidUser.test(roles);
  }

  @Override
  public Mono<UserRes> checkDbTokenForUser() {
    return userRepository
        .findFirstById(userId)
        .flatMap(user -> tokenService.validateAccessToken(user, token));
  }

  public Mono<List<Object>> getJwtClaims(ServerHttpRequest shr) {
    Mono<String> monoAuthToken =
        Mono.just(
                Objects.requireNonNull(shr.getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                    .replace(tokenPrefix, "")
                    .trim())
            .onErrorResume(Exception.class, err -> Mono.error(unauthorized()));

    return monoAuthToken.flatMap(
        authToken -> Mono.just(Arrays.asList(jwtService.testToken(authToken), authToken)));
  }

  public ServiceInfo fetchServiceInfo(RequestPath requestPath) {
    List<String> elements = Authorization.getPathElements(requestPath);
    // System.out.println(elements);
    if (elements.size() <= 1) throw badRequest();

    Predicate<String> isNotId = elem -> elem.chars().filter(Character::isDigit).count() <= 2;
    String lvl0Service = elements.stream().limit(2).collect(Collectors.joining("/"));
    String lvl1Service = elements.stream().skip(2).filter(isNotId).collect(Collectors.joining("/"));
    // System.out.println(lvl0Service);
    // System.out.println(lvl1Service);

    Supplier<ServiceInfo[]> lvi1ServeInfoArr =
        () ->
            Arrays.stream(LvL0_Services.values())
                .collect(
                    Collectors.toMap(
                        Service0Info::getServiceUrl,
                        Service0Info::getInfoServices,
                        (prev, next) -> next,
                        HashMap::new))
                .get(lvl0Service);

    Function<ServiceInfo[], ServiceInfo> fetchLvL1ServiceInfo =
        serviceInfos ->
            Arrays.stream(serviceInfos)
                .collect(
                    Collectors.toMap(
                        ServiceInfo::getServiceUrl,
                        Function.identity(),
                        (prev, next) -> next,
                        HashMap::new))
                .get(lvl1Service);

    if (lvi1ServeInfoArr.get() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    return fetchLvL1ServiceInfo.apply(lvi1ServeInfoArr.get());
  }
}
