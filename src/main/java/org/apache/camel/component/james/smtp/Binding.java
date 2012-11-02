package org.apache.camel.component.james.smtp;

import org.apache.james.protocols.api.Encryption;

public class Binding {

	private String IP;

	private int port;

	private Encryption encryption;

	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Encryption getEncryption() {
		return encryption;
	}

	public void setEncryption(Encryption encryption) {
		this.encryption = encryption;
	}

}
