package com.amonnuns.doorman;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserLoginForm {
    private String username;
    private String password;

    public UserLoginForm(@JsonProperty("username") String username,
                         @JsonProperty("password") String password) {
        this.username = username;
        this.password = password;
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
