package com.droukos.aedservice.environment.dto.server.user;

import com.droukos.aedservice.environment.enums.Availability;
import com.droukos.aedservice.model.user.RoleModel;
import com.droukos.aedservice.model.user.UserRes;
import com.droukos.aedservice.model.user.personal.PhoneModel;
import lombok.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.droukos.aedservice.environment.constants.PhoneKeys.PH_RESCUER_KEY;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RequestedPreviewRescuer {
    private String id;
    private String user;
    private String name;
    private String sur;
    private String email;
    private String avatar;
    private List<String> phones;
    private boolean on;
    private int status;
    private List<String> roles;



    public static RequestedPreviewRescuer build(UserRes user) {
        Map<String, PhoneModel> phones = user.getPrsn().getPhones();

        return new RequestedPreviewRescuer(
                user.getId(),
                user.getUser(),
                user.getName(),
                user.getSurname(),
                user.getEmail(),
                user.getAvatar(),
                phones == null || phones.get(PH_RESCUER_KEY) == null ? null : Collections.singletonList(phones.get(PH_RESCUER_KEY).getPhone()),
                user.getAppState().getStatus() != Availability.INVISIBLE.getCode() && user.getAppState().isOn(),
                user.getAppState().getStatus(),
                user.getAllRoles().stream().map(RoleModel::getRole).collect(Collectors.toList())
        );
    }
}
