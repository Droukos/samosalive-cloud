package com.droukos.cdnservice.model.aed_device;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.geo.GeoJson;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AedDevice {
    private String id;
    private String nickname;
    private String modelName;
    private String descr;

    private LocalDateTime added;
    private String addedBy;
    private LocalDateTime created;
    private Integer status;
    private String statusDescr;

    //TODO check on the raw parameterized class 'GeoJson'
    private GeoJson defMap;
    private String picUrl;
    private String addrPicUrl;
    private String addr;

    //TODO check on the raw parameterized class 'GeoJson'
    private GeoJson onMap;
    private String onEvId;
    private String onUId;
    private LocalDateTime takenOn;
    private long onEstFin;
}
