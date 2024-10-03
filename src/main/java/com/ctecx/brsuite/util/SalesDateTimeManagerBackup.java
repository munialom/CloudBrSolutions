package com.ctecx.brsuite.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class SalesDateTimeManagerBackup {

    private static final LocalTime SALES_START_TIME = LocalTime.of(8, 0);

    public LocalDateTime getCurrentTransactionDateTime() {
        return LocalDateTime.now();
    }

    public LocalDate getSalesDate(LocalDateTime transactionDateTime) {
        LocalDate transactionDate = transactionDateTime.toLocalDate();
        LocalTime transactionTime = transactionDateTime.toLocalTime();

        if (transactionTime.isBefore(SALES_START_TIME)) {
            return transactionDate.minusDays(1);
        } else {
            return transactionDate;
        }
    }
}