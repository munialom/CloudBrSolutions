package com.ctecx.brsuite.chartofaccounts;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/account-charts")
public class AccountChartController {
    private final AccountChartService accountChartService;

    public AccountChartController(AccountChartService accountChartService) {
        this.accountChartService = accountChartService;
    }

    @PostMapping
    public ResponseEntity<AccountChart> createAccountChart(@RequestBody AccountChartDTO dto) {
        AccountChart createdAccountChart = accountChartService.createAccountChart(dto);
        return ResponseEntity.ok(createdAccountChart);
    }

    @GetMapping("/bank-accounts")
    public ResponseEntity<List<AccountChart>> getBankAccounts() {
        List<AccountChart> bankAccounts = accountChartService.getBankAccounts();
        return ResponseEntity.ok(bankAccounts);
    }

    @GetMapping(path = "/search", produces = "application/json")
    public List<AccountChartData> searchAccountCharts(@RequestParam String keyword) {
        return accountChartService.searchAccountCharts(keyword);
    }

    @GetMapping("/search-expenses")
    public List<AccountChartData> searchExpenseAccountCharts(@RequestParam String keyword) {
        return accountChartService.searchExpenseAccountCharts(keyword);
    }
    @GetMapping(path = "/all-account-charts", produces = "application/json")
    public ResponseEntity<List<Map<String, Object>>> getAllMembers() {
        List<Map<String, Object>> allAccountChartsRecords = accountChartService.allAccountChartsRecords();
        return ResponseEntity.ok(allAccountChartsRecords);
    }


    @GetMapping("/{id}")
    public ResponseEntity<AccountChart> getAccountChartById(@PathVariable Integer id) {
        return accountChartService.getAccountChartById(id)
                .map(accountChart -> new ResponseEntity<>(accountChart, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccountChart(@PathVariable Integer id) {
        accountChartService.deleteAccountChart(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PostMapping("/check-name")
    public ResponseEntity<Boolean> checkAccountChartByName(@RequestBody String name) {
        boolean exists = accountChartService.checkAccountChartByName(name);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }

    /*@PostMapping
    public ResponseEntity<AccountChart> createAccountChart(@RequestBody AccountChart accountChart) {
        AccountChart savedAccountChart = accountChartService.createAccountChart(accountChart);
        return new ResponseEntity<>(savedAccountChart, HttpStatus.CREATED);
    }*/

    @GetMapping("/parent-accounts")
    public ResponseEntity<List<AccountChart>> getAllParentAccountCharts() {
        List<AccountChart> parentAccountCharts = accountChartService.getAllParentAccountCharts();
        return new ResponseEntity<>(parentAccountCharts, HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<List<AccountChart>> getAllAccountCharts() {
        List<AccountChart> parentAccountCharts = accountChartService.getAllAccountCharts();
        return new ResponseEntity<>(parentAccountCharts, HttpStatus.OK);
    }
}