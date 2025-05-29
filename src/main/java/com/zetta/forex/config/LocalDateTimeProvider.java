package com.zetta.forex.config;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static com.zetta.forex.util.Util.cutToNDigits;

@Component
public class LocalDateTimeProvider {

    public long getTimeEpoch(){

        return cutToNDigits(LocalDateTime.now()
                .atZone(ZoneOffset.UTC)
                .toInstant()
                .toEpochMilli(),10);
    }

    public LocalDateTime getTime(){
        return LocalDateTime.now();
    }


}
