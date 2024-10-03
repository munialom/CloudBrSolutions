package com.ctecx.brsuite.util;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RestController
public class TimezoneService {

    private static final ZoneId MAIN_TIMEZONE = ZoneId.of("UTC+3");
    private static final ZoneId SERVER_TIMEZONE = ZoneId.of("America/Los_Angeles");

    @GetMapping("/api/timezone-info")
    public Map<String, String> getTimezoneInfo() {
        Map<String, String> timezoneInfo = new HashMap<>();

        ZoneId systemZone = ZoneId.systemDefault();

        ZonedDateTime mainTime = ZonedDateTime.now(MAIN_TIMEZONE);
        ZonedDateTime systemTime = ZonedDateTime.now(systemZone);
        ZonedDateTime serverTime = ZonedDateTime.now(SERVER_TIMEZONE);

        timezoneInfo.put("mainTimezone", MAIN_TIMEZONE.toString());
        timezoneInfo.put("mainCurrentTime", mainTime.toString());
        timezoneInfo.put("systemTimezone", systemZone.toString());
        timezoneInfo.put("systemCurrentTime", systemTime.toString());
        timezoneInfo.put("serverTimezone", SERVER_TIMEZONE.toString());
        timezoneInfo.put("serverCurrentTime", serverTime.toString());

        // Calculate offsets
        long systemOffsetMinutes = (mainTime.getOffset().getTotalSeconds() - systemTime.getOffset().getTotalSeconds()) / 60;
        long serverOffsetMinutes = (mainTime.getOffset().getTotalSeconds() - serverTime.getOffset().getTotalSeconds()) / 60;

        timezoneInfo.put("systemOffsetFromMainTimezone", String.format("%d minutes", systemOffsetMinutes));
        timezoneInfo.put("serverOffsetFromMainTimezone", String.format("%d minutes", serverOffsetMinutes));

        return timezoneInfo;
    }
}