package com.ctecx.brsuite.systemsetup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchoolSettingsDto {

    private String schoolEmail;
    private String schoolKra;
    private String schoolLocation;
    private String schoolLogo;
    private String schoolMotto;
    private String schoolName;
    private String schoolNhif;
    private String schoolNita;
    private String schoolNssf;
    private String schoolPhone;
    private String schoolPostal;

    public SchoolSettingsDto() {
    }

    public SchoolSettingsDto(String schoolEmail, String schoolKra, String schoolLocation, String schoolLogo,
                             String schoolMotto, String schoolName, String schoolNhif, String schoolNita,
                             String schoolNssf, String schoolPhone, String schoolPostal) {
        this.schoolEmail = schoolEmail;
        this.schoolKra = schoolKra;
        this.schoolLocation = schoolLocation;
        this.schoolLogo = schoolLogo;
        this.schoolMotto = schoolMotto;
        this.schoolName = schoolName;
        this.schoolNhif = schoolNhif;
        this.schoolNita = schoolNita;
        this.schoolNssf = schoolNssf;
        this.schoolPhone = schoolPhone;
        this.schoolPostal = schoolPostal;
    }

    public String getSchoolEmail() {
        return schoolEmail;
    }

    public void setSchoolEmail(String schoolEmail) {
        this.schoolEmail = schoolEmail;
    }

    public String getSchoolKra() {
        return schoolKra;
    }

    public void setSchoolKra(String schoolKra) {
        this.schoolKra = schoolKra;
    }

    public String getSchoolLocation() {
        return schoolLocation;
    }

    public void setSchoolLocation(String schoolLocation) {
        this.schoolLocation = schoolLocation;
    }

    public String getSchoolLogo() {
        return schoolLogo;
    }

    public void setSchoolLogo(String schoolLogo) {
        this.schoolLogo = schoolLogo;
    }

    public String getSchoolMotto() {
        return schoolMotto;
    }

    public void setSchoolMotto(String schoolMotto) {
        this.schoolMotto = schoolMotto;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolNhif() {
        return schoolNhif;
    }

    public void setSchoolNhif(String schoolNhif) {
        this.schoolNhif = schoolNhif;
    }

    public String getSchoolNita() {
        return schoolNita;
    }

    public void setSchoolNita(String schoolNita) {
        this.schoolNita = schoolNita;
    }

    public String getSchoolNssf() {
        return schoolNssf;
    }

    public void setSchoolNssf(String schoolNssf) {
        this.schoolNssf = schoolNssf;
    }

    public String getSchoolPhone() {
        return schoolPhone;
    }

    public void setSchoolPhone(String schoolPhone) {
        this.schoolPhone = schoolPhone;
    }

    public String getSchoolPostal() {
        return schoolPostal;
    }

    public void setSchoolPostal(String schoolPostal) {
        this.schoolPostal = schoolPostal;
    }

    public static SchoolSettingsDto from(List<Map<String, Object>> settingsList) {
        if (settingsList == null || settingsList.isEmpty()) {
            return new SchoolSettingsDto();
        }

        Map<String,String> settingsMap = new HashMap<>();
        settingsList.forEach(settings ->{
            settingsMap.put((String)settings.get("key"), (String)settings.get("value"));
        });

        return new SchoolSettingsDto(
                settingsMap.getOrDefault("SCHOOL_EMAIL",""),
                settingsMap.getOrDefault("SCHOOL_KRA",""),
                settingsMap.getOrDefault("SCHOOL_LOCATION",""),
                settingsMap.getOrDefault("SCHOOL_LOGO",""),
                settingsMap.getOrDefault("SCHOOL_MOTTO",""),
                settingsMap.getOrDefault("SCHOOL_NAME",""),
                settingsMap.getOrDefault("SCHOOL_NHIF",""),
                settingsMap.getOrDefault("SCHOOL_NITA",""),
                settingsMap.getOrDefault("SCHOOL_NSSF",""),
                settingsMap.getOrDefault("SCHOOL_PHONE",""),
                settingsMap.getOrDefault("SCHOOL_POSTAL","")

        );

    }
}