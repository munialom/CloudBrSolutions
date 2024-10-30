package com.ctecx.brsuite.requisitions;

import lombok.Data;

import java.util.List;

@Data
public class RequisitionDTO {

    private String requisitionNumber;
    private List<RequestedProducts>requestedProducts;

    private String comments;

}