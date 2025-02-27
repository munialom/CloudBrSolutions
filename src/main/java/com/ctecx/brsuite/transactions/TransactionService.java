package com.ctecx.brsuite.transactions;

import com.ctecx.brsuite.customproductsmanager.CustomManagerProductService;
import com.ctecx.brsuite.util.SalesDateTimeManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CustomManagerProductService customManagerProductService;

    private final SalesDateTimeManager salesDateTimeManager;

    @Transactional
    public List<Transaction> processSaleTransaction(SaleTransactionDTO dto) {
        List<Transaction> transactions = new ArrayList<>();
        BigDecimal totalAmount = dto.getTotalAmount();
        BigDecimal receivedAmount = dto.getReceivedAmount();
        List<String> paymentModes = dto.getPaymentModes();

        // Validate input
        if (totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Total amount must be greater than zero");
        }
        if (receivedAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Received amount must be greater than zero");
        }
        if (receivedAmount.compareTo(totalAmount) > 0) {
            throw new IllegalArgumentException("Received amount cannot exceed total amount");
        }
        if (paymentModes.isEmpty()) {
            throw new IllegalArgumentException("At least one payment mode must be selected");
        }

        BigDecimal remainingTotal = totalAmount;
        BigDecimal remainingReceived = receivedAmount;

        // Process CASH first if it's a selected payment mode
        if (paymentModes.contains("CASH")) {
            BigDecimal cashAmount = remainingReceived;
            transactions.add(createTransaction(dto, "CASH", cashAmount));
            remainingTotal = remainingTotal.subtract(cashAmount);
            remainingReceived = BigDecimal.ZERO;
        }

        // Distribute remaining total amount to other payment modes
        for (String mode : paymentModes) {
            if (!mode.equals("CASH") && remainingTotal.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal amount = remainingTotal;
                transactions.add(createTransaction(dto, mode, amount));
                remainingTotal = BigDecimal.ZERO;
                break; // Only one additional transaction needed
            }
        }

        if (transactions.isEmpty()) {
            throw new IllegalArgumentException("No valid payment amounts calculated");
        }
        int updatedCount=customManagerProductService.updateStatus(dto.getSerialNumber());
        return transactionRepository.saveAll(transactions);
    }

    private Transaction createTransaction(SaleTransactionDTO dto, String paymentMode, BigDecimal amount) {
        ZonedDateTime transactionDateTime = salesDateTimeManager.getCurrentTransactionDateTime();
        LocalDate salesDate = salesDateTimeManager.getSalesDate(transactionDateTime);
        Transaction transaction = new Transaction();
        transaction.setSerialNumber(dto.getSerialNumber());
        transaction.setAmount(amount);
        transaction.setCredit(amount);
        transaction.setDebit(BigDecimal.ZERO);
        transaction.setDescription(dto.getDescription());
        transaction.setStatus("COMPLETED");
        transaction.setAccountName("Sales Account");
        transaction.setPaymentMode(paymentMode);
        transaction.setModule("SALES");
        transaction.setRef(dto.getSerialNumber());
        transaction.setTransaction_type("CREDIT");
        transaction.setSubmodule("CASH_SALE");
        transaction.setTransactionDate(salesDate);
        transaction.setPaymentState(PaymentState.PAID);
        transaction.setOrderState(OrderState.COMPLETED);
        return transaction;
    }
}