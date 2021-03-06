package org.tario.imaporganizer.destination;

import javax.mail.Message;
import javax.mail.MessagingException;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class DummyDestination implements Destination {

	private String section;

	@Override
	public void connect(String section) {
		this.section = section;
		System.out.println("Connect " + section);
	}

	@Override
	public void disconnect() {
		System.out.println("Disconnect " + section);
	}

	@Override
	public void send(Message message, String rule) {
		try {
			System.out.println("Send " + section + " " + rule + ": " + message.getSubject());
		} catch (final MessagingException e) {
			throw new IllegalStateException(e);
		}
	}
}
