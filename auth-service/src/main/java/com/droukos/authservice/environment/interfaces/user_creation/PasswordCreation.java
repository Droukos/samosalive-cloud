package com.droukos.authservice.environment.interfaces.user_creation;

public interface PasswordCreation {
   NameCreation password(String password);
   NameCreation passwordWithPassConf(String password, String passwordConfirmed);
}
