package org.tario.imaporganizer;

import java.util.Collection;

import javax.mail.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tario.imaporganizer.destination.SmtpDestination;
import org.tario.imaporganizer.source.ImapSource;

@Component
public class Organizer {

	private final ImapSource source;
	private final SmtpDestination destination;

	@Autowired
	public Organizer(ImapSource source, SmtpDestination destination) {
		this.source = source;
		this.destination = destination;
	}

	public void run() throws Exception {
		source.connect();
		final Collection<Message> messages = source.fetch();
		for (final Message message : messages) {
			System.out.println(message.getSubject());
		}
		source.disconnect();
	}

}
