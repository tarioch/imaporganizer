package org.tario.imaporganizer.destination;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tario.imaporganizer.conf.Configuration;

@Component
public class SmtpDestination {

	private final Configuration conf;
	private Transport transport;

	@Autowired
	public SmtpDestination(Configuration conf) {
		this.conf = conf;
	}

	public void connect() {
		try {
			final Properties props = new Properties();
			props.setProperty("mail.transport.protocol", conf.isSsl() ? "smtps" : "smtp");

			final Session session = Session.getInstance(props);
			transport = session.getTransport();
			transport.connect(conf.getServer(), conf.getUser(), conf.getPassword());
		} catch (final MessagingException e) {
			throw new IllegalStateException(e);
		}
	}

	public void disconnect() {
		try {
			transport.close();
		} catch (final MessagingException e) {
			throw new IllegalStateException(e);
		}
	}

	public void send(Message message) {
		try {
			transport.sendMessage(message, new Address[] { new InternetAddress(conf.getTo()) });
		} catch (final MessagingException e) {
			throw new IllegalStateException(e);
		}
	}
}
