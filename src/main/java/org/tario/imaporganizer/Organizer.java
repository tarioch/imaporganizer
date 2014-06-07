package org.tario.imaporganizer;

import java.util.Collection;
import java.util.List;

import javax.mail.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.tario.imaporganizer.conf.Configuration;
import org.tario.imaporganizer.destination.Destination;
import org.tario.imaporganizer.source.Source;

@Component
public class Organizer {

	private final Configuration conf;
	private final ApplicationContext appContext;

	@Autowired
	public Organizer(Configuration conf, ApplicationContext appContext) {
		this.conf = conf;
		this.appContext = appContext;
	}

	public void run() throws Exception {
		final List<String> rules = conf.getRules();
		for (final String rule : rules) {
			final String sourceSection = conf.getSource(rule);
			final String sourceType = conf.getType(sourceSection);
			final Source source = (Source) appContext.getBean(Class.forName(sourceType));
			source.connect(sourceSection);

			final String destinationSection = conf.getDestination(rule);
			final String destinationType = conf.getType(destinationSection);
			final Destination destination = (Destination) appContext.getBean(Class.forName(destinationType));
			destination.connect(destinationSection);

			final Collection<Message> messages = source.fetch(rule);
			for (final Message message : messages) {
				destination.send(message, rule);
			}

			destination.disconnect();
			source.disconnect();
		}

	}
}
