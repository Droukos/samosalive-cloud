package com.droukos.aedservice.environment.dto.server.aed.aed_device;

import com.droukos.aedservice.util.GeoJsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.List;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AedDeviceRescuer {
    private String id;
    private String uniqueNickname;
    private String modelName;
    private String description;
    private String status;
    private String picUrl;
    private String address;
    @JsonDeserialize(using = GeoJsonDeserializer.class)
    private GeoJsonPoint homePoint;
    private String onEventId;
    private String takenOn;
    private double estimatedFinish;
    private String rescuerUsername;
    private String rescuerName;
    private String rescuerSurname;
    private String rescuerEmail;
    private List<String> rescuerPhones;
    private String rescuerAvatar;
    private String rescuerStatus;
    private List<String> rescuerRoles;
}
