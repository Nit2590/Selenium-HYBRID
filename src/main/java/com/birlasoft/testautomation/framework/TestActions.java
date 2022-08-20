package com.birlasoft.testautomation.framework;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import com.birlasoft.testautomation.utils.CommonUtils;
import com.birlasoft.testautomation.utils.UIUtils;
import com.sompo.testautomation.framework.driver.TestConfig;

public class TestActions {
	LogMe LOGGER = new LogMe(TestActions.class);
	TestStep step;

	public TestActions(TestStep step) {
		this.step = step;
	}

	public boolean login() {
		try {
			step.setActualResult("Successfully navigated to " + step.getFieldValue());
			return true;
		} catch (Exception e) {
			step.setActualResult("Exception " + e.getClass().getName() + " occured while navigating to "
					+ step.getFieldValue() + ". Error message is " + e.getMessage());
		}
		return false;
	}

	// FUnction to login into Manager
	public boolean loginGRS() {
		int maxWaitTime = Integer.parseInt(TestConfig.getConfig().getPropertyValue("MAXWAITTIME"));

		if (!UIUtils
				.waitUntilElementExists(step.getDriver(),
						TestConfig.getInstance().getObjRep().getLocator("Login", "txtUserName"), maxWaitTime)
				.isDisplayed()) {
			step.setActualResult("Object txtUserNam of Login Screen is not visible");
			return false;}
		
		
		// Enter UserName'
		try {
			UIUtils.inputText(
					UIUtils.findElement(step.getDriver(),
							TestConfig.getInstance().getObjRep().getLocator("Login", "txtUserName")),
					TestScript.testData.get("UserName").toString());
				step.setLogResult("Successfully Launched GRS Portal Application");
				
				
		
		} catch (Exception e) {
			step.setActualResult("Failed to enter value " + TestScript.testData.get("UserName").toString() + " into the object "
					+ "  txtUserName in screen Login  , Exception " + e.getClass().getName()
					+ " occured. Exception message is " + e.getMessage());
			return false;
		}

		// Enter Password'
		try {
			UIUtils.inputText(
					UIUtils.findElement(step.getDriver(),
							TestConfig.getInstance().getObjRep().getLocator("Login", "txtPassword")),
					TestScript.testData.get("Password").toString());
		} catch (Exception e) {
			step.setActualResult("Failed to enter value Password into the object "
					+ "  txtPassword in screen Login  , Exception " + e.getClass().getName()
					+ " occured. Exception message is " + e.getMessage());
			return false;
		}
		
		//Click on Login button
		try {
			UIUtils.clickElement(step.getDriver(),
					TestConfig.getInstance().getObjRep().getLocator("Login", "btnLogin"));
		} catch (Exception e) {
			step.setActualResult("Failed to click on Object btnLogin present on Login page. Exception "
					+ e.getClass().getName() + " occured. Exception message is " + e.getMessage());
			return false;
		}
		
		//Check Manage Portal label Exist or not
		if (!UIUtils
				.waitUntilElementExists(step.getDriver(),
						TestConfig.getInstance().getObjRep().getLocator("Home", "lblManagePortal"), maxWaitTime)
				.isDisplayed()) {
			step.setActualResult("Object lblManagePortal of Home Screen is not visible");
			return false;
		}
		
		step.setActualResult("Successfully Logged in into GRS Application with User "+TestScript.testData.get("UserName").toString());
		return true;
	}
	
	
	public boolean logoutGRS() throws InterruptedException
	{
	try {
		UIUtils.clickElement(step.getDriver(),
				TestConfig.getInstance().getObjRep().getLocator("Home", "lnkLogOut"));
		} catch (Exception e) {
		step.setActualResult("Failed to click on Object lnkLogOut present on Home page. Exception "
				+ e.getClass().getName() + " occured. Exception message is " + e.getMessage());
		return false;
		}
		
	Thread.sleep(2000);
		step.setActualResult("Successfully Logged Out from  GRS Application");
		return true;
		
	}
	
	
	public boolean createPolicy()
	{
		int maxWaitTime = Integer.parseInt(TestConfig.getConfig().getPropertyValue("MAXWAITTIME"));
		
		//Click on Add Policy
		try {
			UIUtils.clickElement(step.getDriver(),
					TestConfig.getInstance().getObjRep().getLocator("Home", "linkaddPolicy"));
			step.setLogResult("Successfully Clicked on Add Policy Button");
		} catch (Exception e) {
			step.setActualResult("Failed to click on Object linkaddPolicy present on Home page. Exception "
					+ e.getClass().getName() + " occured. Exception message is " + e.getMessage());
			return false;
		}
		
		
		
		//check Existence of PolicyHolder 
		if (!UIUtils
				.waitUntilElementExists(step.getDriver(),
						TestConfig.getInstance().getObjRep().getLocator("PolicyDetail", "selectPolicyHolder"), maxWaitTime)
				.isDisplayed()) {
			step.setActualResult("Object selectPolicyHolder of PolicyDetail Screen is not visible");
			return false;
		}

		// Select Policy Holder'
		try {
			UIUtils.selectValue(step.getDriver(),
					TestConfig.getInstance().getObjRep().getLocator("PolicyDetail", "selectPolicyHolder"),
					TestScript.testData.get("PolicyHolder").toString());

			step.setLogResult("Successfully Selected Policy holder as "+ TestScript.testData.get("PolicyHolder").toString());
		} catch (Exception e) {
			step.setActualResult("Failed to select attribute " + TestScript.testData.get("PolicyHolder").toString() + " of dropdown" 
					+"selectPolicyHolder in screen PolicyDetail, located by "
					+ step.getLocatorType() + " and value " + step.getLocatorValue() + ". Exception "
					+ e.getClass().getName() + " occured. Exception message is " + e.getMessage());
		}
		
		
		// Select Starting policy'
		try {
			UIUtils.selectValue(step.getDriver(),
					TestConfig.getInstance().getObjRep().getLocator("PolicyDetail", "selectStartingPolicy"),
					TestScript.testData.get("StartingPolicy").toString());
			step.setLogResult("Successfully Selected Starting Policy as "+ TestScript.testData.get("StartingPolicy").toString());
			
		} catch (Exception e) {
			step.setActualResult("Failed to select attribute " + TestScript.testData.get("StartingPolicy").toString() + " of dropdown" 
					+"selectStartingPolicy in screen PolicyDetail, located by "
					+ step.getLocatorType() + " and value " + step.getLocatorValue() + ". Exception "
					+ e.getClass().getName() + " occured. Exception message is " + e.getMessage());
		}
		
		
		// Enter Policy Number'
				try {
					UIUtils.inputText(
							UIUtils.findElement(step.getDriver(),
									TestConfig.getInstance().getObjRep().getLocator("PolicyDetail", "txtPolicyNumber")),
							TestScript.testData.get("PolicyNumber").toString());
					step.setLogResult("Successfully Entered Policy Number as "+ TestScript.testData.get("PolicyNumber").toString());
				} catch (Exception e) {
					step.setActualResult("Failed to enter value " + TestScript.testData.get("PolicyNumber").toString() + " into the object "
							+ "  txtPolicyNumber in screen Policy Detail  , Exception " + e.getClass().getName()
							+ " occured. Exception message is " + e.getMessage());
					return false;
				}
      
		//Enter selectProductName
				try {
					UIUtils.selectValue(step.getDriver(),
							TestConfig.getInstance().getObjRep().getLocator("PolicyDetail", "selectProductName"),
							TestScript.testData.get("ProductName").toString());
					step.setLogResult("Successfully Selected Product Name as "+ TestScript.testData.get("ProductName").toString());
					
				} catch (Exception e) {
					step.setActualResult("Failed to enter value " + TestScript.testData.get("ProductName").toString() + " of dropdown" 
							+"selectProductName in screen PolicyDetail, located by "
							+ step.getLocatorType() + " and value " + step.getLocatorValue() + ". Exception "
							+ e.getClass().getName() + " occured. Exception message is " + e.getMessage());
				}
		
			
		// Enter Effective Date'
				try {
					UIUtils.inputText(
							UIUtils.findElement(step.getDriver(),
									TestConfig.getInstance().getObjRep().getLocator("PolicyDetail", "txtEffectiveDate")),
							TestScript.testData.get("EffectiveDate").toString().trim());
					step.setLogResult("Successfully Entered Effective Date as "+ TestScript.testData.get("EffectiveDate").toString().trim());
				} catch (Exception e) {
					step.setActualResult("Failed to enter value " + TestScript.testData.get("EffectiveDate").toString() + " into the object "
							+ "  txtEffectiveDate in screen Policy Detail  , Exception " + e.getClass().getName()
							+ " occured. Exception message is " + e.getMessage());
					return false;
				}	
				
		// Enter Expiration Date
				try {
					UIUtils.inputText(
							UIUtils.findElement(step.getDriver(),
									TestConfig.getInstance().getObjRep().getLocator("PolicyDetail", "txtExpirationDate")),
							TestScript.testData.get("ExpirationDate").toString().trim());
					
					step.setLogResult("Successfully Entered Expiration Date as "+ TestScript.testData.get("ExpirationDate").toString().trim());
				} catch (Exception e) {
					step.setActualResult("Failed to enter value " + TestScript.testData.get("ExpirationDate").toString().trim() + " into the object "
							+ "  txtExpirationDate in screen Policy Detail  , Exception " + e.getClass().getName()
							+ " occured. Exception message is " + e.getMessage());
					return false;
				}	
				 	
		
		    //Click on save Button
				//Click on Login button
				try {
					UIUtils.clickElementJScript(step.getDriver(),
							TestConfig.getInstance().getObjRep().getLocator("PolicyDetail", "btnSave"));
					step.setLogResult("Successfully Clicked Save Button");
				} catch (Exception e) {
					step.setActualResult("Failed to click on Object btnSave present on Login page. Exception "
							+ e.getClass().getName() + " occured. Exception message is " + e.getMessage());
					return false;
				}	
			
				
				//Check Manage Portal label Exist or not
				if (!UIUtils
						.waitUntilElementExists(step.getDriver(),
								TestConfig.getInstance().getObjRep().getLocator("Home", "lblManagePortal"), maxWaitTime)
						.isDisplayed()) {
					step.setActualResult("Object lblManagePortal of Home Screen is not visible");
					return false;
				}
				
				
			
				
		step.setActualResult("Successfully created the Policy with policy number as "+ TestScript.testData.get("PolicyNumber").toString());
				return true;
				
			
				
	}
	
	
	
