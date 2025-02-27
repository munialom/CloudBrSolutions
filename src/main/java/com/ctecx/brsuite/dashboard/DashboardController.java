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


	@GetMapping("stats")
	public String currentstats(Model model) {

		return "dashboard/stats";

	}



	@GetMapping("monthly-summary-data")
	public String monthlySummary(Model model) {

		return "dashboard/monthly-summary";

	}
	@GetMapping("valuation-summary")
	public String stockValuations(Model model) {

		return "dashboard/stock-valuations-summary";

	}

	@GetMapping("month-sales-summary")
	public String monthlySales(Model model) {

		return "dashboard/monthly-sales-data";

	}



	@GetMapping("all-cashiers")
	public String allCashiers(Model model) {

		return "dashboard/all-cashiers";

	}
	@GetMapping("waiter-bills")
	public String waiterBillsData(Model model) {

		return "dashboard/waiters-bills-data";

	}




}