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

import org.apache.james.protocols.smtp.SMTPConfiguration;


/**
 * Parse a given uri and set the configuration for the specified parameters in the uri
 *
 */
public class SMTPURIConfiguration implements SMTPConfiguration{

    private final String bindIP;
    private final int bindPort;
    private boolean enforceHeloEhlo = true;
    private boolean enforceBrackets = true;
    private String greeting = "Camel SMTP 1.0";
    private String softwareName = "Camel SMTP 1.0";
    private final int resetLength = 0;
    private long maxMessageSize = 0;
    private String helloName = "Camel SMTP";
    private List<String> localDomains;
    
    public SMTPURIConfiguration(URI uri) {
    	bindIP = uri.getHost();
        bindPort = uri.getPort();
    }

    /*
     * (non-Javadoc)
     * @see org.apache.james.protocols.smtp.SMTPConfiguration#getHelloName()
     */
    public String getHelloName() {
        return helloName;
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
    public int getResetLength() {
        return resetLength;
    }

    /**
     * Auth is not required 
     */
    public boolean isAuthRequired(String arg0) {
        return false;
    }

    /**
     * Relaying is allowed
     */
    public boolean isRelayingAllowed(String arg0) {
        return true;
    }

    /**
     * No StartTLS is supported
     */
    public boolean isStartTLSSupported() {
        return false;
    }

    /*
     * (non-Javadoc)
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
    
    /**
     * Return the IP address to bind the SMTP server to
     * 
     * @return bindIP
     */
    public String getBindIP() {
        return bindIP;
    }
   
    
    /**
     * Return the port to bind the SMTP server to
     * 
     * @return bindPort
     */
    public int getBindPort() {
        return bindPort;
    }
    
    /**
     * Return the domains for which we want to accept mails
     * 
     * @return domains
     */
    public List<String> getLocalDomains() {
        return localDomains;
    }

    /**
     * Return the greeting which is output when connecting to the server
     * 
     * @return greeting
     */
	public String getGreeting() {
		return greeting;
	}

	/**
	 * Return the name of the SMTP software
	 * 
	 * @return name
	 */
	public String getSoftwareName() {
		return softwareName;
	}

	public boolean isEnforceHeloEhlo() {
		return enforceHeloEhlo;
	}

	public void setEnforceHeloEhlo(boolean enforceHeloEhlo) {
		this.enforceHeloEhlo = enforceHeloEhlo;
	}

	public boolean isEnforceBrackets() {
		return enforceBrackets;
	}

	public void setEnforceBrackets(boolean enforceBrackets) {
		this.enforceBrackets = enforceBrackets;
	}

	public void setGreeting(String greeting) {
		this.greeting = greeting;
	}

	public void setSoftwareName(String softwareName) {
		this.softwareName = softwareName;
	}

	public void setMaxMessageSize(long maxMessageSize) {
		this.maxMessageSize = maxMessageSize;
	}

	public void setHelloName(String helloName) {
		this.helloName = helloName;
	}

	public void setLocalDomains(List<String> localDomains) {
		this.localDomains = localDomains;
	}
}
