package org.tario.imaporganizer;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Organizer {
	final String server;
	final String user;
	final String password;
	final boolean ssl;
	final String port;

	@Autowired
	public Organizer(@Value("${conf.server}") String server, @Value("${conf.user}") String user,
			@Value("${conf.password}") String password, @Value("${conf.ssl}") boolean ssl,
			@Value("${conf.port}") String port) {
		this.server = server;
		this.user = user;
		this.password = password;
		this.ssl = ssl;
		this.port = port;
	}

	public void run() throws Exception {

		final String protocol = ssl ? "imaps" : "imap";
		final Properties props = new Properties();
		props.setProperty("mail.store.protocol", protocol);
		props.setProperty("mail." + protocol + ".host", server);
		props.setProperty("mail." + protocol + ".port", port);

		final Session session = Session.getDefaultInstance(props);
		final Store store = session.getStore(protocol);
		store.connect(server, user, password);
		System.out.println(store.isConnected());

		final Folder folder = store.getFolder("INBOX");
		folder.open(Folder.READ_ONLY);
		final Message[] messages = folder.getMessages();
		for (final Message message : messages) {
			System.out.println(message.getSubject());
		}

		store.close();
	}

}
