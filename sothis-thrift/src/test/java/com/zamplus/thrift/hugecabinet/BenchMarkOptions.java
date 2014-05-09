package com.zamplus.thrift.hugecabinet;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class BenchMarkOptions {
	private String host;
	private int port;
	private int connections;
	private int threads;
	private int requests;
	private int sleep;
	private int delay;
	private int eventThreads;

	private final static Options options = new Options();
	static {
		options.addOption(new Option("h", true, "the host to perform the benchmark."));
		options.addOption(new Option("d", true, "initial delay in seconds, default to 0."));
		options.addOption(new Option("p", true, "the host port to perform the benchmark, default to 12001."));
		options.addOption(new Option("c", true, "concurrent connections to use, default to 1."));
		options.addOption(new Option("n", true, "request to perform on every single connection, default to 1."));
		options.addOption(new Option("t", true, "test threads, default to 1."));
		options.addOption(new Option("e", true, "events threads, default to 1."));
		options.addOption(new Option("s", true,
				"time to sleep for each request in a single test thread, default to 0, means do not sleep."));
	}

	public static void printUseage() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("thrifttest -h host [options]", options);
	}

	public static BenchMarkOptions parse(String[] args) throws Exception {

		CommandLineParser parser = new BasicParser();
		CommandLine cmdLine = parser.parse(options, args);

		if (!cmdLine.hasOption('h')) {
			return null;
		}
		BenchMarkOptions options = new BenchMarkOptions();

		options.host = cmdLine.getOptionValue("h");
		options.port = 12001;
		if (cmdLine.hasOption("p")) {
			options.port = Integer.parseInt(cmdLine.getOptionValue("p"));
		}
		options.connections = 1;
		if (cmdLine.hasOption("c")) {
			options.connections = Integer.parseInt(cmdLine.getOptionValue("c"));
		}
		options.requests = 1;
		if (cmdLine.hasOption("n")) {
			options.requests = Integer.parseInt(cmdLine.getOptionValue("n"));
		}
		options.threads = 1;
		if (cmdLine.hasOption("t")) {
			options.threads = Integer.parseInt(cmdLine.getOptionValue("t"));
		}
		options.sleep = 0;
		if (cmdLine.hasOption("s")) {
			options.sleep = Integer.parseInt(cmdLine.getOptionValue("s"));
		}
		options.delay = 0;
		if (cmdLine.hasOption("d")) {
			options.delay = Integer.parseInt(cmdLine.getOptionValue("d"));
		}
		options.eventThreads = 1;
		if (cmdLine.hasOption("e")) {
			options.eventThreads = Integer.parseInt(cmdLine.getOptionValue("e"));
		}

		return options;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public int getConnections() {
		return connections;
	}

	public int getRequests() {
		return requests;
	}

	public int getThreads() {
		return threads;
	}

	public int getSleep() {
		return sleep;
	}

	public int getDelay() {
		return delay;
	}

	public int getEventThreads() {
		return eventThreads;
	}
}
