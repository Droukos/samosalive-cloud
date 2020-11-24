package com.droukos.cdnservice.model.user;

import com.droukos.cdnservice.model.user.personal.AddressModel;
import com.droukos.cdnservice.model.user.personal.Personal;
import com.droukos.cdnservice.model.user.personal.PhoneModel;
import com.droukos.cdnservice.model.user.personal.Profile;
import com.droukos.cdnservice.model.user.privacy.PrivacySettingMap;
import com.droukos.cdnservice.model.user.system.UserSystem;
import com.droukos.cdnservice.model.user.system.security.jwt.platforms.AndroidJWT;
import com.droukos.cdnservice.model.user.system.security.jwt.platforms.IosJWT;
import com.droukos.cdnservice.model.user.system.security.jwt.platforms.WebJWT;
import com.droukos.cdnservice.model.user.system.security.jwt.tokens.AccessToken;
import com.droukos.cdnservice.model.user.system.security.jwt.tokens.RefreshToken;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Document
public class UserRes {

    @Id
    private String id;
    @Indexed
    private String user;
    private String userC;
    private String pass;
    @Transient
    private String passC;
    @Indexed
    private String email;
    private String emailC;
    @Indexed
    private List<RoleModel> allRoles;
    private Personal prsn;
    private PrivacySettingMap privy;
    private UserSystem sys;
    private AppState appState;

    public Personal getPersonalModel() {
        return this.prsn;
    }

    public UserSystem getUserSystemModel() {
        return this.sys;
    }

    public Profile getProfileModel() {
        return this.prsn.getProf();
    }

    public Map<String, PhoneModel> getPhoneModels() {
        return this.prsn.getPhones();
    }

    public AddressModel getAddressModel() {
        return this.prsn.getAddr();
    }

    public String getName() {
        return this.prsn.getName();
    }

    public String getSurname() {
        return this.prsn.getSur();
    }

    public String getDescription() {
        return this.prsn.getProf().getDesc();
    }

    public String getCountryIso() {
        return this.prsn.getAddr().getCIso();
    }

    public String getProvince() {
        return this.prsn.getAddr().getState();
    }

    public String getCity() {
        return this.prsn.getAddr().getCity();
    }

    public String getAvatar() {
        return this.prsn.getProf().getAv();
    }

    public Map<String, PhoneModel> getPhones() {
        return this.prsn.getPhones();
    }

    public LocalDateTime getAndroidLastLogin() {
        return this.sys.getSec().getAndroidLog().getLLogin();
    }

    public LocalDateTime getIosLastLogin() {
        return this.sys.getSec().getIosLog().getLLogin();
    }

    public LocalDateTime getWebLastLogin() {
        return this.sys.getSec().getWebLog().getLLogin();
    }

    public LocalDateTime getAndroidLastLogout() {
        return this.sys.getSec().getAndroidLog().getLLogout();
    }

    public LocalDateTime getIosLastLogout() {
        return this.sys.getSec().getIosLog().getLLogout();
    }

    public LocalDateTime getWebLastLogout() {
        return this.sys.getSec().getWebLog().getLLogout();
    }

    public LocalDateTime getAccountCreated() {
        return this.sys.getAccC();
    }

    public AndroidJWT getAndroidJwtModel() {
        return this.sys.getSec().getAndroidJWT();
    }

    public IosJWT getIosJwtModel() {
        return this.sys.getSec().getIosJWT();
    }

    public WebJWT getWebJwtModel() {
        return this.sys.getSec().getWebJWT();
    }

    public RefreshToken getAndroidRefreshTokenModel() {
        return this.sys.getSec().getAndroidJWT().getReToken();
    }

    public RefreshToken getIosRefreshTokenModel() {
        return this.sys.getSec().getIosJWT().getReToken();
    }

    public RefreshToken getWebRefreshTokenModel() {
        return this.sys.getSec().getWebJWT().getReToken();
    }

    public String getAndroidRefreshTokenId() {
        return this.sys.getSec().getAndroidJWT().getReToken().getId();
    }

    public String getIosRefreshTokenId() {
        return this.sys.getSec().getIosJWT().getReToken().getId();
    }

    public String getWebRefreshTokenId() {
        return this.sys.getSec().getWebJWT().getReToken().getId();
    }

    public Date getAndroidRefreshTokenExp() {
        return this.sys.getSec().getAndroidJWT().getReToken().getExp();
    }

    public Date getIosRefreshTokenExp() {
        return this.sys.getSec().getIosJWT().getReToken().getExp();
    }

    public Date getWebRefreshTokenExp() {
        return this.sys.getSec().getWebJWT().getReToken().getExp();
    }

    public AccessToken getAndroidAccessTokenModel() {
        return this.sys.getSec().getAndroidJWT().getAccToken();
    }

    public AccessToken getIosAccessTokenModel() {
        return this.sys.getSec().getIosJWT().getAccToken();
    }

    public AccessToken getWebAccessTokenModel() {
        return this.sys.getSec().getWebJWT().getAccToken();
    }

    public String getAndroidAccessTokenId() {
        return this.sys.getSec().getAndroidJWT().getAccToken().getId();
    }

    public String getIosAccessTokenId() {
        return this.sys.getSec().getIosJWT().getAccToken().getId();
    }

    public String getWebAccessTokenId() {
        return this.sys.getSec().getWebJWT().getAccToken().getId();
    }
}
