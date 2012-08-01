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
package me.normanmaurer.camel.smtp;

import java.net.URI;
import java.util.List;

import me.normanmaurer.camel.smtp.authentication.SMTPAuthenticator;

import org.apache.james.protocols.smtp.SMTPConfiguration;


/**
 * The Class SMTPURIConfiguration.
 */
public class SMTPURIConfiguration implements SMTPConfiguration{

    /** The bind ip. */
    private final String bindIP;
    
    /** The bind port. */
    private final int bindPort;
    
    /** The enforce helo ehlo. */
    private boolean enforceHeloEhlo = true;
    
    /** The enforce brackets. */
    private boolean enforceBrackets = true;
    
    /** The greeting. */
    private String greeting = "Camel SMTP 1.0";
    
    /** The software name. */
    private String softwareName = "Camel SMTP 1.0";
    
    /** The reset length. */
    private final int resetLength = 0;
    
    /** The max message size. */
    private long maxMessageSize = 0;
    
    /** The hello name. */
    private String helloName = "Camel SMTP";
    
    /** The local domains. */
    private List<String> localDomains;
    
    /** The authenticator. */
    private SMTPAuthenticator authenticator;
    
    /** The consumer hook. */
    private DefaultConsumerHook consumerHook;
    
    /**
     * Instantiates a new sMTPURI configuration.
     *
     * @param uri the uri
     */
    public SMTPURIConfiguration(URI uri) {
    	bindIP = uri.getHost();
        bindPort = uri.getPort();
    }

	/**
	 * Gets the authenticator.
	 *
	 * @return the authenticator
	 */
	public SMTPAuthenticator getAuthenticator() {
		return authenticator;
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
	public DefaultConsumerHook getConsumerHook() {
		return consumerHook;
	}

    /* (non-Javadoc)
     * @see org.apache.james.protocols.api.ProtocolConfiguration#getGreeting()
     */
	public String getGreeting() {
		return greeting;
	}

    /*
     * (non-Javadoc)
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
     * @see org.apache.james.protocols.smtp.SMTPConfiguration#getMaxMessageSize()
     */
    public long getMaxMessageSize() {
        return maxMessageSize;
    }

    /*
     * (non-Javadoc)
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

    /* (non-Javadoc)
     * @see org.apache.james.protocols.api.ProtocolConfiguration#getSoftwareName()
     */
	public String getSoftwareName() {
		return softwareName;
	}

    /**
     * Auth is not required.
     *
     * @param arg0 the arg0
     * @return true, if is auth required
     */
    public boolean isAuthRequired(String arg0) {
        return false;
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
     * @param arg0 the arg0
     * @return true, if is relaying allowed
     */
    public boolean isRelayingAllowed(String arg0) {
        return true;
    }
    
    /**
     * Checks if is start tls supported.
     *
     * @return true, if is start tls supported
     */
    public boolean isStartTLSSupported() {
        return false;
    }

    /**
     * Sets the authenticator.
     *
     * @param authenticator the new authenticator
     */
    public void setAuthenticator(SMTPAuthenticator authenticator) {
		this.authenticator = authenticator;
	}

	/**
	 * Sets the consumer hook.
	 *
	 * @param consumerHook the new consumer hook
	 */
	public void setConsumerHook(DefaultConsumerHook consumerHook) {
		this.consumerHook = consumerHook;
	}

	/**
	 * Sets the enforce brackets.
	 *
	 * @param enforceBrackets the new enforce brackets
	 */
	public void setEnforceBrackets(boolean enforceBrackets) {
		this.enforceBrackets = enforceBrackets;
	}

	/**
	 * Sets the enforce helo ehlo.
	 *
	 * @param enforceHeloEhlo the new enforce helo ehlo
	 */
	public void setEnforceHeloEhlo(boolean enforceHeloEhlo) {
		this.enforceHeloEhlo = enforceHeloEhlo;
	}

	/**
	 * Sets the greeting.
	 *
	 * @param greeting the new greeting
	 */
	public void setGreeting(String greeting) {
		this.greeting = greeting;
	}

	/**
	 * Sets the hello name.
	 *
	 * @param helloName the new hello name
	 */
	public void setHelloName(String helloName) {
		this.helloName = helloName;
	}

	/**
	 * Sets the local domains.
	 *
	 * @param localDomains the new local domains
	 */
	public void setLocalDomains(List<String> localDomains) {
		this.localDomains = localDomains;
	}

	/**
	 * Sets the max message size.
	 *
	 * @param maxMessageSize the new max message size
	 */
	public void setMaxMessageSize(long maxMessageSize) {
		this.maxMessageSize = maxMessageSize;
	}

	/**
	 * Sets the software name.
	 *
	 * @param softwareName the new software name
	 */
	public void setSoftwareName(String softwareName) {
		this.softwareName = softwareName;
	}

    /* (non-Javadoc)
     * @see org.apache.james.protocols.smtp.SMTPConfiguration#useAddressBracketsEnforcement()
     */
    public boolean useAddressBracketsEnforcement() {
        return enforceBrackets;
    }

	/*
     * (non-Javadoc)
     * @see org.apache.james.protocols.smtp.SMTPConfiguration#useHeloEhloEnforcement()
     */
    public boolean useHeloEhloEnforcement() {
        return enforceHeloEhlo;
    }
}
