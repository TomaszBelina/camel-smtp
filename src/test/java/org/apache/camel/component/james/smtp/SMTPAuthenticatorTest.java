package org.apache.camel.component.james.smtp;

import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.james.smtp.relay.DenyToRelayHandler;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.AvailablePortFinder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.commons.net.smtp.AuthenticatingSMTPClient;
import org.apache.commons.net.smtp.AuthenticatingSMTPClient.AUTH_METHOD;
import org.apache.commons.net.smtp.SMTPClient;
import org.apache.james.protocols.smtp.SMTPSession;
import org.apache.james.protocols.smtp.hook.AuthHook;
import org.apache.james.protocols.smtp.hook.HookResult;
import org.junit.BeforeClass;
import org.junit.Test;

public class SMTPAuthenticatorTest extends CamelTestSupport {

	private static final String TEST_USER = "testuser";

	private static final String TEST_PASSWORD = "password";

	/** The result endpoint. */
	@EndpointInject(uri = "mock:result")
	protected MockEndpoint resultEndpoint;

	private static int port;

	@BeforeClass
	public static void findPort() {
		port = AvailablePortFinder.getNextAvailable();
	}

	@Override
	protected JndiRegistry createRegistry() throws Exception {
		JndiRegistry registry = super.createRegistry();
		registry.bind("myAuthHook", new TestSMTPAuthenticator());
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
						"james-smtp:localhost:"
								+ port
								+ "?greeting=CamelSMTP&authHook=#myAuthHook&authRequiredToRelayHandler=#denyToRelayHandler")
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
		client.connect("localhost", port);
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
		resultEndpoint.expectedMessageCount(1);

		resultEndpoint.assertIsSatisfied();
	}

	@Test
	public void authenticationFails() throws Exception {
		resultEndpoint.reset();
		String sender = "sender@localhost";
		String rcpt = "rcpt@localhost";
		String body = "Subject: test\r\n\r\nTestmail";
		AuthenticatingSMTPClient client = new AuthenticatingSMTPClient();
		client.connect("localhost", port);
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
		client.connect("localhost", port);
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
		resultEndpoint.expectedMessageCount(0);
		resultEndpoint.assertIsSatisfied();
	}

	private final class TestSMTPAuthenticator implements AuthHook {

		public HookResult doAuth(SMTPSession session, String username,
				String password) {
			if (TEST_USER.equals(username) && TEST_PASSWORD.equals(password)) {
				session.setRelayingAllowed(true);
				return HookResult.ok();
			} else {
				return HookResult.disconnect();
			}
		}
	}

}
