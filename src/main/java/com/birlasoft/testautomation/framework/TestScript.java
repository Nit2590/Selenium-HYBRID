package com.birlasoft.testautomation.framework;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jfairy.Fairy;
import org.openqa.selenium.WebDriver;

import com.birlasoft.testautomation.utils.Constants;
import com.birlasoft.testautomation.utils.ExcelObject;
import com.sompo.testautomation.framework.driver.TestConfig;
import com.sompo.testautomation.framework.driver.TestDriver;

public class TestScript {
	LogMe LOGGER = new LogMe(TestScript.class);

	private WebDriver driver = null;

	private String moduleName;
	private String tsId;
	private String tsName;
	private String tsDescription;
	private String browserName;
	private String testType;
	private String mainHandle;

	public List<TestStep> testSteps = new ArrayList<TestStep>();

	private String tsStartTime;
	private String tsEndTime;

	private boolean tsResult;

	public static Map<String, String> runTimeVars = new HashMap<>();
	public static Map<String, String> testData = new HashMap<>();

	private static int totalTSCount = 0;
	private static int passedTSCount = 0;
	private static int failedTSCount = 0;

	private Fairy fairy = Fairy.create();

	public TestScript(String moduleName, String tsId, String tsName, String tsDescription, String testType,
			String browserName) throws Exception {
		this.moduleName = moduleName;
		this.tsId = tsId;
		this.tsName = tsName;
		this.tsDescription = tsDescription;
		this.testType = testType;
		this.browserName = browserName;
		this.tsResult = true;
		totalTSCount++;

		// Fetching test Data
		getRecordsFromDataSheet();
	}

	public String getTsName() {
		return tsName;
	}

	public static int getTotalTSCount() {
		return totalTSCount;
	}

	public static int getPassedTSCount() {
		return passedTSCount;
	}

	public static int getFailedTSCount() {
		return failedTSCount;
	}

	public Map<String, String> getTestData() {
		return testData;
	}

