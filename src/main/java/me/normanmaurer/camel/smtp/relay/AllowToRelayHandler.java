package me.normanmaurer.camel.smtp.relay;

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * Check if the domain is local and if so accept the email. If not reject it. <br/>
 * Note: If the <code>localDomains</code> variable is null, mails for all
 * domains will be accepted even if authentication is not used or fails.
 * 
 * 
 */
public class AllowToRelayHandler extends AbstractAuthRequiredToRelayHandler {

	/**
	 * Instantiates a new allow to relay handler.
	 */
	public AllowToRelayHandler() {
		this(null);
	}

	public AllowToRelayHandler(List<String> localDomains) {
		super(localDomains);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.james.protocols.smtp.core.AbstractAuthRequiredToRelayRcptHook
	 * #isLocalDomain(java.lang.String)
	 */
	@Override
	protected boolean isLocalDomain(String domain) {
		if (localDomains == null) {
			// no restriction was set.. accept it!
			return true;
		} else {
			return localDomains.contains(domain.trim());
		}
	}

}