package com.birlasoft.testautomation.framework;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;

import com.sompo.testautomation.framework.driver.TestConfig;

public class TestStep {
	LogMe LOGGER = new LogMe(TestStep.class);

	private WebDriver driver;
	private String mainHandle;

	private Keyword actionName;
	private String screenName;
	private String objectName;
	private String fieldValue;

	private String locatorType;
	private String locatorValue;

	private List<String> arguments = new ArrayList<>();
	private List<String> printReport= new ArrayList<>();

	private String testStepId;
	private boolean stepResult;

	private String actualResult;

	private boolean exitFlag;

	private TestActions action;

	public TestStep(Keyword actionName, String screenName, String objectName, String fieldValue, int stepNumber) {
		this.actionName = actionName;
		this.screenName = screenName;
		this.objectName = objectName;
		this.fieldValue = fieldValue;

		if (StringUtils.isNotBlank(screenName) && StringUtils.isNotBlank(objectName)) {
			String[] actionObject = TestConfig.getInstance().getObjRep().getObject(screenName, objectName);

			if (actionObject != null) {
				locatorType = actionObject[0];
				locatorValue = actionObject[1];
			} else {
				LOGGER.logError("Object Description not found");
			}
		}
		testStepId = "Step " + stepNumber;
	}

	public TestStep(Keyword actionName, String screenName, String objectName, String fieldValue, int stepNumber,
			List<String> arguments) {
		this(actionName, screenName, objectName, fieldValue, stepNumber);
		this.arguments = arguments;
	}

	public TestStep(Keyword actionName, String screenName, String objectName, String fieldValue, int stepNumber,
			WebDriver driver, String mainHandle) {
		this(actionName, screenName, objectName, fieldValue, stepNumber);
		this.driver = driver;
		this.mainHandle = mainHandle;
	}

	public TestStep(Keyword actionName, String screenName, String objectName, String fieldValue, int stepNumber,
			WebDriver driver, String mainHandle, List<String> arguments) {
		this(actionName, screenName, objectName, fieldValue, stepNumber, driver, mainHandle);
		this.arguments = arguments;
	}
	
	
	

	
	
	
	public WebDriver getDriver() {
		return driver;
	}

	public Keyword getActionName() {
		return actionName;
	}

	public String getScreenName() {
		return screenName;
	}