	public void execute() {
		LOGGER.logBeginTestCase(tsName, tsDescription);

		try {
			if (testType.equalsIgnoreCase("UI")) {
				this.driver = TestDriver.driverInstantiation(browserName.toUpperCase());
				this.driver.manage().window().maximize();
				this.mainHandle = driver.getWindowHandle();
			}

			DateFormat dtFormat = new SimpleDateFormat("HH:mm:ss");
			tsStartTime = dtFormat.format(new Date());

			LOGGER.logInfo("Test " + tsName + " started " + " @ " + tsStartTime);

			String testModulePath = TestConfig.getInstance().getTestModulesPath() + File.separatorChar + moduleName
					+ ".xlsx";

			String columnNames = "Action" + Constants.CONDITION_SEPARATOR + "ScreenName" + Constants.CONDITION_SEPARATOR
					+ "ObjectName" + Constants.CONDITION_SEPARATOR + "FieldValue" + Constants.CONDITION_SEPARATOR;

			for (int ctr = 1; ctr <= 10; ctr++) {
				columnNames += "Attribute" + ctr + Constants.CONDITION_SEPARATOR;
			}

			columnNames = columnNames.substring(0, columnNames.length() - Constants.CONDITION_SEPARATOR.length());

			ExcelObject tcExcel = new ExcelObject(testModulePath, "Scripts");
			List<List<Object>> queryResult = tcExcel.getExcelData("Scripts",
					"TestScript" + Constants.CONDITIONVALUE_SEPARATOR + tsId, columnNames);
			tcExcel.closeWorkbook();

			LOGGER.logInfo("Total Number of test keywords in test '" + tsName + "' = " + queryResult.size());

			TestStep testStep = null;
			int stepNumber = 0;

			for (int counter = 0; counter < queryResult.size(); counter++) {
				List<Object> stepData = queryResult.get(counter);

				Keyword keyword = Keyword.valueOf(stepData.get(0).toString().trim().toUpperCase());
				String screenName = stepData.get(1).toString().trim();
				String objectName = stepData.get(2).toString().trim();
				String fieldValue = stepData.get(3).toString().trim();

				if (objectName.toUpperCase().startsWith("VAR#")) {
					if (!(keyword.equals(Keyword.GETVALUE))) {
						for (String key : runTimeVars.keySet()) {
							if (objectName.toUpperCase().contains(key.toUpperCase())) {
								objectName = objectName.replaceAll(key, runTimeVars.get(key));
							}
						}
					}
				}

				if (fieldValue.toUpperCase().startsWith("VAR#")) {
					if (!(keyword.equals(Keyword.GETVALUE))) {
						for (String key : runTimeVars.keySet()) {
							if (fieldValue.toUpperCase().contains(key.toUpperCase())) {
								fieldValue = fieldValue.replaceAll(key, runTimeVars.get(key));
							}
						}
					}
				} else if (fieldValue.toUpperCase().startsWith("RANDOM_")) {
					fieldValue = randomData(fieldValue.toUpperCase().replaceAll("RANDOM_", ""));
				}

				stepNumber++;

				List<String> arguments = new ArrayList<String>();
				for (int ctr = 4; ctr < stepData.size(); ctr++) {
					if (StringUtils.isBlank(String.valueOf(stepData.get(ctr)).trim())) {
						break;
					} else {
						String argument = String.valueOf(stepData.get(ctr)).trim();
						if (argument.toUpperCase().contains("VAR#")) {
							if (!(keyword.equals(Keyword.GETVALUE))) {
								for (String key : runTimeVars.keySet()) {
									if (argument.toUpperCase().contains(key.toUpperCase())) {
										argument = argument.replaceAll(key, runTimeVars.get(key));
									}
								}
							}
						} else if (argument.toUpperCase().startsWith("RANDOM_")) {
							argument = randomData(argument.toUpperCase().replaceAll("RANDOM_", ""));
						}
						arguments.add(argument);
					}
				}

				if (testType.equalsIgnoreCase("UI")) {
					if (arguments.size() == 0) {
						testStep = new TestStep(keyword, screenName, objectName, fieldValue, stepNumber, driver,
								mainHandle);
					} else {
						testStep = new TestStep(keyword, screenName, objectName, fieldValue, stepNumber, driver,
								mainHandle, arguments);
					}
				} else {
					if (arguments.size() == 0) {
						testStep = new TestStep(keyword, screenName, objectName, fieldValue, stepNumber);
					} else {
						testStep = new TestStep(keyword, screenName, objectName, fieldValue, stepNumber, arguments);
					}
				}

				testSteps.add(testStep);

				try {
					testStep.executeStep();
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					testStep.setStepResult(false);
					testStep.setExitFlag(true);
					testStep.logStepResult("Exception thrown from the action method for keyword "
							+ testStep.getActionName() + " supplied. Exception Message is " + e.getMessage());
				} catch (Exception e) {
					testStep.setStepResult(false);
					testStep.logStepResult("Exception thrown from the action method for keyword "
							+ testStep.getActionName() + " supplied. Exception Message is " + e.getMessage());
				}

				if (!testStep.isStepResult()) {
					tsResult = false;
				}

				if (!testStep.isStepResult() && testStep.isExitFlag()) {
					break;
				}
			}

			if (tsResult) {
				passedTSCount++;
				LOGGER.logInfo("Test Case " + tsName + " Passed");
			} else {
				failedTSCount++;
				LOGGER.logError("Test Case " + tsName + " Failed");
			}

			tsEndTime = dtFormat.format(new Date());
			LOGGER.logInfo("Test " + tsName + " ended " + " @ " + tsEndTime);

			
			  if (testType.equalsIgnoreCase("UI")) 
			  { 
				  Set<String> winHandles = this.driver.getWindowHandles(); 
				  for (String handle : winHandles) 
				  {
				  this.driver.switchTo().window(handle);
				  this.driver.close();
				  } 
			  }
			 
		} catch (Exception e) {
			LOGGER.logError("Exception " + e.getClass().getSimpleName() + " occured");
		}
		LOGGER.logEndTestCase(tsName);
	}

