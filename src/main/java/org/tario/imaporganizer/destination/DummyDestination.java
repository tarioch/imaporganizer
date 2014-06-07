package org.tario.imaporganizer.destination;

import javax.mail.Message;
import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Qualifier("Dummy")
@Component
public class DummyDestination implements Destination {

	@Override
	public void connect(String section) {
		System.out.println("Connect " + section);
	}

	@Override
	public void disconnect() {
		System.out.println("Disconnect");
	}

	@Override
	public void send(Message message, String section) {
		try {
			System.out.println("Send " + section + ": " + message.getSubject());
		} catch (final MessagingException e) {
			throw new IllegalStateException(e);
		}
	}

}
