/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app_part1;
import java.util.regex.Pattern;
/**
 *
 * @author tumi
 */
public class Login {    
    private static final Pattern CELL_PHONE_PATTERN = Pattern.compile("^\\+27[0-9]{1,10}$");

    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String cellPhoneNumber;
    private boolean lastLoginSuccessful;

    // Constructor
    public Login(String firstName, String lastName, String userName, String password, String cellPhoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
        this.lastLoginSuccessful = false;
    }
}