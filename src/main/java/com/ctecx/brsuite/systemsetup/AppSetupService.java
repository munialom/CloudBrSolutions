package com.ctecx.brsuite.systemsetup;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AppSetupService {
    @Autowired
    private final AppSetupRepository appSetupRepository;

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
