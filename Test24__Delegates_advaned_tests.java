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
* This test tests the Delegates menu
* -----------------
* Tests:
*    - Login with user and create a Meeting
*    - Add a participant to the Meeting, make it Delegate and login to it
*    1. Verify that the created Meeting is displayed
*    2. Verify that you can edit the Meeting subject
*    3. Verify that you can add an Action to the Meeting 
*    4. Verify that you can edit the Action 
*    5. Verify that you can delete the Action
*    6. Verify that you can delete the Meeting
*    
*    - Login with user and create a Meeting
*    - Add a participant to the Meeting, make it Participant and login to it
*    1. Verify that the created Meeting is displayed
*    2. Verify that you can't edit the Meeting subject
*    3. Verify that you can add an Action to the Meeting 
*    4. Verify that you can edit the Action 
*    5. Verify that you can delete the Action
*    6. Verify that you can't delete the Meeting
*    7. Delete the Meeting via the Owner
* 
* Results:
*    As excepted
*
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test24__Delegates_advaned_tests {
	
  private WebDriver 	driver, driver2;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  static  GlobalVars 	testVars;
  static  GlobalFuncs	testFuncs;
  static  Meeting		meeting;
  static  String 		meetingSubject;
  static  Participant	participant;

  // Default constructor for print the name of the used browser 
  public Test24__Delegates_advaned_tests(browserTypes browser) {
	  
	  Log.info("Browser - "  + browser);
	  this.usedBrowser = browser; 
  }
  
  //Define each browser as a parameter
  @SuppressWarnings("rawtypes")
  @Parameters(name="{0}")
  public static Collection data() {
	  
	  testVars  = new GlobalVars();
	  testFuncs = new GlobalFuncs(testVars); 
	  meeting	= new Meeting(testFuncs, testVars); 
	  participant = new Participant(testFuncs, testVars, meeting);
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
	driver2 = testFuncs.defineUsedBrowser(this.usedBrowser);
    driver2.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }
  
  @Rule
  public TestName name= new TestName();
  
  @Ignore
  @Test
  public void Delegate_actions() throws Exception {
	  		
	  Log.startTestCase(name.getMethodName());
	  String id 	  		 = testFuncs.getId();
	  String meetingSubject  = "meetingSubject" + id;
	  String updatedSubject  = "updatedSubject" + id;
	  String actionData      = "myActionData"   + id;
	  String partName 		 = testVars.getPartReg();	
	  String iconName 	     = testVars.getActionIconName();
	  String header   	  	 = "Action";
	  
	  // Login the system, create a Meeting, Add a participant, Make it Delegate and login to it
	  testFuncs.myDebugPrinting("Login the system, create a Meeting, Add a participant, Make it Delegate and login to it");  	
	  testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 	
	  meeting.createMeetingAfterProcMode(driver, meetingSubject, testVars.getMeetingPlayerMp4Path(), 1000); 		
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO			 );
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO_PARTICIPANTS);
	  participant.addParticipant(driver,  partName);
	  participant.setPartDelegate(driver, testVars.getPartRegNickname());
	  
	  // Step 1 - Login via the Delegate and verify that the created Meeting is displayed
	  testFuncs.myDebugPrinting("Step 1 - Login via the Delegate and verify that the created Meeting is displayed");  	
	  testFuncs.ntlmLogin(driver2, testVars.getUrl(), testVars.getPartRegNickname(), testVars.getPartPassword(), testVars.getGoodLoginHeader()); 	
	  meeting.verifyMeeting(driver2, meetingSubject);
	  
	  // Step 2 - Verify that you can edit the Meeting subject
	  testFuncs.myDebugPrinting("Step 2 - Verify that you can edit the Meeting subject");  		
	  meeting.updateSubject_listView(driver2, meetingSubject, updatedSubject);

	  // Step 3 - Verify that you can add an Action to the the Meeting
	  testFuncs.myDebugPrinting("Step 3 - Verify that you can add an Action to the the Meeting");
	  testFuncs.enterMenu(driver2, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);	
	  meeting.addAction(driver2, actionData, enumsClass.meetingActType.ACTION.getMode(), iconName);

	  // Step 4 - Verify that you can edit the created Action
	  testFuncs.myDebugPrinting("Step 4 - Verify that you can edit the created Action");  	
	  String newActionData = "newActiondata" + id;
	  meeting.editAction(driver2, actionData, newActionData, header, iconName);

	  // Step 5 - Verify that you can delete the created Action
	  testFuncs.myDebugPrinting("Step 5 - Verify that you can delete the created Action");  		
	  meeting.deleteAction(driver2, newActionData, header);

	  // Step 6 - Verify that you can delete the created Meeting
	  testFuncs.myDebugPrinting("Step 6 - Verify that you can delete the created Meeting");
	  testFuncs.pressHomeIcon(driver2);
	  meeting.deleteMeeting(driver2, updatedSubject);
  }
  
  @Test
  public void Participant_actions() throws Exception {
	  		
	  Log.startTestCase(name.getMethodName());
	  String id 	  		 = testFuncs.getId();
	  String meetingSubject  = "meetingSubject" + id;
	  String actionData      = "myActionData"   + id;
	  String partName 		 = testVars.getPartReg();	
	  String iconName 	     = testVars.getActionIconName();
	  String header   	  	 = "Action";
	  
	  // Login the system, create a Meeting, Add a participant, Make it Participant and login to it
	  testFuncs.myDebugPrinting("Login the system, create a Meeting, Add a participant, Make it Participant and login to it");  	
	  testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 	
	  meeting.createMeetingAfterProcMode(driver, meetingSubject, testVars.getMeetingPlayerMp4Path(), 1000); 		
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO			 );
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO_PARTICIPANTS);
	  participant.addParticipant(driver,  partName);
	  
	  // Step 1 - Login via the Participant and verify that the created Meeting is displayed
	  testFuncs.myDebugPrinting("Step 1 - Login via the Participant and verify that the created Meeting is displayed");  	
	  testFuncs.ntlmLogin(driver2, testVars.getUrl(), testVars.getPartRegNickname(), testVars.getPartPassword(), testVars.getGoodLoginHeader()); 	
	  meeting.verifyMeeting(driver2, meetingSubject);
	  
	  // Step 2 - Verify that you can't edit the Meeting subject available
	  testFuncs.myDebugPrinting("Step 2 - Verify that you can edit the Meeting subject"); 
	  testFuncs.myHover(driver2, By.id("myedittd"), 2000);
	  testFuncs.myAssertTrue("Edit subject option is available !", !driver2.findElement(By.xpath("//*[@id='myeditdiv']/img[2]")).isDisplayed());

	  // Step 3 - Verify that you can add an Action to the the Meeting
	  testFuncs.myDebugPrinting("Step 3 - Verify that you can add an Action to the the Meeting");
	  testFuncs.enterMenu(driver2, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);	
	  meeting.addAction(driver2, actionData, enumsClass.meetingActType.ACTION.getMode(), iconName);

	  // Step 4 - Verify that you can edit the created Action
	  testFuncs.myDebugPrinting("Step 4 - Verify that you can edit the created Action");  	
	  String newActionData = "newActiondata" + id;
	  meeting.editAction(driver2, actionData, newActionData, header, iconName);
	  
	  // Step 5 - Verify that you can delete the created Action
	  testFuncs.myDebugPrinting("Step 5 - Verify that you can delete the created Action");  		
	  meeting.deleteAction(driver2, newActionData, header);
	  
	  // Step 6 - Verify that you can't delete the created Meeting
	  testFuncs.myDebugPrinting("Step 6 - Verify that you can delete the created Meeting");
	  testFuncs.pressHomeIcon(driver2);
	  meeting.verifyMeeting(driver2, meetingSubject);
	  testFuncs.myAssertTrue("Button Actions is not at wanted state !", driver2.findElement(By.id("btnGroupDrop1")).getAttribute("disabled") != null);			

	  // Step 7 - Delete the created Meeting
	  testFuncs.myDebugPrinting("Step 7 - Delete the created Meeting");
	  testFuncs.pressHomeIcon(driver);
	  meeting.deleteMeeting(driver, meetingSubject);
  }

  @After
  public void tearDown() throws Exception {
	  
    driver.quit();
    driver2.quit();
    System.clearProperty("webdriver.chrome.driver");
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
    	
      testFuncs.myFail(verificationErrorString);
    }
  }
}