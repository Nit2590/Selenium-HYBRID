package com.birlasoft.testautomation.framework;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.birlasoft.testautomation.utils.Constants;
import com.birlasoft.testautomation.utils.ExcelObject;

public class TestSuite {
	LogMe LOGGER = new LogMe(TestSuite.class);
	
	private List<TestScript> testCases = new ArrayList<TestScript>();
	private String moduleName;

	public List<TestScript> getTestCases() {
		return testCases;
	}

	public TestSuite(String modulePath, String moduleName) throws Exception {
		this.moduleName = moduleName;
		String testModulePath = modulePath + File.separator + moduleName + ".xlsx";
		String columnNames = "ExecFlag" + Constants.CONDITION_SEPARATOR + "TestScriptId" + Constants.CONDITION_SEPARATOR
				+ "TestScriptName" + Constants.CONDITION_SEPARATOR + "Description" + Constants.CONDITION_SEPARATOR
				+ "TestType" + Constants.CONDITION_SEPARATOR + "Browser";

		ExcelObject tcExcel = new ExcelObject(testModulePath, "TestCases");
		List<List<Object>> queryResult = tcExcel.getExcelColumnsData("TestCases", columnNames);
		tcExcel.closeWorkbook();

		for (int i = 0; i < queryResult.size(); i++) {
			List<Object> tsInfo = queryResult.get(i);
			boolean execFlag = "Yes".equalsIgnoreCase(tsInfo.get(0).toString().trim()) ? true : false;

			if (execFlag) {
				TestScript testScript = new TestScript(moduleName, tsInfo.get(1).toString().trim(),
						tsInfo.get(2).toString().trim(), tsInfo.get(3).toString().trim(),
						tsInfo.get(4).toString().trim(), tsInfo.get(5).toString().trim());
				testCases.add(testScript);
			}
		}
		LOGGER.logInfo(
				"Total number of test scripts to be executed in module " + moduleName + " are : " + testCases.size());
	}

	public String getModuleName() {
		return moduleName;
	}
}