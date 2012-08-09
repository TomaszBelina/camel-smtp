package org.apache.camel.component.james.smtp;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map;

import javax.mail.Header;
import javax.mail.internet.MimeMessage;


import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.james.smtp.MailEnvelopeMessage;
import org.apache.camel.component.james.smtp.authentication.SMTPAuthenticator;
import org.apache.camel.component.james.smtp.relay.DenyToRelayHandler;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.commons.net.smtp.AuthenticatingSMTPClient;
import org.apache.commons.net.smtp.AuthenticatingSMTPClient.AUTH_METHOD;
import org.apache.commons.net.smtp.SMTPClient;
import org.junit.Test;

public class SMTPAuthenticatorTest extends CamelTestSupport {

	private static final String TEST_USER = "testuser";

	private static final String TEST_PASSWORD = "password";

	/** The result endpoint. */
	@EndpointInject(uri = "mock:result")
	protected MockEndpoint resultEndpoint;

	@Override
	protected JndiRegistry createRegistry() throws Exception {
		JndiRegistry registry = super.createRegistry();
		registry.bind("myAuthenticator", new TestSMTPAuthenticator());
		registry.bind("denyToRelayHandler", new DenyToRelayHandler());
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
				from(
						"james-smtp:localhost:2525?greeting=CamelSMTP&authenticator=#myAuthenticator&authRequiredToRelayHandler=#denyToRelayHandler")
						.to("mock:result");
			}
		};
	}

	@Test
	public void sendWithAuthentication() throws Exception {
		String sender = "sender@localhost";
		String rcpt = "rcpt@localhost";
		String body = "Subject: test\r\n\r\nTestmail";
		AuthenticatingSMTPClient client = new AuthenticatingSMTPClient();
		client.connect("localhost", 2525);
		;
		client.elogin("localhost");
		// client.execTLS();
		boolean isAuthenticated = client.auth(AUTH_METHOD.PLAIN, TEST_USER,
				TEST_PASSWORD);
		assertTrue(isAuthenticated);
		client.setSender(sender);
		client.addRecipient(rcpt);

		client.sendShortMessageData(body);
		client.quit();
		client.disconnect();
		Thread.sleep(2000);
		System.out.println("Wait...");
		resultEndpoint.expectedMessageCount(1);
		resultEndpoint.expectedBodyReceived().body(InputStream.class);
		Exchange ex = resultEndpoint.getReceivedExchanges().get(0);
		Map<String, Object> headers = ex.getIn().getHeaders();
		assertEquals(sender,
				headers.get(MailEnvelopeMessage.SMTP_SENDER_ADRRESS));
		assertEquals(rcpt,
				headers.get(MailEnvelopeMessage.SMTP_RCPT_ADRRESS_LIST));

		// check type converter
		MimeMessage message = ex.getIn().getBody(MimeMessage.class);
		Enumeration<Header> mHeaders = message.getAllHeaders();
		Header header = null;
		while (mHeaders.hasMoreElements()) {
			header = mHeaders.nextElement();
			if (header.getName().equals("Subject")) {
				break;
			}
		}
		assertNotNull(header);
		assertEquals("Subject", header.getName());
		assertEquals(header.getValue(), "test");

		resultEndpoint.assertIsSatisfied();
	}

	@Test
	public void authenticationFails() throws Exception {
		resultEndpoint.reset();
		String sender = "sender@localhost";
		String rcpt = "rcpt@localhost";
		String body = "Subject: test\r\n\r\nTestmail";
		AuthenticatingSMTPClient client = new AuthenticatingSMTPClient();
		client.connect("localhost", 2525);
		;
		client.elogin("localhost");
		// client.execTLS();
		boolean isAuthenticated = client.auth(AUTH_METHOD.PLAIN, TEST_USER,
				"WRONG PASSWORD");
		assertFalse(isAuthenticated);
		try {
			client.setSender(sender);
			client.addRecipient(rcpt);

			client.sendShortMessageData(body);
			client.quit();
			client.disconnect();
		} catch (Exception e) {
			// fail silently
		}
		Thread.sleep(2000);
		System.out.println("Wait...");
		resultEndpoint.expectedMessageCount(0);
		resultEndpoint.assertIsSatisfied();
	}
	
	@Test
	public void noAuthenticationFails() throws Exception {
		resultEndpoint.reset();
		String sender = "sender@localhost";
		String rcpt = "rcpt@localhost";
		String body = "Subject: test\r\n\r\nTestmail";
		SMTPClient client = new SMTPClient();
		client.connect("localhost", 2525);
		client.helo("localhost");
		// client.execTLS();
		try {
			client.setSender(sender);
			client.addRecipient(rcpt);

			client.sendShortMessageData(body);
			client.quit();
			client.disconnect();
		} catch (Exception e) {
			// fail silently
		}
		Thread.sleep(2000);
		System.out.println("Wait...");
		resultEndpoint.expectedMessageCount(0);
		resultEndpoint.assertIsSatisfied();
	}

	private final class TestSMTPAuthenticator implements SMTPAuthenticator {
		public boolean authenticate(String username, String password) {
			if (TEST_USER.equals(username) && TEST_PASSWORD.equals(password)) {
				return true;
			} else {
				return false;
			}
		}
	}

}
