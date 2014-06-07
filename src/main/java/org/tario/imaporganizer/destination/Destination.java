package org.tario.imaporganizer.destination;

import javax.mail.Message;

public interface Destination {

	public abstract void connect(String section);

	public abstract void disconnect();

	public abstract void send(Message message, String section);

}