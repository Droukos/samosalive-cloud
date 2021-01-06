package com.droukos.authservice.service.auth;

import com.droukos.authservice.repo.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService {

  private final UserRepository userRepository;

}
