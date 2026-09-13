/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Login;
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

    // Validation methods
    public boolean checkUserName() {
        return userName != null && userName.contains("_") && userName.length() <= 5;
    }

    public boolean checkPasswordComplexity() {
        if (password == null || password.length() < 8) return false;

        boolean hasUpperCase = false, hasDigit = false, hasSpecialChar = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpperCase = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else if (!Character.isLetterOrDigit(c)) hasSpecialChar = true;
        }
        return hasUpperCase && hasDigit && hasSpecialChar;
    }

    public boolean checkCellPhoneNumber() {        
        return cellPhoneNumber != null && CELL_PHONE_PATTERN.matcher(cellPhoneNumber).matches();
    }

    public String registerUser() {
        if (!checkUserName()) {
            return "Username is not correctly formatted; must contain an underscore and be ≤ 5 characters.";
        }
        if (!checkPasswordComplexity()) {
            return "Password is not correctly formatted; must be ≥ 8 chars, include uppercase, number, and special char.";
        }
        if (!checkCellPhoneNumber()) {
            return "Cell number is incorrectly formatted; must start with +27 and up to 10 digits.";
        }
        return "User registered successfully.";
    }

    public boolean loginUser(String enteredUserName, String enteredPassword) {
        lastLoginSuccessful = userName.equals(enteredUserName) && password.equals(enteredPassword);
        return lastLoginSuccessful;
    }

    public String returnLoginStatus() {
        return lastLoginSuccessful
                ? "Welcome " + firstName + " " + lastName + ", it is great to see you again."
                : "Username or password incorrect, please try again.";
    }

    // Getters
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getUserName() { return userName; }
    public String getCellPhoneNumber() { return cellPhoneNumber; }
}


