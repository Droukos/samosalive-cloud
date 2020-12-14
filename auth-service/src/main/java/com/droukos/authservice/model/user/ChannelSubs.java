package com.droukos.authservice.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChannelSubs {
    private List<String> aedEvSubs;
    private List<String> aedPrSubs;
}
