package com.birlasoft.testautomation.framework;

import org.apache.log4j.Logger;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class LogMe {
	private static Logger LOGGER;
	private static ExtentTest extentTest;

	public static ExtentTest getExtentTest() {
		return extentTest;
	}

	public LogMe(String loggerClass) {
		LOGGER = Logger.getLogger(loggerClass);
	}

	@SuppressWarnings("rawtypes")
	public LogMe(Class loggerClass) {
		LOGGER = Logger.getLogger(loggerClass);
	}

	public void logInfo(String message) {
		LOGGER.info("---INFO--- " + message);

		if (extentTest != null) {
			extentTest.log(LogStatus.INFO, "---INFO--- " + message);
		}
	}

	public void logInfo(String message, Throwable t) {
		LOGGER.info("---INFO--- " + message, t);

		if (extentTest != null) {
			extentTest.log(LogStatus.INFO, "---INFO--- " + message + ". Exception message is " + t.getMessage());
		}
	}

	public void logWarn(String message) {
		LOGGER.warn("---WARN--- " + message);

		if (extentTest != null) {
			extentTest.log(LogStatus.WARNING, "---WARN--- " + message);
		}
	}

	public void logWarn(String message, Throwable t) {
		LOGGER.warn("---WARN--- " + message, t);

		if (extentTest != null) {
			extentTest.log(LogStatus.WARNING, "---WARN--- " + message + ". Exception message is " + t.getMessage());
		}
	}

	public void logError(String message) {
		LOGGER.error("---ERROR--- " + message);

		if (extentTest != null) {
			extentTest.log(LogStatus.FAIL, "---ERROR--- " + message);
		}
	}

	public void logError(String message, Throwable t) {
		LOGGER.error("---ERROR--- Exception " + t.getClass().getSimpleName() + " encountered");
		LOGGER.error("---ERROR--- " + message, t);

		if (extentTest != null) {
			extentTest.log(LogStatus.FAIL, "---ERROR--- " + message + ". Exception message is " + t.getMessage());
		}
	}

	public void logAssert(boolean assertion, String msg) {
		if (assertion) {
			msg = "---INFO--- " + msg;

			if (extentTest != null) {
				extentTest.log(LogStatus.INFO, msg);
			}
		} else {
			msg = "---ERROR--- " + msg;

			if (extentTest != null) {
				extentTest.log(LogStatus.ERROR, msg);
			}
		}
		LOGGER.assertLog(assertion, msg);
	}

	public void logBeginTestCase(String tsName) {
		LOGGER.info("---INFO---Strating test case " + tsName);
		extentTest = ExtentManager.getInstance().startTest(tsName);
	}

	public void logBeginTestCase(String tsName, String desc) {
		LOGGER.info("---INFO---Strating Test Case : " + tsName);
		LOGGER.info("---INFO---Test Case Description : " + desc);
		extentTest = ExtentManager.getInstance().startTest(tsName, desc);
	}

	public void logEndTestCase(String tsName) {
		LOGGER.info("---INFO---Test Case : " + tsName + " finished");
		ExtentManager.getInstance().endTest(extentTest);
	}

	public void logTestStep(String stepNo, String desc) {
		LOGGER.info("---INFO---Step No. " + stepNo + " : Description : " + desc);
		extentTest.log(LogStatus.INFO, stepNo, desc);
	}

	public void logTestStep(String stepNo, String status, String expected, String actual, Throwable... throwables) {
		LogStatus stepStatus = LogStatus.valueOf(status.toUpperCase());

		switch (stepStatus) {
		case PASS:
			if (throwables.length != 0) {
				LOGGER.info("---PASS---Step No. " + stepNo + " : Expected Result is : " + expected
						+ " and Actual Result is : " + actual + " Exception " + throwables[0].getClass().getSimpleName()
						+ " occured");
	
				extentTest.log(LogStatus.PASS, stepNo,
						"---PASS---Step No. " + stepNo + " : Expected Result is : " + expected
								+ " and Actual Result is : " + actual + " Exception "
								+ throwables[0].getClass().getSimpleName() + " occured");
			} else {
				LOGGER.info("---PASS---Step No. " + stepNo + " : Expected Result is : " + expected
						+ " and Actual Result is : " + actual);
				extentTest.log(LogStatus.PASS, stepNo, "---PASS---Executing Step No. " + stepNo
						+ " : Expected Result is : " + expected + " and Actual Result is : " + actual);
			}
			break;
		case FAIL:
			if (throwables.length != 0) {
				logError("---FAIL---Step No. " + stepNo + " : Expected Result is : " + expected
						+ " but Actual Result is : " + actual, throwables[0]);
			} else {
				logError("---FAIL---Step No. " + stepNo + " : Expected Result is : " + expected
						+ " but Actual Result is : " + actual);
			}
			break;
		case SKIP:
		case WARNING:
			if (throwables.length != 0) {
				logWarn("---WARNING---Step No. " + stepNo + " : Expected Result is : " + expected
						+ " but Actual Result is : " + actual, throwables[0]);
			} else {
				logWarn("---WARNING---Step No. " + stepNo + " : Expected Result is : " + expected
						+ " but Actual Result is : " + actual);
			}
			break;
		default:
			break;
		}
	}
}