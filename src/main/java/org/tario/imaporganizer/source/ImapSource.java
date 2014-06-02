package org.tario.imaporganizer.source;

import java.util.Collection;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.search.FromTerm;
import javax.mail.search.SearchTerm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tario.imaporganizer.conf.Configuration;

import com.google.common.collect.Lists;

@Component
public class ImapSource {
	private final Configuration conf;
	private Store store;

	@Autowired
	public ImapSource(Configuration conf) {
		this.conf = conf;
	}

	public void connect() {
		try {
			store = connectToStore();
		} catch (final MessagingException e) {
			throw new IllegalStateException(e);
		}
	}

	public void disconnect() {
		try {
			store.close();
		} catch (final MessagingException e) {
			throw new IllegalStateException(e);
		}
	}

	public Collection<Message> fetch() {
		try {
			final Folder folder = getFolder(store);
			final SearchTerm searchTerm = new FromTerm(new InternetAddress(conf.getFrom()));
			final Message[] messages = folder.search(searchTerm);
			final Collection<Message> result = Lists.newArrayList(messages);

			return result;
		} catch (final MessagingException e) {
			throw new IllegalStateException(e);
		}
	}

	private Folder getFolder(final Store store) throws MessagingException {
		Folder folder;
		folder = store.getFolder("INBOX");
		folder.open(Folder.READ_ONLY);
		return folder;
	}

	private Store connectToStore() throws MessagingException {
		final String protocol = conf.isSsl() ? "imaps" : "imap";
		final Properties props = new Properties();
		props.setProperty("mail.store.protocol", protocol);
		props.setProperty("mail." + protocol + ".host", conf.getServer());
		props.setProperty("mail." + protocol + ".port", conf.getPort());

		final Session session = Session.getDefaultInstance(props);
		Store store;
		store = session.getStore(protocol);
		store.connect(conf.getServer(), conf.getUser(), conf.getPassword());
		return store;
	}
}
