package org.apache.camel.component.james.smtp.relay;

import java.util.List;

/**
 * Denies to relay domains except when they are contained in the local domain
 * list. If no local domains are specified, relaying will be denied. <br/>
 * If authentication is used and a user is successfully authenticated, this
 * behavior is overriden
 * 
 * @author Marco Zapletal
 * 
 */
public class DenyToRelayHandler extends AbstractAuthRequiredToRelayHandler {

	/**
	 * Instantiates a new deny to relay handler.
	 */
	public DenyToRelayHandler() {
		this(null);
	}

	public DenyToRelayHandler(List<String> localDomains) {
		super(localDomains);
	}

	@Override
	protected boolean isLocalDomain(String domain) {
		if (localDomains == null) {
			return false;
		} else {
			return localDomains.contains(domain.trim());
		}
	}

	@Override
	public void setLocalDomains(List<String> localDomains) {
		this.localDomains = localDomains;
	}

}