	public String getObjectName() {
		return objectName;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public String getLocatorType() {
		return locatorType;
	}

	public String getLocatorValue() {
		return locatorValue;
	}

	public String getMainHandle() {
		return mainHandle;
	}

	public List<String> getArguments() {
		return arguments;
	}

	public List<String> getPrintReport() {
		return printReport;
	}

	public boolean isStepResult() {
		return stepResult;
	}

	public boolean isExitFlag() {
		return exitFlag;
	}

	public void setExitFlag(boolean exitFlag) {
		this.exitFlag = exitFlag;
	}

	
	public void setLogResult(String strResult)
	{
		printReport.add(strResult);
	}
	public void setStepResult(boolean stepResult) {
		this.stepResult = stepResult;
	}

	public void setActualResult(String actualResult) {
		this.actualResult = actualResult;
	}

	public void executeStep() throws Exception {
		action = new TestActions(this);
		Method[] methods = action.getClass().getMethods();
		for (Method method : methods) {
			if (method.getName().equalsIgnoreCase(actionName.toString())) {
				Object result = method.invoke(action);
				stepResult = Boolean.valueOf(result.toString());
				break;
			}
		}
		
		for(String sLog : printReport)
		{
			LOGGER.logTestStep(testStepId, sLog.toString());
		}
		// LOG step result
		logStepResult(actualResult);
	}

	public void logStepResult(String actualResult) {
		if (stepResult) {
			LOGGER.logTestStep(testStepId, "PASS", funcExpectedResult(), actualResult);
		} else {
			LOGGER.logTestStep(testStepId, "FAIL", funcExpectedResult(), actualResult);
		}
	}

	private String funcExpectedResult() {
		String strExpected = "";
		switch (actionName) {
		case NAVIGATE:
			strExpected = "User should be navigated to " + fieldValue;
			break;
		case WAITTIME:
			strExpected = "Execution should be paused for " + fieldValue + " mili seconds";
			break;
		case WAITFORELEMENTEXIST:
			strExpected = "Object " + objectName + " of screen " + screenName + " should exist & visible";
			break;
		case WAITFORELEMENTNOTEXIST:
			strExpected = "Object " + objectName + " of screen " + screenName + " should not exist or visible";
			break;
		case CLICK:
		case CLICKFOCUS:
			strExpected = "User should be able to click on Object " + objectName + " of screen " + screenName;
			break;
		case SELECT:
			strExpected = "User should be able to select attribute " + fieldValue + " of dropdown " + objectName
					+ " in screen " + screenName;
			break;
		case MOVETO:
			strExpected = "User should be moved control to Object " + objectName + " of screen " + screenName;
			break;
		case MOVEANDCLICK:
			strExpected = "User should be moved control and clicked on Object " + objectName + " of screen "
					+ screenName;
			break;
		case INPUT:
		case KEYBOARDINPUT:
			strExpected = "User should be able to input value " + fieldValue + " in object " + objectName
					+ " of screen " + screenName;
			break;
		case SETCHECKBOX:
			strExpected = "User should be able to " + fieldValue + " checkbox(s) " + objectName + " of screen "
					+ screenName;
			break;
		case CLEAR:
			strExpected = "User should be able to clear the value froom the object " + objectName + " of screen "
					+ screenName;
			break;
		case HANDLEALERT:
			switch (objectName.toUpperCase()) {
			case "ACCEPT":
				strExpected = "User should be able to accept the alert";
				break;
			case "DISMISS":
				strExpected = "User should be able to dismiss the alert";
				break;
			case "DOWNLOAD":
				strExpected = "User should be able to download the alert";
				break;
			case "VERIFYTEXT":
				strExpected = "Alert text should be " + fieldValue;
				break;
			case "CONTAINSTEXT":
				strExpected = "Alert text should contain " + fieldValue;
				break;
			default:
				break;
			}
		case SWITCHWINDOW:
			switch (fieldValue.toUpperCase()) {
			case "DIFFERENTFRAME":
				if (arguments.size() > 0) {
					strExpected = "User should be able to switch focus to the " + arguments.get(0).trim();
				} else {
					strExpected = "User should be able to switch focus to the frame " + objectName + " of screen "
							+ screenName;
				}
				break;
			case "DEFAULTCONTENT":
				strExpected = "User should be able to switch focus to the default content";
				break;
			case "MAINWINDOW":
				strExpected = "User should be able to switch focus to the main window";
				break;
			case "FIRSTWINDOW":
				strExpected = "User should be able to switch focus to the first tab";
				break;
			case "LASTWINDOW":
				strExpected = "User should be able to switch focus to the last tab";
				break;
			default:
				break;
			}
		case SCROLL:
			strExpected = "User should be able to scroll " + fieldValue + " the object " + objectName + " of screen "
					+ screenName;
			break;
		case SCROLLINTOVIEW:
			strExpected = "User should be able to scroll the screen down so that the object " + objectName
					+ " of screen " + screenName + " is in view";
			break;
		case FILEUPLOAD:
			strExpected = "User should be able to upload file " + fieldValue + " in the screen " + screenName;
			break;
		case GETVALUE:
			strExpected = "User should fetch value of object " + objectName + " of screen " + screenName
					+ " from UI and stored it in variable " + fieldValue;
			break;
		case DATEPICK:
			strExpected = "User should be able to enter the " + fieldValue + " date in the object " + objectName
					+ " of screen " + screenName;
			break;
		case ISELEMENTEXIST:
			strExpected = "Object " + objectName + " of screen " + screenName + " should exist and visible";
			break;
		case ISELEMENTNOTEXIST:
			strExpected = "Object " + objectName + " of screen " + screenName + " should not exist or visible";
			break;
		case ISELEMENTENABLED:
			strExpected = "Object " + objectName + " of screen " + screenName + " should be enabled";
			break;
		case VERIFYTEXT:
			strExpected = "Verify that the text of object " + objectName + " in screen " + screenName + " should be "
					+ fieldValue;
			break;
		case CONTAINSTEXT:
			strExpected = "Verify that the text of object " + objectName + " in screen " + screenName
					+ " contains text " + fieldValue;
			break;
		case VERIFYATTRIBUTE:
			strExpected = "Verify attribute " + arguments.get(0) + " value of object " + objectName + " in screen "
					+ screenName + " is " + arguments.get(1);
			break;
		case VALIDATELIST:
			if (fieldValue.toUpperCase().contains("CONTAINS=") || fieldValue.toUpperCase().contains("CONTAINS =")) {
				strExpected = "Verify the text of each element of list " + objectName + " in screen " + screenName
						+ " contains text " + fieldValue;
			} else {
				strExpected = "Verify the text of each element of list " + objectName + " in screen " + screenName
						+ " should be " + fieldValue;
			}
			break;
		case VALIDATETEXTNOTINLIST:
			if (fieldValue.toUpperCase().contains("CONTAINS=") || fieldValue.toUpperCase().contains("CONTAINS =")) {
				strExpected = "Verify the text of each element of list " + objectName + " in screen " + screenName
						+ " does not contain text " + fieldValue;
			} else {
				strExpected = "Verify the text of each element of list " + objectName + " in screen " + screenName
						+ " should not be " + fieldValue;
			}
			break;
		case RUNPROCESS:
			strExpected = "User should be able to execute the process " + fieldValue;
			break;
		default:
			break;
		}
		return strExpected;
	}
	
	
	
	
}