	private String randomData(String input) {
		String output = null;
		switch (input.toUpperCase()) {
		case "NAME":
			output = fairy.textProducer().randomString(10);
			break;
		case "PERSONNAME":
			output = fairy.person().fullName();
			break;
		case "INTEGER":
			output = String.valueOf(fairy.baseProducer().randomBetween(0, 1000));
			break;
		case "PERCENTAGE":
			output = String.valueOf(fairy.baseProducer().randomBetween(0, 100));
			break;
		case "DOUBLE":
			output = String.valueOf(fairy.baseProducer().randomBetween(0, 100000.0));
			break;
		case "TEXT":
			output = fairy.textProducer().paragraph(2);
			break;
		case "DATE":
			DateFormat df = new SimpleDateFormat(TestConfig.getConfig().getPropertyValue("dateFormat"));
			output = df.format(fairy.dateProducer().randomDateInTheFuture().toDate());
			break;
		case "ALPHA" : 
			output = "AUT"+String.valueOf(fairy.baseProducer().randomBetween(0, 100000));
			break;
		default:
			break;
		}
		return output;
	}

	public void getRecordsFromDataSheet() throws IOException {
		// code to create object of test Data Excel
		//String testDataPath = TestConfig.getInstance().getTestDataPath() + File.separatorChar + "TestData" + ".xlsx";
		//Get Test Data Path
		String testDataPath = TestConfig.getInstance().getTestDataPath() + File.separatorChar + tsId + ".xlsx";
		
		ExcelObject tcExcel = new ExcelObject(testDataPath, "Data");
		List<List<Object>> tData = tcExcel.getExcelData("Data");
		tcExcel.closeWorkbook();
		/*
		 * int testStartRowNum = 0, testEndRowNum = 0;
		 * 
		 * if (tData.size() > 0) { for (List<Object> row : tData) { if
		 * (row.get(0).toString().equalsIgnoreCase(tsId)) { break; } testStartRowNum++;
		 * }
		 * 
		 * for (List<Object> row : tData) { if (row.isEmpty() ||
		 * StringUtils.isAllBlank(row.get(0).toString())) { break; } testEndRowNum++; }
		 * }
		 * 
		 * testStartRowNum++;
		 * 
		 * List<String> keys = new ArrayList<>();
		 * 
		 * for (Object key : tData.get(testStartRowNum)) { keys.add(key.toString()); }
		 * 
		 * for (int ctr = testStartRowNum; ctr <= testEndRowNum; ctr++) { List<Object>
		 * value = new ArrayList<>();
		 * 
		 * for (String key : keys) { testData.put(key, value); } }
		 * 
		 */		
		int testStartRowNum = 0;

		for (List<Object> row : tData) {
			if (row.get(0).toString().equalsIgnoreCase(tsId)) {
				break;
			}
			testStartRowNum++;
		}

		if (testStartRowNum == 0) {
			return ;
		}

		// int colStartRowNum = testStartRowNum + 1;
		// int dataStartRowNum = testStartRowNum + 2;

		// calculate rows of data
		for (int i = 0; i <= tData.get(0).size()-1; i++) {
			if (tData.get(testStartRowNum).get(i).toString().toUpperCase().startsWith("RANDOM_"))
			{
				testData.put(tData.get(0).get(i).toString(), randomData(tData.get(testStartRowNum).get(i).toString().toUpperCase().replaceAll("RANDOM_", "")));
			}
			else
			{
				testData.put(tData.get(0).get(i).toString(), tData.get(testStartRowNum).get(i).toString());
			}
			
			
			
		}

		
		
		
		
		
	}
}