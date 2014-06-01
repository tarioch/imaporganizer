package org.tario.imaporganizer;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tario.imaporganizer.conf.Configuration;

@Component
public class Organizer {

	private final Configuration conf;

	@Autowired
	public Organizer(Configuration conf) {
		this.conf = conf;
	}

	public void run() throws Exception {

		final String protocol = conf.isSsl() ? "imaps" : "imap";
		final Properties props = new Properties();
		props.setProperty("mail.store.protocol", protocol);
		props.setProperty("mail." + protocol + ".host", conf.getServer());
		props.setProperty("mail." + protocol + ".port", conf.getPort());

		final Session session = Session.getDefaultInstance(props);
		final Store store = session.getStore(protocol);
		store.connect(conf.getServer(), conf.getUser(), conf.getPassword());
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
