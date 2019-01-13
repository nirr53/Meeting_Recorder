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
* This test tests the Meeting Video Player options
* -----------------
* Tests:
* 	 - Create a meeting, and enter the Video of the meeting
*    1. Play the video, add action to it, replay it, verify the action is displayed on creation time
*    2. Play the video, add note to it, replay it, verify the action is displayed on creation time
*    3. Play the video, add tag to it, replay it, verify the action is displayed on creation time
* 	 4. Delete the created action, note & tag
*    5. Delete the created meeting
*    
* Results:
*    1-3. The selected item should be displayed properly on creation time
*  	   4. The items should be deleted successfully
*      5. The Meeting should be deleted successfully
*
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test14__Meeting_video_action_note_tag {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  static  GlobalVars 	testVars;
  static  GlobalFuncs	testFuncs;
  static  Meeting		meeting;
  static  VideoPlayer	videoPlayer;
  static Tags			tags;

  // Default constructor for print the name of the used browser 
  public Test14__Meeting_video_action_note_tag(browserTypes browser) {
	  
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
  public void Video_add_action() throws Exception {
	  	  
	Log.startTestCase(name.getMethodName());		
	String id 			  =  testFuncs.getId();
	String meetingSubject = "meetingSubject" + id;
	String actionData     = "myActionData"   + id;
	final String iconName = testVars.getActionIconName();
	  
	// Login the system, selected the pre-created Meeting and enter the Play-Video menu
	testFuncs.myDebugPrinting("Login the system, selected the pre-created Meeting and enter the Play-Video menu");  
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 
	meeting.createMeetingAfterProcMode(driver, meetingSubject, testVars.getMeetingMp4Path(), 240);
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);	
	
	// Step 2 - Add Action
	testFuncs.myDebugPrinting("Step 2 - Add Action"); 
	videoPlayer.playVideo(driver, 2000);
	String currDisTime = meeting.addAction(driver, actionData, enumsClass.meetingActType.ACTION.getMode(), iconName);
	testFuncs.myWait(13000);
	videoPlayer.playVideo(driver, 1000);
	testFuncs.myAssertTrue("Item <" + actionData + "> is not displayed at <" + currDisTime + "> !!", videoPlayer.verifyThatItemIsDisplayed(driver, currDisTime, actionData));
	
	// Step 3 - Delete the created Meeting
	testFuncs.myDebugPrinting("Step 3 - Delete the created Meeting");
	testFuncs.pressHomeIcon(driver);
	meeting.deleteMeeting(driver, meetingSubject);
  }
  
  @Test
  public void Video_add_note() throws Exception {
	  	  
	Log.startTestCase(name.getMethodName());	
	String id 			  =  testFuncs.getId();
	String meetingSubject = "meetingSubject" + id;
	String noteData       = "myNoteData"     + id;
	final String iconName = testVars.getNoteIconName();
	  
	// Login the system, selected the pre-created Meeting and enter the Play-Video menu
	testFuncs.myDebugPrinting("Login the system, selected the pre-created Meeting and enter the Play-Video menu");  
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 
	meeting.createMeetingAfterProcMode(driver, meetingSubject, testVars.getMeetingMp4Path(), 240);
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);	
	
	// Step 2 - Add Note
	testFuncs.myDebugPrinting("Step 2 - Add Note"); 
	videoPlayer.playVideo(driver, 2000);
	String currDisTime = meeting.addAction(driver, noteData, enumsClass.meetingActType.NOTE.getMode(), iconName);
	testFuncs.myWait(13000);
	videoPlayer.playVideo(driver, 500);
	testFuncs.myAssertTrue("Item <" + noteData + "> is not displayed at <" + currDisTime + "> !!", videoPlayer.verifyThatItemIsDisplayed(driver, currDisTime, noteData));
	
	// Step 3 - Delete the created Meeting
	testFuncs.myDebugPrinting("Step 3 - Delete the created Meeting");
	testFuncs.pressHomeIcon(driver);
	meeting.deleteMeeting(driver, meetingSubject);
  }
  
  @Test
  public void Video_add_tag() throws Exception {
	  	  
	Log.startTestCase(name.getMethodName());
	String id			  = testFuncs.getId();
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
	
	// Step 2 - Enter the Settings menu delete the created Tag
	testFuncs.myDebugPrinting("Step 2 - Enter the Settings menu delete the created Tag");  
	testFuncs.pressHomeIcon(driver);
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETTINGS);
	tags.deleteTag(driver, tagName);	
	
	// Step 3 - Delete the created Meeting
	testFuncs.myDebugPrinting("Step 3 - Delete the created Meeting");
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