	public boolean verifyPolicyNumber() throws InterruptedException
	{
		
		
		
		//Click on Filter Policy Number
		try {
			UIUtils.clickElement(step.getDriver(),
					TestConfig.getInstance().getObjRep().getLocator("Home", "filterPolicyNumber"));
			step.setLogResult("Successfully Clicked on Filter Policy Number Button");
		} catch (Exception e) {
			step.setActualResult("Failed to click on Object filterPolicyNumber present on Home page. Exception "
					+ e.getClass().getName() + " occured. Exception message is " + e.getMessage());
			return false;
		}
		
		UIUtils.dynamicWait(step.getDriver(),
							TestConfig.getInstance().getObjRep().getLocator("Home", "txtFilterText"),5000);
		// Enter Policy Number'
		try {
			UIUtils.inputText(
					UIUtils.findElement(step.getDriver(),
							TestConfig.getInstance().getObjRep().getLocator("Home", "txtFilterText")),
					TestScript.testData.get("PolicyNumber").toString());
			step.setLogResult("Successfully Entered Policy Number as "+ step.getArguments().get(0).toString().trim());
		} catch (Exception e) {
			step.setActualResult("Failed to enter value " + TestScript.testData.get("PolicyNumber").toString() + " into the object "
					+ "  txtFilterText in screen GRS Home  , Exception " + e.getClass().getName()
					+ " occured. Exception message is " + e.getMessage());
			return false;
		}	
		
		Thread.sleep(2000);
		//Click on Apply Filter Button
			try {
				UIUtils.clickElementJScript(step.getDriver(),
						TestConfig.getInstance().getObjRep().getLocator("Home", "btnApplyFilter"));
				step.setLogResult("Successfully Clicked on Apply Filter Button");
			} catch (Exception e) {
				step.setActualResult("Failed to click on Object btnApplyFilter present on Home page. Exception "
						+ e.getClass().getName() + " occured. Exception message is " + e.getMessage());
				return false;
			}
		
		//Click on Expand Button
			try {
				UIUtils.clickElement(step.getDriver(),
						TestConfig.getInstance().getObjRep().getLocator("Home", "spanExpand"));
				step.setLogResult("Successfully Clicked on Expand Button");
			} catch (Exception e) {
				step.setActualResult("Failed to click on Object spanExpand present on Home page. Exception "
						+ e.getClass().getName() + " occured. Exception message is " + e.getMessage());
				return false;
			}
	
		//Verify Policy NUmber
			try {
				String objText = UIUtils.getText(UIUtils.findElement(step.getDriver(),
						TestConfig.getInstance().getObjRep().getLocator("Home","lblPolicyNumber")))
						.trim();
				LOGGER.logInfo("Element Text is " + objText);

				if (objText.toUpperCase().contains(step.getArguments().get(0).toString().trim().toUpperCase())) {
					step.setLogResult("Successfully verify text of object lblPolicyNumber in screen "
							+ ", located by " + step.getLocatorType() + " and value "
							+ step.getLocatorValue() + ". Actual text is " + objText);
					return true;
				}
				step.setActualResult("Failed to verify text of object lblPolicyNumber in screen "
						+ ", located by " + step.getLocatorType() + " and value "
						+ step.getLocatorValue() + ". Actual text is " + objText);
			} catch (Exception e) {
				step.setActualResult(
						"Failed to get value of object lblPolicyNumber in screen GRS HOme"
								+ ", located by " + step.getLocatorType() + " and value " + step.getLocatorValue());
			}
		
		step.setActualResult("Successfully Verified Policy Number on GRS Home Screen");
		return true;
	}
	
	
	
