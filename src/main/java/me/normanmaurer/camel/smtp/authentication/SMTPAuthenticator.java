package me.normanmaurer.camel.smtp.authentication;

// TODO: Auto-generated Javadoc
/**
 * The Interface SMTPAuthenticator.
 */
public interface SMTPAuthenticator {

	/**
	 * Authenticate.
	 * 
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 * @return true, if successful
	 */
	boolean authenticate(String username, String password);

}
