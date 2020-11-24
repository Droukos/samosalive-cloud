package com.droukos.cdnservice.model.user.personal;

import lombok.*;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddressModel {
    private String cIso;
    private String state;
    private String city;
}
