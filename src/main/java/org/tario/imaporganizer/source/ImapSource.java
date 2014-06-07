package org.tario.imaporganizer.source;

import java.util.Collection;
import java.util.List;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.search.FromTerm;
import javax.mail.search.SearchTerm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.tario.imaporganizer.conf.Configuration;
import org.tario.imaporganizer.conf.ImapSourceConfig;
import org.tario.imaporganizer.conf.ImapSourceOperationConfig;

import com.google.common.collect.Lists;

@Component
@Scope("prototype")
public class ImapSource implements Source {
	private final Configuration conf;
	private Store store;

	@Autowired
	public ImapSource(Configuration conf) {
		this.conf = conf;
	}

	@Override
	public void connect(String section) {
		try {
			final ImapSourceConfig imapConfig = conf.getImapSourceConfig(section);

			final Properties props = new Properties();
			props.setProperty("mail.store.protocol", imapConfig.isSsl() ? "imaps" : "imap");

			final Session session = Session.getInstance(props);
			store = session.getStore();
			store.connect(imapConfig.getServer(), imapConfig.getUser(), imapConfig.getPassword());
		} catch (final MessagingException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void disconnect() {
		try {
			store.close();
		} catch (final MessagingException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public Collection<Message> fetch(String section) {
		try {
			final ImapSourceOperationConfig opConfig = conf.getImapSourceOperationConfig(section);

			final Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_ONLY);

			final SearchTerm searchTerm = new FromTerm(new InternetAddress(opConfig.getFrom()));
			final Message[] messages = folder.search(searchTerm);

			final List<Message> result = Lists.newArrayList();
			for (final Message message : messages) {
				if (message instanceof MimeMessage) {
					result.add(new MimeMessage((MimeMessage) message));
				} else {
					throw new UnsupportedOperationException("Can not handle message type "
							+ message.getClass().getName());
				}
			}

			return result;
		} catch (final MessagingException e) {
			throw new IllegalStateException(e);
		}
	}
}
