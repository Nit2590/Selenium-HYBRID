package com.birlasoft.testautomation.framework;

//http://relevantcodes.com/Tools/ExtentReports2/javadoc/index.html?com/relevantcodes/extentreports/ExtentReports.html

import java.io.File;
import java.io.IOException;
import java.util.Date;

import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;
import com.sompo.testautomation.framework.driver.TestConfig;

public class ExtentManager {
	private static ExtentReports extent;

	private ExtentManager() {
	}

	public static ExtentReports getInstance() {
		if (extent == null) {
			Date d = new Date();
			String fileName = d.toString().replace(":", "_").replace(" ", "_") + ".html";
			try {
				extent = new ExtentReports(new File(TestConfig.getInstance().getReportPath() + "//" + fileName).getCanonicalPath(), true,
						DisplayOrder.NEWEST_FIRST);
			} catch (IOException e) {
				e.printStackTrace();
			}

			extent.loadConfig(new File("ReportConfig.xml"));
			extent.addSystemInfo("Selenium Version", "3.141.0").addSystemInfo("Environment", "QA");
		}
		return extent;
	}
}