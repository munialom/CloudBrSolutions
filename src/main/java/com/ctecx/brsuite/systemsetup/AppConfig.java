package com.ctecx.brsuite.systemsetup;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class AppConfig {

    @Bean
    public SchoolSettingsDto schoolSettingsDto(AppSetupService appSetupService) {
        List<AppSetup> appSetups = appSetupService.schoolServerSettings();
        List<Map<String, Object>> settingsList = appSetups.stream()
                .map(appSetup -> {
                    Map<String, Object> setupMap = new HashMap<>();
                    setupMap.put("key", appSetup.getKey());
                    setupMap.put("value", appSetup.getValue());
                    return setupMap;
                }).toList();
        return SchoolSettingsDto.from(settingsList);
    }

}