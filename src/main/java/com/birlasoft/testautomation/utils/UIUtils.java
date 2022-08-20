package com.birlasoft.testautomation.utils;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sompo.testautomation.framework.driver.TestConfig;

public class UIUtils {
	private static Logger LOGGER = Logger.getLogger(UIUtils.class);
	private static int maxWaitTime = Integer.parseInt(TestConfig.getConfig().getPropertyValue("MAXWAITTIME"));
	private static Config config;

	public static ExpectedCondition<Boolean> waitForPageLoad;

	private static final String JQUERY_ACTIVE_CONNECTIONS_QUERY = "return $.active == 0;";

	static {
		try {
			config = new Config("Framework\\Test_Config\\config.properties");
		} catch (IOException e) {
			LOGGER.error(e.getClass().getSimpleName(), e);
		}

		waitForPageLoad = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				try {
					return executeScript(driver, "return document.readyState").equals("complete");
				} catch (Exception e) {
					return false;
				}
			}
		};
	}

	public static final ExpectedCondition<Boolean> EXPECT_DOC_READY_STATE = new ExpectedCondition<Boolean>() {
		String script = "if (typeof window != 'undefined') { return document.readyState;} else { return 'notready';}";

		@Override
		public Boolean apply(WebDriver driver) {
			try {
				String result = String.valueOf(executeScript(driver, script));
				return (result.equals("complete") || result.equals("interactive"));
			} catch (Exception e) {
				LOGGER.error(e.getClass().getSimpleName(), e);
				return false;
			}
		}
	};

	public static Config getConfig() {
		return config;
	}

	private static DesiredCapabilities getBrowserCapabilities(String browserType, String driverPath) throws Exception {
		DesiredCapabilities dc = null;

		switch (browserType.toUpperCase()) {
		case "FIREFOX":
			if (StringUtils.isNotBlank(driverPath)) {
				System.setProperty("webdriver.gecko.driver", driverPath);
			} else {
				System.setProperty("webdriver.gecko.driver", config.getPropertyValue("GeckoDriverPath"));
			}
			dc = DesiredCapabilities.firefox();
			dc.setBrowserName(BrowserType.FIREFOX);
			break;
		case "CHROME":
			if (StringUtils.isNotBlank(driverPath)) {
				System.setProperty("webdriver.chrome.driver", driverPath);
			} else {
				System.setProperty("webdriver.chrome.driver", config.getPropertyValue("ChromeDriverPath"));
			}
			dc = DesiredCapabilities.chrome();
			ChromeOptions options = new ChromeOptions();
			options.addArguments("test-type");
			dc.setBrowserName(BrowserType.CHROME);
			dc.setCapability(ChromeOptions.CAPABILITY, options);
			break;
		case "IE":
			if (StringUtils.isNotBlank(driverPath)) {
				System.setProperty("webdriver.ie.driver", driverPath);
			} else {
				System.setProperty("webdriver.ie.driver", config.getPropertyValue("InternetExplorerDriverPath"));
			}
			dc = DesiredCapabilities.internetExplorer();
			dc.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			dc.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
			dc.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
			dc.setCapability("ignoreProtectedModeSettings", true);
			dc.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			dc.setBrowserName(BrowserType.IE);
			break;
		default:
			break;
		}
		return dc;
	}

	public static WebDriver createDriverInstance(String browserType, String driverPath) throws Exception {
		WebDriver driver = null;

		switch (browserType) {
		case "FIREFOX":
			if (StringUtils.isNotBlank(driverPath)) {
				System.setProperty("webdriver.gecko.driver", driverPath);
			} else {
				System.setProperty("webdriver.gecko.driver", config.getPropertyValue("GeckoDriverPath"));
			}
			driver = new FirefoxDriver();
			break;
		case "CHROME":
			if (StringUtils.isNotBlank(driverPath)) {
				System.setProperty("webdriver.chrome.driver", driverPath);
			} else {
				System.setProperty("webdriver.chrome.driver", config.getPropertyValue("ChromeDriverPath"));
			}
			driver = new ChromeDriver();
			break;
		case "IE":
			DesiredCapabilities dc = getBrowserCapabilities(browserType, driverPath);
			InternetExplorerOptions ieOptions = new InternetExplorerOptions(dc);
			driver = new InternetExplorerDriver(ieOptions);
			break;
		default:
			break;
		}

		driver.manage().timeouts().setScriptTimeout(Long.parseLong(config.getPropertyValue("ScriptTimeoutSeconds")),
				TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(Long.parseLong(config.getPropertyValue("AVGWAITTIME")),
				TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(Long.parseLong(config.getPropertyValue("AVGWAITTIME")),
				TimeUnit.SECONDS);
		return driver;
	}

	public static WebDriver createDriverInstance(String browserType, String driverPath, String gridURL)
			throws Exception {
		WebDriver driver = new RemoteWebDriver(new URL(gridURL), getBrowserCapabilities(browserType, driverPath));
		driver.manage().timeouts().setScriptTimeout(Long.parseLong(config.getPropertyValue("ScriptTimeoutSeconds")),
				TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(Long.parseLong(config.getPropertyValue("AVGWAITTIME")),
				TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(Long.parseLong(config.getPropertyValue("AVGWAITTIME")),
				TimeUnit.SECONDS);
		return driver;
	}

	public static Object executeScript(WebDriver driver, String script, Object... args) {
		return ((JavascriptExecutor) (driver)).executeScript(script, args);
	}

	public static Object executeAsyncScript(WebDriver driver, String script, Object... args) {
		return ((JavascriptExecutor) (driver)).executeAsyncScript(script, args);
	}

	public static boolean isObjectExist(WebDriver driver, By by) {
		return (driver.findElements(by).size() > 0);
	}

	public static By getLocatorObject(String locatorType, String locatorValue) {
		By by = null;

		switch (locatorType.toUpperCase()) {
		case "XPATH":
			by = By.xpath(locatorValue);
			break;
		case "ID":
			by = By.id(locatorValue);
			break;
		case "NAME":
			by = By.name(locatorValue);
			break;
		case "TAGNAME":
		case "TAG":
			by = By.tagName(locatorValue);
			break;
		case "CLASSNAME":
		case "CLASS":
			by = By.className(locatorValue);
			break;
		case "CSSSELECTOR":
		case "CSS":
			by = By.cssSelector(locatorValue);
			break;
		case "LINKTEXT":
		case "LINK":
			by = By.linkText(locatorValue);
			break;
		case "PARTIALLINKTEXT":
			by = By.partialLinkText(locatorValue);
			break;
		default:
			break;
		}

		return by;
	}

	public static By getLocatorObject(String locator) {
		return getLocatorObject(locator.split(config.getPropertyValue("LocatorValueSeparator"))[0],
				locator.split(config.getPropertyValue("LocatorValueSeparator"))[1]);
	}

	public static WebElement findElement(WebDriver driver, By by) throws Exception {
		waitUntilElementExists(driver, by, maxWaitTime);
		return findElement(driver, by, Integer.valueOf(config.getPropertyValue("AVGWAITTIME")));
	}

	public static WebElement findElement(WebDriver driver, By by, int waitTime) {
		WebDriverWait wWait = new WebDriverWait(driver, waitTime);
		return wWait.until(ExpectedConditions.presenceOfElementLocated(by));
	}

	public static void highLightElement(WebDriver driver, WebElement element) {
		executeScript(driver, "arguments[0].setAttribute('style', 'border: 2px solid blue;');", element);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}

		executeScript(driver, "arguments[0].setAttribute('style', 'border: 3px solid blue;');", element);
	}

	public static void hoverElement(WebDriver driver, WebElement element) {
		Actions action = new Actions(driver);
		action.moveToElement(element).build().perform();
	}

	public static boolean isAlertPresent(WebDriver driver) {
		boolean result = false;

		try {
			driver.switchTo().alert();
			result = true;
			driver.switchTo().defaultContent();
		} catch (NoAlertPresentException e) {
			LOGGER.error(e.getClass().getSimpleName(), e);
		}

		return result;
	}

	public static Alert getAlert(WebDriver driver) {
		return getAlert(driver, Integer.valueOf(config.getPropertyValue("AVGWAITTIME")));
	}

	public static void handleAlert() {
		try {
			Robot robot = new Robot();

			robot.keyPress(KeyEvent.VK_TAB);
			Thread.sleep(200);
			robot.keyRelease(KeyEvent.VK_TAB);
			Thread.sleep(200);

			robot.keyPress(KeyEvent.VK_TAB);
			Thread.sleep(200);
			robot.keyRelease(KeyEvent.VK_TAB);
			Thread.sleep(200);

			robot.keyPress(KeyEvent.VK_ENTER);
			Thread.sleep(200);
			robot.keyRelease(KeyEvent.VK_ENTER);

			Thread.sleep(5000);
		} catch (Exception e) {
			LOGGER.error(e.getClass().getSimpleName(), e);
		}
	}

	public static Alert getAlert(WebDriver driver, int waitTime) {
		return new WebDriverWait(driver, waitTime).until(ExpectedConditions.alertIsPresent());
	}

	public static void alertAccept(WebDriver driver) throws Exception {
		getAlert(driver).accept();
		driver.switchTo().defaultContent();
	}

	public static void alertDismiss(WebDriver driver) throws Exception {
		getAlert(driver).dismiss();
		driver.switchTo().defaultContent();
	}

	public static void alertAccept(WebDriver driver, int waitTime) {
		getAlert(driver, waitTime).accept();
		driver.switchTo().defaultContent();
	}

	public static void alertDismiss(WebDriver driver, int waitTime) {
		getAlert(driver, waitTime).dismiss();
		driver.switchTo().defaultContent();
	}

	public static void takeScreenshot(WebDriver driver, String filePath) throws IOException {
		File file = new File(filePath);

		if (!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		FileUtils.copyFile(((TakesScreenshot) (driver)).getScreenshotAs(OutputType.FILE), file);
	}

	public static void closeAllPopUps(WebDriver driver) {
		String mainWinHanlde = driver.getWindowHandle();

		// Closing all but the main window
		for (String winHandle : driver.getWindowHandles()) {
			driver.switchTo().window(winHandle);
			if (!winHandle.equalsIgnoreCase(mainWinHanlde)) {
				driver.close();
			}
		}

		// Focusing back to main Window
		driver.switchTo().window(mainWinHanlde);
	}
	
	
	
	
	public static void selectValue(WebDriver driver, By by, String optionText) {
		optionText = optionText.trim();

		if (optionText.toLowerCase().startsWith("index=")) {
			selectValue(driver, by, "index", optionText.replaceAll("index=", ""));
		} else if (optionText.toLowerCase().startsWith("text=")) {
			selectValue(driver, by, "text", optionText.replaceAll("text=", ""));
		} else if (optionText.toLowerCase().startsWith("containstext=")) {
			selectValue(driver, by, "containstext", optionText.replaceAll("containstext=", ""));
		} else if (optionText.startsWith("value=")) {
			selectValue(driver, by, "value", optionText.replaceAll("value=", ""));
		} else {
			new Select(driver.findElement(by)).selectByVisibleText(optionText);
		}
	}

	public static void selectValue(WebDriver driver, By by, String selectBy, String option) {
		Select select = new Select(driver.findElement(by));

		switch (selectBy.toLowerCase()) {
		case "index":
			select.selectByIndex(Integer.valueOf(option));
			break;
		case "text":
			select.selectByVisibleText(option);
			break;
		case "value":
			select.selectByValue(option);
			break;
		case "containstext":
			int indexNum = 1;
			for (WebElement element : select.getOptions()) {
				if (element.getText().toLowerCase().contains(option.toLowerCase())) {
					select.selectByIndex(indexNum);
					break;
				}
				indexNum++;
			}
			break;
		default:
			break;
		}
	}

	public static void verticalScrollWindow(WebDriver driver, String direction) {
		if (direction.equalsIgnoreCase("Up")) {
			executeScript(driver, "scroll(250, 0)");
		} else {
			executeScript(driver, "scroll(0, 250)");
		}
	}

	public static FluentWait<WebDriver> getFluentWait(WebDriver driver, Integer... waitTimes) {
		if (waitTimes != null) {
		} else {
		}

		if (waitTimes.length > 1) {
		} else {
		}

		return new FluentWait<>(driver).ignoring(NoSuchElementException.class)
				.ignoring(StaleElementReferenceException.class);
	}

	public static boolean waitForPageLoad(WebDriver driver, int waitTime) {
		return new WebDriverWait(driver, waitTime).until(waitForPageLoad);
	}

	public static boolean waitForPageLoad(WebDriver driver) throws Exception {
		return waitForPageLoad(driver, Integer.valueOf(config.getPropertyValue("AVGWAITTIME")));
	}

	@SafeVarargs
	public static boolean waitForPageLoad(WebDriver driver, int waitTime, ExpectedCondition<Boolean>... conditions) {
		boolean isLoaded = false;

		try {
			waitUntilAjaxRequestCompletes(driver);
			Wait<WebDriver> wWait = getFluentWait(driver);

			for (ExpectedCondition<Boolean> condition : conditions) {
				isLoaded = wWait.until(condition);
				if (!isLoaded) {
					// Stop checking on first condition returning false
					break;
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getClass().getSimpleName(), e);
		}
		return isLoaded;
	}

	public static WebElement waitUntilElementExists(WebDriver driver, final By by, Integer... waitTimes) {
		final Wait<WebDriver> wWait = getFluentWait(driver, waitTimes);
		WebElement element = null;
		try {
			wWait.until(new ExpectedCondition<WebElement>() {
				@Override
				public WebElement apply(WebDriver driver) {
					return driver.findElement(by);
				}
			});
			element = wWait.until(ExpectedConditions.visibilityOfElementLocated(by));
		} catch (Exception e) {
			LOGGER.error(e.getClass().getSimpleName(), e);
		}
		return element;
	}

	public static boolean waitUntilElementNotExists(WebDriver driver, By by, Integer... waitTimes) {
		boolean result = false;
		try {
			result = getFluentWait(driver, waitTimes).until(
					ExpectedConditions.or(ExpectedConditions.not(ExpectedConditions.presenceOfElementLocated(by)),
							ExpectedConditions.invisibilityOfElementLocated(by)));
		} catch (Exception e) {
			LOGGER.error(e.getClass().getSimpleName(), e);
		}
		return result;
	}

	private static void waitUntilAjaxRequestCompletes(WebDriver driver, Integer... waitTimes) {
		final Wait<WebDriver> wWait = getFluentWait(driver, waitTimes);

		wWait.until(new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				return (Boolean) executeScript(driver, JQUERY_ACTIVE_CONNECTIONS_QUERY);
			}
		});
	}

	public static boolean dynamicWait(WebDriver driver, By by, int waitTime) {
		for (int i = 1; i < waitTime; i++) {
			try {
				return driver.findElement(by).isDisplayed() == true;
			} catch (Exception e) {
			}
		}
		return false;
	}

	public static void clickElement(WebDriver driver, By by) throws Exception {
		
		waitUntilElementExists(driver, by, maxWaitTime);
		WebElement element = findElement(driver, by);
		new Actions(driver).moveToElement(element).click().build().perform();
	}

	public static void clickEnter(WebDriver driver, By by) throws Exception {
		waitUntilElementExists(driver, by, maxWaitTime);
		WebElement element = findElement(driver, by);
		new Actions(driver).moveToElement(element).build().perform();
		element.sendKeys(Keys.ENTER);
	}

	public static void clickElementJScript(WebDriver driver, By by) throws Exception {
		waitUntilElementExists(driver, by, maxWaitTime);
		WebElement element = null;
		element = findElement(driver, by);
		clickElementJScript(driver, element);
	}

	public static void clickElementJScript(WebDriver driver, WebElement element) {
		executeScript(driver, "arguments[0].scrollIntoView(true);", element);
		executeScript(driver, "arguments[0].click();", element);
	}

	public static void inputValue(WebElement element, String data) {
		element.clear();

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
		}

		for (char chr : data.toCharArray()) {
			element.sendKeys(new StringBuilder(chr));
		}
	}

	public static void inputText(WebElement element, String data) {
		
		
		element.clear();

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
		}

		element.sendKeys(data);
	}

	public static void inputTextJScript(WebDriver driver, WebElement element, String data) {
		element.clear();

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
		}

		executeScript(driver, "arguments[0].scrollIntoView(true);", element);
		executeScript(driver, "arguments[0].setAttribute('value', '" + data + "');", element);
	}

	public static String getText(WebElement element) {
		
		
		String text = element.getText();

		if (StringUtils.isBlank(text)) {
			text = element.getAttribute("value");
		}

		return text;
	}

	public static String getChildText(WebElement element) {
		String elementText = getText(element);
		StringBuilder sb;

		if (StringUtils.isNotBlank(elementText)) {
			sb = new StringBuilder(elementText);
		} else {
			sb = new StringBuilder();
		}

		List<WebElement> childrens = element.findElements(By.xpath(".//*"));
		for (WebElement child : childrens) {
			try {
				if (child.isDisplayed() && StringUtils.isNotBlank(child.getText())
						&& !(elementText.equalsIgnoreCase(child.getText()))) {
					sb.append(child.getText());
				}
			} catch (Exception e) {
			}
		}
		return sb.toString();
	}

	public static void moveTo(WebDriver driver, By by) throws Exception {
		WebElement element = findElement(driver, by);
		new Actions(driver).moveToElement(element).build().perform();
	}

	public static void moveAndClick(WebDriver driver, List<By> byList) throws Exception {
		Actions actions = new Actions(driver);
		WebElement element;

		for (int i = 0; i < byList.size() - 1; i++) {
			element = findElement(driver, byList.get(i));
			actions.moveToElement(element).perform();
			Thread.sleep(3000);
		}

		element = findElement(driver, byList.get(byList.size() - 1));
		actions.moveToElement(element).click().build().perform();
	}

	public static String generateAbsoluteXPath(WebElement childElement, String current) {
		String childTag = childElement.getTagName();

		if ("html".equals(childTag)) {
			return "/html" + current;
		}

		WebElement parentElement = childElement.findElement(By.xpath(".."));
		List<WebElement> childElements = parentElement.findElements(By.xpath("*"));

		int count = 0;

		for (WebElement webElement : childElements) {
			if (webElement.getTagName().equals(childTag)) {
				count++;
			}

			if (childElement.equals(webElement)) {
				return generateAbsoluteXPath(parentElement, "/" + childTag + "[" + count + "]" + current);
			}
		}
		return null;
	}

	public static WebElement getGridParentRowElement(WebElement element) {
		WebElement parentRow = null;
		int rowCounter = 0;

		if (element != null) {
			parentRow = element;

			do {
				parentRow = parentRow.findElement(By.xpath(".."));
				rowCounter++;
			} while (parentRow.getTagName().toLowerCase().compareTo("tr") != 0 && rowCounter < 10);

			if (rowCounter >= 10) {
				return null;
			}
		}
		return parentRow;
	}

	public static Map<Integer, List<WebElement>> getGridCells(WebDriver driver, String tableXPath) throws Exception {
		Map<Integer, List<WebElement>> gridElements = new HashMap<Integer, List<WebElement>>();
		String rowsXPath = tableXPath + "//tbody/tr";
		List<WebElement> rowElements = driver.findElements(By.xpath(rowsXPath));

		int rowIndex = 1;
		for (WebElement rowElement : rowElements) {
			List<WebElement> gridCells = rowElement.findElements(By.xpath(".//td"));

			gridElements.put(rowIndex, gridCells);
			rowIndex++;
		}

		return gridElements;
	}

	public static Map<Integer, List<String>> getGridCellValues(WebDriver driver, String tableXPath) throws Exception {
		Map<Integer, List<String>> values = new HashMap<Integer, List<String>>();
		Map<Integer, List<WebElement>> gridElements = getGridCells(driver, tableXPath);

		for (int rowIndex = 1; rowIndex <= gridElements.size(); rowIndex++) {
			List<WebElement> gridCells = gridElements.get(rowIndex);
			List<String> rowValues = new ArrayList<String>();

			for (WebElement gridCell : gridCells) {
				rowValues.add(UIUtils.getChildText(gridCell));
			}

			values.put(rowIndex, rowValues);
		}
		return values;
	}

	public static List<WebElement> getGridRowElements(WebDriver driver, String tableXPath, String itemValue)
			throws Exception {
		String rowXPath = tableXPath + "//*[text()='" + itemValue + "']//ancestor::tr[1]";
		WebElement rowElement = UIUtils.findElement(driver, By.xpath(rowXPath));

		return rowElement.findElements(By.xpath(".//td"));
	}

	public static List<String> getGridRowValues(WebDriver driver, String tableXPath, String itemValue)
			throws Exception {
		List<WebElement> gridRowElements = getGridRowElements(driver, tableXPath, itemValue);
		List<String> rowValues = new ArrayList<String>();

		for (WebElement gridCell : gridRowElements) {
			rowValues.add(UIUtils.getChildText(gridCell));
		}

		return rowValues;
	}

	public static int getGridColNumber(WebDriver driver, String tableXPath, String tableHeader) {
		String colXPath = tableXPath + "//th";
		List<WebElement> colHeaders = driver.findElements(By.xpath(colXPath));
		int colNum = 1;

		for (WebElement colHeader : colHeaders) {
			WebElement title;

			try {
				title = colHeader.findElement(By.xpath(".//*[text() = '" + tableHeader + "']"));
				if (title.isDisplayed()) {
					return colNum;
				}
			} catch (Exception e) {
			}
			colNum++;
		}

		return -1;
	}

	public static String getGridColValueForItem(WebDriver driver, String tableXPath, String itemValue, int colNum)
			throws Exception {
		String rowXPath = tableXPath + "//*[text()='" + itemValue + "']//ancestor::tr[1]";
		WebElement rowElement = UIUtils.findElement(driver, By.xpath(rowXPath));
		String colValue = getChildText(rowElement.findElement(By.xpath(".//td[" + colNum + "]")));
		return colValue;
	}

	public static Map<String, List<WebElement>> getGridCellsColWise(WebDriver driver, String tableXPath)
			throws Exception {
		Map<String, List<WebElement>> gridElements = new HashMap<String, List<WebElement>>();

		String colXPath = tableXPath + "//th";
		List<WebElement> colElements = driver.findElements(By.xpath(colXPath));

		int colNum = 1;

		for (WebElement colHeader : colElements) {
			String rowXPath = tableXPath + "//tbody/tr[" + colNum + "]";
			List<WebElement> gridCells = driver.findElement(By.xpath(rowXPath)).findElements(By.xpath(".//td"));

			gridElements.put(getChildText(colHeader), gridCells);
		}
		return gridElements;
	}

	public static Map<String, List<String>> getGridCellValuesColWise(WebDriver driver, String tableXPath)
			throws Exception {
		Map<String, List<WebElement>> gridElements = getGridCellsColWise(driver, tableXPath);
		Map<String, List<String>> gridCellValues = new HashMap<String, List<String>>();

		Set<String> headers = gridElements.keySet();

		for (String header : headers) {
			List<WebElement> colElements = gridElements.get(header);
			List<String> colValues = new ArrayList<String>();

			for (WebElement cell : colElements) {
				colValues.add(getChildText(cell));
			}
			gridCellValues.put(header, colValues);
		}

		return gridCellValues;
	}

	public static void fileUpload(String filePath) throws AWTException, InterruptedException {
		
		
		StringSelection stringSelection = new StringSelection(filePath);
	        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	        clipboard.setContents(stringSelection, null);
	        Thread.sleep(6000);
	        Robot robot = null;
	 
	        try {
	            robot = new Robot();
	        } catch (AWTException e) {
	            e.printStackTrace();
	        }
	 
	        robot.delay(250);
	        robot.keyPress(KeyEvent.VK_ENTER);
	        robot.keyRelease(KeyEvent.VK_ENTER);
	        robot.keyPress(KeyEvent.VK_CONTROL);
	        robot.keyPress(KeyEvent.VK_V);
	        robot.keyRelease(KeyEvent.VK_V);
	        robot.keyRelease(KeyEvent.VK_CONTROL);
	        robot.keyPress(KeyEvent.VK_ENTER);
	        robot.delay(150);
	        robot.keyRelease(KeyEvent.VK_ENTER);
	        Thread.sleep(2000);
		/*
		StringSelection selection = new StringSelection(filePath);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(selection, selection);

		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);

		Thread.sleep(6000);

		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_ENTER);*/
	}
}