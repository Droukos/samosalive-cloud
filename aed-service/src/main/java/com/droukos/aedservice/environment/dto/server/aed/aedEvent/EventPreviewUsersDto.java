package com.droukos.aedservice.environment.dto.server.aed.aedEvent;

import com.droukos.aedservice.environment.dto.server.user.RequestedPreviewUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EventPreviewUsersDto {
    private String username;
    private List<RequestedPreviewUser> users;
}
