package com.ctecx.brsuite.chartofaccounts;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AccountChartService {
    private final AccountChartRepository accountChartRepository;

    public List<AccountChart> getBankAccounts() {
        return accountChartRepository.findByBankAccountTrue();
    }

    public AccountChart createAccountChart(AccountChartDTO dto) {
        AccountChart accountChart = new AccountChart();
        accountChart.setName(dto.getName());
        accountChart.setAlias(dto.getAlias());
        accountChart.setAccountGroupEnum(dto.getAccountGroup());
        accountChart.setAccountGroup(dto.getAccountGroup().getDisplayText());
        accountChart.setParentGroup(dto.getAccountGroup().getParentGroup());
        accountChart.setBankAccount(dto.isBankAccount());
        accountChart.setCurrencyCode(dto.getCurrencyCode());
        accountChart.setStatus(true);

        if (dto.getParentId() != null) {
            AccountChart parent = accountChartRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent account not found"));
            accountChart.setParent(parent);
        }

        if (dto.getLinkedBankAccountId() != null) {
            AccountChart linkedBankAccount = accountChartRepository.findById(dto.getLinkedBankAccountId())
                    .orElseThrow(() -> new RuntimeException("Linked bank account not found"));
            if (!linkedBankAccount.isBankAccount()) {
                throw new RuntimeException("Linked account is not a bank account");
            }
            accountChart.setLinkedBankAccount(linkedBankAccount);
        }

        int accountCode = generateAccountCode(dto.getAccountGroup());
        accountChart.setAccountCode(accountCode);

        return accountChartRepository.save(accountChart);
    }

    public List<AccountChartData> searchAccountCharts(String keyword) {
        List<AccountChart> accountCharts = accountChartRepository.findByNameContainingIgnoreCase(keyword);
        return accountCharts.stream()
                .map(accountChart -> new AccountChartData(
                        accountChart.getId(),
                        accountChart.getName(),
                        accountChart.getAccountCode()))
                .collect(Collectors.toList());
    }

    public List<AccountChartData> searchExpenseAccountCharts(String keyword) {
        List<AccountGroup> expenseGroups = List.of(
                AccountGroup.OPERATING_EXPENSES,
                AccountGroup.NON_OPERATING_EXPENSES,
                AccountGroup.COST_OF_GOODS_SOLD
        );

        List<AccountChart> accountCharts = accountChartRepository.findByNameContainingIgnoreCaseAndAccountGroupEnumIn(keyword, expenseGroups);

        return accountCharts.stream()
                .map(accountChart -> new AccountChartData(
                        accountChart.getId(),
                        accountChart.getName(),
                        accountChart.getAccountCode()
                ))
                .collect(Collectors.toList());
    }



    public Optional<AccountChart> getAccountChartById(Integer id) {
        return accountChartRepository.findById(id);
    }

    public void deleteAccountChart(Integer id) {
        accountChartRepository.deleteById(id);
    }

    public int generateAccountCode(AccountGroup accountGroup) {
        Integer highestExistingCode = accountChartRepository.findHighestCodeByAccountGroup(accountGroup);
        if (highestExistingCode == null) {
            return AccountCodeGenerator.getDefaultCode(accountGroup);
        } else {
            return highestExistingCode + 10;
        }
    }

    public List<AccountChart> getAllParentAccountCharts() {
        return accountChartRepository.findByParentIsNull();
    }

    public List<AccountChart> getAllAccountCharts() {
        return accountChartRepository.findAll();
    }

    public boolean checkAccountChartByName(String name) {
        return accountChartRepository.existsByName(name);
    }

    List<Map<String, Object>> allAccountChartsRecords(){
        return  accountChartRepository.getAllAccountCharts();
    }

    public AccountChart getAccountByName(String name) {
        Optional<AccountChart> accountChart = accountChartRepository.findByName(name);
        return accountChart.orElseThrow(() -> new RuntimeException("Account not found with name: " + name));
    }
}