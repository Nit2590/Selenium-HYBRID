package com.sompo.testautomation.framework.driver;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.birlasoft.testautomation.framework.ObjectRepository;
import com.birlasoft.testautomation.utils.Config;
import com.birlasoft.testautomation.utils.ExcelObject;

public class TestConfig {
	static Logger LOGGER = Logger.getLogger(TestConfig.class);

	private static TestConfig testConfig;

	private static Config config;
	private static String configWorkbook;

	private ObjectRepository objRep;
	private String testModulesPath;
	private String appBaseURL;
	private String reportPath;
	private String screenShotPath;
	private String testDataPath;
	

	private boolean remoteExecution = false;
	private String gridURL;

	private String execEnvironment;

	private TestConfig() {
	}

	static {
		try {
			config = new Config("Framework\\Test_Config\\config.properties");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		PropertyConfigurator.configure("log4j.properties");
		try {
			configWorkbook = new File("Framework\\Test_config\\Config.xlsx").getCanonicalPath();
		} catch (IOException e) {
			LOGGER.error("Unable to find Config workbook", e);
		}
	}

	public static TestConfig getInstance() {
		if (testConfig == null) {
			testConfig = new TestConfig();
		}
		return testConfig;
	}

	public static Config getConfig() {
		return config;
	}

	public static String getConfigWorkbook() {
		return configWorkbook;
	}

	public ObjectRepository getObjRep() {
		return objRep;
	}

	public String getReportPath() {
		return reportPath;
	}

	public String getScreenShotPath() {
		return screenShotPath;
	}
	
	public String getTestDataPath() {
		return testDataPath;
	}
	
	public String getTestModulesPath() {
		return testModulesPath;
	}

	public String getAppBaseURL() {
		return appBaseURL;
	}

	public boolean isRemoteExecution() {
		return remoteExecution;
	}

	public String getGridURL() {
		return gridURL;
	}

	public String getExecEnvironment() {
		return execEnvironment;
	}

	// Framework Initialization
	public void suiteSetup() {
		try {
			testConfig.frameworkSetup();
		} catch (Exception e) {
			LOGGER.error("Exception " + e.getClass().getName() + " caught from suite setup method", e);
		}
		
	}

	private void frameworkSetup() throws IOException {
		ExcelObject tcExcel = new ExcelObject(configWorkbook, "Config");

		objRep = new ObjectRepository("Framework\\OR\\ObjectRepository.xlsx");
		reportPath = String.valueOf(tcExcel.getCellValue("Config", "Value", "Key=ReportsPath")).trim();
		screenShotPath = String.valueOf(tcExcel.getCellValue("Config", "Value", "Key=ScreenshotPath")).trim();
		testDataPath=String.valueOf(tcExcel.getCellValue("Config", "Value", "Key=TestDataPath")).trim();
		testModulesPath = new File("Framework\\Test_Scripts").getCanonicalPath();
		appBaseURL = String.valueOf(tcExcel.getCellValue("Config", "Value", "Key=AppBaseURL")).trim();

		if ("Yes".equalsIgnoreCase(
				String.valueOf(tcExcel.getCellValue("Config", "Value", "Key=RemoteExecution")).trim())) {
			remoteExecution = true;
		}

		gridURL = String.valueOf(tcExcel.getCellValue("Config", "Value", "Key=GridURL")).trim();

		execEnvironment = String.valueOf(tcExcel.getCellValue("Config", "Value", "Key=Environment")).trim();

		tcExcel.closeWorkbook();
	}
}