package com.droukos.authservice.model.user.personal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AddressModel {
  private String cIso;
  private String state;
  private String city;
}