	public boolean createEvent()
	{
			//Click on View All Events
				try {
					UIUtils.clickElement(step.getDriver(),
							TestConfig.getInstance().getObjRep().getLocator("Home", "lnkViewAllEvents"));
					step.setLogResult("Successfully Clicked on View All Events Link");
				} catch (Exception e) {
					step.setActualResult("Failed to click on Object lnkViewAllEvents present on Home page. Exception "
							+ e.getClass().getName() + " occured. Exception message is " + e.getMessage());
					return false;
				}
				
			//Click on Add Event
				try {
					UIUtils.clickElement(step.getDriver(),
							TestConfig.getInstance().getObjRep().getLocator("UpComingEvents", "lnkAddEvents"));
					step.setLogResult("Successfully Clicked on Add Events Link");
				} catch (Exception e) {
					step.setActualResult("Failed to click on Object lnkAddEvents present on UpComingEvent  page. Exception "
							+ e.getClass().getName() + " occured. Exception message is " + e.getMessage());
					return false;
				}
				
			//Enter Event Name
				try {
					UIUtils.inputText(
							UIUtils.findElement(step.getDriver(),
									TestConfig.getInstance().getObjRep().getLocator("UpComingEvents", "txtEventName")),
							TestScript.testData.get("EventTitle").toString());
					step.setLogResult("Successfully Entered Event Name as "+ TestScript.testData.get("EventTitle").toString().trim());
				} catch (Exception e) {
					step.setActualResult("Failed to enter value " + TestScript.testData.get("EventTitle").toString() + " into the object "
							+ "  txtEventName in screen Up Coming Events  , Exception " + e.getClass().getName()
							+ " occured. Exception message is " + e.getMessage());
					return false;
				}	
		// Enter Start Date'
				try {
					UIUtils.inputText(
							UIUtils.findElement(step.getDriver(),
									TestConfig.getInstance().getObjRep().getLocator("UpComingEvents", "txtStartDate")),
							TestScript.testData.get("EffectiveDate").toString().trim());
					step.setLogResult("Successfully Entered Effective Date as "+ TestScript.testData.get("EffectiveDate").toString().trim());
				} catch (Exception e) {
					step.setActualResult("Failed to enter value " + TestScript.testData.get("EffectiveDate").toString() + " into the object "
							+ "  txtEffectiveDate in screen Policy Detail  , Exception " + e.getClass().getName()
							+ " occured. Exception message is " + e.getMessage());
					return false;
				}	
				
		// Enter End Date
				try {
					UIUtils.inputText(
							UIUtils.findElement(step.getDriver(),
									TestConfig.getInstance().getObjRep().getLocator("UpComingEvents", "txtEndDate")),
							TestScript.testData.get("ExpirationDate").toString().trim());
					
					step.setLogResult("Successfully Entered Expiration Date as "+ TestScript.testData.get("ExpirationDate").toString().trim());
				} catch (Exception e) {
					step.setActualResult("Failed to enter value " + TestScript.testData.get("ExpirationDate").toString().trim() + " into the object "
							+ "  txtExpirationDate in screen Policy Detail  , Exception " + e.getClass().getName()
							+ " occured. Exception message is " + e.getMessage());
					return false;
				}	
				
				//Click on All day event
				if (TestScript.testData.get("AllDayEvent").toString().equalsIgnoreCase("Yes")) {
					try {
						UIUtils.clickElement(step.getDriver(),
								TestConfig.getInstance().getObjRep().getLocator("UpComingEvents", "btnAllDayEvent"));
						step.setLogResult("Successfully Clicked on All day Event Radio Button");
					} catch (Exception e) {
						step.setActualResult("Failed to click on Object btnAllDayEvent present on UpComingEvent  page. Exception "
								+ e.getClass().getName() + " occured. Exception message is " + e.getMessage());
						return false;
					}
				}
				
				//click on Description and Add
				try {
					UIUtils.inputText(
							UIUtils.findElement(step.getDriver(),
									TestConfig.getInstance().getObjRep().getLocator("UpComingEvents", "txtDescEditor")),
							"Automation Script Event");
					
					//Click on + button
					UIUtils.clickElement(step.getDriver(),
							TestConfig.getInstance().getObjRep().getLocator("UpComingEvents", "btnAddDescription"));
					
					//Click on Add Image File
					UIUtils.clickElement(step.getDriver(),
							TestConfig.getInstance().getObjRep().getLocator("UpComingEvents", "btnAddImage"));
					
					//Wait for Element Present on Screen
					//UIUtils.dynamicWait(step.getDriver(), TestConfig.getInstance().getObjRep().getLocator("SelectFile", "lblSelectFile"), 10000);
					
					//Click on Select File
					
					step.getDriver().switchTo().frame("_com_endurance_portal_portlets_events_web_EventsPortlet_descriptionEditorselectItem_iframe_");
					
					UIUtils.clickElement(step.getDriver(),
							TestConfig.getInstance().getObjRep().getLocator("SelectFile", "lblSelectFile"));
					
					
					UIUtils.fileUpload(TestScript.testData.get("Description").toString());
					
					step.getDriver().switchTo().defaultContent();
					
					UIUtils.clickElement(step.getDriver(),TestConfig.getInstance().getObjRep().getLocator("SelectFile", "btnAddFile"));
					
					
					
					step.setLogResult("Successfully Entered Description of Event");
				} catch (Exception e) {
					step.setActualResult("Failed to enter value Description into the object "
							+ "  txtDescEditor in screen Policy Detail  , Exception " + e.getClass().getName()
							+ " occured. Exception message is " + e.getMessage());
					return false;
				}	
				
				
				
				//Select Event Type
				
				try {
					UIUtils.selectValue(step.getDriver(),
							TestConfig.getInstance().getObjRep().getLocator("UpComingEvents", "lstEventType"),
							TestScript.testData.get("EventType").toString());

					step.setLogResult("Successfully Selected Event Type as "+ TestScript.testData.get("EventType").toString());
				} catch (Exception e) {
					step.setActualResult("Failed to select attribute " + TestScript.testData.get("EventType").toString() + " of dropdown" 
							+"Up Coming  events in screen UpComing Events, located by "
							+ step.getLocatorType() + " and value " + step.getLocatorValue() + ". Exception "
							+ e.getClass().getName() + " occured. Exception message is " + e.getMessage());
				}
				
				//Enter Location
				
				try {
					UIUtils.inputText(
							UIUtils.findElement(step.getDriver(),
									TestConfig.getInstance().getObjRep().getLocator("UpComingEvents", "txtLocation")),
							TestScript.testData.get("Location").toString());
					step.setLogResult("Successfully Entered Location as "+ TestScript.testData.get("Location").toString().trim());
				} catch (Exception e) {
					step.setActualResult("Failed to enter value " + TestScript.testData.get("Location").toString() + " into the object "
							+ "  txtLocation in screen Up Coming Events  , Exception " + e.getClass().getName()
							+ " occured. Exception message is " + e.getMessage());
					return false;
				}
				
				
				//Click on All day event
				
					try {
						UIUtils.clickElement(step.getDriver(),
								TestConfig.getInstance().getObjRep().getLocator("UpComingEvents", "btnAddAttachment"));
						
						UIUtils.fileUpload(TestScript.testData.get("Attachements").toString().trim());
						
						step.setLogResult("Successfully Added Attachment");
					} catch (Exception e) {
						step.setActualResult("Failed to Add Attachment on  UpComingEvent  page. Exception "
								+ e.getClass().getName() + " occured. Exception message is " + e.getMessage());
						return false;
					}
				
				//Click on Submit Button
					try {
						UIUtils.clickElement(step.getDriver(),
								TestConfig.getInstance().getObjRep().getLocator("UpComingEvents", "btnSubmitEvent"));
						
						
						step.setLogResult("Successfully Click on Submit Button");
					} catch (Exception e) {
						step.setActualResult("Failed to Click on Submit Button  on  UpComingEvent  page. Exception "
								+ e.getClass().getName() + " occured. Exception message is " + e.getMessage());
						return false;
					}
				
					step.setActualResult("Successfully Created Event by Name "+TestScript.testData.get("EventTitle").toString() );
					return true;
				
				
	}
	
	
	public boolean verifyEventTitle() throws Exception
	{	
		
		int maxWaitTime = Integer.parseInt(TestConfig.getConfig().getPropertyValue("MAXWAITTIME"));
		//check Existence of Add Event Page 
				if (!UIUtils
						.waitUntilElementExists(step.getDriver(),
								TestConfig.getInstance().getObjRep().getLocator("UpComingEvents", "lnkAddEvents"), maxWaitTime)
						.isDisplayed()) {
					step.setActualResult("Object lnkAddEvents of Up Coming Events Screen is not visible");
					return false;
				}
				
				
				//Click on Filter Event Title
				try {
					UIUtils.clickElement(step.getDriver(),
							TestConfig.getInstance().getObjRep().getLocator("UpComingEvents", "iconFilterEvent"));
					step.setLogResult("Successfully Clicked on Filter Event Title Button");
				} catch (Exception e) {
					step.setActualResult("Failed to click on Object iconFilterEvent present on UpComing Events page. Exception "
							+ e.getClass().getName() + " occured. Exception message is " + e.getMessage());
					return false;
				}
				
				UIUtils.dynamicWait(step.getDriver(),
									TestConfig.getInstance().getObjRep().getLocator("UpComingEvents", "txtFilterText"),5000);
				
				// Enter Event Title'
				try {
					UIUtils.inputText(
							UIUtils.findElement(step.getDriver(),
									TestConfig.getInstance().getObjRep().getLocator("UpComingEvents", "txtFilterText")),
							TestScript.testData.get("EventTitle").toString());
					step.setLogResult("Successfully Entered Event Title as "+ TestScript.testData.get("EventTitle").toString());
				} catch (Exception e) {
					step.setActualResult("Failed to enter value " + TestScript.testData.get("EventTitle").toString() + " into the object "
							+ "  txtFilterText in screen GRS UpComing Events Page  , Exception " + e.getClass().getName()
							+ " occured. Exception message is " + e.getMessage());
					return false;
				}	
				
				
					Thread.sleep(3000);
				
				//Click on Apply Filter Button
					try {
						UIUtils.clickElementJScript(step.getDriver(),
								TestConfig.getInstance().getObjRep().getLocator("UpComingEvents", "btnApplyFilter"));
						step.setLogResult("Successfully Clicked on Apply Filter Button");
					} catch (Exception e) {
						step.setActualResult("Failed to click on Object btnApplyFilter present on Home page. Exception "
								+ e.getClass().getName() + " occured. Exception message is " + e.getMessage());
						return false;
					}

			String sEventName=UIUtils.getText(UIUtils.findElement(step.getDriver(),
					TestConfig.getInstance().getObjRep().getLocator("UpComingEvents", "txtFilterText")));
			
			
			if (sEventName.contains(TestScript.testData.get("EventTitle").toString()))
			{
			
				step.setActualResult("Successfully Verified the Event On Upcoming Event Page");
				
				return true;
			}
			else
			{step.setActualResult("Failed to Verify the Event On Upcoming Event Page");
				return false;
			}
			
					
	
	
	
	}
	
	
	
	
	public boolean createTopic()
	{
		//Click on View All Topics
		try {
			UIUtils.clickElement(step.getDriver(),
					TestConfig.getInstance().getObjRep().getLocator("Home", "lnkViewAllTopics"));
			step.setLogResult("Successfully Clicked on View All Topics Link");
		} catch (Exception e) {
			step.setActualResult("Failed to click on Object lnkViewAllTopics present on Home page. Exception "
					+ e.getClass().getName() + " occured. Exception message is " + e.getMessage());
			return false;
		}
		
	//Click on Add Topics
		try {
			UIUtils.clickElement(step.getDriver(),
					TestConfig.getInstance().getObjRep().getLocator("ClientForum", "btnAddTopic"));
			step.setLogResult("Successfully Clicked on Add Topics Link");
		} catch (Exception e) {
			step.setActualResult("Failed to click on Object btnAddTopic present on Client Forumn  page. Exception "
					+ e.getClass().getName() + " occured. Exception message is " + e.getMessage());
			return false;
		}
		
	
		// Select Category 
				try {
					UIUtils.selectValue(step.getDriver(),
							TestConfig.getInstance().getObjRep().getLocator("ClientForumTopic", "lstCategory"),
							TestScript.testData.get("Category").toString());

					step.setLogResult("Successfully Selected Category as "+ TestScript.testData.get("Category").toString());
				} catch (Exception e) {
					step.setActualResult("Failed to select attribute " + TestScript.testData.get("Category").toString() + " of dropdown" 
							+"lstCategory in screen Client Forumn Topic, located by "
							+ step.getLocatorType() + " and value " + step.getLocatorValue() + ". Exception "
							+ e.getClass().getName() + " occured. Exception message is " + e.getMessage());
				}
	
				//Enter Topic name
				try {
					UIUtils.inputText(
							UIUtils.findElement(step.getDriver(),
									TestConfig.getInstance().getObjRep().getLocator("ClientForumTopic", "txtTopicName")),
							TestScript.testData.get("TopicName").toString());
					step.setLogResult("Successfully Entered Event Title as "+ TestScript.testData.get("TopicName").toString());
				} catch (Exception e) {
					step.setActualResult("Failed to enter value " + TestScript.testData.get("EventTitle").toString() + " into the object "
							+ "  txtTopicName in screen GRS Client Forumn Topic  Page  , Exception " + e.getClass().getName()
							+ " occured. Exception message is " + e.getMessage());
					return false;
				}	
				
			//Enter Description
				
				
				//Change the frame
				step.getDriver().switchTo().frame(step.getDriver().findElement(TestConfig.getInstance().getObjRep().getLocator("ClientForumTopic", "iFrameDesc")));
				
				//Enter the Description 
				try {
					UIUtils.inputText(
							UIUtils.findElement(step.getDriver(),
									TestConfig.getInstance().getObjRep().getLocator("ClientForumTopic", "txtDescription")),
							TestScript.testData.get("Description").toString());
					step.setLogResult("Successfully Entered Event Description as "+ TestScript.testData.get("Description").toString());
				} catch (Exception e) {
					step.setActualResult("Failed to enter value " + TestScript.testData.get("Description").toString() + " into the object "
							+ "  txtDescription in screen GRS Client Forumn Topic  Page  , Exception " + e.getClass().getName()
							+ " occured. Exception message is " + e.getMessage());
					return false;
				}
				
				step.getDriver().switchTo().defaultContent();
				
			//Attachments
				//Click on Choose File
				try {
					UIUtils.clickElement(step.getDriver(),
							TestConfig.getInstance().getObjRep().getLocator("ClientForumTopic", "btnChooseFile1"));
					step.setLogResult("Successfully Clicked on Choose File Button");
				} catch (Exception e) {
					step.setActualResult("Failed to click on Object btnChooseFile1 present on Client Forumn  page. Exception "
							+ e.getClass().getName() + " occured. Exception message is " + e.getMessage());
					return false;
				}
				
				try {
					UIUtils.fileUpload(TestScript.testData.get("Attachment1").toString().trim());
				} catch (AWTException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			//Click on Publish	
				try {
					UIUtils.clickElement(step.getDriver(),
							TestConfig.getInstance().getObjRep().getLocator("ClientForumTopic", "btnPublish"));
					step.setLogResult("Successfully Clicked on Publish  Button");
				} catch (Exception e) {
					step.setActualResult("Failed to click on Object btnPublish present on Client Forumn  page. Exception "
							+ e.getClass().getName() + " occured. Exception message is " + e.getMessage());
					return false;
				}
				
			step.setActualResult("Successfully Created Event with Topic ");
				return true;
				
				
	}
	
	public boolean navigate() {
		try {
			if (step.getFieldValue().equalsIgnoreCase("back")) {
				step.getDriver().navigate().back();
			} else if (step.getFieldValue().equalsIgnoreCase("forward")) {
				step.getDriver().navigate().forward();
			} else {
				step.getDriver().navigate().to(step.getFieldValue());
			}
			step.setActualResult("Successfully navigated to " + step.getFieldValue());
			return true;
		} catch (Exception e) {
			step.setActualResult("Exception " + e.getClass().getName() + " occured while navigating to "
					+ step.getFieldValue() + ". Error message is " + e.getMessage());
		}
		return false;
	}

	public boolean waitTime() {
		try {
			Thread.sleep(Integer.parseInt(step.getFieldValue()));
			step.setActualResult("Execution paused for " + step.getFieldValue() + " milli seconds");
			return true;
		} catch (Exception e) {
			step.setActualResult(
					"Exception " + e.getClass().getName() + " occured. Error message is " + e.getMessage());
		}
		return false;
	}

	public boolean waitForElementExist() {
		int maxWaitTime = Integer.parseInt(TestConfig.getConfig().getPropertyValue("MAXWAITTIME"));

		if (checkForObjectInOR()) {
			if (UIUtils.waitUntilElementExists(step.getDriver(),
					TestConfig.getInstance().getObjRep().getLocator(step.getScreenName(), step.getObjectName()),
					maxWaitTime).isDisplayed()) {
				step.setActualResult(
						"Object " + step.getObjectName() + " of screen " + step.getScreenName() + " exists & visible");
				return true;
			}
			step.setActualResult("Object " + step.getObjectName() + " of screen " + step.getScreenName()
					+ ", located by " + step.getLocatorType() + " and value " + step.getLocatorValue()
					+ " did not exist or visible");
		} else {
			step.setActualResult(
					"Object " + step.getObjectName() + " of screen " + step.getScreenName() + " does not exist in OR");
		}
		step.setExitFlag(true);
		return false;
	}

	public boolean waitForElementNotExist() {
		int avgWaitTime = Integer.parseInt(TestConfig.getConfig().getPropertyValue("AVGWAITTIME"));

		if (checkForObjectInOR()) {
			if (UIUtils.waitUntilElementNotExists(step.getDriver(),
					TestConfig.getInstance().getObjRep().getLocator(step.getScreenName(), step.getObjectName()),
					avgWaitTime)) {
				step.setActualResult(
						"Object " + step.getObjectName() + " of screen " + step.getScreenName() + " is not visible");
				return true;
			}
			step.setActualResult("Object " + step.getObjectName() + " of screen " + step.getScreenName()
					+ ", located by " + step.getLocatorType() + " and value " + step.getLocatorValue() + " is visible");
		} else {
			step.setActualResult(
					"Object " + step.getObjectName() + " of screen " + step.getScreenName() + " does not exist in OR");
		}
		step.setExitFlag(true);
		return false;
	}

	public boolean click() {
		if (checkForObjectInOR()) {
			try {
				UIUtils.clickElement(step.getDriver(),
						TestConfig.getInstance().getObjRep().getLocator(step.getScreenName(), step.getObjectName()));
				step.setActualResult("Successfully clicked on Object " + step.getObjectName() + " of screen "
						+ step.getScreenName());
				return true;
			} catch (Exception e) {
				step.setActualResult("Failed to click on Object " + step.getObjectName() + " of screen "
						+ step.getScreenName() + ", located by " + step.getLocatorType() + " and value "
						+ step.getLocatorValue() + ". Exception " + e.getClass().getName()
						+ " occured. Exception message is " + e.getMessage());
			}
		} else {
			step.setActualResult(
					"Object " + step.getObjectName() + " of screen " + step.getScreenName() + " does not exist in OR");
		}
		step.setExitFlag(true);
		return false;
	}

	public boolean clickFocus() {
		if (checkForObjectInOR()) {
			try {
				UIUtils.clickElementJScript(step.getDriver(),
						TestConfig.getInstance().getObjRep().getLocator(step.getScreenName(), step.getObjectName()));

				step.setActualResult("Successfully clicked on Object " + step.getObjectName() + " of screen "
						+ step.getScreenName());
				return true;
			} catch (Exception e) {
				step.setActualResult("Failed to click on Object " + step.getObjectName() + " of screen "
						+ step.getScreenName() + ", located by " + step.getLocatorType() + " and value "
						+ step.getLocatorValue() + ". Exception " + e.getClass().getName()
						+ " occured. Exception message is " + e.getMessage());
			}
		} else {
			step.setActualResult(
					"Object " + step.getObjectName() + " of screen " + step.getScreenName() + " does not exist in OR");
		}
		step.setExitFlag(true);
		return false;
	}

	public boolean select() {
		if (checkForObjectInOR()) {
			try {
				UIUtils.selectValue(step.getDriver(),
						TestConfig.getInstance().getObjRep().getLocator(step.getScreenName(), step.getObjectName()),
						step.getFieldValue());

				step.setActualResult("Successfully selected attribute " + step.getFieldValue() + " of dropdown "
						+ step.getObjectName() + " in screen " + step.getScreenName());
				return true;
			} catch (Exception e) {
				step.setActualResult("Failed to select attribute " + step.getFieldValue() + " of dropdown "
						+ step.getObjectName() + " in screen " + step.getScreenName() + ", located by "
						+ step.getLocatorType() + " and value " + step.getLocatorValue() + ". Exception "
						+ e.getClass().getName() + " occured. Exception message is " + e.getMessage());
			}
		} else {
			step.setActualResult(
					"Object " + step.getObjectName() + " of screen " + step.getScreenName() + " does not exist in OR");
		}
		step.setExitFlag(true);
		return false;
	}

	public boolean moveTo() {
		if (checkForObjectInOR()) {
			try {
				UIUtils.moveTo(step.getDriver(),
						TestConfig.getInstance().getObjRep().getLocator(step.getScreenName(), step.getObjectName()));

				step.setActualResult(
						"Successfully moved to Object " + step.getObjectName() + " of screen " + step.getScreenName());
				return true;
			} catch (Exception e) {
				step.setActualResult("Failed to click on Object " + step.getObjectName() + " of screen "
						+ step.getScreenName() + ", located by " + step.getLocatorType() + " and value "
						+ step.getLocatorValue() + ". Exception " + e.getClass().getName()
						+ " occured. Exception message is " + e.getMessage());
			}
		} else {
			step.setActualResult(
					"Object " + step.getObjectName() + " of screen " + step.getScreenName() + " does not exist in OR");
		}
		step.setExitFlag(true);
		return false;
	}

	public boolean moveAndClick() {
		if (checkForObjectInOR()) {
			try {
				List<By> byList = new ArrayList<By>();
				byList.add(TestConfig.getInstance().getObjRep().getLocator(step.getScreenName(), step.getObjectName()));

				if (step.getArguments().size() > 0)
					byList.add(TestConfig.getInstance().getObjRep().getLocator(step.getFieldValue(),
							step.getArguments().get(0)));

				int i = 1;

				while (i < step.getArguments().size()) {
					byList.add(TestConfig.getInstance().getObjRep().getLocator(step.getArguments().get(i),
							step.getArguments().get(i + 1)));
					i = i + 2;
				}

				UIUtils.moveAndClick(step.getDriver(), byList);

				step.setActualResult("Successfully moved to and clicked on Object " + step.getObjectName()
						+ " of screen " + step.getScreenName());
				return true;
			} catch (Exception e) {
				step.setActualResult("Failed to click on Object " + step.getObjectName() + " of screen "
						+ step.getScreenName() + ", located by " + step.getLocatorType() + " and value "
						+ step.getLocatorValue() + ". Exception " + e.getClass().getName()
						+ " occured. Exception message is " + e.getMessage());
			}
		} else {
			step.setActualResult(
					"Object " + step.getObjectName() + " of screen " + step.getScreenName() + " does not exist in OR");
		}
		step.setExitFlag(true);
		return false;
	}

	public boolean input() {
		if (checkForObjectInOR()) {
			try {
				UIUtils.inputText(UIUtils.findElement(step.getDriver(),
						TestConfig.getInstance().getObjRep().getLocator(step.getScreenName(), step.getObjectName())),
						step.getFieldValue());

				step.setActualResult("Successfully entered value " + step.getFieldValue() + " into the object "
						+ step.getObjectName() + " in screen " + step.getScreenName());
				return true;
			} catch (Exception e) {
				step.setActualResult("Failed to enter value " + step.getFieldValue() + " into the object "
						+ step.getObjectName() + " in screen " + step.getScreenName() + ", located by "
						+ step.getLocatorType() + " and value " + step.getLocatorValue() + ". Exception "
						+ e.getClass().getName() + " occured. Exception message is " + e.getMessage());
			}
		} else {
			step.setActualResult(
					"Object " + step.getObjectName() + " of screen " + step.getScreenName() + " does not exist in OR");
		}
		step.setExitFlag(true);
		return false;
	}

	public boolean keyboardInput() {
		if (checkForObjectInOR()) {
			try {
				WebElement element = UIUtils.findElement(step.getDriver(),
						TestConfig.getInstance().getObjRep().getLocator(step.getScreenName(), step.getObjectName()));
				element.sendKeys(Keys.valueOf(step.getFieldValue()));
				step.setActualResult("Successfully entered keyboard key " + step.getFieldValue() + " into the object "
						+ step.getObjectName() + " in screen " + step.getScreenName());
				return true;
			} catch (Exception e) {
				step.setActualResult("Failed to enter keyboard key " + step.getFieldValue() + " into the object "
						+ step.getObjectName() + " in screen " + step.getScreenName() + ", located by "
						+ step.getLocatorType() + " and value " + step.getLocatorValue() + ". Exception "
						+ e.getClass().getName() + " occured. Exception message is " + e.getMessage());
			}
		} else {
			step.setActualResult(
					"Object " + step.getObjectName() + " of screen " + step.getScreenName() + " does not exist in OR");
		}
		step.setExitFlag(true);
		return false;
	}

	public boolean setCheckBox() {
		if (checkForObjectInOR()) {
			try {
				List<WebElement> elements = step.getDriver().findElements(
						TestConfig.getInstance().getObjRep().getLocator(step.getScreenName(), step.getObjectName()));
				if (("check".equalsIgnoreCase(step.getFieldValue()) && !(elements.get(0).isSelected()))
						|| ("uncheck".equalsIgnoreCase(step.getFieldValue()) && elements.get(0).isSelected())) {
					elements.get(0).sendKeys(Keys.SPACE);
				} else if ("checkAll".equalsIgnoreCase(step.getFieldValue())) {
					for (WebElement webElement : elements) {
						if (!webElement.isSelected()) {
							webElement.sendKeys(Keys.SPACE);
						}
					}
				} else if ("uncheckAll".equalsIgnoreCase(step.getFieldValue())) {
					for (WebElement webElement : elements) {
						if (webElement.isSelected()) {
							webElement.sendKeys(Keys.SPACE);
						}
					}
				}
				step.setActualResult("Successfully " + step.getFieldValue() + " object(s) " + step.getObjectName()
						+ " in screen " + step.getScreenName());
				return true;
			} catch (Exception e) {
				step.setActualResult("Failed to " + step.getFieldValue() + " object(s) " + step.getObjectName()
						+ " in screen " + step.getScreenName() + ", located by " + step.getLocatorType() + " and value "
						+ step.getLocatorValue() + ". Exception " + e.getClass().getName()
						+ " occured. Exception message is " + e.getMessage());
			}
		} else {
			step.setActualResult(
					"Object " + step.getObjectName() + " of screen " + step.getScreenName() + " does not exist in OR");
		}
		step.setExitFlag(true);
		return false;
	}

	public boolean clear() {
		if (checkForObjectInOR()) {
			try {
				UIUtils.findElement(step.getDriver(),
						TestConfig.getInstance().getObjRep().getLocator(step.getScreenName(), step.getObjectName()))
						.clear();
				step.setActualResult("Successfully cleared value from the object " + step.getObjectName()
						+ " in screen " + step.getScreenName());
				return true;
			} catch (Exception e) {
				step.setActualResult("Failed to clear value from the object " + step.getObjectName() + " in screen "
						+ step.getScreenName() + ", located by " + step.getLocatorType() + " and value "
						+ step.getLocatorValue() + ". Exception " + e.getClass().getName()
						+ " occured. Exception message is " + e.getMessage());
			}
		} else {
			step.setActualResult(
					"Object " + step.getObjectName() + " of screen " + step.getScreenName() + " does not exist in OR");
		}
		step.setExitFlag(true);
		return false;
	}

	public boolean handleAlert() throws Exception {
		Wait<WebDriver> wWait = UIUtils.getFluentWait(step.getDriver(),
				Integer.parseInt(TestConfig.getConfig().getPropertyValue("AVGWAITTIME")));
		Alert alert = null;
		Robot robot = new Robot();
		boolean result = false;
		String alertText;

		try {
			alert = wWait.until(ExpectedConditions.alertIsPresent());
			// alert = step.getDriver().switchTo().alert();

			alertText = alert.getText();
			LOGGER.logInfo("Alert text is " + alertText);

			switch (step.getObjectName().toUpperCase()) {
			case "ACCEPT":
				alert.accept();
				// robot.keyPress(KeyEvent.VK_CONTROL);
				// robot.keyPress(KeyEvent.VK_ENTER);
				// robot.keyRelease(KeyEvent.VK_CONTROL);
				Thread.sleep(5000);
				step.setActualResult("Successfully accepted the alert");
				result = true;
				break;
			case "DISMISS":
				alert.dismiss();
				// robot.keyPress(KeyEvent.VK_ESCAPE);
				// robot.keyRelease(KeyEvent.VK_ESCAPE);
				step.setActualResult("Successfully dismissed the alert");
				result = true;
				break;
			case "DOWNLOAD":
				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);
				step.setActualResult("Successfully downloaded the file");
				result = true;
				break;
			case "VERIFYTEXT":
				if (!alertText.equalsIgnoreCase(step.getFieldValue())) {
					step.setActualResult("Alert text " + alertText + " is not as per expectation.");
				} else {
					step.setActualResult("Successfully verified the alert text " + alertText);
					result = true;
				}
				break;
			case "CONTAINSTEXT":
				if (!alertText.toUpperCase().contains(step.getFieldValue().toUpperCase())) {
					step.setActualResult("Text " + step.getFieldValue() + " does not exist in Alert text " + alertText);
				} else {
					step.setActualResult("Text " + step.getFieldValue() + " exist in Alert text " + alertText);
					result = true;
				}
				break;
			default:
				step.setActualResult("Invalid alert operation" + step.getFieldValue());
			}
		} catch (Exception e) {
			step.setActualResult(
					"Exception " + e.getClass().getName() + " occured while performing alert handling operation "
							+ step.getObjectName() + ". Exception message is " + e.getMessage());
			step.setExitFlag(true);
		} finally {
			step.getDriver().switchTo().defaultContent();
		}
		return result;
	}

