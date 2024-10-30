package com.ctecx.brsuite.requisitions;

import lombok.Data;

@Data
public class RequestedProducts {

    private Long productId;
    private Integer quantityRequested;
    private String ProductCode;
}
