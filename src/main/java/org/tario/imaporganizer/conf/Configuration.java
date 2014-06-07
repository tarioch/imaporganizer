package org.tario.imaporganizer.conf;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.google.common.base.Splitter;

@Component
@PropertySource("file:imaporganizer.properties")
public class Configuration {
	private final Environment env;

	@Autowired
	public Configuration(Environment env) {
		this.env = env;
	}

	public List<String> getRules() {
		return Splitter.on(",").omitEmptyStrings().splitToList(env.getProperty("rules"));
	}

	public String getSource(String section) {
		return env.getProperty(section + ".source");
	}

	public String getDestination(String section) {
		return env.getProperty(section + ".destination");
	}

	public String getType(String section) {
		return env.getProperty(section + ".type");
	}

	public ImapSourceConfig getImapSourceConfig(String section) {
		final String server = env.getProperty(section + ".server");
		final String user = env.getProperty(section + ".user");
		final String password = env.getProperty(section + ".password");
		final boolean ssl = env.getProperty(section + ".ssl", Boolean.class, Boolean.FALSE).booleanValue();

		return new ImapSourceConfig(server, user, password, ssl);
	}

	public ImapSourceOperationConfig getImapSourceOperationConfig(String section) {
		final String from = env.getProperty(section + ".source.from");

		return new ImapSourceOperationConfig(from);
	}

	public SmtpDestinationConfig getSmtpDestinationConfig(String section) {
		final String server = env.getProperty(section + ".server");
		final String user = env.getProperty(section + ".user");
		final String password = env.getProperty(section + ".password");
		final boolean ssl = env.getProperty(section + ".ssl", Boolean.class, Boolean.FALSE).booleanValue();

		return new SmtpDestinationConfig(server, user, password, ssl);
	}

	public SmtpDestinationOperationConfig getSmtpDestinationOperationConfig(String section) {
		final String to = env.getProperty(section + ".destination.to");

		return new SmtpDestinationOperationConfig(to);
	}

}
