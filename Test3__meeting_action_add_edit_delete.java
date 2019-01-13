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
*    2. Create an Action, edit it and delete it
*    3. Create multiple actions, edit them and delete them
*    4. Delete the created Meeting
* 
* Results:
*    1. Menu should be displayed properly
*  2-3. Actions should be added/edited/deleted successfully
*    4. Delete the created Meeting should end successfully
*
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test3__meeting_action_add_edit_delete {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  static GlobalVars 	testVars;
  static GlobalFuncs	testFuncs;
  static Meeting		meeting;

  // Default constructor for print the name of the used browser 
  public Test3__meeting_action_add_edit_delete(browserTypes browser) {
	  
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
  public void Meeting_signal_add_edit_delete_Action() throws Exception {
	  	  
	Log.startTestCase(name.getMethodName());
	String id 			  = testFuncs.getId();
	String meetingSubject = "meetingSubject" + id;
	String actionData     = "myActionData"   + id;
	String header   	  = "Action";
	String iconName 	  = testVars.getActionIconName();
	  
	// Login the system, Enter the Import-Meeting menu and create a meeting
	testFuncs.myDebugPrinting("Login the system, Enter the Import-Meeting menu and create a meeting");  
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader());   
	meeting.createMeetingAfterProcMode(driver, meetingSubject, testVars.getMeetingMp4Path(), 240);
	
    // Step 1 - Enter the Play-Video option of the meeting and check page
	testFuncs.myDebugPrinting("Step 1 - Enter the Play-Video option of the meeting and check page");  
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);	
	verifyPage(meetingSubject);
	
	// Step 2 - Add action
	testFuncs.myDebugPrinting("Step 2 - Add action"); 
	meeting.addAction(driver, actionData, enumsClass.meetingActType.ACTION.getMode(), iconName);
	
	// Step 3 - Edit action
	testFuncs.myDebugPrinting("Step 3 - Edit action"); 
	String newActionData = "newActiondata" + id;
	meeting.editAction(driver, actionData, newActionData, header, iconName);

	// Step 4 - Delete action
	testFuncs.myDebugPrinting("Step 4 - Delete action"); 
	meeting.deleteAction(driver, newActionData, header);

	// Step 5 - Delete the created Meeting
	testFuncs.myDebugPrinting("Step 5 - Delete the created Meeting");
	testFuncs.pressHomeIcon(driver);
	meeting.deleteMeeting(driver, meetingSubject);
  }
  
  @Test
  public void Meeting_multiple_add_edit_delete_Action() throws Exception {
	  	  
	Log.startTestCase(name.getMethodName());
	String id 			  = testFuncs.getId();
	String meetingSubject = "meetingSubject" + id;
	String [] actionNames = {"firstAction"   + id, "secondAction"   + id, "thirdAction"   + id};
	String header   	  = "Action";
	String iconName 	  = testVars.getActionIconName();
	  
	// Login the system, Enter the Import-Meeting menu and create a meeting
	testFuncs.myDebugPrinting("Login the system, Enter the Import-Meeting menu and create a meeting");  
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader());   
	meeting.createMeetingAfterProcMode(driver, meetingSubject, testVars.getMeetingMp4Path(), 240);
    meeting.verifyMeeting(driver, meetingSubject);
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);	
	
	// Step 2 - Add multiple actions
	testFuncs.myDebugPrinting("Step 2 - Add multiple actions");
	for (String actionName : actionNames) {
		
		meeting.addAction(driver, actionName, enumsClass.meetingActType.ACTION.getMode(), iconName);	
	}
	
	// Step 3 - Edit multiple actions
	testFuncs.myDebugPrinting("Step 3 - Edit multiple actions"); 
	for (String actionName : actionNames) {
		
		meeting.editAction(driver, actionName, "new_" + actionName, header, testVars.getActionIconName());
	}

	// Step 4 - Delete multiple actions
	testFuncs.myDebugPrinting("Step 4 - Delete multiple actions");
	for (String actionName : actionNames) {
		
		meeting.deleteAction(driver, "new_" + actionName, header);
	}

	// Step 5 - Delete the created Meeting
	testFuncs.myDebugPrinting("Step 5 - Delete the created Meeting");
	testFuncs.pressHomeIcon(driver);
	meeting.deleteMeeting(driver, meetingSubject);
  }

  // Verify Meeting page
  private void verifyPage(String subject) {
	  
	  testFuncs.verifyStrByXpath(driver, "/html/body/div[2]/div[3]/div[2]/div[1]/div[1]/table/tbody/tr/td[1]/table/tbody/tr[1]/td[2]/p"		 , subject); 
	  testFuncs.verifyStrByXpath(driver, "/html/body/div[2]/div[3]/div[2]/div[1]/div[1]/table/tbody/tr/td[5]/table/tbody/tr[1]/td/ul/li[1]/a", "COMMENTS AND TAGS");  
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