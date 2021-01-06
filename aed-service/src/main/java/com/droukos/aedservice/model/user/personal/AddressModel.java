package com.droukos.aedservice.model.user.personal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class AddressModel {
  private String cIso;
  private String state;
  private String city;

  public static AddressModel withCountryIso(String countryIso) {
    return new AddressModel(countryIso, null, null);
  }

  public static AddressModel withStateArea(String stateArea) {
    return new AddressModel(null, stateArea, null);
  }

  public static AddressModel withCity(String city) {
    return new AddressModel(null, null, city);
  }

  public static AddressModel withoutCity(String countryIso, String stateArea){
    return new AddressModel(countryIso, stateArea, null);
  }

  public static AddressModel withoutStateArea(String countryIso, String city) {
    return new AddressModel(countryIso, null, city);
  }

  public static AddressModel withoutCountryIso(String stateArea, String city) {
    return new AddressModel(null, stateArea, city);
  }

  public static AddressModel withCIsoStateCity(AddressModel addressModel, String countryIso, String stateArea, String city) {
    return new AddressModel(countryIso, stateArea, city);
  }


}
