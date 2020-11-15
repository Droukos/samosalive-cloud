package com.droukos.aedservice.environment.dto.client.aed_problems;

import lombok.*;

@ToString
@NoArgsConstructor @AllArgsConstructor
@Getter
public class AedProblemsDtoCreate {
        private String username;
        private String problemsTitle;
        private String address;
        private String information;
        private Number status;
        private String uploadedTime;
}
