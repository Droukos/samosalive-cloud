package com.droukos.authservice.environment.interfaces;

public interface AuthConcat {
   AuthVariable and();
   AuthVariable or();
   AuthResult checkAllAuthVars();
   boolean checkSingleAuthVar();
}
