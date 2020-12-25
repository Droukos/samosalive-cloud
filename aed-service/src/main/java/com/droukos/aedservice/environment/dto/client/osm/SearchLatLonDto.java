package com.droukos.aedservice.environment.dto.client.osm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchLatLonDto {
    private double lat;
    private double lon;
}
