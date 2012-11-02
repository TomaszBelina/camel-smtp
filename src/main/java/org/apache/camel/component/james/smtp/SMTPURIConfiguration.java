/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/
package org.apache.camel.component.james.smtp;

import java.net.URI;
import java.util.List;

import org.apache.camel.component.james.smtp.relay.AbstractAuthRequiredToRelayHandler;
import org.apache.camel.component.james.smtp.relay.AllowToRelayHandler;
import org.apache.james.protocols.api.Encryption;
import org.apache.james.protocols.smtp.SMTPConfiguration;
import org.apache.james.protocols.smtp.hook.AuthHook;

/**
 * The Class SMTPURIConfiguration.
 */
public class SMTPURIConfiguration implements SMTPConfiguration {

	/** The bind ip. */
	private final String bindIP;

	/** The bind port. */
	private final int bindPort;

	/** The enforce helo ehlo. */
	private boolean enforceHeloEhlo = true;

	/** The enforce brackets. */
	private boolean enforceBrackets = true;

	/** The greeting. */
	private String greeting = "smtp.example.com";

	/** The software name. */
	private String softwareName = "Apache Camel SMTP Server/based on Apache James";

	/** The reset length. */
	private final int resetLength = 0;

	/** The max message size. */
	private long maxMessageSize = 0;

	/** The hello name. */
	private String helloName = "Nice to meet you!";

	/** The local domains. */
	private List<String> localDomains;

	/** The authenticator. */
	private AuthHook authHook;

	/** The consumer hook. */
	private DefaultMessageHook messageHook;

	private AbstractAuthRequiredToRelayHandler authRequiredToRelayHandler;

	private Encryption encryption;

	private List<Binding> additionalBindings;

	/**
	 * Instantiates a new sMTPURI configuration.
	 * 
	 * @param uri
	 *            the uri
	 */
	public SMTPURIConfiguration(URI uri) {
		bindIP = uri.getHost();
		bindPort = uri.getPort();
		// we use the AllowToRelayHandler per default
		authRequiredToRelayHandler = new AllowToRelayHandler(localDomains);
	}

	public AuthHook getAuthHook() {
		return authHook;
	}

	public AbstractAuthRequiredToRelayHandler getAuthRequiredToRelayHandler() {
		return authRequiredToRelayHandler;
	}

	/**
	 * Gets the bind ip.
	 * 
	 * @return the bind ip
	 */
	public String getBindIP() {
		return bindIP;
	}

	/**
	 * Gets the bind port.
	 * 
	 * @return the bind port
	 */
	public int getBindPort() {
		return bindPort;
	}

	/**
	 * Gets the consumer hook.
	 * 
	 * @return the consumer hook
	 */
	public DefaultMessageHook getMessageHook() {
		return messageHook;
	}

	public Encryption getEncryption() {
		return encryption;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.james.protocols.api.ProtocolConfiguration#getGreeting()
	 */
	public String getGreeting() {
		return greeting;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.james.protocols.smtp.SMTPConfiguration#getHelloName()
	 */
	public String getHelloName() {
		return helloName;
	}

	/**
	 * Gets the local domains.
	 * 
	 * @return the local domains
	 */
	public List<String> getLocalDomains() {
		return localDomains;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.james.protocols.smtp.SMTPConfiguration#getMaxMessageSize()
	 */
	public long getMaxMessageSize() {
		return maxMessageSize;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.james.protocols.smtp.SMTPConfiguration#getResetLength()
	 */
	/**
	 * Gets the reset length.
	 * 
	 * @return the reset length
	 */
	public int getResetLength() {
		return resetLength;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.james.protocols.api.ProtocolConfiguration#getSoftwareName()
	 */
	public String getSoftwareName() {
		return softwareName;
	}

	public boolean isAuthRequired(String remoteIP) {
		// auth is required if an auth hook was set
		return getAuthHook() != null ? true : false;
	}

	/**
	 * Checks if is enforce brackets.
	 * 
	 * @return true, if is enforce brackets
	 */
	public boolean isEnforceBrackets() {
		return enforceBrackets;
	}

	/**
	 * Checks if is enforce helo ehlo.
	 * 
	 * @return true, if is enforce helo ehlo
	 */
	public boolean isEnforceHeloEhlo() {
		return enforceHeloEhlo;
	}

	/**
	 * Relaying is allowed.
	 * 
	 * @param arg0
	 *            the arg0
	 * @return true, if is relaying allowed
	 */
	public boolean isRelayingAllowed(String arg0) {
		return false;
	}

	/**
	 * Checks if is start tls supported.
	 * 
	 * @return true, if is start tls supported
	 */
	public boolean isStartTLSSupported() {
		return false;
	}

	public void setAuthHook(AuthHook authHook) {
		this.authHook = authHook;
	}

	public void setAuthRequiredToRelayHandler(
			AbstractAuthRequiredToRelayHandler authRequiredToRelayHook) {
		authRequiredToRelayHandler = authRequiredToRelayHook;
	}

	/**
	 * Sets the consumer hook.
	 * 
	 * @param messageHook
	 *            the new consumer hook
	 */
	public void setMessageHook(DefaultMessageHook consumerHook) {
		messageHook = consumerHook;
	}

	public void setEncryption(Encryption encryption) {
		this.encryption = encryption;
	}

	/**
	 * Sets the enforce brackets.
	 * 
	 * @param enforceBrackets
	 *            the new enforce brackets
	 */
	public void setEnforceBrackets(boolean enforceBrackets) {
		this.enforceBrackets = enforceBrackets;
	}

	/**
	 * Sets the enforce helo ehlo.
	 * 
	 * @param enforceHeloEhlo
	 *            the new enforce helo ehlo
	 */
	public void setEnforceHeloEhlo(boolean enforceHeloEhlo) {
		this.enforceHeloEhlo = enforceHeloEhlo;
	}

	/**
	 * Sets the greeting.
	 * 
	 * @param greeting
	 *            the new greeting
	 */
	public void setGreeting(String greeting) {
		this.greeting = greeting;
	}

	/**
	 * Sets the hello name.
	 * 
	 * @param helloName
	 *            the new hello name
	 */
	public void setHelloName(String helloName) {
		this.helloName = helloName;
	}

	/**
	 * Sets the local domains.
	 * 
	 * @param localDomains
	 *            the new local domains
	 */
	public void setLocalDomains(List<String> localDomains) {
		this.localDomains = localDomains;
	}

	/**
	 * Sets the max message size.
	 * 
	 * @param maxMessageSize
	 *            the new max message size
	 */
	public void setMaxMessageSize(long maxMessageSize) {
		this.maxMessageSize = maxMessageSize;
	}

	/**
	 * Sets the software name.
	 * 
	 * @param softwareName
	 *            the new software name
	 */
	public void setSoftwareName(String softwareName) {
		this.softwareName = softwareName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.james.protocols.smtp.SMTPConfiguration#
	 * useAddressBracketsEnforcement()
	 */
	public boolean useAddressBracketsEnforcement() {
		return enforceBrackets;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.james.protocols.smtp.SMTPConfiguration#useHeloEhloEnforcement
	 * ()
	 */
	public boolean useHeloEhloEnforcement() {
		return enforceHeloEhlo;
	}

	public List<Binding> getAdditionalBindings() {
		return additionalBindings;
	}

	public void setAdditionalBindings(List<Binding> additionalBindings) {
		this.additionalBindings = additionalBindings;
	}

}
