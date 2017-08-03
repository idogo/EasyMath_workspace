package com.easymath.common;

/**
 * Represents  a user
 * @author liadpc
 *
 */
public class User {
	
	/**
	 * Username
	 */
   public static String userName;
   
   /**
    * Password
    */
   public static String password;

   // Ctor
   
    public User (String username , String password) {
    	User.userName = username;
    	User.password = password;
    }
    
    // Getters and Setters
    
    public String getPassword() {
        return password;
    }
    public String getName() {
        return userName;
    }
    public void setPassword(String password) {
    	User.password = password;
    	}
    public void setName(String userName) {
    	User.userName = userName;
    } 
}
