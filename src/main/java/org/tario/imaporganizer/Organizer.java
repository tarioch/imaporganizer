package org.tario.imaporganizer;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.mail.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.tario.imaporganizer.conf.Configuration;
import org.tario.imaporganizer.destination.Destination;
import org.tario.imaporganizer.source.Source;

import com.google.common.collect.Maps;

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

		final Map<String, Source> sources = Maps.newHashMap();
		final Map<String, Destination> destinations = Maps.newHashMap();

		for (final String rule : rules) {
			final String sourceSection = conf.getSource(rule);
			Source source = sources.get(sourceSection);
			if (source == null) {
				final String sourceType = conf.getType(sourceSection);
				source = (Source) appContext.getBean(Class.forName(sourceType));
				source.connect(sourceSection);
				sources.put(sourceSection, source);
			}

			final String destinationSection = conf.getDestination(rule);
			Destination destination = destinations.get(destinationSection);
			if (destination == null) {
				final String destinationType = conf.getType(destinationSection);
				destination = (Destination) appContext.getBean(Class.forName(destinationType));
				destination.connect(destinationSection);
				destinations.put(destinationSection, destination);
			}

			final Collection<Message> messages = source.fetch(rule);
			for (final Message message : messages) {
				destination.send(message, rule);
			}

		}

		for (final Destination destination : destinations.values()) {
			destination.disconnect();
		}
		for (final Source source : sources.values()) {
			source.disconnect();
		}
	}
}
