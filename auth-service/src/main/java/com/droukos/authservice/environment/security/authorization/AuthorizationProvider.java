package com.droukos.authservice.environment.security.authorization;

import com.droukos.authservice.config.jwt.AccessTokenConfig;
import com.droukos.authservice.environment.dto.server.SecurityDto;
import com.droukos.authservice.environment.interfaces.core_services.Service0Info;
import com.droukos.authservice.environment.interfaces.core_services.ServiceInfo;
import com.droukos.authservice.environment.security.tokens.AccessJwtService;
import com.droukos.authservice.environment.services.LvL0_Services;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.RequestPath;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.droukos.authservice.util.factories.HttpExceptionFactory.*;

@Component
@RequiredArgsConstructor
public class AuthorizationProvider {

  @NonNull private final AccessJwtService accessJwtService;
  @NonNull private final AccessTokenConfig accessTokenConfig;

  public Mono<SecurityDto> runSec(SecurityDto securityDto) {

    return Mono.just(securityDto)
        .flatMap(this::setSecurityDtoJwtToken)
        .doOnNext(this::setSecurityDtoRoles)
        .flatMap(this::setSecurityDtoServiceInfo)
        .doOnNext(this::setSecurityDtoUserId)
        .doOnNext(this::setSecurityDtoPathVarId)
        .flatMap(this::checkCaseServiceInfoNotExist)
        .flatMap(this::caseRunSecurity);
  }

  private Mono<SecurityDto> caseRunSecurity(SecurityDto securityDto) {

    return securityDto.getServiceInfo().runSecurity()
        ? new Authorization().run(securityDto)
        : Mono.just(securityDto);
  }

  private Mono<SecurityDto> checkCaseServiceInfoNotExist(SecurityDto securityDto) {
    return securityDto.getServiceInfo() == null
        ? Mono.error(notFound("Service Info Not Found"))
        : Mono.just(securityDto);
  }

  public Mono<SecurityDto> setSecurityDtoJwtToken(SecurityDto securityDto) {

    return Mono.just(
            Objects.requireNonNull(
                    securityDto.getHttpRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .replace(accessTokenConfig.getTokenPrefix(), "")
                .trim())
        .onErrorResume(Exception.class, err -> Mono.error(unauthorized()))
        .doOnNext(securityDto::setAccToken)
        .then(Mono.just(securityDto));
  }

  private Mono<SecurityDto> setSecurityDtoServiceInfo(SecurityDto securityDto) {
    List<String> elements = getPathElements(securityDto.getHttpRequest().getPath());
    if (elements.size() <= 1) return Mono.error(badRequest());

    Predicate<String> isNotId = elem -> elem.chars().filter(Character::isDigit).count() <= 2;
    String lvl0Service = elements.stream().limit(2).collect(Collectors.joining("/"));
    String lvl1Service = elements.stream().skip(2).filter(isNotId).collect(Collectors.joining("/"));

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
      return Mono.error(badRequest());
    }
    securityDto.setServiceInfo(fetchLvL1ServiceInfo.apply(lvi1ServeInfoArr.get()));
    return Mono.just(securityDto);
  }

  private void setSecurityDtoUserId(SecurityDto securityDto) {
    securityDto.setUserId(accessJwtService.getUserIdClaim(securityDto.getAccToken()));
  }

  private void setSecurityDtoPathVarId(SecurityDto securityDto) {
    if (securityDto.getServiceInfo().getRunByInfo().getPutFromSameUserId()) {
      List<String> elements = getPathElements(securityDto.getHttpRequest().getPath());
      securityDto.setPathVarId(elements.get(elements.size() - 1));
    }
  }

  private void setSecurityDtoRoles(SecurityDto securityDto) {
    securityDto.setRoles(
        securityDto.getAuthentication().getAuthorities().stream()
            .map(Object::toString)
            .collect(Collectors.toList()));
  }

  private List<String> getPathElements(RequestPath requestPath) {
    Predicate<PathContainer.Element> slashFilter = elem -> !elem.value().equals("/");
    return requestPath.pathWithinApplication().elements().stream()
            .filter(slashFilter)
            .map(PathContainer.Element::value)
            .collect(Collectors.toList());
  }
}
