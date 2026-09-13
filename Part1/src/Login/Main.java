/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Login;

/**
 *
 * @author tumi
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    // Create a Login object
        Login user = new Login("Tumisho", "Mokoena", "tum_1", "Pass@1234!", "+27838968976");

        // Try registering the user
        System.out.println(user.registerUser());

        // Attempt login with correct credentials
        boolean loggedIn = user.loginUser("tum_1", "Pass@1234!");
        System.out.println(user.returnLoginStatus());

        // Attempt login with wrong password
        user.loginUser("tum_1", "wrongPassword");
        System.out.println(user.returnLoginStatus());    
    }
    
}
