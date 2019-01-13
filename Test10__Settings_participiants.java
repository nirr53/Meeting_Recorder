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
* This test tests the Participants tag
* -----------------
* Tests:
*    1. Check menu and player appearance
*    2. Add registered & unregistered participants 
*    3. Delete the created participants via the Player-Meeting menu
* 
* Results:
*    1. The menu should be displayed properly
*  	 2. The participants should be created successfully
*    3. The participants should be deleted successfully
*
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test10__Settings_participiants {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  static  GlobalVars 	testVars;
  static  GlobalFuncs	testFuncs;
  static  Meeting		meeting;
  static  Participant	participant;

  // Default constructor for print the name of the used browser 
  public Test10__Settings_participiants(browserTypes browser) {
	  
	  Log.info("Browser - "  + browser);
	  this.usedBrowser = browser; 
  }
  
  //Define each browser as a parameter
  @SuppressWarnings("rawtypes")
  @Parameters(name="{0}")
  public static Collection data() {
	  
	  testVars    = new GlobalVars();
	  testFuncs   = new GlobalFuncs(testVars); 
	  meeting	  = new Meeting(testFuncs, testVars);
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
  }
  
  @Rule
  public TestName name= new TestName();
 
  @Test
  public void Check_menu_and_player_appearnce() throws Exception {
	  	  
	Log.startTestCase(name.getMethodName());	
	String meetingSubject = "meetingSubject" + testFuncs.getId();
	  
	// Login the system, create a meeting and via Play-Video menu enter PARTICIPANTS menu
	testFuncs.myDebugPrinting("Login the system, create a meeting and via Play-Video menu enter PARTICIPANTS menu");  
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 	  
	meeting.createMeetingAfterProcMode(driver, meetingSubject, testVars.getMeetingMp4Path(), 240);	
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO			 );
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO_PARTICIPANTS);
    
    // Step 1 - Verify page
	testFuncs.myDebugPrinting("Step 1 - Verify page");
	String txt = driver.findElement(By.xpath("//*[@id='tab2parts']/div/div[1]/table/tbody/tr[1]/td[1]/input")).getAttribute("placeholder");
	testFuncs.myAssertTrue("Participant text-field was not detected !", txt.contains("Participant Name"));
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO_ADD_ACTION_NOTE_TAG);

	// Step 2 - Check that time counter is updated with the video
	testFuncs.myDebugPrinting("Step 2 - Check that time counter is updated with the video");
	VideoPlayer videoPlayer = new VideoPlayer(testFuncs); 
	videoPlayer.playVideo(driver, 5000);
	for (int i = 0; i < 5; ++i) {
			
		int timer1 = videoPlayer.getCurrPlayedTime(driver, "//*[@id='example_video_1']/div[5]/div[2]/div");	
		int timer2 = Integer.valueOf(driver.findElement(By.xpath("//*[@id='tab2details']/div[2]/div/div/table/tbody/tr[2]/td/span")).getText().split(":")[2]);
		testFuncs.myDebugPrinting("<timer1 - " + timer1 + "> --- <timer2 - " + timer2 + ">", enumsClass.logModes.MINOR);  
		testFuncs.myAssertTrue("Time counters do not match !!", Math.abs(timer2-timer1) < 2);
		testFuncs.myWait(1000);
	}
	
	// Step 3 - Delete the created Meeting
	testFuncs.myDebugPrinting("Step 3 - Delete the created Meeting");
	testFuncs.pressHomeIcon(driver);
	meeting.deleteMeeting(driver, meetingSubject);
  }
  
  @Test
  public void Add_delete_participants() throws Exception {
  	  
	Log.startTestCase(name.getMethodName());	
	String meetingSubject = "meetingSubject" + testFuncs.getId();
	  
	// Login the system, create a meeting and via Play-Video menu enter PARTICIPANTS menu
	testFuncs.myDebugPrinting("Login the system, create a meeting and via Play-Video menu enter PARTICIPANTS menu");  
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader());  
	meeting.createMeetingAfterProcMode(driver, meetingSubject, testVars.getMeetingMp4Path(), 240);
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO			 );
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO_PARTICIPANTS);
     
    // Step 1 - Add unregistered participant
	testFuncs.myDebugPrinting("Step 1 - Add unregistered participant");  
	participant.addParticipant(driver,  testVars.getPartUnreg());
	
	// Step 2 - Add registered participant
	testFuncs.myDebugPrinting("Step 2 - Add registered participant");  
	participant.addParticipant(driver,  testVars.getPartReg());
	
	// Step 3 - Delete participants via Player
	testFuncs.myDebugPrinting("Delete participants via Player"); 
	participant.deleteParticipantViaPlayerMenu(driver, testVars.getPartUnreg());
	participant.deleteParticipantViaPlayerMenu(driver, testVars.getPartRegNickname());
	
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