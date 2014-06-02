package org.tario.imaporganizer.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@PropertySource("file:imaporganizer.properties")
public class Configuration {
	final String server;
	final String user;
	final String password;
	final boolean ssl;
	final String port;
	final String from;

	@Autowired
	public Configuration(Environment env) {
		server = env.getProperty("source.server");
		user = env.getProperty("source.user");
		password = env.getProperty("source.password");
		ssl = env.getProperty("source.ssl", Boolean.class, Boolean.FALSE).booleanValue();
		port = env.getProperty("source.port", ssl ? "993" : "143");
		from = env.getProperty("source.from");
	}

	public String getServer() {
		return server;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public boolean isSsl() {
		return ssl;
	}

	public String getPort() {
		return port;
	}

	public String getFrom() {
		return from;
	}

}
