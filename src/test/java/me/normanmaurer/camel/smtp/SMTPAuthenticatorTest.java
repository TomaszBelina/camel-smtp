package me.normanmaurer.camel.smtp;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map;

import javax.mail.Header;
import javax.mail.internet.MimeMessage;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.commons.net.smtp.SMTPClient;
import org.junit.BeforeClass;
import org.junit.Test;



public class SMTPAuthenticatorTest extends CamelTestSupport {
	
	 /** The result endpoint. */
    @EndpointInject(uri = "mock:result")
    protected MockEndpoint resultEndpoint;
    
    @BeforeClass
    public void setUpClass() {
    	
    }
    
    /* (non-Javadoc)
     * @see org.apache.camel.test.junit4.CamelTestSupport#createRouteBuilder()
     */
    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            @Override
			public void configure() {            	
                from("james-smtp:localhost:2525?greeting=CamelSMTP?authenticator=#myAuthenticator").to("mock:result");
            }
        };
    }
    
    @Test
    public void sendWithAuthentication() throws Exception {
    	String sender = "sender@localhost";
        String rcpt = "rcpt@localhost";
        String body = "Subject: test\r\n\r\nTestmail";
        SMTPClient client = new SMTPClient();
        client.connect("localhost", 2525);
        client.helo("localhost");
        client.setSender(sender);
        client.addRecipient(rcpt);
        
        client.sendShortMessageData(body);
        client.quit();
        client.disconnect();
        resultEndpoint.expectedMessageCount(1);
        resultEndpoint.expectedBodyReceived().body(InputStream.class);
        Exchange ex = resultEndpoint.getReceivedExchanges().get(0);
        Map<String, Object> headers = ex.getIn().getHeaders();
        assertEquals(sender, headers.get(MailEnvelopeMessage.SMTP_SENDER_ADRRESS));
        assertEquals(rcpt, headers.get(MailEnvelopeMessage.SMTP_RCPT_ADRRESS_LIST));
        
        

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

}
