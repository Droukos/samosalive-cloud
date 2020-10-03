package com.droukos.authservice.environment.security.authorization;

import com.droukos.authservice.environment.dto.server.SecurityDto;
import com.droukos.authservice.environment.interfaces.core_services.SecRunByInfo;
import com.droukos.authservice.util.RolesUtil;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.droukos.authservice.environment.security.HttpExceptionFactory.unauthorized;
import static com.droukos.authservice.environment.services.GeneralSemantics.*;

@RequiredArgsConstructor
public class Authorization {
  private final HashSet<String> validRolesSet = new HashSet<>();
  private final List<String> allLvL1 = new ArrayList<>();
  private final List<String> allLvL0 = new ArrayList<>();

  private final Function<String, String[]> splitBySplitter =
      code -> code.split(SPLITTER.getEscapedCode());
  private final Function<String, String[]> splitByBetween =
      code -> code.split(BETWEEN.getEscapedCode());

  private final BiPredicate<List<String>, List<String>> lvl0AnyCheckNotValid =
      (userRoles, allLvL0List) ->
          !userRoles.contains(ANY.getCode())
              && allLvL0List.stream()
                  .noneMatch(role -> splitBySplitter.apply(role)[1].equals(allLvL0.get(0)));
  private final BiPredicate<List<String>, List<String>> lvl1AnyCheckNotValid =
      (userRoles, allLvL1List) ->
          userRoles.stream()
              .noneMatch(
                  role -> {
                    String userLvL0Role = splitBySplitter.apply(role)[0];
                    return allLvL1.stream().anyMatch(lvl1Role -> lvl1Role.equals(userLvL0Role));
                  });

  private final Predicate<List<String>> isNotValidUser =
      userAllRoles -> {
        userAllRoles = userAllRoles.stream().map(RolesUtil::roleCode).collect(Collectors.toList());
        if (!allLvL0.isEmpty() && !lvl0AnyCheckNotValid.test(userAllRoles, allLvL0)) return false;
        if (!allLvL1.isEmpty() && !lvl1AnyCheckNotValid.test(userAllRoles, allLvL1)) return false;
        return userAllRoles.stream().noneMatch(validRolesSet::contains);
      };

  public Mono<SecurityDto> run(SecurityDto securityDto) {

    SecRunByInfo secRunByInfo = securityDto.getServiceInfo().getRunByInfo();
    if (secRunByInfo.getPutFromSameUserId()
        && !securityDto.getPathVarId().equals(securityDto.getUserId())
        && hasNotValidRoles(securityDto.getRoles(), secRunByInfo)) {
      return Mono.error(unauthorized());
    } else {
      if (secRunByInfo.getCodes() != null
          && hasNotValidRoles(securityDto.getRoles(), secRunByInfo)) {
        return Mono.error(unauthorized());
      }
    }
    return Mono.just(securityDto);
  }

  private boolean hasNotValidRoles(List<String> roles, SecRunByInfo secRunByInfo) {

    buildValidRolesSet(secRunByInfo.getCodes());
    return isNotValidUser.test(roles);
  }

  private void buildValidRolesSet(List<String> codes) {
    codes.forEach(
        code -> {
          if (code.contains(BETWEEN.getCode())) analyzeBetweenRoles(code, validRolesSet);
          else if (code.contains(ANY.getCode())) analyzeAnyRoles(code);
          else validRolesSet.add(code);
        });
  }

  private void analyzeAnyRoles(String codeRole) {
    String[] splitRole = splitBySplitter.apply(codeRole);
    Predicate<String> isAnyCode = splintedRole -> splintedRole.equals(ANY.getCode());
    boolean isLvL0AnyCode = isAnyCode.test(splitRole[0]);
    boolean isLvL1AnyCode = isAnyCode.test(splitRole[1]);

    if (isLvL0AnyCode && isLvL1AnyCode) allLvL0.add(ANY.getCode());
    else if (isLvL0AnyCode) allLvL0.add(splitRole[1]);
    else if (isLvL1AnyCode) allLvL1.add(splitRole[0]);
  }

  private void analyzeBetweenRoles(String codeRole, HashSet<String> validRolesSet) {
    // 1st case like: 0.0_0.100, after split [0.0][0.100]
    // 2nd case like: 0.0_100, after split [0.0][100]

    String[] splitBetween = splitByBetween.apply(codeRole);
    String[] splitRole1 = splitBySplitter.apply(splitBetween[0]);

    IntFunction<String> addRoleString = value -> splitRole1[0] + SPLITTER.getCode() + value;

    IntStream.range(
            Integer.parseInt(splitRole1[1]),
            (splitBetween[1].contains(SPLITTER.getCode()))
                ? Integer.parseInt(splitBySplitter.apply(splitBetween[1])[1] + 1)
                : Integer.parseInt(splitBetween[1]) + 1)
        .forEach(value -> validRolesSet.add(addRoleString.apply(value)));
  }
}
