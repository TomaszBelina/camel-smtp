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

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Endpoint;
import org.apache.camel.Processor;
import org.apache.camel.component.james.smtp.relay.AbstractAuthRequiredToRelayHandler;
import org.apache.camel.impl.DefaultConsumer;
import org.apache.james.protocols.api.Encryption;
import org.apache.james.protocols.api.logger.ProtocolLoggerAdapter;
import org.apache.james.protocols.netty.NettyServer;
import org.apache.james.protocols.smtp.SMTPProtocol;
import org.apache.james.protocols.smtp.SMTPProtocolHandlerChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: Auto-generated Javadoc
/**
 * Consumer which starts an SMTPServer and forward mails to the processor once
 * they are received.
 */
public class SMTPConsumer extends DefaultConsumer {

	/** The config. */
	final SMTPURIConfiguration config;

	/** The server. */
	private final List<NettyServer> servers = new ArrayList<NettyServer>();

	/** The chain. */
	private SMTPProtocolHandlerChain chain;

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory
			.getLogger(SMTPConsumer.class);

	/**
	 * Instantiates a new sMTP consumer.
	 * 
	 * @param endpoint
	 *            the endpoint
	 * @param processor
	 *            the processor
	 * @param config
	 *            the config
	 */
	public SMTPConsumer(Endpoint endpoint, Processor processor,
			SMTPURIConfiguration config) {
		super(endpoint, processor);
		this.config = config;

	}

	/**
	 * Startup the SMTP Server.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Override
	protected void doStart() throws Exception {
		super.doStart();
		chain = new SMTPProtocolHandlerChain(true);
		DefaultMessageHook consumerHook;
		if (config.getMessageHook() != null) {
			consumerHook = config.getMessageHook();
		} else {
			consumerHook = new DefaultMessageHook();

		}
		consumerHook.setSmtpConsumer(this);
		chain.add(consumerHook);

		AbstractAuthRequiredToRelayHandler abstractAuthRequiredToRelayHandler = config
				.getAuthRequiredToRelayHandler();
		abstractAuthRequiredToRelayHandler.setLocalDomains(config
				.getLocalDomains());
		chain.add(abstractAuthRequiredToRelayHandler);

		if (config.getAuthHook() != null) {
			chain.add(config.getAuthHook());
		}
		chain.wireExtensibleHandlers();

		SMTPProtocol protocol = new SMTPProtocol(chain, config,
				new ProtocolLoggerAdapter(LOG));
		// check whether secure connection (either TLS or STARTTLS) is required

		servers.add(bindNettyServer(config.getBindIP(), config.getBindPort(),
				protocol, config.getEncryption()));

		if (config.getAdditionalBindings() != null) {
			for (Binding b : config.getAdditionalBindings()) {
				servers.add(bindNettyServer(b.getIP(), b.getPort(), protocol,
						b.getEncryption()));
			}
		}
	}

	private NettyServer bindNettyServer(String ip, int port,
			SMTPProtocol protocol, Encryption encryption) throws Exception {
		NettyServer server;
		if (encryption != null) {
			server = new NettyServer(protocol, encryption);
		} else {
			server = new NettyServer(protocol);
		}
		// if the IP equeals '*' then bind to the wildcard address
		if (ip.equals("all")) {
			server.setListenAddresses(new InetSocketAddress(port));
		} else {
			server.setListenAddresses(new InetSocketAddress(ip, port));
		}
		server.bind();
		return server;
	}

	/**
	 * Shutdown the SMTPServer.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Override
	protected void doStop() throws Exception {
		super.doStop();
		for (NettyServer s : servers) {
			s.unbind();
		}
	}

}
