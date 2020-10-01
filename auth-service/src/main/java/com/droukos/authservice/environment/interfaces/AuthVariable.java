package com.droukos.authservice.environment.interfaces;

public interface AuthVariable {
    AuthConcat isAnyAdmin();
    AuthConcat isGeneralAdmin();
    AuthConcat isAreaAdmin();
    AuthConcat isAnyUser();
    AuthConcat isSameUserById();
}
