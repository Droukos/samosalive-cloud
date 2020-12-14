package com.droukos.cdnservice.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@ToString
@Document
@AllArgsConstructor
@NoArgsConstructor
public class RoleModel {

    private String role;
    private String code;
    private boolean active;
    private LocalDateTime added;
    private String addedBy;
}