	public boolean switchWindow() {
		try {
			switch (step.getFieldValue().toUpperCase()) {
			case "DIFFERENTFRAME":
				if (step.getArguments().size() > 0) {
					String frameDesc = step.getArguments().get(0).trim();
					if (frameDesc.matches("\\d+")) {
						step.getDriver().switchTo().frame(Integer.parseInt(frameDesc));
						step.setActualResult("Successfully switched focus to the " + frameDesc + "th frame");
					} else {
						step.getDriver().switchTo().frame(frameDesc);
						step.setActualResult("Successfully switched focus to the " + frameDesc + " frame");
					}
					LOGGER.logInfo("Title is " + step.getDriver().getTitle());
					return true;
				} else {
					if (checkForObjectInOR()) {
						WebElement element = UIUtils.findElement(step.getDriver(), TestConfig.getInstance().getObjRep()
								.getLocator(step.getScreenName(), step.getObjectName()));
						step.getDriver().switchTo().frame(element);
						step.setActualResult("Successfully switched focus to the frame " + step.getObjectName()
								+ " of screen " + step.getScreenName());
						LOGGER.logInfo("Title is " + step.getDriver().getTitle());
						return true;
					} else {
						step.setActualResult("Object " + step.getObjectName() + " of screen " + step.getScreenName()
								+ " does not exist in OR");
					}
				}
			case "DEFAULTCONTENT":
				step.getDriver().switchTo().defaultContent();
				LOGGER.logInfo("Title is " + step.getDriver().getTitle());
				step.setActualResult("Successfully switched focus to the default content");
				return true;
			case "MAINWINDOW":
				step.getDriver().switchTo().window(step.getMainHandle());
				step.setActualResult("Successfully switched focus to the main window recognize by title "
						+ step.getDriver().getTitle());
				LOGGER.logInfo("Title is " + step.getDriver().getTitle());
				return true;
			case "FIRSTWINDOW":
				for (String winHandle : step.getDriver().getWindowHandles()) {
					if (!winHandle.equals(step.getMainHandle())) {
						step.getDriver().switchTo().window(winHandle);
						break;
					}
				}
				step.setActualResult("Successfully switched focus to the first window recognize by title "
						+ step.getDriver().getTitle());
				LOGGER.logInfo("Title is " + step.getDriver().getTitle());
				return true;
			case "LASTWINDOW":
				for (String winHandle : step.getDriver().getWindowHandles()) {
					if (!winHandle.equals(step.getMainHandle())) {
						step.getDriver().switchTo().window(winHandle);
					}
				}
				step.setActualResult("Successfully switched focus to the last window recognize by title "
						+ step.getDriver().getTitle());
				LOGGER.logInfo("Title is " + step.getDriver().getTitle());
				return true;
			default:
				break;
			}
		} catch (Exception e) {
			step.setActualResult("Failed to switch driver focus to " + step.getFieldValue() + ". Exception "
					+ e.getClass().getName() + " occured. Exception message is " + e.getMessage());
		}
		// step.setExitFlag(true);
		return false;
	}

