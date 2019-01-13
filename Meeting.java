package MeetingRecorder;

import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import MeetingRecorder.enumsClass.tagModes;

public class Meeting extends EditFuncs {
	
	GlobalFuncs			testFuncs;
	GlobalVars 			testVars;
	
	/**
	*  Default constructor
	*/
	public Meeting(GlobalFuncs testFuncs, GlobalVars testVars){
				
		super(testFuncs, testVars);
		this.testFuncs = testFuncs;
		this.testVars  = testVars;	
	}
	
	/**
	*  Interface for Create a meeting via Import when edit option is default
	*  @param driver  	  - given WebDriver driver
	*  @param subject     - given meeting subject
	*  @param mp4Path 	  - given mp4 file path
	*  @param maxWaitTime - given max waiting time for Meeting for being in Processing mode
	*/ 
	public void createMeetingAfterProcMode(WebDriver driver, String subject, String mp4Path, int maxWaitTime) {
	
		createMeetingAfterProcMode(driver, subject, mp4Path, maxWaitTime, true);
	}
	
	/**
	*  Create a meeting via Import and publish it if needed
	*  @param driver  	  - given WebDriver driver
	*  @param subject     - given meeting subject
	*  @param mp4Path 	  - given mp4 file path
	*  @param maxWaitTime - given max waiting time for Meeting for being in Processing mode
	*  @param isEdit	  - given flag for identify if the Meeting should be at Edit state
	*/ 
	public void createMeetingAfterProcMode(WebDriver driver, String subject, String mp4Path, int maxWaitTime, Boolean isEdit) {

		int i = 0;

		// Create a Meeting without Save changes while the Meeting still at Processing Mode
		testFuncs.myDebugPrinting("Create a Meeting without Save changes while the Meeting still at Processing Mode", enumsClass.logModes.NORMAL);	  	
		testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_IMPORT);
		testFuncs.mySendKeys(driver, By.id("subject"), subject, 2000);  	
		testFuncs.myClick(driver, By.xpath("/html/body/div[3]/div[3]/div/div[2]/table/tbody/tr[1]/td[3]/div/button"), 5000);  		
		testFuncs.verifyStrByXpath(driver, "/html/body/div[3]/div[3]/div/div[2]/table/tbody/tr[2]/td/div/div[1]/h3", "Upload Meeting Content"); 		
		testFuncs.myDebugPrinting("path - " + mp4Path, enumsClass.logModes.MINOR);	  		
		driver.findElement(By.name("file[]")).sendKeys(mp4Path);
		testFuncs.myWait(1000);		
		
		// Wait for file upload
		testFuncs.myDebugPrinting("Wait for file upload", enumsClass.logModes.MINOR);	  	
		while (driver.findElement(By.tagName("body")).getText().contains("Upload Meeting Content")) {
			
			testFuncs.myWait(10000);
			testFuncs.myDebugPrinting(((++i) * 10) + ". still wait for upload", enumsClass.logModes.DEBUG);
			testFuncs.myAssertTrue("Timeout excceds !! <i- " + i + ">", i < 200);
		}
		testFuncs.myWait(2000);
		
		// Verify Meeting
		testFuncs.myDebugPrinting("Verify Meeting", enumsClass.logModes.MINOR);
		testFuncs.pressHomeIcon(driver);
	    verifyMeeting(driver, subject);
	    testFuncs.myAssertTrue("Processing of Meeting was not ended after " + maxWaitTime + " seconds !!", waitForProcessingEnd(driver, maxWaitTime));
	    
