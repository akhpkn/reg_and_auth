package com.example.payload;

import javax.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank
    private String usernameOrEmail;
    @NotBlank
    private String password;

    public String getPassword() {
        return password;
    }

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }
}
