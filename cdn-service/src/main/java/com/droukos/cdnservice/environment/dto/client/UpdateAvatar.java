package com.droukos.cdnservice.environment.dto.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.File;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateAvatar {
  private String userid;
  private File av;
}