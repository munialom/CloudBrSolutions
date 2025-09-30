package com.ctecx.brsuite.systemsetup;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@AllArgsConstructor
public class AppSetupService {
    @Autowired
    private final AppSetupRepository appSetupRepository;



    // New method to fetch only SCHOOL_NAME, SCHOOL_PHONE, SCHOOL_POSTAL as a Map
    public Map<String, Object> getLimitedSchoolSettingsMap() {
        Set<String> requiredKeys = Set.of("SCHOOL_NAME", "SCHOOL_PHONE", "SCHOOL_POSTAL","SCHOOL_KRA");
        List<AppSetup> settings = appSetupRepository.findBySetupCategory(SetupCategory.SCHOOL);
        Map<String, Object> settingsMap = new HashMap<>();
        settings.stream()
                .filter(setting -> requiredKeys.contains(setting.getKey()))
                .forEach(setting -> settingsMap.put(setting.getKey(), setting.getValue()));
        return settingsMap;
    }

    // Optional: Convert AppSetup entity to DTO (if needed elsewhere)
    private AppSetupDTO toDTO(AppSetup appSetup) {
        AppSetupDTO dto = new AppSetupDTO();
        dto.setKey(appSetup.getKey());
        dto.setValue(appSetup.getValue());
        dto.setSetupCategory(appSetup.getSetupCategory().name());
        return dto;
    }

    public List<AppSetup> appSetupList() {

        return (List<AppSetup>) appSetupRepository.findAll();
    }

    public void saveAll(List<AppSetup> appSetups) {

        appSetupRepository.saveAll(appSetups);

    }

    public List<AppSetup> mailServerSettings() {

        return appSetupRepository.findBySetupCategory(SetupCategory.MAIL_SERVER);
    }

    public List<AppSetup> smsServerSettings() {

        return appSetupRepository.findBySetupCategory(SetupCategory.SMS);
    }

    public List<AppSetup> schoolServerSettings() {

        return appSetupRepository.findBySetupCategory(SetupCategory.SCHOOL);
    }

    public List<AppSetup> posHardware() {

        return appSetupRepository.findBySetupCategory(SetupCategory.FEES);
    }

    public List<AppSetup> mailTemplateSettings() {

        return appSetupRepository.findBySetupCategory(SetupCategory.MAIL_TEMPLATE);
    }

    public EmailSetting getEmailSettings() {
        List<AppSetup> allGeneralSettings = appSetupRepository.findBySetupCategory(SetupCategory.MAIL_SERVER);
        allGeneralSettings.addAll(appSetupRepository.findBySetupCategory(SetupCategory.MAIL_TEMPLATE));

        return new EmailSetting(allGeneralSettings);
    }

    public SmsSetting getSmsSetting() {
        List<AppSetup> allGeneralSettings = appSetupRepository.findBySetupCategory(SetupCategory.SMS);
        //allGeneralSettings.addAll(appSetupRepository.findBySetupCategory(SetupCategory.MAIL_TEMPLATE));

        return new SmsSetting(allGeneralSettings);
    }
}
