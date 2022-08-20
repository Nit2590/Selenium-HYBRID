package com.sompo.testautomation.framework.driver;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.birlasoft.testautomation.framework.ExtentManager;
import com.birlasoft.testautomation.framework.TestScript;
import com.birlasoft.testautomation.framework.TestSuite;
import com.birlasoft.testautomation.utils.Constants;
import com.birlasoft.testautomation.utils.ExcelObject;
import com.birlasoft.testautomation.utils.UIUtils;

public class TestDriver {
	static Logger LOGGER = Logger.getLogger(TestDriver.class);


	private static int totalTestScript;
	private static int currentTestScript;

	public static int getTotalTestScript() {
		return totalTestScript;
	}

	public static int getCurrentTestScript() {
		return currentTestScript;
	}

	// Framework Initialization
	@BeforeSuite
	public void suiteSetup() {
		try {
			TestConfig.getInstance().suiteSetup();
			LOGGER.info("*********EXECUTION STARTED**********\n\n");
		} catch (Exception e) {
			LOGGER.error("Exception " + e.getClass().getName() + " caught from suite setup method", e);
		}
	}

	// *** Test Method *** //
	@Test(dataProvider = "getTest")
	// , threadPoolSize = 2, invocationCount = 1)
	public void newTest(TestSuite testSuite) {
		LOGGER.info("Started execution of module " + testSuite.getModuleName());

		for (TestScript currentTest : testSuite.getTestCases()) {
			currentTestScript++;
			LOGGER.info("Running test case# " + currentTestScript + " out of " + totalTestScript + " test cases.");
			try {
				currentTest.execute();
			} catch (Exception e) {
				LOGGER.error("Unable to execute test case " + currentTest.getTsName(), e);
			}
		}
	}

	@DataProvider(name = "getTest")
	public Object[][] getTestObject() {
		Object[][] arrTestCase = null;
		String moduleName;
		try {
			ExcelObject tcExcel = new ExcelObject(TestConfig.getConfigWorkbook(), "ModuleList");

			List<List<Object>> queryResult = tcExcel.getExcelData("ModuleList",
					"Execute" + Constants.CONDITIONVALUE_SEPARATOR + "Yes");
			tcExcel.closeWorkbook();

			List<TestSuite> listSuites = new ArrayList<TestSuite>();

			for (int counter = 0; counter < queryResult.size(); counter++) {
				List<Object> moduleInfo = queryResult.get(counter);
				moduleName = String.valueOf(moduleInfo.get(1)).trim();
				TestSuite suite = new TestSuite(TestConfig.getInstance().getTestModulesPath(), moduleName);
				listSuites.add(suite);
				totalTestScript += suite.getTestCases().size();
			}

			arrTestCase = new Object[listSuites.size()][1];

			for (int counter = 0; counter < listSuites.size(); counter++) {
				arrTestCase[counter][0] = listSuites.get(counter);
			}
		} catch (Exception e) {
			LOGGER.error("Exception " + e.getClass().getName() + " caught while fetching test cases from module sheets",
					e);
		}
		return arrTestCase;
	}

	@AfterSuite
	public void suiteTearDown() {
		LOGGER.info("All tests get executed");

		LOGGER.info("Total test cases executed:" + TestScript.getTotalTSCount());
		LOGGER.info("Total passed test cases:" + TestScript.getPassedTSCount());
		LOGGER.info("Total failed test cases:" + TestScript.getFailedTSCount());
		LOGGER.info("\n\n*********EXECUTION STOPPED**********");

		try {
			ExtentManager.getInstance().flush();
		} finally {
			ExtentManager.getInstance().close();
		}
	}

	public static WebDriver driverInstantiation(String browserName) throws Exception {
		WebDriver driver;

		if (TestConfig.getInstance().isRemoteExecution()) {
			driver = UIUtils.createDriverInstance(browserName, "", TestConfig.getInstance().getGridURL());
		} else {
			driver = UIUtils.createDriverInstance(browserName, "");
		}

		driver.navigate().to(TestConfig.getInstance().getAppBaseURL());
		UIUtils.waitForPageLoad(driver);

		try {
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_0);
			robot.keyRelease(KeyEvent.VK_CONTROL);
		} catch (AWTException e) {
		}
		return driver;
	}
}