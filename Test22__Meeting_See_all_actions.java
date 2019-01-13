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
* This test tests the See-All-ac-New and See-All buttons
* -----------------
* Tests:
* Login the system, selected the pre-created Meeting and enter the Play-Video menu
* 	For Action and Note:
*    1. Verify that See-All button is activate
*    2. Play Action via See-all toolbar
*    3. Delete Action via See-all toolbar
*    4. Delete the created Meeting
*    
* Results:
*    1-4. As excepted
*
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test22__Meeting_See_all_actions {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  static  GlobalVars 	testVars;
  static  GlobalFuncs	testFuncs;
  static  Meeting		meeting;
  static  VideoPlayer	videoPlayer;
  static Tags			tags;

  // Default constructor for print the name of the used browser 
  public Test22__Meeting_See_all_actions(browserTypes browser) {
	  
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
  public void See_all_action_toolbar_tests() throws Exception {
	  	  
	Log.startTestCase(name.getMethodName());
	String id 			  =  testFuncs.getId();
	String meetingSubject = "meetingSubject" + id;
	String actionData     = "myActionData"   + id;
	  
	// Login the system, selected the pre-created Meeting and enter the Play-Video menu
	testFuncs.myDebugPrinting("Login the system, selected the pre-created Meeting and enter the Play-Video menu");  
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 
	meeting.createMeetingAfterProcMode(driver, meetingSubject, testVars.getMeetingMp4Path(), 240);
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);	
	meeting.addAction(driver, actionData, enumsClass.meetingActType.ACTION.getMode(), testVars.getActionIconName());

	// Step 1 - Verify that See-All button is activate
	testFuncs.myDebugPrinting("Step 1 - Verify that See-All button is activate"); 
	testFuncs.pressHomeIcon(driver);
	meeting.verifyMeeting(driver, meetingSubject);
	testFuncs.myAssertTrue("See All button for Meeting <" + meetingSubject + "> is disabled !!", driver.findElement(By.xpath("//*[@id='example']/tbody[2]/tr/td[7]/button[1]")).getAttribute("disabled") == null);

	// Step 2 - Play Action via See-all toolbar
	testFuncs.myDebugPrinting("Step 2 - Play Action via See-all toolbar"); 
	meeting.pressSeeAllButton(driver);
	meeting.playActionViaSeeAllToolBar(driver, actionData);
	
	// Step 3 - Delete Action via See-all toolbar
	testFuncs.myDebugPrinting("Step 3 - Delete Action via See-all toolbar"); 
	testFuncs.pressHomeIcon(driver);
	meeting.verifyMeeting(driver, meetingSubject);
	meeting.pressSeeAllButton(driver);
	meeting.deleteActionViaSeeAllToolBar(driver, actionData, meetingSubject, "Action");

	// Step 4 - Delete the created Meeting
	testFuncs.myDebugPrinting("Step 4 - Delete the created Meeting");
	testFuncs.pressHomeIcon(driver);
	meeting.deleteMeeting(driver, meetingSubject);
  }
  
  @Test
  public void See_all_note_toolbar_tests() throws Exception {
	  	  
	Log.startTestCase(name.getMethodName());
	String id 			  =  testFuncs.getId();
	String meetingSubject = "meetingSubject" + id;
	String noteData       = "myNoteData"     + id;
	  
	// Login the system, selected the pre-created Meeting and enter the Play-Video menu
	testFuncs.myDebugPrinting("Login the system, selected the pre-created Meeting and enter the Play-Video menu");  
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 
	meeting.createMeetingAfterProcMode(driver, meetingSubject, testVars.getMeetingMp4Path(), 240);
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);	
	meeting.addAction(driver, noteData, enumsClass.meetingActType.NOTE.getMode(), testVars.getNoteIconName());

	// Step 1 - Verify that See-All button is activate
	testFuncs.myDebugPrinting("Step 1 - Verify that See-All button is activate"); 
	testFuncs.pressHomeIcon(driver);
	meeting.verifyMeeting(driver, meetingSubject);
	testFuncs.myAssertTrue("See All button for Meeting <" + meetingSubject + "> is disabled !!", driver.findElement(By.xpath("//*[@id='example']/tbody[2]/tr/td[7]/button[1]")).getAttribute("disabled") == null);

	// Step 2 - Play Action via See-all toolbar
	testFuncs.myDebugPrinting("Step 2 - Play Action via See-all toolbar"); 
	meeting.pressSeeAllButton(driver);
	meeting.playActionViaSeeAllToolBar(driver, noteData);
	
	// Step 3 - Delete Action via See-all toolbar
	testFuncs.myDebugPrinting("Step 3 - Delete Action via See-all toolbar"); 
	testFuncs.pressHomeIcon(driver);
	meeting.verifyMeeting(driver, meetingSubject);
	meeting.pressSeeAllButton(driver);
	meeting.deleteActionViaSeeAllToolBar(driver, noteData, meetingSubject, "Note");

	// Step 4 - Delete the created Meeting
	testFuncs.myDebugPrinting("Step 4 - Delete the created Meeting");
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