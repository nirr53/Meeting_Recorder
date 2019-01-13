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
* This test tests the delete of Action/Note/Tag Meeting Video Player options
* -----------------
* Tests:
*    1. Create a Meeting & Play its video
*    	Add an Action to it
*    	Replay it & verify the Action is displayed on creation time
*    	Delete the created Action
*    	Replay the video it & verify that the Action is NOT displayed on creation time
*    	Delete the created Meeting
*    2. Create a Meeting & Play its video
*    	Add a Note to it
*    	Replay it & verify the Note is displayed on creation time
*    	Delete the created Note
*    	Replay the video it & verify that the Note is NOT displayed on creation time
*    	Delete the created Meeting  
*    3. Create a Meeting & Play its video
*    	Add a Tag to it
*    	Replay it & verify the Tag is displayed on creation time
*    	Delete the added Tag
*    	Replay the video it & verify that the Tag is NOT displayed on creation time
*    	Delete the created Tag
*    	Delete the created Meeting
*    
* Results:
*    1-4. As excepted
*
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test20__Meeting_video_advanced_tests {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  static  GlobalVars 	testVars;
  static  GlobalFuncs	testFuncs;
  static  Meeting		meeting;
  static  VideoPlayer	videoPlayer;
  static Tags			tags;

  // Default constructor for print the name of the used browser 
  public Test20__Meeting_video_advanced_tests(browserTypes browser) {
	  
	  Log.info("Browser - "  + browser);
	  this.usedBrowser = browser; 
  }
  
  //Define each browser as a parameter
  @SuppressWarnings("rawtypes")
  @Parameters(name="{0}")
  public static Collection data() {
	  
	  testVars    = new GlobalVars();
	  testFuncs   = new GlobalFuncs(testVars); 
	  meeting	  = new Meeting(testFuncs	 , testVars);
	  videoPlayer = new VideoPlayer(testFuncs);    
	  tags	  	  = new Tags(testFuncs); 
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
  public void Video_verify_added_action_is_deleted() throws Exception {
	  	  
	Log.startTestCase(name.getMethodName());
	
	String id 			  =  testFuncs.getId();
	String meetingSubject = "meetingSubject" + id;
	String actionData     = "myActionData"   + id;
	String header   	  = "Action";
	String iconName 	  = testVars.getActionIconName();
	  
	// Login the system, selected the pre-created Meeting and enter the Play-Video menu
	testFuncs.myDebugPrinting("Login the system, selected the pre-created Meeting and enter the Play-Video menu");  
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 
	meeting.createMeetingAfterProcMode(driver, meetingSubject, testVars.getMeetingMp4Path(), 240);
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);	
	
	// Step 1 - Add Action and verify it displayed
	testFuncs.myDebugPrinting("Step 1 - Add Action and verify it displayed");
	videoPlayer.playVideo(driver, 2000);
	String currDisTime = meeting.addAction(driver, actionData, enumsClass.meetingActType.ACTION.getMode(), iconName);
	testFuncs.myWait(13000);
	videoPlayer.playVideo(driver, 500);
	testFuncs.myAssertTrue("Item <" + actionData + "> is not displayed at <" + currDisTime + "> !!", videoPlayer.verifyThatItemIsDisplayed(driver, currDisTime, actionData));

	// Step 2 - Delete the Added Action and verify it not displayed on next play
	testFuncs.myDebugPrinting("Step 2 - Delete the Added Action and verify it not displayed on next play"); 
	meeting.deleteAction(driver, actionData, header);
	reEnterMeeting(meetingSubject);
	videoPlayer.playVideo(driver, 1000);
	testFuncs.myAssertTrue("Item <" + actionData + "> is still displayed at <" + currDisTime + "> after delete !!", !videoPlayer.verifyThatItemIsDisplayed(driver, currDisTime, actionData));
	
	// Step 3 - Delete the created Meeting
	testFuncs.myDebugPrinting("Step 3 - Delete the created Meeting");
	testFuncs.pressHomeIcon(driver);
	meeting.deleteMeeting(driver, meetingSubject);
  }

  @Test
  public void Video_verify_added_note_is_deleted() throws Exception {
	  	  
	Log.startTestCase(name.getMethodName());
	String id 			  =  testFuncs.getId();
	String meetingSubject = "meetingSubject" + id;
	String noteData       = "myNoteData"     + id;
	String header   	  = "Note";
	String iconName 	  = testVars.getNoteIconName();
	  
	// Login the system, selected the pre-created Meeting and enter the Play-Video menu
	testFuncs.myDebugPrinting("Login the system, selected the pre-created Meeting and enter the Play-Video menu");  
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 
	meeting.createMeetingAfterProcMode(driver, meetingSubject, testVars.getMeetingMp4Path(), 240);
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);	
	
	// Step 1 - Add Action and verify it displayed
	testFuncs.myDebugPrinting("Step 1 - Add Action and verify it displayed");
	videoPlayer.playVideo(driver, 2000);
	String currDisTime = meeting.addAction(driver, noteData, enumsClass.meetingActType.NOTE.getMode(), iconName);
	testFuncs.myWait(13000);
	videoPlayer.playVideo(driver, 500);
	testFuncs.myAssertTrue("Item <" + noteData + "> is not displayed at <" + currDisTime + "> !!", videoPlayer.verifyThatItemIsDisplayed(driver, currDisTime, noteData));

	// Step 2 - Delete the Added Action and verify it not displayed on next play
	testFuncs.myDebugPrinting("Step 2 - Delete the Added Action and verify it not displayed on next play"); 
	meeting.deleteAction(driver, noteData, header);
	reEnterMeeting(meetingSubject);	
	videoPlayer.playVideo(driver, 1000);
	testFuncs.myAssertTrue("Item <" + noteData + "> is still displayed at <" + currDisTime + "> after delete !!", !videoPlayer.verifyThatItemIsDisplayed(driver, currDisTime, noteData));
	
	// Step 3 - Delete the created Meeting
	testFuncs.myDebugPrinting("Step 3 - Delete the created Meeting");
	testFuncs.pressHomeIcon(driver);
	meeting.deleteMeeting(driver, meetingSubject);
  }
  
  @Test
  public void Video_verify_added_tag_is_deleted() throws Exception {
	  	  
	Log.startTestCase(name.getMethodName());	
	String id 			  =  testFuncs.getId();
	String meetingSubject = "meetingSubject" + id;
	String tagName   	  = "myTagName" 	 + id;
	  
	// Login the system, create a Meeting and Tag and enter the Play-Video menu
	testFuncs.myDebugPrinting("Login the system, create a Meeting and Tag and enter the Play-Video menu");  
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 
	meeting.createMeetingAfterProcMode(driver, meetingSubject, testVars.getMeetingMp4Path(), 240);
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETTINGS);
	tags.createTag(driver, tagName);
	testFuncs.pressHomeIcon(driver);
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);	
	
	// Step 1 - Add Tag
	testFuncs.myDebugPrinting("Step 1 - Add Tag"); 
	videoPlayer.playVideo(driver, 2000);
	String currDisTime = meeting.addTag(driver, tagName, enumsClass.tagModes.PUBLIC);
	testFuncs.myWait(13000);
	videoPlayer.playVideo(driver, 500);
	testFuncs.myAssertTrue("Item <" + tagName + "> is not displayed at <" + currDisTime + "> !!", videoPlayer.verifyThatItemIsDisplayed(driver, currDisTime, tagName));
	
	// Step 2 - Delete the Added Action and verify it not displayed on next play
	testFuncs.myDebugPrinting("Step 2 - Delete the Added Action and verify it not displayed on next play"); 
    meeting.deleteTag(driver, meetingSubject, tagName);
	reEnterMeeting(meetingSubject);	
	videoPlayer.playVideo(driver, 1000);
	testFuncs.myAssertTrue("Item <" + tagName + "> is still displayed at <" + currDisTime + "> after delete !!", !videoPlayer.verifyThatItemIsDisplayed(driver, currDisTime, tagName));
	
	// Step 3 - Enter the Settings menu delete the created Tag
	testFuncs.myDebugPrinting("Step 3 - Enter the Settings menu delete the created Tag");  
	testFuncs.pressHomeIcon(driver);
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETTINGS);
	tags.deleteTag(driver, tagName);	
	
	// Step 4 - Delete the created Meeting
	testFuncs.myDebugPrinting("Step 4 - Delete the created Meeting");
	testFuncs.pressHomeIcon(driver);
	meeting.deleteMeeting(driver, meetingSubject);
  }

  // Re-enter Meeting
  private void reEnterMeeting(String meetingSubject) {
	  
		testFuncs.myDebugPrinting("Re-enter Meeting", enumsClass.logModes.NORMAL);
		testFuncs.pressHomeIcon(driver);
	    meeting.verifyMeeting(driver, meetingSubject);
		testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);
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