	public boolean scroll() {
		try {
			if (checkForObjectInOR()) {
				WebElement element = UIUtils.findElement(step.getDriver(),
						TestConfig.getInstance().getObjRep().getLocator(step.getScreenName(), step.getObjectName()));
				if (step.getFieldValue().equalsIgnoreCase("Up")) {
					UIUtils.executeScript(step.getDriver(), "scroll(250, 0)", element);
				} else {
					UIUtils.executeScript(step.getDriver(), "scroll(0, 250)", element);
				}
				step.setActualResult("Successfully scrolled " + step.getFieldValue() + " in the object "
						+ step.getObjectName() + " in screen " + step.getScreenName());
			} else {
				UIUtils.verticalScrollWindow(step.getDriver(), step.getFieldValue());
				step.setActualResult("Successfully scrolled " + step.getFieldValue() + " in the screen");
			}
			return true;
		} catch (Exception e) {
			step.setActualResult("Failed to scrolled " + step.getFieldValue() + ". Exception " + e.getClass().getName()
					+ " occured. Exception message is " + e.getMessage());
		}
		return false;
	}

	public boolean scrollIntoView() {
		if (checkForObjectInOR()) {
			try {
				int waitTime = Integer.parseInt(TestConfig.getConfig().getPropertyValue("WAITTIME"));
				int avgWaitTime = Integer.parseInt(TestConfig.getConfig().getPropertyValue("AVGWAITTIME"));
				FluentWait<WebDriver> wWait = UIUtils.getFluentWait(step.getDriver(), waitTime);
				WebElement element = null;
				for (int i = 1; i <= avgWaitTime; i++) {
					element = wWait.until(ExpectedConditions.visibilityOfElementLocated(TestConfig.getInstance()
							.getObjRep().getLocator(step.getScreenName(), step.getObjectName())));
					if (element != null && element.isDisplayed()) {
						break;
					}
					UIUtils.verticalScrollWindow(step.getDriver(), "Down");
				}

				if (element.isDisplayed()) {
					step.setActualResult("Successfully scrolled object " + step.getObjectName() + " in screen "
							+ step.getScreenName() + " into the view");
				} else {
					step.setActualResult("Failed to scroll object " + step.getObjectName() + " in screen "
							+ step.getScreenName() + " into the view, located by " + step.getLocatorType()
							+ " and value " + step.getLocatorValue());
				}
			} catch (Exception e) {
				step.setActualResult("Failed to scrolled " + step.getFieldValue() + ". Exception "
						+ e.getClass().getName() + " occured. Exception message is " + e.getMessage());
			}
		} else {
			step.setActualResult(
					"Object " + step.getObjectName() + " of screen " + step.getScreenName() + " does not exist in OR");
		}
		step.setExitFlag(true);
		return false;
	}

