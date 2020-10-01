package com.droukos.authservice.model.user.personal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@AllArgsConstructor
public class PhoneList {
  private List<String> phones;
}
