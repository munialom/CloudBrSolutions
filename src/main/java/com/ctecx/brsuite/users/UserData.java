package com.ctecx.brsuite.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserData {
    private Integer id;
    private String password;
    private String firstName;
    private String lastName;
}
