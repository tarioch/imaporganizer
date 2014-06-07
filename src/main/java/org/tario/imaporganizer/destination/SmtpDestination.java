package org.tario.imaporganizer.destination;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.tario.imaporganizer.conf.Configuration;
import org.tario.imaporganizer.conf.SmtpDestinationConfig;
import org.tario.imaporganizer.conf.SmtpDestinationOperationConfig;

@Qualifier("Smtp")
@Component
public class SmtpDestination implements Destination {

	private final Configuration conf;
	private Transport transport;

	@Autowired
	public SmtpDestination(Configuration conf) {
		this.conf = conf;
	}

	@Override
	public void connect(String section) {
		try {
			final SmtpDestinationConfig smtpConfig = conf.getSmtpDestinationConfig(section);

			final Properties props = new Properties();
			props.setProperty("mail.transport.protocol", smtpConfig.isSsl() ? "smtps" : "smtp");

			final Session session = Session.getInstance(props);
			transport = session.getTransport();
			transport.connect(smtpConfig.getServer(), smtpConfig.getUser(), smtpConfig.getPassword());
		} catch (final MessagingException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void disconnect() {
		try {
			transport.close();
		} catch (final MessagingException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void send(Message message, String section) {
		final SmtpDestinationOperationConfig opConfig = conf.getSmtpDestinationOperationConfig(section);
		try {
			transport.sendMessage(message, new Address[] { new InternetAddress(opConfig.getTo()) });
		} catch (final MessagingException e) {
			throw new IllegalStateException(e);
		}
	}
}
