package com.amonnuns.doorman;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserLoginForm {
    @NotBlank
    @Size(max=10)
    private String username;
    @NotBlank
    @Size(min = 6,max = 12)
    private String password;

    public UserLoginForm(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserLoginForm(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
