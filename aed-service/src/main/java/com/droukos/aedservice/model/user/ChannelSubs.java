package com.droukos.aedservice.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChannelSubs {
    private Map<String, EventChannel> aedEvSubs;
    private Map<String, ProblemChannel> aedPrSubs;
}
