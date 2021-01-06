package com.droukos.authservice.environment.dto.client.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UpRoles {
    private List<UpRole> upRoles;
}
