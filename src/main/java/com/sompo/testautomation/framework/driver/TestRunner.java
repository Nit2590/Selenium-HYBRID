package com.sompo.testautomation.framework.driver;

import java.util.List;

import org.apache.log4j.Logger;
import org.testng.TestNG;
import org.testng.collections.Lists;

public class TestRunner {
	static Logger LOGGER = Logger.getLogger(TestRunner.class);

	public static void main(String[] arg) {
		long startTime = System.currentTimeMillis();
		TestNG testNG = new TestNG();

		List<String> suites = Lists.newArrayList();
		suites.add("testng.xml");
		testNG.setTestSuites(suites);

		testNG.run();
		
		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;
		
		LOGGER.info("Time taken in suite " + duration);
		
		System.exit(0);
	}
}