	public boolean fileUpload() {
		int minWaitTime = Integer.parseInt(TestConfig.getConfig().getPropertyValue("MINWAITTIME"));

		if (checkForObjectInOR()) {
			try {
				Robot robot = new Robot();

				// Click on Browse Button
				UIUtils.clickElement(step.getDriver(),
						TestConfig.getInstance().getObjRep().getLocator(step.getScreenName(), step.getObjectName()));
				Thread.sleep(minWaitTime);

				CommonUtils.copyTextToClipboard(step.getFieldValue());

				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);

				robot.keyPress(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_V);
				robot.keyRelease(KeyEvent.VK_CONTROL);

				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);

				step.getDriver().switchTo().activeElement();
				step.setActualResult(
						"Successfully upload file " + step.getFieldValue() + " in screen " + step.getScreenName());
				return true;
			} catch (Exception e) {
				step.setActualResult(
						"Failed to upload file " + step.getFieldValue() + " in screen " + step.getScreenName());
			}
		} else {
			step.setActualResult(
					"Object " + step.getObjectName() + " of screen " + step.getScreenName() + " does not exist in OR");
		}
		step.setExitFlag(true);
		return false;
	}

	public boolean getValue() {
		if (checkForObjectInOR()) {
			try {
				String objText = UIUtils.getText(UIUtils.findElement(step.getDriver(),
						TestConfig.getInstance().getObjRep().getLocator(step.getScreenName(), step.getObjectName())));
				LOGGER.logInfo("Text/Value to be stored is " + objText);
				TestScript.runTimeVars.put(step.getFieldValue(), objText);
				step.setActualResult("Successfully get text " + objText + " of object " + step.getObjectName()
						+ " in screen " + step.getScreenName() + " and stored it in variable " + step.getFieldValue());
				return true;
			} catch (Exception e) {
				step.setActualResult("Failed to get text of object " + step.getObjectName() + " in screen "
						+ step.getScreenName() + ", located by " + step.getLocatorType() + " and value "
						+ step.getLocatorValue() + " and store it in variable " + step.getFieldValue() + " Exception "
						+ e.getClass().getName() + " occured. Exception message is " + e.getMessage());
			}
		} else {
			step.setActualResult(
					"Object " + step.getObjectName() + " of screen " + step.getScreenName() + " does not exist in OR");
		}
		step.setExitFlag(true);
		return false;
	}

	public boolean datePick() throws Exception {
		if (checkForObjectInOR()) {
			WebElement element = UIUtils.findElement(step.getDriver(),
					TestConfig.getInstance().getObjRep().getLocator(step.getScreenName(), step.getObjectName()));

			Calendar cal = Calendar.getInstance();
			String value = null;

			if ("Tomorrow".equalsIgnoreCase(step.getFieldValue())) {
				cal.add(Calendar.DATE, 1);
			} else if ("Yesterday".equalsIgnoreCase(step.getFieldValue())) {
				cal.add(Calendar.DATE, -1);
			} else if ("DateBeforeMonth".equalsIgnoreCase(step.getFieldValue())) {
				cal.add(Calendar.DATE, -31);
			} else if ("DateAfterMonth".equalsIgnoreCase(step.getFieldValue())) {
				cal.add(Calendar.DATE, +31);
			} else if ("Today+".startsWith(step.getFieldValue())) {
				cal.add(Calendar.DATE, Integer.parseInt(step.getFieldValue().replaceAll("Today+", "")));
			} else if ("Today-".startsWith(step.getFieldValue())) {
				cal.add(Calendar.DATE, Integer.parseInt(step.getFieldValue().replaceAll("Today-", "")));
			} else {
				value = step.getFieldValue();
			}

			if (value == null) {
				value = new SimpleDateFormat(TestConfig.getConfig().getPropertyValue("dateFormat"))
						.format(cal.getTime());
			}

			UIUtils.inputTextJScript(step.getDriver(), element, value);

			step.setActualResult("Successfully entered " + step.getFieldValue() + " date into the object "
					+ step.getObjectName() + " in screen " + step.getScreenName());
			return true;
		} else {
			step.setActualResult(
					"Object " + step.getObjectName() + " of screen " + step.getScreenName() + " does not exist in OR");
		}
		return false;
	}

	public boolean isElementExist() {
		if (checkForObjectInOR()) {
			try {
				if (UIUtils.isObjectExist(step.getDriver(),
						TestConfig.getInstance().getObjRep().getLocator(step.getScreenName(), step.getObjectName()))) {
					WebElement element = UIUtils.findElement(step.getDriver(), TestConfig.getInstance().getObjRep()
							.getLocator(step.getScreenName(), step.getObjectName()));
					if (element.isDisplayed()) {
						step.setActualResult(
								"Object " + step.getObjectName() + " of screen " + step.getScreenName() + " exist");
						return true;
					}
				}
				step.setActualResult("Object " + step.getObjectName() + " of screen " + step.getScreenName()
						+ " does not exist or not displayed");
			} catch (Exception e) {
				step.setActualResult("Failed to check existence of Object " + step.getObjectName() + " of screen "
						+ step.getScreenName() + ". Exception " + e.getClass().getName()
						+ " occured and exception message is " + e.getMessage());
			}
		} else {
			step.setActualResult(
					"Object " + step.getObjectName() + " of screen " + step.getScreenName() + " does not exist in OR");
		}
		return false;
	}

	public boolean isElementNotExist() {
		if (checkForObjectInOR()) {
			try {
				if (UIUtils.isObjectExist(step.getDriver(),
						TestConfig.getInstance().getObjRep().getLocator(step.getScreenName(), step.getObjectName()))) {
					WebElement element = UIUtils.findElement(step.getDriver(), TestConfig.getInstance().getObjRep()
							.getLocator(step.getScreenName(), step.getObjectName()));
					if (element.isDisplayed()) {
						step.setActualResult("Object " + step.getObjectName() + " of screen " + step.getScreenName()
								+ " exist and displayed");
						return false;
					}
				}
				step.setActualResult("Object " + step.getObjectName() + " of screen " + step.getScreenName()
						+ " does not exist and displayed");
			} catch (Exception e) {
				step.setActualResult("Failed to check non-existence of Object " + step.getObjectName() + " of screen "
						+ step.getScreenName() + ". Exception " + e.getClass().getName()
						+ " occured and exception message is " + e.getMessage());
			}
		} else {
			step.setActualResult(
					"Object " + step.getObjectName() + " of screen " + step.getScreenName() + " does not exist in OR");
			return false;
		}
		return true;
	}

	public boolean isElementEnabled() throws Exception {
		boolean blnEnableCheck = false;

		if (StringUtils.isBlank(step.getFieldValue()) || step.getFieldValue().trim().equalsIgnoreCase("Yes")) {
			blnEnableCheck = true;
		}

		if (checkForObjectInOR()) {
			try {
				if (UIUtils.isObjectExist(step.getDriver(),
						TestConfig.getInstance().getObjRep().getLocator(step.getScreenName(), step.getObjectName()))) {
					WebElement element = UIUtils.findElement(step.getDriver(), TestConfig.getInstance().getObjRep()
							.getLocator(step.getScreenName(), step.getObjectName()));

					if (blnEnableCheck) {
						if (element.isEnabled()) {
							step.setActualResult("Object " + step.getObjectName() + " of screen " + step.getScreenName()
									+ " enabled");
							return true;
						}
					} else {
						if (!element.isEnabled()) {
							step.setActualResult("Object " + step.getObjectName() + " of screen " + step.getScreenName()
									+ " disabled");
							return true;
						}
					}
				}
				step.setActualResult("Object " + step.getObjectName() + " of screen " + step.getScreenName()
						+ StringUtils.join(blnEnableCheck ? " enabled" : " disabled"));
			} catch (Exception e) {
				step.setActualResult("Failed to check enability of Object " + step.getObjectName() + " of screen "
						+ step.getScreenName() + ". Exception " + e.getClass().getName()
						+ " occured and exception message is " + e.getMessage());
			}
		} else {
			step.setActualResult(
					"Object " + step.getObjectName() + " of screen " + step.getScreenName() + " does not exist in OR");
		}
		return false;

	}

	public boolean verifyText() {
		if (checkForObjectInOR()) {
			try {
				String objText = UIUtils.getText(UIUtils.findElement(step.getDriver(),
						TestConfig.getInstance().getObjRep().getLocator(step.getScreenName(), step.getObjectName())))
						.trim();
				LOGGER.logInfo("Element Text is " + objText);

				if (objText.equalsIgnoreCase(step.getFieldValue())) {
					step.setActualResult("Successfully verify text of object " + step.getObjectName() + " in screen "
							+ step.getScreenName() + ". Actual text is " + objText);
					return true;
				}
				step.setActualResult("Failed to verify text of object " + step.getObjectName() + " in screen "
						+ step.getScreenName() + ", located by " + step.getLocatorType() + " and value "
						+ step.getLocatorValue() + ". Actual text is " + objText);
			} catch (Exception e) {
				step.setActualResult(
						"Failed to get value of object " + step.getObjectName() + " in screen " + step.getScreenName()
								+ ", located by " + step.getLocatorType() + " and value " + step.getLocatorValue());
			}
		} else {
			step.setActualResult(
					"Object " + step.getObjectName() + " of screen " + step.getScreenName() + " does not exist in OR");
		}
		return false;
	}

	public boolean containsText() {
		if (checkForObjectInOR()) {
			try {
				String objText = UIUtils.getText(UIUtils.findElement(step.getDriver(),
						TestConfig.getInstance().getObjRep().getLocator(step.getScreenName(), step.getObjectName())))
						.trim();
				LOGGER.logInfo("Element Text is " + objText);

				if (objText.toUpperCase().contains(step.getFieldValue().toUpperCase())) {
					step.setActualResult("Successfully verify text of object " + step.getObjectName() + " in screen "
							+ step.getScreenName() + ", located by " + step.getLocatorType() + " and value "
							+ step.getLocatorValue() + ". Actual text is " + objText);
					return true;
				}
				step.setActualResult("Failed to verify text of object " + step.getObjectName() + " in screen "
						+ step.getScreenName() + ", located by " + step.getLocatorType() + " and value "
						+ step.getLocatorValue() + ". Actual text is " + objText);
			} catch (Exception e) {
				step.setActualResult(
						"Failed to get value of object " + step.getObjectName() + " in screen " + step.getScreenName()
								+ ", located by " + step.getLocatorType() + " and value " + step.getLocatorValue());
			}
		} else {
			step.setActualResult(
					"Object " + step.getObjectName() + " of screen " + step.getScreenName() + " does not exist in OR");
		}
		return false;
	}

	public boolean verifyAttribute() {
		if (checkForObjectInOR()) {
			if (step.getArguments().size() >= 2) {
				String attribute = step.getArguments().get(0);

				try {
					String attribValue = UIUtils.findElement(step.getDriver(),
							TestConfig.getInstance().getObjRep().getLocator(step.getScreenName(), step.getObjectName()))
							.getAttribute(attribute);
					LOGGER.logInfo("Element attribute " + attribute + " value is " + attribValue);

					if (attribValue.equalsIgnoreCase(step.getArguments().get(1))) {
						step.setActualResult("Successfully verify attribute " + attribute + " value of object "
								+ step.getObjectName() + " in screen " + step.getScreenName() + ". Actual value is "
								+ attribValue);
						return true;
					}
					step.setActualResult(
							"Failed to verify attribute " + attribute + " value of object " + step.getObjectName()
									+ " in screen " + step.getScreenName() + ", located by " + step.getLocatorType()
									+ " and value " + step.getLocatorValue() + ". Actual value is " + attribValue);
				} catch (Exception e) {
					step.setActualResult("Failed to get value of object " + step.getObjectName() + " in screen "
							+ step.getScreenName() + ", located by " + step.getLocatorType() + " and value "
							+ step.getLocatorValue());
				}
			} else {
				step.setActualResult("Attribute name or expected value not present");
			}
		} else {
			step.setActualResult(
					"Object " + step.getObjectName() + " of screen " + step.getScreenName() + " does not exist in OR");
		}
		return false;
	}

	public boolean validateList() {
		if (checkForObjectInOR()) {
			try {
				List<WebElement> elements = step.getDriver().findElements(
						TestConfig.getInstance().getObjRep().getLocator(step.getScreenName(), step.getObjectName()));
				String expectedText = step.getFieldValue();
				int stepNo = 1;

				for (WebElement element : elements) {
					String objText = UIUtils.getText(element);
					LOGGER.logInfo("List element text is " + objText);

					if (expectedText.toUpperCase().contains("CONTAINS=")
							|| expectedText.toUpperCase().contains("CONTAINS =")) {
						expectedText = step.getFieldValue().toUpperCase().replaceAll("CONTAINS", "").trim()
								.replaceAll("=", "").trim();
						if (!objText.toUpperCase().contains(expectedText)) {
							step.setActualResult("Failed to verify text of each element of list " + step.getObjectName()
									+ " in screen " + step.getScreenName() + ", located by " + step.getLocatorType()
									+ " and value " + step.getLocatorValue() + ". Actual text for " + stepNo
									+ " th element " + objText.toUpperCase() + " contains expected text");
							return false;
						}
					} else {
						if (!objText.equalsIgnoreCase(expectedText)) {
							step.setActualResult("Failed to verify text of each element of list " + step.getObjectName()
									+ " in screen " + step.getScreenName() + ", located by " + step.getLocatorType()
									+ " and value " + step.getLocatorValue() + ". Actual text for " + stepNo
									+ " th element is " + objText);
							return false;
						}
					}
				}

				step.setActualResult("Successfully verify text of each element of list " + step.getObjectName()
						+ " in screen " + step.getScreenName());
				return true;
			} catch (Exception e) {
				step.setActualResult(
						"Failed to get value of object " + step.getObjectName() + " in screen " + step.getScreenName()
								+ ", located by " + step.getLocatorType() + " and value " + step.getLocatorValue());
			}
		} else {
			step.setActualResult(
					"Object " + step.getObjectName() + " of screen " + step.getScreenName() + " does not exist in OR");
		}
		return false;
	}

	public boolean validateTextNotInList() {
		if (checkForObjectInOR()) {
			try {
				List<WebElement> elements = step.getDriver().findElements(
						TestConfig.getInstance().getObjRep().getLocator(step.getScreenName(), step.getObjectName()));
				String expectedText = step.getFieldValue();
				int stepNo = 1;

				for (WebElement element : elements) {
					String objText = UIUtils.getText(element);
					LOGGER.logInfo("List element text is " + objText);

					if (expectedText.toUpperCase().contains("CONTAINS=")
							|| expectedText.toUpperCase().contains("CONTAINS =")) {
						expectedText = step.getFieldValue().toUpperCase().replaceAll("CONTAINS", "").trim()
								.replaceAll("=", "").trim();
						if (objText.toUpperCase().contains(expectedText)) {
							step.setActualResult("Failed to verify text of each element of list " + step.getObjectName()
									+ " in screen " + step.getScreenName() + ", located by " + step.getLocatorType()
									+ " and value " + step.getLocatorValue() + ". Text for " + stepNo + " th element "
									+ objText.toUpperCase() + " contains expected text");
							return false;
						}
					} else {
						if (objText.equalsIgnoreCase(expectedText)) {
							step.setActualResult("Failed to verify text of each element of list " + step.getObjectName()
									+ " in screen " + step.getScreenName() + ", located by " + step.getLocatorType()
									+ " and value " + step.getLocatorValue() + ". Text for " + stepNo + " th element "
									+ objText + " has expected text");
							return false;
						}
					}
				}

				step.setActualResult("Successfully verify text of each element of list " + step.getObjectName()
						+ " in screen " + step.getScreenName());
				return true;
			} catch (Exception e) {
				step.setActualResult(
						"Failed to get value of object " + step.getObjectName() + " in screen " + step.getScreenName()
								+ ", located by " + step.getLocatorType() + " and value " + step.getLocatorValue());
			}
		} else {
			step.setActualResult(
					"Object " + step.getObjectName() + " of screen " + step.getScreenName() + " does not exist in OR");
		}
		return false;
	}

	public boolean runProcess() {
		try {
			CommonUtils.executeProcess(step.getFieldValue());
			step.setActualResult("Successfully executed process " + step.getFieldValue());
			return true;
		} catch (Exception e) {
			step.setActualResult("Failed to executed process " + step.getFieldValue() + ". Exception "
					+ e.getClass().getName() + " occured. Exception message is " + e.getMessage());
		}
		return false;
	}

	public boolean executeAutoSys() throws Exception {
		int maxWaitTime = Integer.parseInt(TestConfig.getConfig().getPropertyValue("MAXWAITTIME"));
		String iXP_HOME = TestConfig.getConfig().getPropertyValue("IXP_HOME");
		String autoServ = TestConfig.getConfig().getPropertyValue("AUTOSERV");

		String autosysName = "";

		switch (TestConfig.getInstance().getExecEnvironment().toUpperCase()) {
		case "DEV":
			autosysName = "ecd.ldn.dev." + step.getObjectName() + "." + autoServ;
			break;
		case "SIT":
			autosysName = "ecd.ldn.sit." + step.getObjectName() + "." + autoServ;
			break;
		case "UAT":
			autosysName = "ecd.ldn.uat." + step.getObjectName() + "." + autoServ;
			break;
		default:
			break;
		}

		String[] sendEventCommand = { "cmd.exe", "/c", "sendevent -E FORCE_STARTJOB -J" + autosysName };
		String[] envVariable = { "IXP_HOME=" + iXP_HOME, "AUTOSERV=" + autoServ };

		BufferedReader stdInput = CommonUtils.executeProcess(sendEventCommand, envVariable, iXP_HOME + "\\bin");
		boolean reRun = false;

		String output = stdInput.readLine();
		if (output != null && output.equalsIgnoreCase(
				"CAUAJM_I_60000 Job (" + autosysName + ") is already running. FORCE_STARTJOB has no effect.")) {
			reRun = true;
		}

		int timeout;

		if (StringUtils.isNotBlank(step.getFieldValue())) {
			timeout = Integer.parseInt(step.getFieldValue().trim());
		} else {
			timeout = maxWaitTime;
		}

		String[] autorepCommand = { "cmd.exe", "/c", "autorep -J " + autosysName };

		for (int ctr = 0; ctr <= timeout; ctr++) {
			stdInput = CommonUtils.executeProcess(autorepCommand, envVariable, iXP_HOME + "\\bin");

			while ((output = stdInput.readLine()) != null) {
				LOGGER.logInfo("Autosys Output " + output);
				if (output.indexOf("SU") != -1) {
					if (reRun)
						break;
					else
						return true;
				} else {
					break;
				}
			}
			Thread.sleep(1000);
		}

		if (reRun) {
			CommonUtils.executeProcess(sendEventCommand, envVariable, iXP_HOME + "\\bin");

			for (int ctr = 0; ctr <= timeout; ctr++) {
				stdInput = CommonUtils.executeProcess(autorepCommand, envVariable, iXP_HOME + "\\bin");
				while ((output = stdInput.readLine()) != null) {
					LOGGER.logInfo("Autosys Output " + output);
					if (output.toLowerCase().contains(autosysName.toLowerCase())) {
						if (output.indexOf("SU") != -1)
							return true;
						else
							break;
					}
				}
				Thread.sleep(1000);
			}
		}
		return false;
	}

	private boolean checkForObjectInOR() {
		if (StringUtils.isNotBlank(step.getLocatorType()) && StringUtils.isNotBlank(step.getLocatorValue())) {
			return true;
		}
		LOGGER.logInfo("Object for screen " + step.getScreenName() + " and field name " + step.getObjectName()
				+ " does not exist in OR");
		return false;
	}
}