package org.tario.imaporganizer.source;

import java.util.Collection;

import javax.mail.Message;

public interface Source {

	public abstract void connect(String section);

	public abstract void disconnect();

	public abstract Collection<Message> fetch(String section);

}