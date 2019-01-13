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
*    1. Press the Play button
*    2. Press the Seek forward button
*    3. Press the Seek backward button
* 	 4. Press the Mute button
* 	 5. Press the Change speed button
* 	 6. Press the show / hide full screen button
*    7. Delete the created meeting
*    
* Results:
*    1-7. As excepted
*
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test15__Meeting_video_sub_menu_actions {
	
  private static WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  static  GlobalVars 	testVars;
  static  GlobalFuncs	testFuncs;
  static  Meeting		meeting;
  static  VideoPlayer	videoPlayer;
  static  String 		meetingSubject;
  private browserTypes  usedBrowser;

  // Default constructor for print the name of the used browser 
  public Test15__Meeting_video_sub_menu_actions(browserTypes browser) {
	  
	  this.usedBrowser = browser;
	  Log.info("Browser - "  + browser); 
  }
  
  //Define each browser as a parameter
  @SuppressWarnings("rawtypes")
  @Parameters(name="{0}")
  public static Collection data() {
	  
	  testVars    = new GlobalVars();
	  testFuncs   = new GlobalFuncs(testVars); 
	  meeting	  = new Meeting(testFuncs	 , testVars);
	  videoPlayer = new VideoPlayer(testFuncs);	 
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
  public TestName name = new TestName();
  
  
  @Test
  public void Test0() throws Exception {
  	  
	Log.startTestCase(name.getMethodName());
	meetingSubject = "meetingSubject" + testFuncs.getId();
	  
	// Login the system, selected the pre-created Meeting and enter the Play-Video menu
	testFuncs.myDebugPrinting("Login the system, selected the pre-created Meeting and enter the Play-Video menu");  
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 
	meeting.createMeetingAfterProcMode(driver, meetingSubject, testVars.getMeetingPlayerMp4Path(), 1000); 
  }

  @Test
  public void Test1() throws Exception {
	  	  
	Log.startTestCase(name.getMethodName());	
	
	// Step 1 - Press the Play button
	testFuncs.myDebugPrinting("Step 1 - Press the Play button");
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 
    meeting.verifyMeeting(driver, meetingSubject);
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);	
	videoPlayer.playVideo(driver, 3000);
	videoPlayer.verifyPlayState(driver);
	videoPlayer.hoverVideoPlayer(driver, 1000);
	videoPlayer.playVideo(driver, 3000);
	videoPlayer.verifyPausetate(driver);
  }
  
  @Test
  public void Test2() throws Exception {
  	  
	Log.startTestCase(name.getMethodName());	
	
	// Step 2 - Press the Seek Forwards button
	testFuncs.myDebugPrinting("Step 2 - Press the Seek Forwards button"); 
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 
    meeting.verifyMeeting(driver, meetingSubject);
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);	
	videoPlayer.playVideo(driver, 5000);
	int time1 = videoPlayer.getCurrPlayedTime(driver, "//*[@id='example_video_1']/div[5]/div[2]/div");	
	videoPlayer.pressSkipForwards(driver, 5000);
	int time2 = videoPlayer.getCurrPlayedTime(driver, "//*[@id='example_video_1']/div[5]/div[2]/div");
	testFuncs.myAssertTrue("Time was not skip forwardeds !! <time1 - " + time1 + " time 2 - " + time2 + ">", (time2 - time1) > 23);
  }
 
  @Test
  public void Test3() throws Exception {
  	  
	Log.startTestCase(name.getMethodName());	
	
	// Step 3 - Press the Seek Backwards button
	testFuncs.myDebugPrinting("Step 3 - Press the Seek Backwards button");
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 
    meeting.verifyMeeting(driver, meetingSubject);
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);	
	videoPlayer.playVideo(driver, 35000);
	int time1 = videoPlayer.getCurrPlayedTime(driver, "//*[@id='example_video_1']/div[5]/div[2]/div");	
	videoPlayer.pressSkipBackwards(driver, 5000);
	int time2 = videoPlayer.getCurrPlayedTime(driver, "//*[@id='example_video_1']/div[5]/div[2]/div");
	testFuncs.myAssertTrue("Time was not skip backwards !!", (time1 - time2) > 19);
  }
  
  @Test
  public void Test4() throws Exception {
  	  
	Log.startTestCase(name.getMethodName());	
	
	// Step 4 - Press the Mute / Unmute buttons
	testFuncs.myDebugPrinting("Step 4 - Press the Mute / Unmute buttons"); 
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 
	meeting.verifyMeeting(driver, meetingSubject);
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);	
	videoPlayer.playVideo(driver, 5000);
	videoPlayer.pressMuteButton(driver);
	videoPlayer.verifyMuteState(driver, enumsClass.muteState.UNMUTE);
	videoPlayer.pressMuteButton(driver);
	videoPlayer.verifyMuteState(driver, enumsClass.muteState.MUTE);
  }
  
  @Test
  public void Test5() throws Exception {
  	  
	Log.startTestCase(name.getMethodName());	
	
	// Step 5 - Press the Full screen / Non Full Screen buttons
	testFuncs.myDebugPrinting("Step 5 - Press the Full screen / Non Full Screnn buttons"); 
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 
	meeting.verifyMeeting(driver, meetingSubject);
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);	
	videoPlayer.playVideo(driver, 5000);
	videoPlayer.pressFullScreenButton(driver);
	videoPlayer.verifyFullScreenState(driver, enumsClass.fullScreen.FULL);
	videoPlayer.pressFullScreenButton(driver);
	videoPlayer.verifyFullScreenState(driver, enumsClass.fullScreen.NON_FULL);
  }
  
  @Test
  public void Test6() throws Exception {
  	  
	Log.startTestCase(name.getMethodName());	
	
	// Step 6 - Press the Change Speed buttons
	testFuncs.myDebugPrinting("Step 6 - Press the Change Speed buttons"); 
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 
	meeting.verifyMeeting(driver, meetingSubject);
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);	
	videoPlayer.playVideo(driver, 5000);	
	testFuncs.myDebugPrinting("Initial speed - " + videoPlayer.getCurrVideoSpeed(driver), enumsClass.logModes.MINOR); 
	videoPlayer.changeVideoSpeed(driver, "3x");	
	int time1 = videoPlayer.getCurrPlayedTime(driver, "//*[@id='example_video_1']/div[5]/div[2]/div");	
	testFuncs.myWait(5000);
	int time2 = videoPlayer.getCurrPlayedTime(driver, "//*[@id='example_video_1']/div[5]/div[2]/div");	
	testFuncs.myAssertTrue("Play speed was not changed !!", (time2 - time1) > 13);
	videoPlayer.hoverVideoPlayer(driver);
	videoPlayer.changeVideoSpeed(driver, "1x");	
  }
  
  @Test
  public void Test7() throws Exception {
	  
	  Log.startTestCase(name.getMethodName());	
		
	  // Step 7 - Delete the created Meeting
	  testFuncs.myDebugPrinting("Step 7 - Delete the created Meeting"); 
	  testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 
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