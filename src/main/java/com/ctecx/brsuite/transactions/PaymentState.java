/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ctecx.brsuite.transactions;


public enum PaymentState {
    PENDING("Pending"),
    PAID("Paid");



    private final String displayText;

    PaymentState(String displayText) {
        this.displayText = displayText;
    }

    public String getDisplayText() {
        return displayText;
    }
}
