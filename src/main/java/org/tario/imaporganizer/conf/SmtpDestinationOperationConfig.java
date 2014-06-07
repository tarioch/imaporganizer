package org.tario.imaporganizer.conf;

public class SmtpDestinationOperationConfig {
	private final String to;

	public SmtpDestinationOperationConfig(String to) {
		this.to = to;
	}

	public String getTo() {
		return to;
	}

}
