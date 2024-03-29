package com.droukos.aedservice.model.aed_device;

import com.droukos.aedservice.util.GeoJsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AedDevice {
    @Id
    private String id;
    @Indexed
    private String uniqNickname;
    private String modelName;
    private String desc;

    private LocalDateTime added;
    private String addedBy;
    private Integer status;
    private String statusDesc;

    @JsonDeserialize(using = GeoJsonDeserializer.class)
    private GeoJsonPoint homeP;
    private String picUrl;
    private String addrPicUrl;
    private String addr;

    @JsonDeserialize(using = GeoJsonDeserializer.class)
    private GeoJsonPoint onP;
    private String onEvId;
    private String onUId;
    private LocalDateTime takenOn;
    private long onEstFin;
}
