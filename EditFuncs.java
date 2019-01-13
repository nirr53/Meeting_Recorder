package MeetingRecorder;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import MeetingRecorder.enumsClass.tagModes;

public class EditFuncs {
	
	GlobalFuncs	testFuncs;
	GlobalVars 	testVars;
	VideoPlayer	videoPlayer;

	
	public EditFuncs(GlobalFuncs testFuncs, GlobalVars testVars) {
		
		this.testFuncs 	 = testFuncs;
		this.testVars  	 = testVars;
	}

	/**
	* Add an Action Item from the Edit page
	* @param driver 	- given WebDriver driver
	* @param newAction -  given action for add
	*/
	public void addActionViaEditMenu(WebDriver driver, String newAction) {

		// Add an Action Item from the Edit page
		testFuncs.myDebugPrinting("Add an Action Item from the Edit page", enumsClass.logModes.NORMAL);	
		testFuncs.myClick(driver, By.xpath("//*[@id='tab2actions']/div/div/p"), 1000);
		testFuncs.verifyStrByXpath(driver, "//*[@id=\"modalTitleId\"]", 		"Add Action");
		testFuncs.verifyStrByXpath(driver, "/html/body/div[5]/div/button[1]", 	"Add");
		testFuncs.verifyStrByXpath(driver, "/html/body/div[5]/div/button[2]", 	"Cancel");
		testFuncs.mySendKeys(driver, By.id("content-id"), newAction, 3000);
		testFuncs.myClick(driver, By.xpath("/html/body/div[5]/div/button[1]"), 5000);
		
		// Verify the create	
		testFuncs.myDebugPrinting("Verify the create", enumsClass.logModes.MINOR);	
		testFuncs.pressHomeIcon(driver);
		testFuncs.searchStr(driver, newAction);
//		testFuncs.verifyStrByXpath(driver, "//*[@id='tab2actions']/div/div/table/tbody/tr/td[3]/input", newAction);	
	}
	
