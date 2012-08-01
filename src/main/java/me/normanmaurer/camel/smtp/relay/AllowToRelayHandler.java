package me.normanmaurer.camel.smtp.relay;

import java.util.List;

import org.apache.james.protocols.smtp.core.AbstractAuthRequiredToRelayRcptHook;

// TODO: Auto-generated Javadoc
/**
 * Check if the domain is local and if so accept the email. If not reject it
 * 
 * 
 */
public class AllowToRelayHandler extends
		AbstractAuthRequiredToRelayRcptHook {

	/** The local domains. */
	private final List<String> localDomains;

	
	/**
	 * Instantiates a new allow to relay handler.
	 */
	public AllowToRelayHandler() {
		this(null);
	}
	
	/**
	 * Instantiates a new allow to relay handler.
	 *
	 * @param localDomains the local domains
	 */
	public AllowToRelayHandler(List<String> localDomains) {
		this.localDomains = localDomains;
	}

	/* (non-Javadoc)
	 * @see org.apache.james.protocols.smtp.core.AbstractAuthRequiredToRelayRcptHook#isLocalDomain(java.lang.String)
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