package com.birlasoft.testautomation.utils;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

public class CommonUtils {
	private static Logger LOGGER = Logger.getLogger(CommonUtils.class);

	public static BufferedReader executeProcess(String[] cmdArray, String[] env, String... args)
			throws IOException, InterruptedException {
		Process proc;
		LOGGER.info("Running proc with name " + cmdArray[0]);

		if (args != null) {
			proc = Runtime.getRuntime().exec(cmdArray, env, new File(args[0]));
		} else {
			proc = Runtime.getRuntime().exec(cmdArray, env);
		}

		proc.waitFor();
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

		LOGGER.info("Output is " + stdInput.toString());

		return stdInput;
	}

	public static BufferedReader executeProcess(String cmdProcess) throws IOException, InterruptedException {
		LOGGER.info("Running proc with name " + cmdProcess);
		Process proc = Runtime.getRuntime().exec(cmdProcess);
		proc.waitFor();
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		LOGGER.info("Output is " + stdInput.toString());
		return stdInput;
	}

	public static void copyTextToClipboard(String text) {
		StringSelection selection = new StringSelection(text);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(selection, selection);
		LOGGER.info("Text " + text + " is copied to Clipborad");
	}
}