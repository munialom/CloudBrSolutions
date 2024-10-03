package com.ctecx.brsuite.productcategory;


import com.ctecx.brsuite.brands.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {


    private  final CategoryService accountChartService;
    private final BrandService brandService;

    @GetMapping("/chart")
    public String showAccountChart(Model model) {
        List<Category> topLevelAccounts = accountChartService.getTopLevelAccounts();
        model.addAttribute("topLevelAccounts", topLevelAccounts);
        return "coa/chart";
    }





    @PostMapping("/create")
    public String addNewStrand(Category strand, RedirectAttributes redirectAttributes) {
        accountChartService.saveStrands(strand);
        redirectAttributes.addFlashAttribute("success", "Product Category details added successfully");
        return "redirect:/";
    }

    @GetMapping("/account_delete/{id}")
    public String deleteAccountById(@PathVariable(value = "id") Integer id, RedirectAttributes redirectAttributes) {

        try {
            accountChartService.deleletAccountChartById(id);

        } catch (DataIntegrityViolationException e) {

            redirectAttributes.addFlashAttribute("error", "Cannot delete parent entity with reference records");
            return "redirect:/strands/add";

        }
        return "redirect:/strands/add";
    }


}
