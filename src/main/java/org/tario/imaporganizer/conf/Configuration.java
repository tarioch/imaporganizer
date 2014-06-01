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

	@Autowired
	public Configuration(Environment env) {
		server = env.getProperty("conf.server");
		user = env.getProperty("conf.user");
		password = env.getProperty("conf.password");
		ssl = env.getProperty("conf.ssl", Boolean.class, Boolean.FALSE).booleanValue();
		port = env.getProperty("conf.port", ssl ? "993" : "143");
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

}
