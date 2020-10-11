package com.droukos.aedservice.environment.dto.client.aed_problems;

import lombok.*;

@ToString
@NoArgsConstructor @AllArgsConstructor
@Getter
public class AedProblemsDtoCreate {
        private String id;
        private String user;
        private String title;
        private String addr;
        private String info;
        private String status;
}
