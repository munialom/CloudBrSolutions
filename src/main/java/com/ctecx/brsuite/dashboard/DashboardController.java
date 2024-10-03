package com.ctecx.brsuite.dashboard;


import com.ctecx.brsuite.systemsetup.AppSetup;
import com.ctecx.brsuite.systemsetup.AppSetupService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class DashboardController {

	private final AppSetupService appSetupService;

	@GetMapping("/")
	public String index(Model model) {


		List<AppSetup> appSetups = appSetupService.schoolServerSettings();
		for (AppSetup appSetup : appSetups) {

			model.addAttribute(appSetup.getKey(), appSetup.getValue());
		}


	return "dashboard/index";
   
	}

	@GetMapping("dashboard")
	public String dashboard(Model model) {

		return "dashboard/dashboard";

	}










}