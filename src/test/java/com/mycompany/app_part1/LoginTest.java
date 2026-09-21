/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app_part1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test;
/**
 *
 * @author tumi
 */
public class LoginTest {
  @Test
    public void testUserNameCorrectlyFormatted() {
        Login login = new Login("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+2781298967");
        assertTrue(login.checkUserName());
    }

    @Test
    public void testUserNameIncorrectlyFormatted() {
        Login login = new Login("Thabo", "Mutla", "Muti!!!!!", "Ch&&sec@ke99!", "+27838976");
        assertFalse(login.checkUserName());
    }

    @Test
    public void testPasswordMeetsComplexityRequirements() {
        Login login = new Login("Numi", "Sangweni", "SaN-57", "Ch&&sec@ke99!", "+27831968977");
        assertTrue(login.checkPasswordComplexity());
    }

    @Test
    public void testPasswordDoesNotMeetComplexityRequirements() {
        Login login = new Login("Zubo", "Ezweni", "kyl_1", "password", "+27813976");
        assertFalse(login.checkPasswordComplexity());
    }

    @Test
    public void testCellPhoneNumberCorrectlyFormatted() {
        Login login = new Login("Katlego", "Legodi", "kyl_1", "Ch&&sec@ke99!", "+278389976");
        assertTrue(login.checkCellPhoneNumber());
    }

    @Test
    public void testCellPhoneNumberIncorrectlyFormatted() {
        Login login = new Login("Thendo", "Naswiri", "kyl_1", "Ch&&sec@ke99!", "08966553");
        assertFalse(login.checkCellPhoneNumber());
    }

    @Test
    public void testLoginSuccessful() {
        Login login = new Login("Thipi", "Mello", "kyl_1", "Ch&&sec@ke99!", "+27795316711");
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    public void testLoginFailed() {
        Login login = new Login("Qwedi", "Radebe", "kyl_1", "Ch&&sec@ke99!", "+27765110878");
        assertFalse(login.loginUser("kyl_1", "wrongPassword1!"));
    }

    @Test
    public void testRegisterUser_UsernameIncorrectlyFormatted() {
        Login login = new Login("Buhle", "Langa", "kyle!!!!!!", "Ch&&sec@ke99!", "+27814950078");
        assertEquals("Username is not correctly formatted; please ensure that your username contains an "
                + "underscore and is no more than five characters in length.", login.registerUser());
    }

    @Test
    public void testRegisterUser_PasswordIncorrectlyFormatted() {
        Login login = new Login("Khumo", "Luwa", "kyl_1", "password", "+27665902100");
        assertEquals("Password is not correctly formatted; please ensure that the password contains at least "
                + "eight characters, a capital letter, a number, and a special character.", login.registerUser());
    }

    @Test
    public void testRegisterUser_CellPhoneIncorrectlyFormatted() {
        Login login = new Login("Tumi", "Monwa", "kyl_1", "Ch&&sec@ke99!", "07653474098");
        assertEquals("Cell number is incorrectly formatted or does not contain international code; please "
                + "correct the number and try again.", login.registerUser());
    }

    @Test
    public void testReturnLoginStatus_Successful() {
        Login login = new Login("Kamo", "Matibo", "kyl_1", "Ch&&sec@ke99!", "+27814783551");
        login.loginUser("kyl_1", "Ch&&sec@ke99!");
        assertEquals("Welcome Kamo, Matibo, it is great to see you again.", login.returnLoginStatus());
    }

    @Test
    public void testReturnLoginStatus_Failed() {
        Login login = new Login("Wanga", "Stiba", "Wang_5", "Ch&&sec@ke99!", "+27725238871");
        login.loginUser("kyl_1", "wrongPassword1!");
        assertEquals("Username or password incorrect, please try again.", login.returnLoginStatus());
    }  
}
