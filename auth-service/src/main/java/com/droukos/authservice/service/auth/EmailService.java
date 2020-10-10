package com.droukos.authservice.service.auth;

import com.droukos.authservice.repo.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

  @NonNull private final UserRepository userRepository;

}
