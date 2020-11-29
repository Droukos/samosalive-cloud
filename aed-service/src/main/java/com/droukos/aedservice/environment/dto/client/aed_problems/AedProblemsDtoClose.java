package com.droukos.aedservice.environment.dto.client.aed_problems;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AedProblemsDtoClose {
    private String id;
    private String conclusion;
}
