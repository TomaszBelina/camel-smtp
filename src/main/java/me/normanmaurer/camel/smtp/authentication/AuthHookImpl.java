package me.normanmaurer.camel.smtp.authentication;

import static org.apache.camel.util.ObjectHelper.isEmpty;

import org.apache.james.protocols.smtp.SMTPRetCode;
import org.apache.james.protocols.smtp.SMTPSession;
import org.apache.james.protocols.smtp.hook.AuthHook;
import org.apache.james.protocols.smtp.hook.HookResult;
import org.apache.james.protocols.smtp.hook.HookReturnCode;

// TODO: Auto-generated Javadoc
/**
 * The Class AuthHookImpl.
 */
public class AuthHookImpl implements AuthHook {
	
	/** The authenticator. */
	private final SMTPAuthenticator authenticator;
	
	/**
	 * Instantiates a new auth hook impl.
	 *
	 * @param authenticator the authenticator
	 */
	public AuthHookImpl(SMTPAuthenticator authenticator) {
		this.authenticator = authenticator;
	}

	/* (non-Javadoc)
	 * @see org.apache.james.protocols.smtp.hook.AuthHook#doAuth(org.apache.james.protocols.smtp.SMTPSession, java.lang.String, java.lang.String)
	 */
	public HookResult doAuth(SMTPSession session, String username,
			String password) {
		if(isEmpty(username) || isEmpty(password)) {
			return new HookResult(HookReturnCode.DISCONNECT, SMTPRetCode.AUTH_REQUIRED, null);
		}
		boolean authenticated = authenticator.authenticate(username, password);
		if(authenticated) {
			return HookResult.ok();
		}
		else {
			return new HookResult(HookReturnCode.DISCONNECT, SMTPRetCode.AUTH_FAILED, null);
		}
	}

}
