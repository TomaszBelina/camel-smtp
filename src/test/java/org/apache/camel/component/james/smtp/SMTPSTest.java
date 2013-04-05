package org.apache.camel.component.james.smtp;

import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.AvailablePortFinder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.commons.net.smtp.SMTPReply;
import org.apache.commons.net.smtp.SMTPSClient;
import org.apache.james.protocols.api.Encryption;
import org.apache.james.protocols.api.utils.BogusSslContextFactory;
import org.apache.james.protocols.api.utils.BogusTrustManagerFactory;
import org.junit.BeforeClass;
import org.junit.Test;

public class SMTPSTest extends CamelTestSupport {

	private static int port = 0;

	@BeforeClass
	public static void findPort() {
		port = AvailablePortFinder.getNextAvailable();
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
		return registry;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.camel.test.junit4.CamelTestSupport#createRouteBuilder()
	 */
	@Override
	protected RouteBuilder createRouteBuilder() {
		return new RouteBuilder() {
			@Override
			public void configure() {
				// from("file://d:/tmp?noop=true").to("mock:result");

				from("james-smtp:localhost:" + port + "?encryption=#encryption")
						.to("mock:result");
			}
		};
	}

	@Test
	public void testSendMatchingMessage() throws Exception {
		String sender = "sender@localhost";
		String rcpt = "rcpt@localhost";
		String body = "Subject: test\r\n\r\nTestmail";
		SMTPSClient client = createClient();
		client.connect("localhost", port);
		assertTrue(SMTPReply.isPositiveCompletion(client.getReplyCode()));
		client.helo("localhost");
		client.setSender(sender);
		client.addRecipient(rcpt);

		client.sendShortMessageData(body);
		client.quit();
		client.disconnect();
		resultEndpoint.expectedMessageCount(1);

		resultEndpoint.assertIsSatisfied();
	}

	protected SMTPSClient createClient() {
		SMTPSClient client = new SMTPSClient(true,
				BogusSslContextFactory.getClientContext());
		client.setTrustManager(BogusTrustManagerFactory.getTrustManagers()[0]);
		return client;
	}

}
