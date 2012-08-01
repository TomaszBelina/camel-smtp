package me.normanmaurer.camel.smtp;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.james.protocols.smtp.MailEnvelope;
import org.apache.james.protocols.smtp.SMTPSession;
import org.apache.james.protocols.smtp.hook.HookResult;
import org.apache.james.protocols.smtp.hook.HookReturnCode;
import org.apache.james.protocols.smtp.hook.MessageHook;

// TODO: Auto-generated Javadoc
/**
 * Send the {@link Exchange} to the {@link Processor} after receiving a
 * message via SMTP.
 */
public class DefaultConsumerHook implements MessageHook {

	/** The smtp consumer. */
	private final SMTPConsumer smtpConsumer;

	/**
	 * Instantiates a new default consumer hook.
	 *
	 * @param smtpConsumer the smtp consumer
	 */
	public DefaultConsumerHook(SMTPConsumer smtpConsumer) {
		this.smtpConsumer = smtpConsumer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.james.protocols.smtp.hook.MessageHook#onMessage(org.apache
	 * .james.protocols.smtp.SMTPSession,
	 * org.apache.james.protocols.smtp.MailEnvelope)
	 */
	public HookResult onMessage(SMTPSession arg0, MailEnvelope env) {
		Exchange exchange = this.smtpConsumer.getEndpoint().createExchange();
		exchange.setIn(new MailEnvelopeMessage(env));
		try {
			this.smtpConsumer.getProcessor().process(exchange);
		} catch (Exception e) {
			return new HookResult(HookReturnCode.DENYSOFT);
		}
		return new HookResult(HookReturnCode.OK);
	}

}