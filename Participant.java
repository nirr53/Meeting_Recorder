package MeetingRecorder;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class Participant {
	
	GlobalFuncs			testFuncs;
	GlobalVars 			testVars;
	Meeting				meeting;
	
	public Participant(GlobalFuncs testFuncs, GlobalVars testVars, Meeting meeting) {
		
		this.testFuncs = testFuncs;
		this.testVars = testVars;
		this.meeting = meeting;
	}
	
	/**
	*  Create a participant
	*  @param driver   - given WebDriver driver
	*  @param partName - given Participant name
	*/ 
	public void addParticipant(WebDriver driver, String partName) {
		
		int idx;
		
		// Create a Participant
		testFuncs.myDebugPrinting("Create a Participant - " + partName, enumsClass.logModes.NORMAL);  
		testFuncs.mySendKeys(driver, By.xpath("//*[@id='tab2parts']/div/div[1]/table/tbody/tr[1]/td[1]/input"), partName, 2000);	
		testFuncs.myClick(driver, By.xpath("//*[@id='tab2parts']/div/div[1]/table/tbody/tr[1]/td[2]/a"), 5000);

		// Verify the created Participant
		testFuncs.myDebugPrinting("Verify the created Participant", enumsClass.logModes.MINOR);
		if (partName.equals(testVars.getPartReg())) {
			
			testFuncs.myDebugPrinting("The participant is registered. Search for AD diaplay-name", enumsClass.logModes.MINOR);
			String dName = testVars.getPartRegNickname();
			testFuncs.myAssertTrue("Participant <" + dName + "> was not detected !!", (idx = testFuncs.getIdxFromTableNoDup(driver, By.xpath("//*[@id='tab2parts']/div/div[2]/table"), dName)) != -1);
		} else {
					
			testFuncs.myDebugPrinting("The participant is unregistered. Search for given name", enumsClass.logModes.MINOR);
			testFuncs.myAssertTrue("Participant <" + partName + "> was not detected !!", (idx = testFuncs.getIdxFromTableNoDup(driver, By.xpath("//*[@id='tab2parts']/div/div[2]/table"), partName)) != -1);
		}	
		
		// Verify that Participant header is added to new Participant
		testFuncs.myDebugPrinting("Verify that Participant header is added to new Participant", enumsClass.logModes.NORMAL); 		
		idx = (idx + 1) / 2;
		testFuncs.myDebugPrinting("idx - " + idx, enumsClass.logModes.DEBUG);
		testFuncs.verifyStrByXpath(driver, "//*[@id='tab2parts']/div/div[2]/table/tbody/tr["+ idx +"]/td[2]/div[2]", "Participant");	
	}
	
	/**
	*  Delete a participant via Meeting Edit menu
	*  @param driver   	  - given WebDriver driver
	*  @param meetingName - given Meeting name
	*  @param partName    - given Participant name
	*/ 
	public void deleteParticipantViaEditMenu(WebDriver driver, String meetingName, String partName) {
		
		int idx = 0;

		// Delete a Participant
		testFuncs.myDebugPrinting("Delete a Participant - " + partName, enumsClass.logModes.NORMAL); 
	    meeting.verifyMeeting(driver, meetingName);		
		testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_EDIT_MENU			  		  );	
		testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_EDIT_MENU_PARTICIPANT_SECTION);	
		testFuncs.myAssertTrue("Participant <" + partName + "> was not detected !!", (idx = testFuncs.getIdxFromTableNoDup(driver, By.xpath("//*[@id='tab2part']/div/div/table/tbody"), partName)) != -1);
		testFuncs.myClick(driver, By.xpath("//*[@id='tab2part']/div/div/table/tbody/tr[" + idx + "]/td[5]/div"), 1000);
		testFuncs.verifyStrBy(driver, By.id("modalTitleId")  , "Delete Participant");
		testFuncs.verifyStrBy(driver, By.id("modalContentId"), "Are you sure you want to delete this participant");
		testFuncs.myClick(driver, By.xpath("/html/body/div[5]/div/button[1]"), 5000);
			
		// Verify delete
		testFuncs.myDebugPrinting("Verify delete", enumsClass.logModes.MINOR);	  
		testFuncs.pressHomeIcon(driver);
	    meeting.verifyMeeting(driver, meetingName);
		testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_EDIT_MENU			  		  );	
		testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_EDIT_MENU_PARTICIPANT_SECTION);	
		testFuncs.myAssertTrue("Participant <" + partName + "> was not deleted !!", (idx = testFuncs.getIdxFromTable(driver, By.xpath("//*[@id='tab2part']/div/div/table/tbody"), partName)) == -1);
		testFuncs.pressHomeIcon(driver);
	}
	
	/**
	*  Delete a participant via Player menu
	*  @param driver   	  - given WebDriver driver
	*  @param partName    - given Participant name
	*/ 
	public void deleteParticipantViaPlayerMenu(WebDriver driver, String partName) {
		
		int idx = 0;

		// Delete a Participant
		testFuncs.myDebugPrinting("Delete a Participant - " + partName, enumsClass.logModes.NORMAL); 
		testFuncs.myAssertTrue("Participant <" + partName + "> was not detected !!", (idx = testFuncs.getIdxFromTableNoDup(driver, By.xpath("//*[@id='tab2parts']/div/div[2]/table"), partName)) != -1);
		idx = (idx + 1) / 2;
		testFuncs.myDebugPrinting("idx - " + idx, enumsClass.logModes.MINOR); 
		testFuncs.myClick(driver, By.xpath("//*[@id='tab2parts']/div/div[2]/table/tbody/tr[" + idx + "]/td[3]/div")			, 1000);
		testFuncs.clickByName(driver, "Delete Participant", 2000);
		
		// Verify delete
		testFuncs.myDebugPrinting("Verify delete", enumsClass.logModes.MINOR);	
		testFuncs.myAssertTrue("Participant <" + partName + "> was not deleted successfully !!", testFuncs.getIdxFromTableNoDup(driver, By.xpath("//*[@id='tab2parts']/div/div[2]/table"), partName) == -1);	
	}
	
	/**
	*  Set a participant a Delegate via Player menu
	*  @param driver   	  - given WebDriver driver
	*  @param partName    - given Participant name
	*/ 
	public void setPartDelegate(WebDriver driver, String partName) {
		
		int idx = 0;

		// Set a participant a Delegate
		testFuncs.myDebugPrinting("Set a participant " + partName + " a Delegate", enumsClass.logModes.NORMAL); 
		testFuncs.myAssertTrue("Participant <" + partName + "> was not detected !!", (idx = testFuncs.getIdxFromTableNoDup(driver, By.xpath("//*[@id='tab2parts']/div/div[2]/table"), partName)) != -1);
		idx = (idx + 1) / 2;
		testFuncs.myDebugPrinting("idx - " + idx, enumsClass.logModes.MINOR); 
		testFuncs.myClick(driver, By.xpath("//*[@id='tab2parts']/div/div[2]/table/tbody/tr[" + idx + "]/td[3]/div")			, 1000);
		testFuncs.clickByName(driver, "Make Delegate", 2000);
		
		// Verify set
		testFuncs.myDebugPrinting("Verify set", enumsClass.logModes.MINOR);	
		testFuncs.verifyStrByXpath(driver, "//*[@id='tab2parts']/div/div[2]/table/tbody/tr["+ idx +"]/td[2]/div[2]", "Delegate");	
	}
}
