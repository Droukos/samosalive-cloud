package com.droukos.userservice.model.user.personal;

import com.droukos.userservice.environment.dto.client.user.UpdateUserPersonal;
import lombok.*;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddressModel {
  private String cIso;
  private String state;
  private String city;

  public static AddressModel updateAddressInfo(UpdateUserPersonal updateUserPersonal) {
    return new AddressModel(
            updateUserPersonal.getCiso(),
            updateUserPersonal.getState(),
            updateUserPersonal.getCity()
    );
  }
}
