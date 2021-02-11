package com.droukos.userservice.environment.dto.server.user;


import com.droukos.userservice.environment.constants.FieldNames;
import com.droukos.userservice.model.user.RoleModel;
import com.droukos.userservice.model.user.UserRes;
import com.droukos.userservice.model.user.personal.PhoneModel;
import lombok.*;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RequestedUserInfo {

    private String userid;
    private String username;
    private String name;
    private String surname;
    private String email;
    private String avatar;
    private String description;
    private String countryCode;
    private String province;
    private String city;
    private Map<String, PhoneModel> phones;
    private LocalDateTime lastLoginAndroid;
    private LocalDateTime lastLoginIos;
    private LocalDateTime lastLoginWeb;
    private LocalDateTime lastLogoutAndroid;
    private LocalDateTime lastLogoutIos;
    private LocalDateTime lastLogoutWeb;
    private LocalDateTime userCreated;
    private boolean online;
    private Integer availability;
    private List<RoleModel> roleModels;

    public static Mono<RequestedUserInfo> buildMono(Tuple2<UserRes, Set<String>> tuple2) {
        return Mono.just(build(tuple2.getT1(), tuple2.getT2()));
    }

    public static RequestedUserInfo build(UserRes user, Set<String> fieldsToNullify) {
        Map<String, PhoneModel> phones = (user.getPhones() == null)? null : user.getPhones();
        String desc = user.getProfileModel() == null ? null : user.getDescription();
        String avatar = user.getProfileModel() == null ? null : user.getAvatar();
        String cIso = user.getAddressModel() == null ? null : user.getCountryIso();
        String province = user.getAddressModel() == null ? null : user.getProvince();
        String city = user.getAddressModel() == null ? null : user.getCity();

        boolean showFullname = fieldsToNullify.contains(FieldNames.F_PRIVY_FULLNAME);
        boolean showEmail = fieldsToNullify.contains(FieldNames.F_PRIVY_EMAIL);
        boolean showDescription = fieldsToNullify.contains(FieldNames.F_PRIVY_DESCRIPTION);
        boolean showAddress = fieldsToNullify.contains(FieldNames.F_PRIVY_ADDRESS);
        boolean showPhones = fieldsToNullify.contains(FieldNames.F_PRIVY_PHONE);
        boolean showLastLogin = fieldsToNullify.contains(FieldNames.F_PRIVY_LAST_LOGIN);
        boolean showLastLogout = fieldsToNullify.contains(FieldNames.F_PRIVY_LAST_LOGOUT);
        boolean showAccountCreated = fieldsToNullify.contains(FieldNames.F_PRIVY_ACCOUNT_CREATED);
        boolean showAppStateOn = fieldsToNullify.contains(FieldNames.F_PRIVY_ONLINE_STATUS);

        return new RequestedUserInfo(
                user.getId(),
                user.getUserC(),
                showFullname? null : user.getName(),
                showFullname? null : user.getSurname(),
                showEmail? null : user.getEmailC(),
                avatar,
                showDescription? null : desc,
                showAddress? null : cIso,
                showAddress? null : province,
                showAddress? null : city,
                showPhones? null : phones,
                showLastLogin? null :  user.getAndroidLastLogin(),
                showLastLogin? null : user.getIosLastLogin(),
                showLastLogin? null :  user.getWebLastLogin(),
                showLastLogout? null : user.getAndroidLastLogout(),
                showLastLogout? null: user.getIosLastLogout(),
                showLastLogout? null : user.getWebLastLogout(),
                showAccountCreated? null : user.getAccountCreated(),
                !showAppStateOn && user.getAppState().isOn(),
                user.getAppState().getStatus(),
                user.getAllRoles().stream()
                        .map(RoleModel::buildRoleModelWithRoleCode)
                        .collect(Collectors.toList())
        );
    }
}

