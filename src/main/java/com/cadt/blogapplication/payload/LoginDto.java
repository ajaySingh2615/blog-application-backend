package com.cadt.blogapplication.payload;

import lombok.Data;

@Data
public class LoginDto {

    private String usernameOrEmail;
    private String password;
}
