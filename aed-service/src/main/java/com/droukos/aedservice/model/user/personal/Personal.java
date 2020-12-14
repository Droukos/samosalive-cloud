package com.droukos.aedservice.model.user.personal;

import com.droukos.aedservice.model.user.UserRes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Personal {
  String name;
  String sur;
  Profile prof;
  AddressModel addr;
  Map<String, PhoneModel> phones;

  public static Personal noUpdate(UserRes user) {
    return new Personal(
            user.getName(),
            user.getSurname(),
            user.getPrsn().getProf(),
            user.getPrsn().getAddr(),
            user.getPrsn().getPhones()
    );
  }
}
