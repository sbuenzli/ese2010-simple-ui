package controllers;

import play.*;
import play.mvc.*; 

/**
 * Security extension for the Basic Knowledge Base.
 * 
 * @author sbuenzli@student.unibe.ch
 * @version 2010-09-30
 */
public class Security extends Secure.Security {
	/* Authenticate every user with his/her username as the password. */
	static boolean authenticate(String username, String password)
	{
		return username.equals(password);
	}
	
	/* Always redirect back to the homepage after login/logout. */
	static void onAuthenticated()
	{
		Application.index();
	}
	
	static void onDisconnected()
	{
		Application.index();
	}
}
