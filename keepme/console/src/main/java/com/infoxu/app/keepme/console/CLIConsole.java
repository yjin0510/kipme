/**
 * Copyright (C) 2015 Infoxu Inc. All Rights Reserved.
 * Proprietary and confidential
 */
package com.infoxu.app.keepme.console;

import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.infoxu.app.keepme.data.RequestMessage;
import com.infoxu.app.keepme.data.User;
import com.infoxu.app.keepme.queue.MessageQueueFactory;
import com.infoxu.app.keepme.queue.MessageQueueSender;
import com.infoxu.app.keepme.queue.MessageQueueType;
import com.infoxu.app.keepme.util.Util;

/**
 * Command line console for interacting with backend services
 * @author yujin
 *
 */
public class CLIConsole {
	// Logging
	private static final Logger logger = LogManager.getLogger(CLIConsole.class);
	
	// Command line options
	private static final Options options = new Options();
	private static final CommandLineParser parser = new GnuParser();

	// Queue (lazy initialization)
	MessageQueueSender<RequestMessage> requestQ = null;
	
	static {
		options.addOption("f", "fetch", true, "Request to fetch a web page, require a URL input.");
		options.addOption("q", "query", true, "Query an existing snapshot, need an id or a id_phrase");
	}
	
	public static void main(String args[]) throws ParseException, IOException {
		CLIConsole console = new CLIConsole();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			logger.error("Invalid command line arguments.");
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("CLIConsole", options );
			System.exit(1);
		}
		boolean has_option = false;
		if (cmd.hasOption("f")) {
			String url = cmd.getOptionValue("f");
			console.fetch(url);
			has_option = true;
		}
		if (cmd.hasOption("q")) {
			String query = cmd.getOptionValue("q");
			console.query(query);
			has_option = true;
		}
		if (!has_option) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("CLIConsole", options );
		}
	}

	private void query(String query) {
		// Run query
		logger.info("Run query ... ");
	}

	private void fetch(String url) throws IOException {
		if (requestQ == null) {
			requestQ = MessageQueueFactory.getInstance()
					.getMessageQueueSender(MessageQueueType.REQUEST_QUEUE);
		}
		long expireTime = (System.currentTimeMillis() / 1000) + 86400; // one day to expire
		RequestMessage request = new RequestMessage(Util.generateLongId(), 
				url, Util.generateLongId(), User.ANONYMOUS_USER_ID, expireTime);
		requestQ.send(request);
		logger.info("Request send with indexId " + request.getIndexId());
		requestQ.close();
	}
}
