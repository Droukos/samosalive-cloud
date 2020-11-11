package com.droukos.cdnservice.model.user.personal;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@ToString
@Document
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Personal {
  private String name;
  private String sur;
  private Profile prof;
  private AddressModel addr;
  private Map<String, PhoneModel> phones;
}
