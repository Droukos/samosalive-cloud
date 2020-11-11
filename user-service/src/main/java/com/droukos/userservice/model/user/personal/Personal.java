package com.droukos.userservice.model.user.personal;

import com.droukos.userservice.environment.dto.client.user.UpdateUserPersonal;
import com.droukos.userservice.model.user.UserRes;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Personal {
  private String name;
  private String sur;
  private Profile prof;
  private AddressModel addr;
  private Map<String, PhoneModel> phones;

  public static Personal updatePersonalInfo(UserRes user, UpdateUserPersonal updateUserPersonal) {
    return new Personal(
            updateUserPersonal.getName(),
            updateUserPersonal.getSur(),
            Profile.updateProfileInfo(user, updateUserPersonal),
            AddressModel.updateAddressInfo(updateUserPersonal),
            user.getPhones()
    );
  }
}
