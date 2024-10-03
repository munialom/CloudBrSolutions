/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ctecx.brsuite.transactions;


public enum OrderState {
    COMPLETED("Completed"),
    PENDING("Pending"),
    OPEN("Open"),
    CANCELLED("Cancelled");




    private final String displayText;

    OrderState(String displayText) {
        this.displayText = displayText;
    }

    public String getDisplayText() {
        return displayText;
    }
}
