package MeetingRecorder;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.*;
import MeetingRecorder.Log;
import MeetingRecorder.enumsClass.*;

/**
* ----------------
* This test tests the Add,edit & delete Actions to Meeting mechanism
* -----------------
* Tests:
*    - Login the system, Enter the Import-Meeting menu and create a meeting
*    1. Enter the Play-Video option of the meeting and check page
*    2. Create an Note, edit it and delete it
*    3. Create multiple notes, edit them and delete them
*    4. Delete the created Meeting
* 
* Results:
*    1. Menu should be displayed properly
*  2-3. Notes should be added/edited/deleted successfully
*    4. Delete the created Meeting should end successfully
*
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test4__meeting_notes_add_edit_delete {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  static GlobalVars 	testVars;
  static GlobalFuncs	testFuncs;
  static Meeting		meeting;

  // Default constructor for print the name of the used browser 
  public Test4__meeting_notes_add_edit_delete(browserTypes browser) {
	  
	  Log.info("Browser - "  + browser);
	  this.usedBrowser = browser;  
  }
  
  //Define each browser as a parameter
  @SuppressWarnings("rawtypes")
  @Parameters(name="{0}")
  public static Collection data() {
	  
		testVars  = new GlobalVars();
	    testFuncs = new GlobalFuncs(testVars); 
	    meeting	  = new Meeting(testFuncs, testVars);  
	    return Arrays.asList(enumsClass.browserTypes.CHROME);
  }
  
  @BeforeClass 
  public static void setting_SystemProperties() {
	  
	  Log.info("System Properties seting Key value.");
  }  
  
  @Before
  public void setUp() throws Exception {
	  	

    System.setProperty("webdriver.chrome.driver", testVars.getChromeDrvPath());
	testFuncs.myDebugPrinting("Enter setUp(); usedbrowser - " + this.usedBrowser);
	driver = testFuncs.defineUsedBrowser(this.usedBrowser);
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }
  
  @Rule
  public TestName name= new TestName();

  @Test
  public void Meeting_signal_add_edit_delete_Note() throws Exception {
	  	  
	Log.startTestCase(name.getMethodName());
	String id 			  = testFuncs.getId();
	String meetingSubject = "meetingSubject" + id;
	String actionData     = "myNoteData" 	 + id;
	String header   	  = "Note";
	String iconName 	  = testVars.getNoteIconName();
	  
	// Login the system, Enter the Import-Meeting menu and create a meeting
	testFuncs.myDebugPrinting("Login the system, Enter the Import-Meeting menu and create a meeting");  
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader());   
	meeting.createMeetingAfterProcMode(driver, meetingSubject, testVars.getMeetingMp4Path(), 240);
    
    // Step 1 - Enter the Play-Video option of the meeting and add a Note
	testFuncs.myDebugPrinting("Step 1 - Enter the Play-Video option of the meeting and add a Note");  
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);
	meeting.addAction(driver, actionData, enumsClass.meetingActType.NOTE.getMode(), iconName);
	
	// Step 3 - Edit  a Note
	testFuncs.myDebugPrinting("Step 3 - Edit  a Note"); 
	String newActionData = "newNote" + id;
	meeting.editAction(driver, actionData, newActionData, header, iconName);

	// Step 4 - Delete a Note
	testFuncs.myDebugPrinting("Step 4 - Delete a Note"); 
	meeting.deleteAction(driver, newActionData, header);
	
	// Step 5 - Delete the created Meeting
	testFuncs.myDebugPrinting("Step 5 - Delete the created Meeting");
	testFuncs.pressHomeIcon(driver);
	meeting.deleteMeeting(driver, meetingSubject);
  }
  
  @Test
  public void Meeting_multiple_add_edit_delete_Note() throws Exception {
	  	  
	Log.startTestCase(name.getMethodName());
	String id 			  = testFuncs.getId();
	String meetingSubject = "meetingSubject" + id;
	String [] noteNames = {"firstNote"   + id, "secondNote"   + id, "thirdNote"   + id};
	String header   = "Note";
	String iconName = testVars.getNoteIconName();
	  
	// Login the system, Enter the Import-Meeting menu and create a meeting
	testFuncs.myDebugPrinting("Login the system, Enter the Import-Meeting menu and create a meeting");  
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader());   
	meeting.createMeetingAfterProcMode(driver, meetingSubject, testVars.getMeetingMp4Path(), 240);
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);	
	
	// Step 2 - Add multiple notes
	testFuncs.myDebugPrinting("Step 2 - Add multiple notes");
	for (String noteName : noteNames) {
		
		meeting.addAction(driver, noteName, enumsClass.meetingActType.NOTE.getMode(), iconName);	
	}
	
	// Step 3 - Edit multiple notes
	testFuncs.myDebugPrinting("Step 3 - Edit multiple notes"); 
	for (String noteName : noteNames) {
		
		meeting.editAction(driver, noteName, "new_" + noteName, header, iconName);
	}

	// Step 4 - Delete multiple notes
	testFuncs.myDebugPrinting("Step 4 - Delete multiple notes");
	for (String noteName : noteNames) {
		
		meeting.deleteAction(driver, "new_" + noteName, header);
	}

	// Step 5 - Delete the created Meeting
	testFuncs.myDebugPrinting("Step 5 - Delete the created Meeting");
	testFuncs.pressHomeIcon(driver);
	meeting.deleteMeeting(driver, meetingSubject);
  }

  @After
  public void tearDown() throws Exception {
	  
    driver.quit();
    System.clearProperty("webdriver.chrome.driver");
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
    	
      testFuncs.myFail(verificationErrorString);
    }
  }
}