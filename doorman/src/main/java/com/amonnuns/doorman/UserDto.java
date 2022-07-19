package com.amonnuns.doorman;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserDto {

    @NotBlank
    private String firstName;
    private String lastName;
    @NotBlank
    @Size(max=10, message = "The username can't be more than 10 characters long")
    private String userName;
    @NotBlank
    @Size(min = 6,max = 12, message = "Password must be between 6 to 12 character long")
    private String password;

    public UserDto(String firstName, String lastName, String userName, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
    }
    public UserDto(){
        this.firstName = null;
        this.lastName = null;
        this.userName = null;
        this.password = null;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
