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
* This test tests the behavior of played Video during  an add/delete of an Item
* -----------------
* Tests:
* - Login the system and create a meeting
*  1. Play the video, pause it and add an Action
*  2. Play the video, pause it and delete an Action
*  3. Play the video and add an Action
*  4. Play the video and delete an Action
*  5. Wait till the end of the video and add an Action
*  6. Wait till the end of the video and delete an Action
*  7. Delete the created meeting
*    
* Results:
*  1-6. The played time should not be changed after every add/delete action
*    7. The Meeting should be deleted successfully
*
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test23__Meeting_video_edit_action_tests {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  static  GlobalVars 	testVars;
  static  GlobalFuncs	testFuncs;
  static  Meeting		meeting;
  static  VideoPlayer	videoPlayer;
  static  String 		meetingSubject;
  static  String 		id;

  // Default constructor for print the name of the used browser 
  public Test23__Meeting_video_edit_action_tests(browserTypes browser) {
	  
	  Log.info("Browser - "  + browser);
	  this.usedBrowser = browser; 
  }
  
  //Define each browser as a parameter
  @SuppressWarnings("rawtypes")
  @Parameters(name="{0}")
  public static Collection data() {
	  
	  testVars    = new GlobalVars();
	  testFuncs   = new GlobalFuncs(testVars); 
	  videoPlayer = new VideoPlayer(testFuncs); 
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
  public void Test0() throws Exception {
	  		
	  Log.startTestCase(name.getMethodName());
	  id 	  		 =  testFuncs.getId();
	  meetingSubject = "meetingSubject" + id;
	  
	  // Login the system and create a Meeting
	  testFuncs.myDebugPrinting("Login the system and create a Meeting");  	
	  testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 	
	  meeting.createMeetingAfterProcMode(driver, meetingSubject, testVars.getMeetingMp4Path(), 240);			
  }
  
  @Test
  public void Test1() throws Exception {
	  
	  Log.startTestCase(name.getMethodName());
	  String actionData = "actionName" + id;
	  int beforeAction = -1;
	  
	  // Login and enter the pre-created Meeting
	  testFuncs.myDebugPrinting("Login and enter the pre-created Meeting"); 
	  testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 	
	  meeting.verifyMeeting(driver, meetingSubject);  
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);
		
	  // Step 1 - Play the video, pause it and add an Action
	  testFuncs.myDebugPrinting("Step 1 - Play the video, pause it and add an Action"); 
	  videoPlayer.playVideo(driver, 2000);
	  videoPlayer.playVideo(driver, 500);
	  beforeAction = videoPlayer.getCurrPlayedTime(driver, "//*[@id='example_video_1']/div[5]/div[2]/div");
	  meeting.addAction(driver, actionData, enumsClass.meetingActType.ACTION.getMode(), testVars.getActionIconName());
	  checkIfVideoJump(beforeAction, 4);
		
	  // Step 2 - Play the video, pause it and delete the Action
	  testFuncs.myDebugPrinting("Step 2 - Play the video, pause it and delete the Action"); 
	  testFuncs.myWait(13000);
	  videoPlayer.playVideo(driver, 2000);
	  videoPlayer.playVideo(driver, 500);
	  beforeAction = videoPlayer.getCurrPlayedTime(driver, "//*[@id='example_video_1']/div[5]/div[2]/div");
	  meeting.deleteAction(driver, actionData, "Action");	
	  checkIfVideoJump(beforeAction, 4);		
  }
  
  @Test
  public void Test2() throws Exception {
	  
	  Log.startTestCase(name.getMethodName());
	  String actionData = "actionName" + id;
	  int beforeAction = -1;
	  
	  // Login and enter the pre-created Meeting
	  testFuncs.myDebugPrinting("Login and enter the pre-created Meeting"); 
	  testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 	
	  meeting.verifyMeeting(driver, meetingSubject);  
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);
		
	  // Step 3 - Play the video and add an Action
	  testFuncs.myDebugPrinting("Step 3 - Play the video and add an Action"); 
	  videoPlayer.playVideo(driver, 2000);
	  beforeAction = videoPlayer.getCurrPlayedTime(driver, "//*[@id='example_video_1']/div[5]/div[2]/div");
	  meeting.addAction(driver, actionData, enumsClass.meetingActType.ACTION.getMode(), testVars.getActionIconName());
	  checkIfVideoJump(beforeAction, 7);
		
	  // Step 4 - Play the video, pause it and delete the Action
	  testFuncs.myDebugPrinting("Step 4 - Play the video, pause it and delete the Action"); 
	  testFuncs.myWait(13000);
	  videoPlayer.playVideo(driver, 2000);
	  beforeAction = videoPlayer.getCurrPlayedTime(driver, "//*[@id='example_video_1']/div[5]/div[2]/div");
	  meeting.deleteAction(driver, actionData, "Action");	
	  checkIfVideoJump(beforeAction, 10);
  }
  
  @Test
  public void Test3() throws Exception {
	  
	  Log.startTestCase(name.getMethodName());
	  String actionData = "actionName" + id;
	  int beforeAction = -1;
	  
	  // Login and enter the pre-created Meeting
	  testFuncs.myDebugPrinting("Login and enter the pre-created Meeting"); 
	  testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 	
	  meeting.verifyMeeting(driver, meetingSubject);  
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);
		
	  // Step 5 - Wait till the end of the video and add an Action
	  testFuncs.myDebugPrinting("Step 5 - Wait till the end of the video and add an Action"); 
	  videoPlayer.playVideo(driver, 15000);
	  beforeAction = videoPlayer.getCurrPlayedTime(driver, "//*[@id='example_video_1']/div[5]/div[2]/div");
	  meeting.addAction(driver, actionData, enumsClass.meetingActType.ACTION.getMode(), testVars.getActionIconName());
	  checkIfVideoJump(beforeAction, 2);
		
	  // Step 6 - Wait till the end of the video and delete an Action
	  testFuncs.myDebugPrinting("Step 6 - Wait till the end of the video and delete an Action"); 
	  testFuncs.myWait(13000);
	  beforeAction = videoPlayer.getCurrPlayedTime(driver, "//*[@id='example_video_1']/div[5]/div[2]/div");
	  meeting.deleteAction(driver, actionData, "Action");	
	  checkIfVideoJump(beforeAction, 2);
  }
  
  @Test
  public void Test4() throws Exception {

	// Step 7 - Delete the Meeting
	testFuncs.myDebugPrinting("Step 7 - Delete the Meeting"); 
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 	
	meeting.deleteMeeting(driver, meetingSubject);
  }

  // Check if Video jumped after edit action
  private void checkIfVideoJump(int beforeAction, int maxJump) {
	  
	  testFuncs.myDebugPrinting("Check if Video jumped after edit action", enumsClass.logModes.NORMAL); 
	  int afterAction = videoPlayer.getCurrPlayedTime(driver, "//*[@id='example_video_1']/div[5]/div[2]/div");
	  int jump = afterAction - beforeAction;
	  testFuncs.myDebugPrinting("beforeAction - " + beforeAction, enumsClass.logModes.MINOR); 
	  testFuncs.myDebugPrinting("afterAction - "  + afterAction , enumsClass.logModes.MINOR); 
	  testFuncs.myDebugPrinting("jump - " 		  + jump		, enumsClass.logModes.MINOR); 
	  testFuncs.myAssertTrue("Time jump was detected !!\n" +
			  				 "<jump - " 	    + jump 		  + ">\n" +
			  				 "<beforeAction - " + beforeAction +">\n" +
			  				 "<afterAction - "  + afterAction + ">",
			  				 jump <= maxJump && jump > -1);
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