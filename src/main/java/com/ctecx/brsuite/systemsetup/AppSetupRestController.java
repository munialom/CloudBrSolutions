package com.ctecx.brsuite.systemsetup;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/setup")
public class AppSetupRestController {

    private final AppSetupService appSetupService;

    @GetMapping(value = "/company", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AppSetup> getSchoolServerSettings() {
        return appSetupService.schoolServerSettings();
    }
}
