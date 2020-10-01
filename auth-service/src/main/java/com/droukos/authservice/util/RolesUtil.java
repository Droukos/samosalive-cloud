package com.droukos.authservice.util;

import com.droukos.authservice.environment.interfaces.core_roles.RoleInfo;
import com.droukos.authservice.environment.interfaces.core_roles.Roles0Info;
import com.droukos.authservice.environment.roles.roles_list.LvL0Roles;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.droukos.authservice.environment.services.GeneralSemantics.SPLITTER;

public class RolesUtil {

  private RolesUtil() {}

  /*
   * Returns 1 if role1 is bigger
   * Returns 2 if role2 is bigger
   * Returns 0 if role1 and role2 are equal
   */
  public static int compareRole(String role1, String role2) {
    var splitRole1 = role1.split(SPLITTER.getEscapedCode());
    var splitRole2 = role2.split(SPLITTER.getEscapedCode());
    int lvl0R0 = Integer.parseInt(splitRole1[0]);
    int lvl0R1 = Integer.parseInt(splitRole2[0]);
    int lvl1R0 = Integer.parseInt(splitRole1[1]);
    int lvl1R1 = Integer.parseInt(splitRole2[1]);

    if (lvl0R0 > lvl0R1) return 2;
    else if (lvl0R0 == lvl0R1) {
      if (lvl1R0 > lvl1R1) return 2;
      else if (lvl1R0 == lvl1R1) return 0;
      else return 1;
    } else return 1;
  }

  public static String greatestRole(List<String> allRoles) {
    return allRoles.get(
        IntStream.range(0, allRoles.size())
            .reduce(
                (a, b) -> {
                  var splitRole0 = allRoles.get(a).split("\\.");
                  var splitRole1 = allRoles.get(b).split("\\.");
                  int lvl0R0 = Integer.parseInt(splitRole0[0]);
                  int lvl0R1 = Integer.parseInt(splitRole1[0]);
                  int lvl1R0 = Integer.parseInt(splitRole0[1]);
                  int lvl1R1 = Integer.parseInt(splitRole1[1]);
                  if (lvl0R0 > lvl0R1) return b;
                  else if (lvl0R0 == lvl0R1) return (lvl1R0 > lvl1R1) ? b : a;
                  else return a;
                })
            .orElse(0));
  }

    public static Map<Integer, RoleInfo[]> getRolesMap() {
        return Arrays.stream(LvL0Roles.values())
                .collect(
                        Collectors.toMap(
                                Roles0Info::getCode, Roles0Info::getRolesInfo, (prev, next) -> next, HashMap::new));
    }

    public static boolean doesRoleExist(String role) {
        int lvl0Role = Integer.parseInt(role.split(SPLITTER.getEscapedCode())[0]);
        var rolesMap = getRolesMap();
        return rolesMap.containsKey(lvl0Role)
                && Arrays.stream(rolesMap.get(lvl0Role))
                .anyMatch(roleInfo -> roleInfo.getRoleCode().equals(role));
    }
}
