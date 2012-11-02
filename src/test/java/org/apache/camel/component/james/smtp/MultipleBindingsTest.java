package org.apache.camel.component.james.smtp;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.AvailablePortFinder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.commons.net.smtp.SMTPClient;
import org.apache.commons.net.smtp.SMTPReply;
import org.apache.commons.net.smtp.SMTPSClient;
import org.apache.james.protocols.api.Encryption;
import org.apache.james.protocols.api.utils.BogusSslContextFactory;
import org.apache.james.protocols.api.utils.BogusTrustManagerFactory;
import org.junit.BeforeClass;
import org.junit.Test;

public class MultipleBindingsTest extends CamelTestSupport {

	private static int port = 0;

	private static int additionalPort = 0;

	@BeforeClass
	public static void findPort() {
		port = AvailablePortFinder.getNextAvailable();
		additionalPort = AvailablePortFinder.getNextAvailable();
	}

	/** The result endpoint. */
	@EndpointInject(uri = "mock:result")
	protected MockEndpoint resultEndpoint;

	@Override
	protected JndiRegistry createRegistry() throws Exception {
		JndiRegistry registry = super.createRegistry();
		// create a test TLS context
		Encryption encryption = Encryption.createTls(BogusSslContextFactory
				.getServerContext());
		registry.bind("encryption", encryption);

		Binding b = new Binding();
		b.setIP("localhost");
		b.setPort(additionalPort);
		List<Binding> bindings = new ArrayList<Binding>(1);
		bindings.add(b);
		registry.bind("additionalBindings", bindings);

		return registry;
	}

	@Override
	protected RouteBuilder createRouteBuilder() {
		return new RouteBuilder() {
			@Override
			public void configure() {
				// from("file://d:/tmp?noop=true").to("mock:result");

				from(
						"james-smtp:localhost:"
								+ port
								+ "?encryption=#encryption&additionalBindings=#additionalBindings")
						.to("mock:result");
			}
		};
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testSendMatchingMessage() throws Exception {
		String sender = "sender@localhost";
		String rcpt = "rcpt@localhost";
		String body = "Subject: test\r\n\r\nTestmail";
		// send via SMTPS
		SMTPSClient secureClient = createClient();
		secureClient.connect("localhost", port);
		assertTrue(SMTPReply.isPositiveCompletion(secureClient.getReplyCode()));
		secureClient.helo("localhost");
		secureClient.setSender(sender);
		secureClient.addRecipient(rcpt);

		secureClient.sendShortMessageData(body);
		secureClient.quit();
		secureClient.disconnect();

		// send via SMTP
		SMTPClient client = new SMTPClient();
		client.connect("localhost", additionalPort);
		assertTrue(SMTPReply.isPositiveCompletion(client.getReplyCode()));
		client.helo("localhost");
		client.setSender(sender);
		client.addRecipient(rcpt);

		client.sendShortMessageData(body);
		client.quit();
		client.disconnect();

		resultEndpoint.expectedMessageCount(2);

		resultEndpoint.assertIsSatisfied();
	}

	protected SMTPSClient createClient() {
		SMTPSClient client = new SMTPSClient(true,
				BogusSslContextFactory.getClientContext());
		client.setTrustManager(BogusTrustManagerFactory.getTrustManagers()[0]);
		return client;
	}
}
