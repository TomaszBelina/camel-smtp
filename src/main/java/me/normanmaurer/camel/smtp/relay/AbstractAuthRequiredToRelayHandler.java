package me.normanmaurer.camel.smtp.relay;

import java.util.List;

import org.apache.james.protocols.smtp.core.AbstractAuthRequiredToRelayRcptHook;

public abstract class AbstractAuthRequiredToRelayHandler extends
		AbstractAuthRequiredToRelayRcptHook {

	protected List<String> localDomains;

	protected AbstractAuthRequiredToRelayHandler(List<String> localDomains) {
		this.localDomains = localDomains;
	}

	public List<String> getLocalDomains() {
		return localDomains;
	}

	public void setLocalDomains(List<String> localDomains) {
		this.localDomains = localDomains;
	}

}
