package com.droukos.authservice.model.user.personal;

import lombok.*;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Personal {
  private String name;
  private String sur;
  private Profile prof;
  private AddressModel addr;
  private Map<String, PhoneModel> phones;
}
