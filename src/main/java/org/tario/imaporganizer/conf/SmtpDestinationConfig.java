package org.tario.imaporganizer.conf;

public class SmtpDestinationConfig {
	final String server;
	final String user;
	final String password;
	final boolean ssl;

	public SmtpDestinationConfig(String server, String user, String password, boolean ssl) {
		this.server = server;
		this.user = user;
		this.password = password;
		this.ssl = ssl;
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

}
