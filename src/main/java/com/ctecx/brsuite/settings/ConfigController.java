package com.ctecx.brsuite.settings;


import com.ctecx.brsuite.branch.BranchService;
import com.ctecx.brsuite.departments.DepartmentService;
import com.ctecx.brsuite.systemsetup.AppSetup;
import com.ctecx.brsuite.systemsetup.AppSetupService;
import com.ctecx.brsuite.users.User;
import com.ctecx.brsuite.users.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/configs")
@AllArgsConstructor
public class ConfigController {

    private  final UserService userService;
    private final AppSetupService appSetupService;
    private final BranchService branchService;
    private  final DepartmentService departmentService;



    @GetMapping("system-manager")
    public String systemConfigs(Model model) {

        List<AppSetup> appSetups = appSetupService.appSetupList();

        for (AppSetup appSetup : appSetups) {

            model.addAttribute(appSetup.getKey(), appSetup.getValue());
        }

        return "settings/settingsControlPanel";
    }


    @GetMapping("timeZone")
    public String systemConfigsTimeZone(Model model) {



        return "settings/timezone";
    }


    @GetMapping("all-users")
    public String systemConfigsUsers(Model model) {



        return "settings/users";
    }


    @GetMapping("allSettings")
    public String allSettingsTabs(Model model) {

        List<AppSetup> appSetups = appSetupService.appSetupList();

        for (AppSetup appSetup : appSetups) {

            model.addAttribute(appSetup.getKey(), appSetup.getValue());
        }

        return "system/All-Settings";
    }





    @GetMapping
    public String settingForm(Model model) {

        model.addAttribute("rolesData",userService.roleList());
        model.addAttribute("usersList",userService.userList());


        List<AppSetup> appSetups = appSetupService.appSetupList();

        for (AppSetup appSetup : appSetups) {

            model.addAttribute(appSetup.getKey(), appSetup.getValue());
        }

        return "configs/settings";
    }

    @GetMapping("system")
    public String system(Model model) {

        model.addAttribute("branches",branchService.getAllBranches());
        model.addAttribute("locations",departmentService.departmentList());

        List<AppSetup> appSetups = appSetupService.appSetupList();

        for (AppSetup appSetup : appSetups) {

            model.addAttribute(appSetup.getKey(), appSetup.getValue());
        }

        return "configs/system";
    }

    @GetMapping("users")
    public String users(Model model, User user) {

        user.setEnabled(true);
        model.addAttribute("user", user);
        model.addAttribute("rolesData",userService.roleList());
        model.addAttribute("usersList",userService.userList());
        model.addAttribute("branches",branchService.getAllBranches());


        List<AppSetup> appSetups = appSetupService.appSetupList();

        for (AppSetup appSetup : appSetups) {

            model.addAttribute(appSetup.getKey(), appSetup.getValue());
        }

        return "configs/system-users";
    }




    @GetMapping("communication")
    public String communication(Model model) {


        List<AppSetup> appSetups = appSetupService.appSetupList();

        for (AppSetup appSetup : appSetups) {

            model.addAttribute(appSetup.getKey(), appSetup.getValue());
        }

        return "configs/system-communication";
    }


    @GetMapping("communication-entry")
    public String communicationMainEntry(Model model) {


        return "system/communication";
    }


    @GetMapping("company-form")
    public String myCompanyForm(Model model) {

        List<AppSetup> appSetups = appSetupService.appSetupList();

        for (AppSetup appSetup : appSetups) {

            model.addAttribute(appSetup.getKey(), appSetup.getValue());
        }

        return "company/company-dashboard";
    }


    @GetMapping("company")
    public String myCompany(Model model) {


        return "system/company";
    }



    @GetMapping("user-entry")
    public String usersMainEntry(Model model) {


        return "system/users";
    }



    @GetMapping("flow")
    public String flowBoard(Model model) {


        List<AppSetup> appSetups = appSetupService.appSetupList();

        for (AppSetup appSetup : appSetups) {

            model.addAttribute(appSetup.getKey(), appSetup.getValue());
        }

        return "configs/configsFlowBoard";
    }


    @GetMapping("statutory")
    public String statutory(Model model) {


        List<AppSetup> appSetups = appSetupService.appSetupList();

        for (AppSetup appSetup : appSetups) {

            model.addAttribute(appSetup.getKey(), appSetup.getValue());
        }

        return "statutory/statutory-data";
    }




    @GetMapping("departments")
    public String departmentsLoad(Model model) {

        return "system/departments";
    }





    @GetMapping("roles-data")
    public String designationLoad(Model model) {

        return "system/designation";
    }




    @GetMapping("branch-data")
    public String branchesLoad(Model model) {

        return "system/branches";
    }

    @GetMapping("branch-form")
    public String branchesForm(Model model) {

        return "branches/branch-dashboard";
    }


    @GetMapping("banks-form")
    public String banksForm(Model model) {

        return "system/banks";
    }

    @GetMapping("banks-data")
    public String banksData(Model model) {

        return "system/banks-form";
    }


    @GetMapping("prof-form")
    public String professionalForm(Model model) {

        return "system/professions";
    }

    @GetMapping("prof-data")
    public String professionalData(Model model) {

        return "system/professional-form";
    }





}