	    // Publish meeting if needed
	    if (isEdit) {
	    	
			testFuncs.myDebugPrinting("Publish meeting", enumsClass.logModes.MINOR);		
			verifyMeetingEditState(driver, isEdit);    		
			publishMeeting(driver, subject);    		
	    } else {
	    	
			testFuncs.myDebugPrinting("Publish meeting was not needed", enumsClass.logModes.MINOR);
	    }
	}
	
	/**
	*  Publish the meeting
	*  @param driver  	   - given WebDriver driver
	*  @param meettingName - given Meeting name
	*/ 
	public void publishMeeting(WebDriver driver, String meetingName) {

		// Publish the meeting
		testFuncs.myDebugPrinting("Publish the meeting <" + meetingName + ">", enumsClass.logModes.MINOR);	  	
	    verifyMeeting(driver, meetingName);	
		testFuncs.myClick(driver, By.xpath("//*[@id='example']/tbody[2]/tr/td[2]/div[2]/button"), 2000);	
		testFuncs.verifyStrByXpath(driver, "/html/body/div[4]/div[3]/div/div/div[1]/span[1]" , "Editor Panel"); 
		testFuncs.verifyStrByXpath(driver, "/html/body/div[4]/div[3]/div/div/div[1]/span[2]" , meetingName.toUpperCase()); 		
		testFuncs.myClick(driver, By.xpath("/html/body/div[4]/div[3]/div/div/div[2]/div/ul/li[3]/div/button[2]"), 2000);  		
		
		// Verify the Publish
		testFuncs.myDebugPrinting("Verify the Publish", enumsClass.logModes.MINOR);	  	
		testFuncs.pressHomeIcon(driver);
	    verifyMeeting(driver, meetingName);
    	verifyMeetingEditState(driver, false);
	}

	/**
	*  Verify the icon of the Meeting
	*  @param driver  	  - given WebDriver driver
	*  @param isEdit	  - given flag for identify if the Meeting should be at Edit state or not
	*/ 
	private void verifyMeetingEditState(WebDriver driver, Boolean isEdit) {
		
		// Verify the icon of the Meeting
		testFuncs.myDebugPrinting("Verify the icon of the Meeting", enumsClass.logModes.MINOR);	  	
		String imgEditName = testVars.getMeetingEditIcon().replaceAll(" ", "%20");
		testFuncs.myDebugPrinting("imgEditName - " + imgEditName, enumsClass.logModes.DEBUG);
		if (isEdit) {
			
			String currImgName = driver.findElement(By.xpath("//*[@id='example']/tbody[2]/tr/td[2]/div[2]/button/img")).getAttribute("src");
			testFuncs.myDebugPrinting("currImgName - " + currImgName, enumsClass.logModes.DEBUG);
			testFuncs.myAssertTrue("isEdit - TRUE, but <currImgName - " + currImgName + ">", currImgName.contains(imgEditName));	
		} else {
			
			String currImgName = driver.findElement(By.xpath("//*[@id='example']/tbody[2]/tr/td[2]/span")).getText();
			testFuncs.myDebugPrinting("currImgName - " + currImgName, enumsClass.logModes.DEBUG);
			testFuncs.myAssertTrue("isEdit - FALSE, but <currImgName - " + currImgName + ">", !currImgName.contains(imgEditName));	
		}
	}

	/**
	*  Wait for Processing end
	*  @param driver - given WebDriver driver
	*  @param state	 - if processing ended after no more than 240 seconds (TRUE) or not (FALSE)
	*  @return TRUE (processing ended on given time) or not. 
	*/ 	
	public boolean waitForProcessingEnd333(WebDriver driver) {
		
		// Wait for end of Processing a Meeting
		String processState;
		for (int i = 0; i < 240; ++i) {
		
			processState = driver.findElement(By.xpath("//*[@id='example']/tbody[2]/tr/td[2]")).getText();
			testFuncs.myDebugPrinting(i*10 + ". processState - " + processState, enumsClass.logModes.DEBUG);
			if (!processState.contains("Processing")) {
				
				testFuncs.myWait(1000);
				return true;
			}
			testFuncs.myWait(10000);
		}
		
		return false;
	}
	
	/**
	*  Wait for Processing end
	*  @param driver  - given WebDriver driver
	*  @param state	  - if processing ended after no more than given time
	*  @param maxTime - max time for waiting
	*  @return TRUE (processing ended on given time) or not. 
	*/ 	
	public boolean waitForProcessingEnd(WebDriver driver, int maxTime) {
		
		// Wait for end of Processing a Meeting
		testFuncs.myDebugPrinting("Wait for end of Processing a Meeting", enumsClass.logModes.MINOR);
		for (int i = 0; i < maxTime; ++i) {
			
			if (!driver.findElement(By.tagName("body")).getText().contains("Processing")) {
				
				testFuncs.myWait(1000);
				return true;
				
			}
			testFuncs.myDebugPrinting((i*10) + ". processState - Processing", enumsClass.logModes.DEBUG);
			testFuncs.myWait(10000);
		}
		
		return false;
	}

	/**
	*  Create a meeting via Import without wait for Processing mode for finish
	*  @param driver           - given WebDriver driver
	*  @param subject 		   - given meeting subject
	*  @param mp4path		   - given mp4-file path
	*  @param isReturnHomepage - given flag indicates if to return main-menu (true) or not
	*/ 
	public void createMeeting(WebDriver driver, String subject, String mp4path, Boolean isReturnHomepage) {
		
		int i = 0;
		
		// Create a Meeting from Import
		testFuncs.myDebugPrinting("Create a Meeting from Import", enumsClass.logModes.NORMAL);
		testFuncs.verifyStrByXpath(driver, "/html/body/div[3]/div[3]/div/div[1]/h2"							  , "IMPORT MEETING");
		testFuncs.verifyStrByXpath(driver, "/html/body/div[3]/div[3]/div/div[2]/table/tbody/tr[1]/td[1]/label", "Subject");	
		testFuncs.mySendKeys(driver, By.id("subject"), subject, 2000);  	
		testFuncs.myClick(driver, By.xpath("/html/body/div[3]/div[3]/div/div[2]/table/tbody/tr[1]/td[3]/div/button"), 5000);  
		testFuncs.verifyStrByXpath(driver, "/html/body/div[3]/div[3]/div/div[2]/table/tbody/tr[2]/td/div/div[1]/h3", "Upload Meeting Content"); 		
		testFuncs.myDebugPrinting("mp4path - " + mp4path, enumsClass.logModes.DEBUG);	  						
		driver.findElement(By.name("file[]")).sendKeys(mp4path);	
		testFuncs.myWait(5000);
		
		// Wait for file upload
		testFuncs.myDebugPrinting("Wait for file upload", enumsClass.logModes.MINOR);	  	
		while (driver.findElement(By.tagName("body")).getText().contains("Upload Meeting Content")) {
			
			testFuncs.myWait(10000);
			testFuncs.myDebugPrinting(((++i) * 10) + ". still wait for upload", enumsClass.logModes.DEBUG);
			testFuncs.myAssertTrue("Timeout excceds !! <i- " + i + ">", i < 200);
		}
		testFuncs.myWait(1000);
		
		// Return to homepage if needed
		if (isReturnHomepage) {	  
		
			testFuncs.myDebugPrinting("Return Home page", enumsClass.logModes.MINOR);	  
			testFuncs.pressHomeIcon(driver);      
		} 
	}	
	
	/**
	*  Verify a meeting via search
	*  @param driver  	  - given WebDriver driver
	*  @param meetingName - given meeting subject
	*/
	public void verifyMeeting(WebDriver driver, String meetingName) {
		    
		// Verify a meeting via search
		testFuncs.myDebugPrinting("Verify a meeting via search", enumsClass.logModes.NORMAL);
		driver.findElement(By.id("searchInput")).clear();
		testFuncs.myWait(1000);
		testFuncs.mySendKeys(driver, By.id("searchInput"), "subject:\"" + meetingName + "\"", 2000);
	    testFuncs.myClick(driver, By.xpath("//*[@id='form-user_v1']/div[1]/div[1]/div[2]/button/i"), 2000);
	    if (driver.findElement(By.xpath("//*[@id='form-user_v1']/div[1]")).getAttribute("class").contains("backdrop result")) {
	    	
			testFuncs.myDebugPrinting("<backdrop result> detected !!", enumsClass.logModes.MINOR);
			driver.findElement(By.id("searchInput")).sendKeys(Keys.ENTER);
			testFuncs.myWait(2500);	
		    testFuncs.myClick(driver, By.xpath("//*[@id='form-user_v1']/div[1]/div[1]/div[2]/button/i"), 2000);
	    }
	    
	    // Verify the search
	    testFuncs.myAssertTrue("Meeting <" + meetingName + "> was not detected !!", !driver.findElement(By.tagName("body")).getText().contains("No meetings to display"));
	    testFuncs.verifyStrBy(driver, By.xpath("//*[@id='myedittd']/span"), meetingName);
	    testFuncs.myDebugPrinting("The meeting <" + meetingName + "> was detected successfully !!", enumsClass.logModes.MINOR);
	}
	
	/**
	*  Verify a meeting via search by given filter
	*  @param driver - given WebDriver driver
	*  @param filter - given filter
	*/ 
	public void verifyMeetingByGivenFilter(WebDriver driver, String filter) {
					  
		// Verify meeting by given filter	
		testFuncs.myDebugPrinting("Verify meeting by given filter", enumsClass.logModes.NORMAL);
		testFuncs.myDebugPrinting("filter - " + filter			  , enumsClass.logModes.MINOR);		
		driver.findElement(By.id("searchInput")).clear();			
		testFuncs.myWait(2000);	
		testFuncs.mySendKeys(driver, By.id("searchInput"), filter, 2000);		
		testFuncs.myClick(driver, By.xpath("//*[@id='form-user_v1']/div[1]/div[1]/div[2]/button/i"), 2000);		    	
		if (driver.findElement(By.xpath("//*[@id='form-user_v1']/div[1]")).getAttribute("class").contains("backdrop result")) {
			    				
			testFuncs.myDebugPrinting("<backdrop result> detected !!", enumsClass.logModes.MINOR);		
			driver.findElement(By.id("searchInput")).sendKeys(Keys.ENTER);		
			testFuncs.myWait(2500);		
			testFuncs.myClick(driver, By.xpath("//*[@id='form-user_v1']/div[1]/div[1]/div[2]/button/i"), 2000);    
		}
	}
	
	/**
	*  Add an Action to selected meeting
	*  @param driver     - given WebDriver driver
	*  @param actiondata - given Action data
	*  @param i    		 - given action index (ACTION - 2, NOTE - 1)
	*  @param iconName	 - given icon name
	*  @return add time	 - estimated add time
	*/
	public String addAction(WebDriver driver, String actionData, int i, String iconName) {
		
		String addTime = null;
		
		// Add Action
		testFuncs.myDebugPrinting("Add Action", enumsClass.logModes.NORMAL);
		testFuncs.myClick(driver, By.xpath("//*[@id='tab2details']/div[2]/div/div/table/tbody/tr[2]/td[1]/div/div")			      ,  500);
		testFuncs.myClick(driver, By.xpath("//*[@id='tab2details']/div[2]/div/div/table/tbody/tr[2]/td[2]/div/div/div[" + i + "]"),  500);
		testFuncs.mySendKeys(driver, By.xpath("//*[@id='tab2details']/div[2]/div/div/table/tbody/tr[1]/td/textarea"), actionData,  500);  		
		testFuncs.myClick(driver, By.xpath("//*[@id='tab2details']/div[2]/div/div/table/tbody/tr[2]/td[5]/div") 			  , 1000);
				
		// Verify the Add action
		testFuncs.myDebugPrinting("Verify the Add action", enumsClass.logModes.MINOR);
		int tableRows = driver.findElement(By.xpath("//*[@id='tab2details']/div[1]/div/div")).findElements(By.tagName("div")).size() / 2;
		int idx 	  = testFuncs.getIdxFromTable(driver, By.xpath("//*[@id='tab2details']/div[1]"), actionData);
		testFuncs.myDebugPrinting("tableRows - " + tableRows, enumsClass.logModes.DEBUG);
		testFuncs.myAssertTrue("Action <" + actionData + "> was not created successfully !", verifyActionNotesTags(driver, actionData, iconName, tableRows));
		
		// Return Add-Action time
		testFuncs.myDebugPrinting("Return Add-Action time", enumsClass.logModes.MINOR);
		if (tableRows == 1) {	
			addTime = driver.findElement(By.xpath("//*[@id='tab2details']/div[1]/div/div/div/table/tbody/tr[2]/td[2]")).getText();				
		} else {	
			addTime = driver.findElement(By.xpath("//*[@id='tab2details']/div[1]/div/div/div[" + idx + "]/table/tbody/tr[2]/td[2]")).getText();	
		}
		testFuncs.myDebugPrinting("Returned  addTime - " + addTime, enumsClass.logModes.MINOR);
		return addTime;
	}
	
	/**
	*  Add an  Action to selected meeting without verify
	*  @param  driver      - given WebDriver driver
	*  @param  actiondata  - given Action data
	*  @param  i    	   - given action index (ACTION - 1, NOTE - 2 or TAG - 3)
	 * @return currDisTime - return action add time
	*/
	public String addAction(WebDriver driver, String actionData, int i) {
		
		// Add Action
		testFuncs.myDebugPrinting("Add Action", enumsClass.logModes.NORMAL);	
		testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO_ADD_ACTION_NOTE_TAG);
		
		if (i == enumsClass.meetingActType.TAG.getMode()) {
			
			testFuncs.myDebugPrinting("Add tag", enumsClass.logModes.MINOR);	  
			Select tagNames = new Select(driver.findElement(By.id("tag-ui-tag-id")));
			testFuncs.myWait(2000);
			tagNames.selectByVisibleText(actionData); 
			Select tagModes = new Select(driver.findElement(By.id("tag-ui-visibility-id")));
			testFuncs.myWait(2000);
			tagModes.selectByVisibleText(enumsClass.tagModes.PUBLIC.getMode()); 
			
		} else {
			
			testFuncs.myDebugPrinting("Add Action / Note", enumsClass.logModes.MINOR);	  
			testFuncs.mySendKeys(driver, By.xpath("//*[@id='tab2view']/div/div/table[" + i + "]/tbody/tr[3]/td/textarea"), actionData, 1000);    	  	
		}
		testFuncs.myClick(driver, By.xpath("//*[@id='tab2view']/div/div/table["    + i + "]/tbody/tr[4]/td/button"), 100);
		
		// Return Add-Action time
		testFuncs.myDebugPrinting("Return Add-Action time", enumsClass.logModes.MINOR);	  
		String currDisTime = driver.findElement(By.xpath("//*[@id='tab2view']/div/div/table[1]/tbody/tr[2]/td/span")).getText();
		String[] parts = currDisTime.split(":");
		currDisTime = parts[1] + ":" + parts[2];
		testFuncs.myDebugPrinting("Action add time - <" + currDisTime + ">");
		
		return currDisTime;
	}	
		
	/**
	*  Verify if property exists at meeting menu or not
	*  @param driver     - given WebDriver driver
	*  @param actiondata - given action data
	*  @param iconName	 - icon name
	*  @param tableRows  - number of records in the searched table
	*  @return 			 - TRUE (if Action was detected). FALSE otherwise
	*/
	private Boolean verifyActionNotesTags(WebDriver driver, String actionData, String iconName, int tableRows) {
		
		int i = 0;
		String tempElm2 = null;

		// Verify if property exists at meeting menu or not		
		testFuncs.myDebugPrinting("Verify if property exists at meeting menu or not", enumsClass.logModes.NORMAL);	  
		if ((i = testFuncs.getIdxFromTable(driver, By.xpath("//*[@id='tab2details']/div[1]"), actionData)) == -1) {	
			
			testFuncs.myDebugPrinting("getActionIdx() is -1, FALSE returned", enumsClass.logModes.MINOR);	  
			return false;
		}	
		if (tableRows == 1) {

			tempElm2 = driver.findElement(By.xpath("//*[@id='tab2details']/div[1]/div/div/div/table/tbody/tr[2]/td[3]/img")).getAttribute("src");		 		
		} else {

			 tempElm2 = driver.findElement(By.xpath("//*[@id='tab2details']/div[1]/div/div/div[" + i + "]/table/tbody/tr[2]/td[3]/img")).getAttribute("src");		 		
		}
		testFuncs.myAssertTrue("Action icon <" + iconName + "> was not detected ! <tempElm2 - " + tempElm2 + ">", tempElm2.contains(iconName));		
		testFuncs.myDebugPrinting("Icon <" + iconName + "> was detected !!", enumsClass.logModes.MINOR);
		return true;	
	}
	
	/**
	*  Get Action index in main row
	*  @param driver     - given WebDriver driver
	*  @param actiondata - given action data
	*  @return  		 - index of row's action (-1 if null)
	*/
	public int getActionIdx(WebDriver driver, String actionData) {
			
		int i;
		
		// Get Action index in main row
		testFuncs.myDebugPrinting("Get Action index in main row", enumsClass.logModes.NORMAL);	  
		if ((i = testFuncs.getIdxFromTable(driver, By.xpath("//*[@id='tab2details']/div[1]"), actionData)) == -1) {	
			
			testFuncs.myDebugPrinting("getActionIdx() is -1, FALSE returned", enumsClass.logModes.MINOR);	  
			return -1;
		}	
		
		return i;
	}
	
	/**
	*  Edit a pre-created action
	*  @param driver        - given WebDriver driver
	*  @param actiondata    - given action data
	*  @param newActionData - given new action data for edit
	*  @param actionHeader 	- Header for the wanted action
	*  @param iconName		- Name of icon gif
	*/
	public void editAction(WebDriver driver, String actionData, String newActionData, String actionHeader, String iconName) {
		
		int i, tableRows;
		
		// Edit action
		testFuncs.myDebugPrinting("Edit action", enumsClass.logModes.NORMAL);	
		testFuncs.myAssertTrue("<Action was not detected !!>", (i = testFuncs.getIdxFromTable(driver, By.xpath("//*[@id='tab2details']/div[1]"), actionData)) != -1 );
		tableRows = driver.findElement(By.xpath("//*[@id='tab2details']/div[1]/div/div")).findElements(By.tagName("div")).size() /2;
		testFuncs.myDebugPrinting("tableRows - " + tableRows, enumsClass.logModes.DEBUG);		
		if (tableRows == 1) {
			testFuncs.myClick(driver, By.xpath("//*[@id='tab2details']/div[1]/div/div/div/table/tbody/tr[1]/td[2]/div"), 1000);		
		} else {
			
			testFuncs.myClick(driver, By.xpath("//*[@id='tab2details']/div[1]/div/div/div[" + i + "]/table/tbody/tr[1]/td[2]/div"), 1000);
		}
		
		// Press on Edit icon
		testFuncs.myDebugPrinting("Press on Edit icon", enumsClass.logModes.MINOR);	
		testFuncs.clickByName(driver, "Edit", 3000);
		testFuncs.verifyStrBy(driver, By.id("modalTitleId"), "Update " + actionHeader);
		testFuncs.mySendKeys(driver, By.id("content-id"), newActionData, 300);
		testFuncs.myClick(driver, By.xpath("/html/body/div[6]/div/button[1]"), 2000);
						
		// Verify the Edit action
		testFuncs.myDebugPrinting("Verify the Edit action", enumsClass.logModes.MINOR);
		testFuncs.myAssertTrue("Action <" + newActionData + "> was not edited successfully !", verifyActionNotesTags(driver, newActionData, iconName, tableRows));
	}
	
	/**
	*  Delete a pre-created action
	*  @param driver        - given WebDriver driver
	*  @param actiondata    - given action data
	*  @param actionHeader 	- Header for the wanted action
	*/
	public void deleteAction(WebDriver driver, String actiondata, String actionHeader) {
		
		int i, tableRows;
		
		// Delete action
		testFuncs.myDebugPrinting("Delete action", enumsClass.logModes.NORMAL);
		testFuncs.myAssertTrue("<Action was not detected !!>", (i = testFuncs.getIdxFromTable(driver, By.xpath("//*[@id='tab2details']/div[1]"), actiondata)) != -1 );
		tableRows = driver.findElement(By.xpath("//*[@id='tab2details']/div[1]/div/div")).findElements(By.tagName("div")).size() / 2;
		testFuncs.myDebugPrinting("tableRows - " + tableRows, enumsClass.logModes.DEBUG);
		if (tableRows == 1) {
			
			testFuncs.myClick(driver, By.xpath("//*[@id='tab2details']/div[1]/div/div/div/table/tbody/tr[1]/td[2]/div"), 100);		
		} else {
			
			testFuncs.myClick(driver, By.xpath("//*[@id='tab2details']/div[1]/div/div/div[" + i + "]/table/tbody/tr[1]/td[2]/div"), 1000);
		}
		
		// Press on Delete icon
		testFuncs.myDebugPrinting("Press on Edit icon", enumsClass.logModes.MINOR);
		testFuncs.clickByName(driver, "Delete", 2000);
		testFuncs.verifyStrBy(driver, By.id("modalTitleId"), "Delete Action" /* BUG HERE !!!!! + actionHeader*/);
		testFuncs.verifyStrBy(driver, By.id("modalContentId"), "Are you sure you want to delete the action: " + /*actionHeader.toLowerCase() +*/ actiondata + " ?");
		testFuncs.myClick(driver, By.xpath("/html/body/div[6]/div/button[1]"), 3000);

		// Verify the Delete action
		testFuncs.myDebugPrinting("Verify the Delete action", enumsClass.logModes.MINOR);
		testFuncs.myAssertTrue("<Action was not deleted successfully !!>", testFuncs.getIdxFromTable(driver, By.xpath("//*[@id='tab2details']/div[1]"), actiondata) == -1 );
	}
	
	/**
	*  Add Tag to existing Meeting
	*  @param driver  	- given WebDriver driver
	*  @param tagName 	- given Tag name
	*  @param tagMode 	- given Tag mode (PRIVATE or PUBLIC)
	*  @return currTime - estimated add time 
	*/
	public String addTag(WebDriver driver, String tagName, tagModes tagMode) {
		
		String addTime = null;
		int 	   idx = -1;

		// Add tag
		testFuncs.myDebugPrinting("Add Tag", enumsClass.logModes.NORMAL);	
		testFuncs.myClick(driver, By.xpath("//*[@id='tab2details']/div[2]/div/div/table/tbody/tr[2]/td[1]/div/div")	      ,  500);
		testFuncs.myClick(driver, By.xpath("//*[@id='tab2details']/div[2]/div/div/table/tbody/tr[2]/td[2]/div/div/div[3]"),  500);
		Select tagNames = new Select(driver.findElement(By.id("tag-ta-ui-tag-id")));
		testFuncs.myWait(2000);
		tagNames.selectByVisibleText(tagName); 
		
		// Set privacy & submit
		testFuncs.myDebugPrinting("Set privacy & submit", enumsClass.logModes.MINOR);
		setPrivacy(driver, tagMode);
		testFuncs.myClick(driver, By.xpath("//*[@id='tab2details']/div[2]/div/div/table/tbody/tr[2]/td[5]/div"),  2000);		
		
		// Verify the Add Tag action
		testFuncs.myDebugPrinting("Verify the Add Tag action", enumsClass.logModes.MINOR);
		testFuncs.myAssertTrue("Tag <" + tagName + "> was not detected !!", (idx = testFuncs.getIdxFromTable(driver, By.xpath("//*[@id='tab2details']/div[1]"), tagName)) != -1);			
		int tableRows = driver.findElement(By.xpath("//*[@id='tab2details']/div[1]/div/div")).findElements(By.tagName("div")).size() / 2;	
		testFuncs.myDebugPrinting("tableRows - " +  tableRows, enumsClass.logModes.DEBUG);
		if (tableRows == 1) {
			addTime = driver.findElement(By.xpath("//*[@id='tab2details']/div[1]/div/div/div/table/tbody/tr[2]/td[2]")).getText();				
		} else {	
			addTime = driver.findElement(By.xpath("//*[@id='tab2details']/div[1]/div/div/div[" + idx + "]/table/tbody/tr[2]/td[2]")).getText();		
		}
		testFuncs.myDebugPrinting("Returned  addTime - " + addTime, enumsClass.logModes.MINOR);
		return addTime;
	}
	
	/**
	*  Set privacy
	*  @param driver  - given WebDriver driver
	*  @param tagMode - given Tag mode (PRIVATE or PUBLIC)
	*/
	private void setPrivacy(WebDriver driver, tagModes tagMode) {
		
		String state = null;
		
		// Set privacy
		testFuncs.myDebugPrinting("Set privacy", enumsClass.logModes.NORMAL);	
		while (true) {
			
			testFuncs.myClick(driver, By.xpath("//*[@id='tab2details']/div[2]/div/div/table/tbody/tr[2]/td[3]/div/div"),  1000);
			state = driver.findElement(By.xpath("//*[@id='tab2details']/div[2]/div/div/table/tbody/tr[2]/td[3]/div/div/img")).getAttribute("src");
			if ((tagMode == tagModes.PUBLIC  && state.contains("public")) ||
				(tagMode == tagModes.PRIVATE && state.contains("private"))) {
				
				// Set privacy
				testFuncs.myDebugPrinting(tagMode.getMode() + " state was set !", enumsClass.logModes.MINOR);
				break;
			}
		}
	}

	/**
	*  Delete a pre-created Tag from from main menu
	*  @param driver  - given WebDriver driver
	*  @param subject - given existing Meeting name
	*  @param tagName - given existing Tag Name
	*/
	public void deleteTagMainMenu(WebDriver driver, String subject, String tagName) {
		
		int idx = 0;
		
		// Delete tag
		testFuncs.myDebugPrinting("Delete Tag", enumsClass.logModes.NORMAL);	  
	    verifyMeeting(driver, subject);
		testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_EDIT_MENU			  );	
		testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_EDIT_MENU_TAG_SECTION);	
		if ((idx = testFuncs.getIdxFromTable(driver, By.xpath("//*[@id='tab2tags']/div/div/table/tbody"), tagName)) == -1) {
			
			testFuncs.myFail("Record was not detected !!");
		}
		testFuncs.myClick(driver, By.xpath("//*[@id='tab2tags']/div/div/table/tbody/tr[" + idx + "]/td[6]/div"), 1000);
		testFuncs.verifyStrBy(driver, By.id("modalTitleId"), "Delete Tag");
		testFuncs.verifyStrBy(driver, By.id("modalContentId"), "Are you sure you want to delete this tag");
		testFuncs.myClick(driver, By.xpath("/html/body/div[5]/div/button[1]"), 3000);
			
		// Verify delete
		testFuncs.myDebugPrinting("Verify delete", enumsClass.logModes.MINOR);	  
		testFuncs.pressHomeIcon(driver);
	    verifyMeeting(driver, subject);
		testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_EDIT_MENU			  );	
		testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_EDIT_MENU_TAG_SECTION);
		testFuncs.myAssertTrue("Tag <" + tagName + "> was not deleted !!",  !driver.findElement(By.tagName("body")).getText().contains(tagName));
	}
	
	/**
	*  Delete a pre-created Tag from existing Meeting menu
	*  @param driver  - given WebDriver driver
	*  @param subject - given existing Meeting name
	*  @param tagName - given existing Tag Name
	*/
	public void deleteTag(WebDriver driver, String subject, String tagName) {
		
		int i;
		
		// Delete tag
		testFuncs.myDebugPrinting("Delete tag", enumsClass.logModes.NORMAL);		
		testFuncs.myAssertTrue("Tag <" + tagName + "> was not detected !!", (i = testFuncs.getIdxFromTable(driver, By.xpath("//*[@id='tab2details']/div[1]"), tagName)) != -1);	
		int tableRows = driver.findElement(By.xpath("//*[@id='tab2details']/div[1]/div/div")).findElements(By.tagName("div")).size() / 2;
		testFuncs.myDebugPrinting("tableRows - " + tableRows, enumsClass.logModes.MINOR);		
		if (tableRows == 1) {
			
			testFuncs.myClick(driver, By.xpath("//*[@id='tab2details']/div[1]/div/div/div/table/tbody/tr[1]/td[2]/div"), 1000);
		} else {	
			
			testFuncs.myClick(driver, By.xpath("//*[@id='tab2details']/div[1]/div/div/div[" + i + "]/table/tbody/tr[1]/td[2]/div"), 1000);
		}
		List<WebElement> hrefs = driver.findElements(By.tagName("a"));
		for (WebElement href : hrefs) {
			
			if (href.getText().contains("Delete")) {
			
				href.click();
				testFuncs.myWait(1500);
				break;
			}
		}	
		testFuncs.verifyStrBy(driver, By.id("modalTitleId")  , "Delete Action" /* BUG HERE !!!!! should be Delete Tag */);
		
		testFuncs.verifyStrBy(driver, By.id("modalContentId"), "Are you sure you want to delete the action: " + tagName + " ?"/* BUG HERE !!!!! should be 'Are you sure you want to delete the **tag**'*/);
		testFuncs.myClick(driver, By.xpath("/html/body/div[6]/div/button[1]"), 5000);

		// Verify the Delete action
		testFuncs.myDebugPrinting("Verify the Delete action", enumsClass.logModes.MINOR);
		testFuncs.myAssertTrue("Tag <" + tagName + "> was not deleted successfully !!", testFuncs.getIdxFromTable(driver, By.xpath("//*[@id='tab2details']/div[1]"), tagName) == -1);	
	}
	
	/**
	*  Add pre-created Tag to Meeting via main menu
	*  @param driver  - given WebDriver driver
	*  @param tagName - given Tag name
	*  @param tagMode - given Tag mode (PRIVATE or PUBLIC)
	*/
	public void addTagViaMainMenu(WebDriver driver, String tagName, tagModes tagMode) {
		
		// Add tag to Meeting via main menu
		testFuncs.myDebugPrinting("Add tag to Meeting via main menu", enumsClass.logModes.NORMAL);	
		testFuncs.myHover(driver, By.id("tagtd")	, 1000);
		testFuncs.myClick(driver, By.id("addtagdiv"), 2000);
		testFuncs.verifyStrBy(driver, By.id("modalTitleId"), "Add Tag");
		Select tagNames = new Select(driver.findElement(By.id("tag-id")));
		testFuncs.myWait(2000);
		tagNames.selectByVisibleText(tagName);
		Select tagModes = new Select(driver.findElement(By.id("visibility-id")));
		testFuncs.myWait(2000);
		tagModes.selectByVisibleText(tagMode.getMode());
		testFuncs.myClick(driver, By.xpath("/html/body/div[6]/div/button[1]"), 5000);
		
		// Verify the Add-Tag action
		testFuncs.myDebugPrinting("Verify the Add-Tag action", enumsClass.logModes.MINOR);
		String bodyText     = driver.findElement(By.tagName("body")).getText();	
		testFuncs.myAssertTrue("Tag <" + tagName + "> was not created successfully !/nbodyText - " + bodyText, bodyText.contains(tagName));
	}
	
	/**
	*  Delete a Meeting via main menu
	*  @param driver      	   - given WebDriver driver
	*  @param meetingName 	   - given Meeting name
	*  @param verifyDeleteFlag - given verify-delete flag
	*/
	public void deleteMeeting(WebDriver driver, String meetingName, Boolean isVerifyDelete) {
		
		// Delete Meeting
		testFuncs.myDebugPrinting("Delete Meeting", enumsClass.logModes.NORMAL);
	    verifyMeeting(driver, meetingName);
	    waitForProcessingEnd(driver, 200);
	    testFuncs.myClick(driver, By.xpath("//*[@id='btnGroupDrop1']"), 2000);
		testFuncs.clickByName(driver, "Delete"						  , 3000);
	    Alert alert = driver.switchTo().alert();
        String alertMessage= alert.getText();		     		
        testFuncs.myAssertTrue("Delete Meeting text was not detected !! <" + alertMessage + ">", alertMessage.contains("Are you sure you want to delete?"));
        alert.accept();
		testFuncs.myWait(3000);
        
		if (isVerifyDelete) {
			
	        // Verify Meeting not displayed
			testFuncs.myDebugPrinting("Verify Meeting not displayed", enumsClass.logModes.MINOR);	
			verifyMeetingNotDisplayed(driver, meetingName);
		} else {
			
			testFuncs.myDebugPrinting("isVerifyDelete - FALSE", enumsClass.logModes.MINOR);	
		}
	}
	
	/**
	*  Delete a Meeting via main menu (default behavior - verify delete flag is ON)
	*  @param driver      - given WebDriver driver
	*  @param meetingName - given Meeting name
	*/
	public void deleteMeeting(WebDriver driver, String meetingName) {
		
		deleteMeeting(driver, meetingName, true);
	}
	
	/**
	*  Verify Meeting not displayed
	*  @param driver      - given WebDriver driver
	*  @param meetingName - given Meeting name
	*/
	public void verifyMeetingNotDisplayed(WebDriver driver, String meetingName) {
		
		// Verify Meeting not displayed
		testFuncs.mySendKeys(driver, By.id("searchInput"), "subject:\"" + meetingName + "\"", 1000);
	    testFuncs.myClick(driver, By.xpath("//*[@id='form-user_v1']/div[1]/div[1]/div[2]/button/i"), 2000);
	    if (driver.findElement(By.xpath("//*[@id='form-user_v1']/div[1]")).getAttribute("class").contains("backdrop result")) {
	    	
			testFuncs.myDebugPrinting("<backdrop result> detected !!", enumsClass.logModes.MINOR);
			driver.findElement(By.id("searchInput")).sendKeys(Keys.ENTER);
			testFuncs.myWait(2000);	
	    }
	    testFuncs.myAssertTrue("Meeting <" + meetingName + "> was not deleted !!", driver.findElement(By.tagName("body")).getText().contains("No meetings to display"));
	}

	/**
	*  Enter Edit page
	*  @param driver  - given WebDriver driver
	*/  
	public void enterEditPage(WebDriver driver) {
		  	  
		// Enter to the Edit page
		testFuncs.myDebugPrinting("Enter to the Edit page", enumsClass.logModes.NORMAL);		
		testFuncs.myClick(driver, By.xpath("//*[@id=\"btnGroupDrop1\"]/img")						    , 1000);
		testFuncs.myClick(driver, By.xpath("//*[@id=\"example\"]/tbody[2]/tr[1]/td[6]/div/div/div/a[1]"), 3000); 
	}
	
	/**
	* Edit a subject from the List view
	* @param driver          - given WebDriver driver
	* @param oldSubject      - given old subject
	* @param updatedSubject  - given subject for update
	*/
	public void updateSubject_listView(WebDriver driver, String oldSubject, String updatedSubject) {
		
		// Update subject
		testFuncs.myDebugPrinting("Update a subject to a meeting from List view", enumsClass.logModes.NORMAL);
		verifyMeeting(driver, oldSubject);
		testFuncs.myHover(driver, By.id("myedittd"), 2000);
	    testFuncs.myClick(driver, By.xpath("//*[@id='myeditdiv']/img[2]"), 2000);
	    testFuncs.verifyStrByXpath(driver, "//*[@id=\"modalTitleId\"]", "Update Subject");
	    testFuncs.mySendKeys(driver, By.id("content-id"), updatedSubject, 2000);
	    testFuncs.myClick(driver, By.xpath("/html/body/div[6]/div/button[1]"), 5000);
	    
	    // Verify update
		testFuncs.myDebugPrinting("Verify update", enumsClass.logModes.MINOR);
		verifyMeeting(driver, updatedSubject);
	}
	
	/**
	* Delete a subject from the List view
	* @param driver 	 - given WebDriver driver
	* @param delSubject	- name of subject that should be deleted
	*/
	public void deleteSubject_listView(WebDriver driver, String delSubject) {
		
		// Delete Subject
		testFuncs.myDebugPrinting("Delete the subject of the meeting from List view", enumsClass.logModes.NORMAL);	  	
		verifyMeeting(driver, delSubject);
		testFuncs.myHover(driver, By.id("myedittd"), 2000);
	    testFuncs.myClick(driver, By.xpath("//*[@id='myeditdiv']/img[2]"), 2000);
		testFuncs.verifyStrByXpath(driver, "//*[@id=\"modalTitleId\"]", "Update Subject");
		testFuncs.mySendKeys(driver, By.id("content-id"), "", 2000);
		testFuncs.myClick(driver, By.xpath("/html/body/div[6]/div/button[1]"), 5000);
		
	    // Verify delete
		testFuncs.myDebugPrinting("Verify delete", enumsClass.logModes.MINOR);
		verifyMeetingNotDisplayed(driver, delSubject);
	}
	
	/**
	* Add a subject to a meeting from the List view
	* @param driver      - given WebDriver driver
	* @param addSubject  - given subject for add
	*/
	public void addSubject_listView (WebDriver driver, String addSubject ) {
		
		// Add a subject to a meeting from the List view
		testFuncs.myDebugPrinting("Add a subject to a meeting from List view", enumsClass.logModes.NORMAL);	
		driver.findElement(By.id("searchInput")).clear();		
		driver.findElement(By.id("searchInput")).sendKeys(Keys.ENTER);
	    testFuncs.myClick(driver, By.xpath("//*[@id='form-user_v1']/div[1]/div[1]/div[2]/button/i"), 2000);		
		testFuncs.myHover(driver, By.id("myedittd"), 2000);
	    testFuncs.myClick(driver, By.xpath("//*[@id='myeditdiv']/img[1]"), 2000);
	    testFuncs.verifyStrByXpath(driver, "//*[@id=\"modalTitleId\"]", "Add Subject");
	    testFuncs.mySendKeys(driver, By.id("content-id"), addSubject, 1000);
	    testFuncs.myClick(driver, By.xpath("/html/body/div[6]/div/button[1]"), 5000);   
	    
	    // Verify Add
		testFuncs.myDebugPrinting("Verify Add", enumsClass.logModes.MINOR);
		verifyMeeting(driver, addSubject);
	}
	
	/**
	* Press the Add-New button
	* @param driver      - given WebDriver driver
	*/
	public void pressAddNewButton(WebDriver driver) {
		
		// Press the Add-New button
		testFuncs.myDebugPrinting("Press the Add-New button", enumsClass.logModes.NORMAL);		
	    testFuncs.myClick(driver, By.xpath("//*[@id='example']/tbody[2]/tr/td[8]/button[2]"), 5000);
  	    testFuncs.searchStr(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO.getStrForSearch()); 
	}
	
	/**
	* Press the See-All button
	* @param driver      - given WebDriver driver
	*/
	public void pressSeeAllButton(WebDriver driver) {
		
		// Press the See-All button
		testFuncs.myDebugPrinting("Press the See-All button", enumsClass.logModes.NORMAL);		
	    testFuncs.myClick(driver, By.xpath("//*[@id='example']/tbody[2]/tr/td[7]/button[1]"), 3000);
	}
	
	/**
	* Verify Item (Action or Note) at See-All table
	* @param driver   - given WebDriver driver
	* @param itemName - given Item name
	* @param iconName - given Icon name
	*/
	public void verifySeeAllItem(WebDriver driver, String itemName, String iconName) {
		
		int i;
		
		// Verify item at See-All table
		testFuncs.myDebugPrinting("Verify item at See-All table", enumsClass.logModes.NORMAL);
		if ((i = testFuncs.getIdxFromTableNoDup(driver, By.xpath("//*[@id='id_modalActions_Notes']/div/div/div/div/div/table/tbody"), itemName)) == -1) {	
			
			testFuncs.myFail("getActionIdx() is -1, FALSE returned");	  
		}
		
		// Verify item icon at See-All table
		testFuncs.myDebugPrinting("Verify item icon at See-All table", enumsClass.logModes.MINOR);
		String elm = driver.findElement(By.xpath("//*[@id='id_modalActions_Notes']/div/div/div/div/div/table/tbody/tr[" + i + "]/td[1]/img")).getAttribute("src");
		testFuncs.myAssertTrue("The icon <" + iconName + "> is not displayed !!", elm.contains(iconName));

		// Verify item name at See-All table
		testFuncs.myDebugPrinting("Verify item name at See-All table", enumsClass.logModes.MINOR);
		testFuncs.verifyStrByXpath(driver, "//*[@id='id_modalActions_Notes']/div/div/div/div/div/table/tbody/tr[" + i + "]/td[4]", itemName);
	}
	
	/**
	* Play an item via the See-All toolbar
	* @param driver   - given WebDriver driver
	* @param itemName - given item name
	*/
	public void playActionViaSeeAllToolBar(WebDriver driver, String itemName) {
		
		int i, tableRows;
		
		// Play an item via the See-All toolbar
		testFuncs.myDebugPrinting("Play an item via the See-All toolbar", enumsClass.logModes.NORMAL);
		testFuncs.myAssertTrue("getActionIdx() is -1, FALSE returned", (i = testFuncs.getIdxFromTableNoDup(driver, By.xpath("//*[@id='id_modalActions_Notes']/div/div/div/div/div/table/tbody"), itemName)) != -1);
		testFuncs.myAssertTrue("Rows number is -1"					 , (tableRows = driver.findElement(By.xpath("//*[@id='id_modalActions_Notes']/div/div/div/div/div/table/tbody")).findElements(By.tagName("tr")).size()) != 0);
		testFuncs.myDebugPrinting("tableRows - " + tableRows, enumsClass.logModes.DEBUG);
		if (tableRows == 1) {
			
		    testFuncs.myClick(driver, By.xpath("//*[@id='id_modalActions_Notes']/div/div/div/div/div/table/tbody/tr[" + i + "]/td[7]/img"), 3000);			
		} else {
			
		    testFuncs.myClick(driver, By.xpath("//*[@id='id_modalActions_Notes']/div/div/div/div/div/table/tbody/td[7]/img"), 3000);	
		}
		
		// Verify Enter the Video menu
		testFuncs.myDebugPrinting("Verify Enter the Video menu", enumsClass.logModes.MINOR);
		testFuncs.searchStr(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO.getStrForSearch());	
	}
	
	/**
	* Delete an item via the See-All toolbar
	* @param driver   	 - given WebDriver driver
	* @param itemName 	 - given item name
	* @param meetingName - given Meeting name
	* @param itemType	 - given item type (Action) 
	*/
	public void deleteActionViaSeeAllToolBar(WebDriver driver, String itemName, String meetingName, String itemType) {
		
		int i, tableRows;
				
		// Delete an item via the See-All toolbar
		testFuncs.myDebugPrinting("Play an item via the See-All toolbar", enumsClass.logModes.NORMAL);
		testFuncs.myAssertTrue("getActionIdx() is -1, FALSE returned", (i = testFuncs.getIdxFromTableNoDup(driver, By.xpath("//*[@id='id_modalActions_Notes']/div/div/div/div/div/table/tbody"), itemName)) != -1);
		testFuncs.myAssertTrue("Rows number is -1"					 , (tableRows = driver.findElement(By.xpath("//*[@id='id_modalActions_Notes']/div/div/div/div/div/table/tbody")).findElements(By.tagName("tr")).size()) != 0);
		testFuncs.myDebugPrinting("tableRows - " + tableRows, enumsClass.logModes.DEBUG);
		if (tableRows == 1) {
		
			testFuncs.myClick(driver, By.xpath("//*[@id='id_modalActions_Notes']/div/div/div/div/div/table/tbody/tr[" + i + "]/td[6]/img"), 3000);			
			
		} else {
					  
			testFuncs.myClick(driver, By.xpath("//*[@id='id_modalActions_Notes']/div/div/div/div/div/table/tbody/td[6]/img"), 3000);		
		}
		testFuncs.verifyStrBy(driver, By.id("modalTitleId")  , "Delete " + itemType);
		testFuncs.verifyStrBy(driver, By.id("modalContentId"), "Are you sure you want to delete this " + itemType.toLowerCase() + ": " + itemName);
		testFuncs.myClick(driver, By.xpath("/html/body/div[7]/div/button[1]"), 5000);		
		
		// Verify Item delete
		testFuncs.myDebugPrinting("Verify Item delete", enumsClass.logModes.MINOR);
		verifyMeeting(driver, meetingName);
		testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);		
		testFuncs.searchStrFalse(driver, itemName);
	}
	
	/**
	* Delete a Meeting via the Advanced options toolbar
	* @param driver   	 	- given WebDriver driver
	* @param meetingSubject - given Meeting subject
	*/
	public void deleteMeetingViaPlayerMenu(WebDriver driver, String meetingSubject) {
		
		// Delete a Meeting via the Advanced options toolbar
		testFuncs.myDebugPrinting("Delete a Meeting via the Advanced options toolbar", enumsClass.logModes.NORMAL);
		testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO_ADV_BUTTONS);	
		testFuncs.clickByName(driver, "Delete Meeting", 3000);   
		Alert alert = driver.switchTo().alert();
		String alertMessage= alert.getText();		     		  
		testFuncs.myAssertTrue("Delete Meeting text was not detected !! <" + alertMessage + ">", alertMessage.contains("Are you sure you want to delete this meeting?"));
		alert.accept();	
		testFuncs.myWait(5000);
		verifyMeetingNotDisplayed(driver, meetingSubject);
	}
	
	/**
	* Verify the state of Meeting state
	* @param driver   	   - given WebDriver driver
	* @param isPublishFlag - is Publish flag is on (true) or not
	* @param isOwnerFlag   - is the tested user is Owner (true) or not
	*/
	public void verifyButtonsState(WebDriver driver, boolean isPublishFlag, boolean isOwnerFlag) {
		
		// Verify the state of Meeting state
		testFuncs.myDebugPrinting("Verify the state of Meeting state", enumsClass.logModes.NORMAL);	  
		if (isPublishFlag) {
			
			if (isOwnerFlag) {
				
				testFuncs.myAssertTrue("Button Slides Attachment is not at wanted state !", driver.findElement(By.xpath("//*[@id='example']/tbody[2]/tr/td[5]/div/button[1]")).getAttribute("disabled") != null);
				testFuncs.myAssertTrue("Button Slides show is not at wanted state !"	  , driver.findElement(By.xpath("//*[@id='example']/tbody[2]/tr/td[5]/div/button[3]")).getAttribute("disabled") == null);
				testFuncs.myAssertTrue("Button Play Video is not at wanted state !"		  , driver.findElement(By.xpath("//*[@id='example']/tbody[2]/tr/td[5]/div/button[4]")).getAttribute("disabled") == null);
				testFuncs.myAssertTrue("Button Actions is not at wanted state !" 		  , driver.findElement(By.id("btnGroupDrop1")).getAttribute("disabled") 										== null);	
			} else {
				
				testFuncs.myAssertTrue("Button Slides Attachment is not at wanted state !", driver.findElement(By.xpath("//*[@id='example']/tbody[2]/tr/td[5]/div/button[1]")).getAttribute("disabled") != null);
				testFuncs.myAssertTrue("Button Slides show is not at wanted state !"	  , driver.findElement(By.xpath("//*[@id='example']/tbody[2]/tr/td[5]/div/button[3]")).getAttribute("disabled") == null);
				testFuncs.myAssertTrue("Button Play Video is not at wanted state !"		  , driver.findElement(By.xpath("//*[@id='example']/tbody[2]/tr/td[5]/div/button[4]")).getAttribute("disabled") == null);
				testFuncs.myAssertTrue("Button Actions is not at wanted state !" 		  , driver.findElement(By.id("btnGroupDrop1")).getAttribute("disabled") 										!= null);		
			}
		} else {
			
			if (isOwnerFlag) {
				
				testFuncs.myAssertTrue("Button Slides Attachment is not at wanted state !", driver.findElement(By.xpath("//*[@id='example']/tbody[2]/tr/td[5]/div/button[1]")).getAttribute("disabled") != null);
				testFuncs.myAssertTrue("Button Slides show is not at wanted state !"	  , driver.findElement(By.xpath("//*[@id='example']/tbody[2]/tr/td[5]/div/button[3]")).getAttribute("disabled") == null);
				testFuncs.myAssertTrue("Button Play Video is not at wanted state !"		  , driver.findElement(By.xpath("//*[@id='example']/tbody[2]/tr/td[5]/div/button[4]")).getAttribute("disabled") == null);
				testFuncs.myAssertTrue("Button Actions is not at wanted state !" 		  , driver.findElement(By.id("btnGroupDrop1")).getAttribute("disabled") 										== null);			
			} else {
				
				testFuncs.myAssertTrue("Button Slides Attachment is not at wanted state !", driver.findElement(By.xpath("//*[@id='example']/tbody[2]/tr/td[5]/div/button[1]")).getAttribute("disabled") != null);
				testFuncs.myAssertTrue("Button Slides show is not at wanted state !"	  , driver.findElement(By.xpath("//*[@id='example']/tbody[2]/tr/td[5]/div/button[3]")).getAttribute("disabled") != null);
				testFuncs.myAssertTrue("Button Play Video is not at wanted state !"		  , driver.findElement(By.xpath("//*[@id='example']/tbody[2]/tr/td[5]/div/button[4]")).getAttribute("disabled") != null);
				testFuncs.myAssertTrue("Button Actions is not at wanted state !" 		  , driver.findElement(By.id("btnGroupDrop1")).getAttribute("disabled") 										!= null);			
			}
		}
	}
}