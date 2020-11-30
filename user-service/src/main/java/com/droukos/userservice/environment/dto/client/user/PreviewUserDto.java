package com.droukos.userservice.environment.dto.client.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PreviewUserDto {
    private String username;
    private List<Integer> filterRolesCodes;
    private List<String> filterRoles;

    public static PreviewUserDto buildWithRoles(PreviewUserDto previewUserDto, Map<Integer, String>mapCodeRole) {
        List<Integer> filterRoleCodes = previewUserDto.getFilterRolesCodes();

        return new PreviewUserDto(
                previewUserDto.getUsername(),
                filterRoleCodes,
                filterRoleCodes.stream().map(mapCodeRole::get).collect(Collectors.toList())
                );
    }
}