	/**
	* Download an attachment, via Attachments menu of the Player
	* @param driver   - given WebDriver driver
	* @param fileName - given the attached file name
	*/
	public void downloadAttachViaPlayerPage(WebDriver driver, String fileName) {
		
		// Download an attachment, via Attachments menu of the Player
		testFuncs.myDebugPrinting("Download an attachment, via Attachments menu of the Player", enumsClass.logModes.NORMAL);		
		testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO_ATTACHMENTS_TAB);
		testFuncs.myAssertTrue("Missing Download button from window.", driver.findElement(By.xpath("//*[@id=\"tab2atts\"]/div[1]/div/div[1]/div[2]/a/i")).getAttribute("class").contains("fa fa-download  text-primary"));
		testFuncs.myClick(driver, By.xpath("//*[@id=\"tab2atts\"]/div[1]/div/div[1]/div[2]/a/i"), 7000); 
		testFuncs.myDebugPrinting("attachments_" + fileName, enumsClass.logModes.MINOR);
		testFuncs.myAssertTrue("The file was not downloaded successfully via the Player page !!", testFuncs.findFilesByGivenPrefix(testVars.getDownloadsPath(), "attachments_" + fileName)); // Verify that download was successful
	}
	
	/**
	* Update an Action Item from the Edit page
	* @param driver 	  - given WebDriver driver
	* @param updateAction - given action for add
	*/
	public void updateActionViaEditMenu(WebDriver driver, String updateAction) {
		
		// Update Action via Edit menu
		testFuncs.myDebugPrinting("Update the Action Item from the Edit page", enumsClass.logModes.NORMAL);	  	
		testFuncs.mySendKeys(driver, By.xpath("//*[@id=\"tab2actions\"]/div/div/table/tbody/tr/td[3]/input"), updateAction, 1000);
		testFuncs.myClick(driver, By.xpath("/html/body/div[3]/div[3]/div/div[2]/div/div/p/button"), 2000);
		testFuncs.verifyStrByXpath(driver, "/html/body/div[5]/span[2]", "Meeting Successfully Modified");
		testFuncs.verifyStrBy(driver, By.xpath("//*[@id=\"tab2actions\"]/div/div/table/tbody/tr/td[1]/span"), "Me");	
		testFuncs.myFail("TODO: verify the action has changed: testFuncs.searchStr(driver, updateAction);");
		// TODO: verify the action has changed: testFuncs.searchStr(driver, updateAction);
	}
	
	/**
	* Delete an Action Item from the Edit page
	* @param driver - given WebDriver driver
	* @param action - the action you want to delete
	*/
	public void deleteActionViaEditMenu(WebDriver driver, String action) {
		
		testFuncs.myDebugPrinting("Delete an Action Item from the Edit page", enumsClass.logModes.NORMAL);	  	
		testFuncs.myClick(driver, By.xpath("//*[@id='tab2actions']/div/div/table/tbody/tr[1]/td[4]/div/i"), 1000);
		testFuncs.verifyStrByXpath(driver, "//*[@id=\"modalTitleId\"]", "Delete Action");
		testFuncs.verifyStrByXpath(driver, "/html/body/div[5]/div/button[1]", "Delete");
		testFuncs.verifyStrByXpath(driver, "/html/body/div[5]/div/button[2]", "Cancel");
		testFuncs.myClick(driver, By.xpath("/html/body/div[5]/div/button[1]"), 1000);
		testFuncs.myClick(driver, By.xpath("/html/body/div[5]/div/button[1]"), 1000);
		testFuncs.myClick(driver, By.xpath("/html/body/div[3]/div[3]/div/div[2]/div/div/p/button"), 1000);
		testFuncs.verifyStrByXpath(driver, "/html/body/div[5]/span[2]", "Meeting Successfully Modified");
		// TODO: verify the action has been deleted: testFuncs.searchStr(driver, updateAction);
	}
	
	// Note - Edit page
	
	/**
	* Add a Note from the Edit page
	* @param driver 	- given WebDriver driver
	* @param newNote	- given note for add
	*/
	public void addNoteViaEditMenu(WebDriver driver, String newNote) {
		
		testFuncs.myDebugPrinting("Add an Note from the Edit page", enumsClass.logModes.NORMAL);	  	
		testFuncs.myClick(driver, By.xpath("//*[@id=\"tab2notes\"]/div/div/p/button"), 1000);
		testFuncs.verifyStrByXpath(driver, "//*[@id=\"modalTitleId\"]", 		"Add Note");
		testFuncs.verifyStrByXpath(driver, "/html/body/div[5]/div/button[1]", 	"Add");
		testFuncs.verifyStrByXpath(driver, "/html/body/div[5]/div/button[2]", 	"Cancel");
		testFuncs.verifyStrByXpath(driver, "//*[@id=\"content-id\"]", 			"");
		testFuncs.mySendKeys(driver, By.id("content-id"), newNote, 1000);
		testFuncs.myClick(driver, By.xpath("/html/body/div[5]/div/button[1]"), 						1000);
		testFuncs.myClick(driver, By.xpath("/html/body/div[5]/div/button[1]"), 						1000);
		testFuncs.myClick(driver, By.xpath("/html/body/div[3]/div[3]/div/div[2]/div/div/p/button"), 1000);
		testFuncs.verifyStrByXpath(driver, "/html/body/div[5]/span[2]", "Meeting Successfully Modified");
		// TODO: verify the note has been added: testFuncs.searchStr(driver, newNote);

	}
	
	/**
	* Update a Note from the Edit page
	* @param driver	 - given WebDriver driver
	* @param updateNote - given note for add
	*/
	public void updateNoteViaEditMenu(WebDriver driver, String updateNote) {
		
		testFuncs.myDebugPrinting("Update the Note from the Edit page", enumsClass.logModes.NORMAL);
		testFuncs.mySendKeys(driver, By.xpath("//*[@id=\"tab2notes\"]/div/div/table/tbody/tr[1]/td[3]/input"), updateNote, 2000);
		testFuncs.myClick(driver, By.xpath("/html/body/div[3]/div[3]/div/div[2]/div/div/p/button"), 2000);
		testFuncs.verifyStrByXpath(driver, "/html/body/div[5]/span[2]", "Meeting Successfully Modified");
		// TODO: verify the note has changed: testFuncs.searchStr(driver, updateAction);
	}
	
	/**
	* Delete a Note from the Edit page
	* @param driver - given WebDriver driver
	* @param note   - the action you want to delete
	*/
	public void deleteNoteViaEditMenu(WebDriver driver, String note) {
		
		testFuncs.myDebugPrinting("Update the Note from the Edit page", enumsClass.logModes.NORMAL);
		testFuncs.myClick(driver, By.xpath("//*[@id=\"tab2notes\"]/div/div/table/tbody/tr[1]/td[4]/div/i"), 1000);
		testFuncs.verifyStrByXpath(driver, "//*[@id=\"modalTitleId\"]", "Delete Note");
		testFuncs.verifyStrByXpath(driver, "/html/body/div[5]/div/button[1]", "Delete");
		testFuncs.verifyStrByXpath(driver, "/html/body/div[5]/div/button[2]", "Cancel");
		testFuncs.myClick(driver, By.xpath("/html/body/div[5]/div/button[1]"), 1000);
		testFuncs.myClick(driver, By.xpath("/html/body/div[5]/div/button[1]"), 1000);
		testFuncs.myClick(driver, By.xpath("/html/body/div[3]/div[3]/div/div[2]/div/div/p/button"), 1000);
		testFuncs.verifyStrByXpath(driver, "/html/body/div[5]/span[2]", "Meeting Successfully Modified");
		// TODO: verify the action has been deleted: testFuncs.searchStr(driver, updateAction);
	}

	// Share With - Edit page
	
	/**
	* Add a ShareWith user from the Edit page
	* @param driver - given WebDriver driver
	* @param user	- given user for add
	* @param note	- add note to the mail
	*/
	public void addSharedViaEditMenu (WebDriver driver, String user, String note) {
		
		// Add Share With user from the Edit page
		testFuncs.myDebugPrinting("Add Share With user from the Edit page", enumsClass.logModes.NORMAL);
		testFuncs.myClick(driver, By.xpath("//*[@id=\"tab2share\"]/div/div/p/i"), 1000);
		testFuncs.verifyStrByXpath(driver, "//*[@id=\"sharePanel\"]/div[1]/div[1]/div[2]/span", "Share Meeting");
		testFuncs.myAssertTrue("Missing window close icon", driver.findElement(By.xpath("//*[@id=\"sharePanel\"]/div[1]/div[1]/div[3]/div")).getAttribute("class").contains("jsPanel-btn jsPanel-btn-close"));
		testFuncs.verifyStrByXpath(driver, "//*[@id=\"shareemail\"]", 	"");
		testFuncs.verifyStrByXpath(driver, "//*[@id=\"sharenote\"]", 	"");
		testFuncs.verifyStrByXpath(driver, "//*[@id=\"sharePanel\"]/div[2]/div/div[2]/button", "Send");
		testFuncs.mySendKeys(driver, By.id("shareemail"), user, 1000);
		testFuncs.mySendKeys(driver, By.id("sharenote"),  note, 1000);
		testFuncs.myClick(driver, By.xpath("//*[@id=\"sharePanel\"]/div[2]/div/div[2]/button"), 1000);
		testFuncs.verifyStrByXpath(driver, "/html/body/div[5]/span[2]", "Shared Meeting Successfully");
		
		// Verify create
		testFuncs.myDebugPrinting("Verify create", enumsClass.logModes.MINOR);
		testFuncs.searchStr(driver, testVars.getDomain().toUpperCase() + "\\" + testVars.getPartRegNickname());
	}
	
	/**
	* Delete a ShareWith user from the Edit page
	* @param driver - given WebDriver driver
	* @param user	- given user for delete
	*/
	public void deleteSharedViaEditMenu(WebDriver driver) {
		
		// Delete Share With user from the Edit page
		testFuncs.myDebugPrinting("Delete Share With user from the Edit page", enumsClass.logModes.NORMAL);
		testFuncs.myClick(driver, By.xpath("//*[@id=\"tab2share\"]/div/div/table/tbody/tr/td[2]/div/i"), 1000);
		testFuncs.verifyStrByXpath(driver, "//*[@id=\"modalTitleId\"]", 	   "Delete Shared User");
		testFuncs.verifyStrByXpath(driver, "//*[@id=\"modalContentId\"]",	   "Are you sure you want to delete this shared");
		testFuncs.verifyStrByXpath(driver, "/html/body/div[5]/div/button[1]",  "Delete");
		testFuncs.verifyStrByXpath(driver, "/html/body/div[5]/div/button[2]",  "Cancel");
		testFuncs.myClick(driver, By.xpath("/html/body/div[5]/div/button[1]"), 1000);
		testFuncs.verifyStrByXpath(driver, "/html/body/div[5]/span[2]", "Meeting Successfully Modified");
		
		// Verify delete
		testFuncs.myDebugPrinting("Verify delete", enumsClass.logModes.MINOR);
		String bodyText     = driver.findElement(By.tagName("body")).getText();	
		testFuncs.myAssertTrue("Share was not deleted !!", !bodyText.contains(testVars.getPartRegNickname()) &&
														   !bodyText.contains(testVars.getDomain().toUpperCase()));
	}
	
	// Delegate user - Edit page
	
	/**
	* Add a Delegate user from the Edit page
	* @param driver - given WebDriver driver
	* @param user	- given user for add
	*/
	public void addDelegateViaEditMenu (WebDriver driver, String delegate) {
		
		testFuncs.myDebugPrinting("Add Delegate user from the Edit page", enumsClass.logModes.NORMAL);
		testFuncs.myClick(driver, By.xpath("//*[@id=\"tab2delegate\"]/div/div/p/button"), 1000);
		testFuncs.verifyStrByXpath(driver, "//*[@id=\"modalTitleId\"]", 		"Add Delegated User");
		testFuncs.verifyStrByXpath(driver, "/html/body/div[5]/div/button[1]", 	"Add");
		testFuncs.verifyStrByXpath(driver, "/html/body/div[5]/div/button[2]", 	"Cancel");
		testFuncs.verifyStrByXpath(driver, "//*[@id=\"content-id\"]", "");
		testFuncs.mySendKeys(driver, By.id("content-id"), delegate, 1000);
		testFuncs.myClick(driver, By.xpath("/html/body/div[5]/div/button[1]"), 1000);
		testFuncs.myClick(driver, By.xpath("/html/body/div[5]/div/button[1]"), 1000);
		testFuncs.myClick(driver, By.xpath("/html/body/div[3]/div[3]/div/div[2]/div/div/p/button"), 1000);
		testFuncs.verifyStrByXpath(driver, "/html/body/div[5]/span[2]", "Meeting Successfully Modified");
		// TODO verify the delegate user added: testFuncs.searchStr(driver, user);
	}
	
	/**
	* Delete a Delegate user from the Edit page
	* @param driver - given WebDriver driver
	* @param user	- given user for delete
	*/
	public void deleteDelegateViaEditMenu(WebDriver driver) {
		
		testFuncs.myDebugPrinting("Delete a Delegate user from the Edit page", enumsClass.logModes.NORMAL);
		testFuncs.myClick(driver, By.xpath("//*[@id=\"tab2delegate\"]/div/div/table/tbody/tr/td[2]/div/i"), 1000);
		testFuncs.verifyStrByXpath(driver, "//*[@id=\"modalTitleId\"]",		  "Delete Delegated User");
		testFuncs.verifyStrByXpath(driver, "//*[@id=\"modalContentId\"]", 	  "Are you sure you want to delete this delegated");
		testFuncs.verifyStrByXpath(driver, "/html/body/div[5]/div/button[1]", "Delete");
		testFuncs.verifyStrByXpath(driver, "/html/body/div[5]/div/button[2]", "Cancel");
		testFuncs.myClick(driver, By.xpath("/html/body/div[5]/div/button[1]"), 1000);
		testFuncs.myClick(driver, By.xpath("/html/body/div[5]/div/button[1]"), 1000);
		testFuncs.myClick(driver, By.xpath("/html/body/div[3]/div[3]/div/div[2]/div/div/p/button"), 1000);
		testFuncs.verifyStrByXpath(driver, "/html/body/div[5]/span[2]", "Meeting Successfully Modified");
		// TODO verify the delegate user removed: testFuncs.searchStr(driver, user);
	}

	// Participants - Edit page
	
	/**
	* Add a participant from the Edit page
	* @param driver 	 - given WebDriver driver
	* @param participant - given participant for add
	*/
	public void addParticipantViaEditMenu (WebDriver driver, String participant) {
			
		testFuncs.myDebugPrinting("Add a Participant from the Edit page", enumsClass.logModes.NORMAL);
		testFuncs.myClick(driver, By.xpath("//*[@id=\"tab2part\"]/div/div/p/button"), 1000);
		testFuncs.verifyStrByXpath(driver, "//*[@id=\"modalTitleId\"]", 		"Add Participant");
		testFuncs.verifyStrByXpath(driver, "/html/body/div[5]/div/button[1]", 	"Add");
		testFuncs.verifyStrByXpath(driver, "/html/body/div[5]/div/button[2]", 	"Cancel");
		testFuncs.verifyStrByXpath(driver, "//*[@id=\"content-id\"]", 			"");
		testFuncs.mySendKeys(driver, By.id("content-id"), participant, 1000);
		testFuncs.myClick(driver, By.xpath("/html/body/div[5]/div/button[1]"), 1000);
		testFuncs.myClick(driver, By.xpath("/html/body/div[3]/div[3]/div/div[2]/div/div/p/button"), 1000);
		testFuncs.verifyStrByXpath(driver, "/html/body/div[5]/span[2]", "Meeting Successfully Modified");
		// TODO verify the participant added: testFuncs.searchStr(driver, user);
	}
	
	/**
	* Delete a participant from the Edit page
	* @param driver 	 - given WebDriver driver
	* @param participant - given participant for delete
	*/
	public void deleteParticipantViaEditMenu(WebDriver driver) {
		
		testFuncs.myDebugPrinting("Delete a Participant from the Edit page", enumsClass.logModes.NORMAL);
		testFuncs.myClick(driver, By.xpath("//*[@id=\"tab2part\"]/div/div/table/tbody/tr/td[5]/div/i"), 1000);
		testFuncs.verifyStrByXpath(driver, "//*[@id=\"modalTitleId\"]", 	  "Delete Participant");
		testFuncs.verifyStrByXpath(driver, "//*[@id=\"modalContentId\"]", 	  "Are you sure you want to delete this participant");
		testFuncs.verifyStrByXpath(driver, "/html/body/div[5]/div/button[1]", "Delete");
		testFuncs.verifyStrByXpath(driver, "/html/body/div[5]/div/button[2]", "Cancel");
		testFuncs.myClick(driver, By.xpath("/html/body/div[5]/div/button[1]"), 1000);
		testFuncs.myClick(driver, By.xpath("/html/body/div[5]/div/button[1]"), 1000);
		testFuncs.myClick(driver, By.xpath("/html/body/div[3]/div[3]/div/div[2]/div/div/p/button"), 1000);
		testFuncs.verifyStrByXpath(driver, "/html/body/div[5]/span[2]", "Meeting Successfully Modified");
		// TODO verify the participant removed: testFuncs.searchStr(driver, user);
	}

	// Attachments - Edit page	
	/**
	* Attach a file from the edit page
	* @param driver   - given WebDriver driver
	* @param path	  - given file path
	* @param fileName - given the attached file name
	*/
	public void addAttachmentViaEditMenu (WebDriver driver, String path, String fileName) {

		String ip		 = testVars.getUrl().split("/")[0];
		String meetingID = driver.getCurrentUrl().split("=")[1];
		
		testFuncs.myDebugPrinting("Attach a file from the Player page", enumsClass.logModes.NORMAL);
		String classElm1 = driver.findElement(By.xpath("//*[@id=\"fileuploader\"]/div[1]/div/form")).getAttribute("action");
		testFuncs.myAssertTrue("The ''Overwrite existing file(s)'' option is not selected by default. <" + classElm1 + ">", classElm1.contains("http://" + ip + "/api/meetings/" + meetingID + "/uploadMeetingAttachments?overrideFile=true"));
		testFuncs.myDebugPrinting("path - " + path, enumsClass.logModes.MINOR);	  		
		testFuncs.mySendKeysWithoutWait(driver, By.name("file"), path, 7000);
		
		// Click Upload
		testFuncs.myDebugPrinting("Click Upload", enumsClass.logModes.MINOR); 
		testFuncs.myClick(driver, By.xpath("//*[@id=\"tab2atts\"]/div[2]/div[2]/button"), 2000); 
		
		// Confirmation message
		testFuncs.myDebugPrinting("Confirmation message", enumsClass.logModes.MINOR); 
		testFuncs.verifyStrByXpath(driver, "//*[@id=\"modalTitleId\"]", "Upload File");
		testFuncs.verifyStrByXpath(driver, "//*[@id=\"modalContentId\"]", "Attachment uploaded successfully");
		testFuncs.myClick(driver, By.xpath("/html/body/div[6]/div/button[1]"), 2000); // click OK
		
		// Verify at ATTACHMENTS SLIDES mode that all slides are displayed
		testFuncs.myDebugPrinting("Verify at ATTACHMENTS SLIDES mode that all slides are displayed", enumsClass.logModes.MINOR); 
		testFuncs.verifyStrByXpath(driver, "/html/body/div[2]/div[3]/div[2]/div[1]/div[1]/table/tbody/tr/td[4]/div/div[1]/ul/li[1]/a", "ATTACHMENTS SLIDES");
		

	}
		
	/**
	* Delete an attachment, via the edit page
	* @param driver   - given WebDriver driver
	* @param path	  - given file path
	* @param fileName - given the attached file name
	*/
	public void deleteAttachmentViaEditMenu(WebDriver driver, String fileName) {
	
		// Delete an attachment, via the edit page
		testFuncs.myDebugPrinting("Delete an attachment, via the edit page", enumsClass.logModes.NORMAL);
		testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO_ATTACHMENTS_TAB);
		testFuncs.myAssertTrue("Missing Delete button from window.", driver.findElement(By.xpath("//*[@id=\"tab2atts\"]/div[1]/div/div[1]/div[1]/i")).getAttribute("class").contains("fa fa-trash text-danger"));
		
		// Delete
		testFuncs.myClick(driver, By.xpath("//*[@id=\"tab2atts\"]/div[1]/div/div[1]/div[1]"), 5000);
		testFuncs.myAssertTrue("Missing Question icon from window.", driver.findElement(By.xpath("/html/body/div[6]/div/div[2]")).getAttribute("class").contains("swal2-icon swal2-question"));
		testFuncs.verifyStrByXpath(driver, "//*[@id=\"modalTitleId\"]", "Delete Attachment");
		testFuncs.verifyStrByXpath(driver, "//*[@id=\"modalContentId\"]", "Are you sure you want to delete this attachment");
		testFuncs.myClick(driver, By.xpath("/html/body/div[6]/div/button[1]"), 4000);
		testFuncs.verifyStrByXpath(driver, "//*[@id=\"modalTitleId\"]"      , "Delete Attachment");
		testFuncs.verifyStrByXpath(driver, "//*[@id=\"modalContentId\"]"    , "Attachment was removed");
		testFuncs.verifyStrByXpath(driver, "/html/body/div[6]/div/button[1]", "OK");
		testFuncs.myClick(driver, By.xpath("/html/body/div[6]/div/button[1]"), 5000);
		
		// Verify the Attachments icon in the List view
		testFuncs.myDebugPrinting("Verify the Attachments icon in the List view.", enumsClass.logModes.MINOR);
		testFuncs.pressHomeIcon(driver);
		String styleElm = driver.findElement(By.xpath("//*[@id=\"example\"]/tbody[2]/tr[1]/td[5]/div/button[1]")).getAttribute("disabled");
		testFuncs.myAssertTrue("The meeting is marked as having an attachment. <" + styleElm + ">", !styleElm.isEmpty());
	}
	
	/**
	* Add a Tag via Edit menu
	* @param driver  - given WebDriver driver
	* @param tagName - Tag name
	* @param mode	 - Mode of created Tag
	*/
	public void addTagViaEditMeeting(WebDriver driver, String tagName, tagModes mode) {
		
		// Add a Tag via Edit menu
		testFuncs.myDebugPrinting("Add a Tag via Edit menu", enumsClass.logModes.NORMAL);
		testFuncs.myClick(driver, By.xpath("//*[@id='tab2tags']/div/div/p"), 3000);
		testFuncs.verifyStrByXpath(driver, "//*[@id=\"modalTitleId\"]", "Add Tag");
		Select tagNames = new Select(driver.findElement(By.id("tag-id")));
		testFuncs.myWait(2000);
		tagNames.selectByVisibleText(tagName); 
		Select visOpt = new Select(driver.findElement(By.id("visibility-id")));
		testFuncs.myWait(2000);
		visOpt.selectByVisibleText(mode.getMode()); 
		testFuncs.myClick(driver, By.xpath("/html/body/div[5]/div/button[1]"), 6000);
		ignoreChangeMeetingAgainPrompt(driver);

		// Verify add
		testFuncs.myDebugPrinting("Verify add", enumsClass.logModes.MINOR);
		testFuncs.verifyStrByXpath(driver, "//*[@id='tab2tags']/div/div/table/tbody/tr/td[3]/span", tagName);
		testFuncs.verifyStrByXpath(driver, "//*[@id='tab2tags']/div/div/table/tbody/tr/td[4]"	  , mode.getMode());
	}
	
	private void ignoreChangeMeetingAgainPrompt(WebDriver driver) {

		String bodyText = driver.findElement(By.tagName("body")).getText();	
		if (bodyText.contains("This meeting was changed before you.")) {
			
			testFuncs.verifyStrBy(driver, By.id("modalTitleId")	 , "Save Meeting");
			testFuncs.verifyStrBy(driver, By.id("modalContentId"), "This meeting was changed before you. Are you sure you want to change this meeting anyway");
			testFuncs.myClick(driver, By.xpath("/html/body/div[5]/div/button[1]"), 5000);	
		}
	}

	/**
	* Delete a Tag via Edit menu
	* @param driver  - given WebDriver driver
	* @param tagName - Tag name
	*/
	public void deleteTagViaEditMeeting(WebDriver driver, String tagName) {
		
		// Remove a Tag via Edit menu
		testFuncs.myDebugPrinting("Remove a Tag via Edit menu", enumsClass.logModes.NORMAL);
		testFuncs.myClick(driver, By.xpath("//*[@id='tab2tags']/div/div/table/tbody/tr/td[6]/div"), 3000);
		testFuncs.verifyStrBy(driver, By.id("modalTitleId")	 , "Delete Tag");
		testFuncs.verifyStrBy(driver, By.id("modalContentId"), "Are you sure you want to delete this tag");
		testFuncs.myClick(driver, By.xpath("/html/body/div[5]/div/button[1]"), 5000);
		ignoreChangeMeetingAgainPrompt(driver);
		
		// Verify delete
		testFuncs.myDebugPrinting("Verify delete", enumsClass.logModes.MINOR);	
		testFuncs.myAssertTrue("Tag <" + tagName + "> was not deleted !!", !driver.findElement(By.tagName("body")).getText().contains(tagName));
	}
	
	/**
	* Download an attachment via the the List view
	* @param driver   - given WebDriver driver
	* @param path	  - given file path
	* @param fileName - given the attached file name
	*/
	public void downloadAttachViaListView(WebDriver driver, String fileName) {

		String [] parts = fileName.split("\\.");
		String fName   = parts[0];
		String fExt    = parts[1];
		
		testFuncs.pressHomeIcon(driver);
		testFuncs.myClick(driver, By.xpath("//*[@id=\"example\"]/tbody[2]/tr[1]/td[5]/div/button[1]"), 2000);
		testFuncs.verifyStrByXpath(driver, "//*[@id=\"id_modalAttachments\"]/div/div/div/div[1]/h4", "Attachments");
		testFuncs.myClick(driver, By.xpath("//*[@id=\"id_modalAttachments\"]/div/div/div/div[2]/form/div/div/div/div/div/div/table/tbody/tr/td[5]/a/i"), 2000);
		testFuncs.myClick(driver, By.xpath("//*[@id=\"id_modalAttachments\"]/div/div/div/div[1]/button"), 2000); // exit from the Attachment window
		testFuncs.myAssertTrue("The file was not downloaded successfully via the List view !!", testFuncs.findFilesByGivenPrefix(testVars.getDownloadsPath(), "attachments_" + fName + " (1)." + fExt)); // Verify that download was successful
	}
}