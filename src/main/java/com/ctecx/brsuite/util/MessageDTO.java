package com.ctecx.brsuite.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageDTO {
    private String Opmode;
    private String textMessage;
    private Long memberId;

}
