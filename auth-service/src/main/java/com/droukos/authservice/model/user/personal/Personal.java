package com.droukos.authservice.model.user.personal;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@ToString
@Document
@Getter
@AllArgsConstructor
@Setter
public class Personal {
  private String name;
  private String sur;
  private Profile prof;
  private Address addr;
  private PhoneList phoneList;
}
