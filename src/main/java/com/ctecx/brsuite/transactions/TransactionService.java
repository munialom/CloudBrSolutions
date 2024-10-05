package com.ctecx.brsuite.transactions;

import com.ctecx.brsuite.customproductsmanager.CustomManagerProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final CustomManagerProductService customManagerProductService;

    @Transactional
    public List<Transaction> processSaleTransaction(SaleTransactionDTO dto) {
        List<Transaction> transactions = new ArrayList<>();
        BigDecimal totalAmount = dto.getTotalAmount();
        BigDecimal receivedAmount = dto.getReceivedAmount();
        List<String> paymentModes = dto.getPaymentModes();
        BigDecimal changeOut = dto.getChangeOut();

        // Validate input
        validateSaleTransaction(totalAmount, receivedAmount, paymentModes);

        BigDecimal actualReceivedAmount = receivedAmount.subtract(changeOut);

        // Process CASH first if it's a selected payment mode
        if (paymentModes.contains("CASH")) {
            BigDecimal cashAmount = actualReceivedAmount.min(totalAmount);
            transactions.add(createTransaction(dto, "CASH", cashAmount));
            totalAmount = totalAmount.subtract(cashAmount);
        }

        // Distribute remaining total amount to other payment modes
        for (String mode : paymentModes) {
            if (!mode.equals("CASH") && totalAmount.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal amount = totalAmount;
                transactions.add(createTransaction(dto, mode, amount));
                totalAmount = BigDecimal.ZERO;
                break; // Only one additional transaction needed
            }
        }

        if (transactions.isEmpty()) {
            throw new IllegalArgumentException("No valid payment amounts calculated");
        }

        int updatedCount = customManagerProductService.updateStatus(dto.getSerialNumber());
        return transactionRepository.saveAll(transactions);
    }

    private void validateSaleTransaction(BigDecimal totalAmount, BigDecimal receivedAmount, List<String> paymentModes) {
        if (totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Total amount must be greater than zero");
        }
        if (receivedAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Received amount must be greater than zero");
        }
        if (receivedAmount.compareTo(totalAmount) < 0) {
            throw new IllegalArgumentException("Received amount cannot be less than total amount");
        }
        if (paymentModes.isEmpty()) {
            throw new IllegalArgumentException("At least one payment mode must be selected");
        }
    }

    private Transaction createTransaction(SaleTransactionDTO dto, String paymentMode, BigDecimal amount) {
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
        transaction.setTransactionDate(LocalDate.now());
        transaction.setPaymentState(PaymentState.PAID);
        transaction.setOrderState(OrderState.COMPLETED);
        return transaction;
    }
}