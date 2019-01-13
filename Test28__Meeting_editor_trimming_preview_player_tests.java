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
* This test tests the Preview-Player of Trimming menu
* -----------------
* Tests:
* 	 - Login, Create a meeting, and enter the Editor Panel and Trimming menu
*	 1. Via Preview button, Press the play and pause buttons of the video
*	 2. Via Preview button, Press the mute and unmute buttons of the video
*	 3. Via Preview button, Check the full-screen on/off button
*	 4. press on frame and verify that movie skip to it
*	 5. Delete the Meeting 
*
* Results:
*    1-5. As excepted
*    
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test28__Meeting_editor_trimming_preview_player_tests {
	
  private static WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  static  GlobalVars 	testVars;
  static  GlobalFuncs	testFuncs;
  static  Meeting		meeting;
  static  String 		meetingSubject;
  static  Trim			trim;

  // Default constructor for print the name of the used browser 
  public Test28__Meeting_editor_trimming_preview_player_tests(browserTypes browser) {
	  
	  this.usedBrowser = browser;
	  Log.info("Browser - "  + browser); 
  }
  
  //Define each browser as a parameter
  @SuppressWarnings("rawtypes")
  @Parameters(name="{0}")
  public static Collection data() {
	  
	  testVars    = new GlobalVars();
	  testFuncs   = new GlobalFuncs(testVars); 
	  trim		  = new Trim(testFuncs);
	  meeting	  = new Meeting(testFuncs	 , testVars);
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
	
	// Set data
	meetingSubject = "meetingSubject" + testFuncs.getId();
	  
	// Login the system and create a Meeting
	testFuncs.myDebugPrinting("Login the system and create a Meeting");  
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 
	meeting.createMeetingAfterProcMode(driver, meetingSubject, testVars.getMeetingMp4Path(), 240);
  }
  
  @Test
  public void Test1() throws Exception {
  	  
	Log.startTestCase(name.getMethodName());
		  
	// Login the system, and enter the Trimming menu
	testFuncs.myDebugPrinting("Login the system, and enter the Trimming menu");  
	loginAndEnterPreviewTrimmingMenu();
	
	// Step 1 - Press the play and pause buttons of the video 
	testFuncs.myDebugPrinting("Step 1 - Press the play and pause buttons of the video");	   
	trim.playTrimMenuVideoPreview(driver, 2000);
	trim.verifyPlayStatePreview(driver);
	trim.playTrimMenuVideoPreview(driver, 2000);
	trim.verifyPauseStatePreview(driver);
  }
  
  @Test
  public void Test2() throws Exception {
  	  
	Log.startTestCase(name.getMethodName());
		  
	// Login the system, and enter the Trimming menu
	testFuncs.myDebugPrinting("Login the system, and enter the Trimming menu");  
	loginAndEnterPreviewTrimmingMenu();
	
	// Step 2 - Press the mute and umute buttons of the video
	testFuncs.myDebugPrinting("Step 2 - Press the mute and umute buttons of the video");
	trim.playTrimMenuVideoPreview(driver	  , 2000);
	trim.verifyMuteStatePreview(driver, enumsClass.muteState.UNMUTE);	
	trim.pressMuteButtonPreview(driver);	
	trim.verifyMuteStatePreview(driver, enumsClass.muteState.MUTE);
	trim.pressMuteButtonPreview(driver);
  }
  
  @Test
  public void Test3() throws Exception {
  	  
	Log.startTestCase(name.getMethodName());
		  
	// Login the system, and enter the Trimming menu
	testFuncs.myDebugPrinting("Login the system, and enter the Trimming menu");  
	loginAndEnterPreviewTrimmingMenu();
	
	// Step 3 - Press the full-screen on-off
	testFuncs.myDebugPrinting("Step 3 - Press the full-screen on-off");
	trim.playTrimMenuVideoPreview(driver	  , 2000);
	trim.pressFullScreenButtonPreview(driver);
	trim.verifyFullScreenStatePreview(driver, enumsClass.fullScreen.FULL);
	trim.pressFullScreenButtonPreview(driver);		
	trim.verifyFullScreenStatePreview(driver, enumsClass.fullScreen.NON_FULL);
  }
  
  @Test
  public void Test4() throws Exception {
  	  
	Log.startTestCase(name.getMethodName());
	String time;
		  
	// Login the system, and enter the Trimming menu
	testFuncs.myDebugPrinting("Login the system, and enter the Trimming menu");  
	loginAndEnterPreviewTrimmingMenu();
	 
	// Step 4 - press on frame and verify that movie skip to it
	testFuncs.myDebugPrinting("Step 4 - press on frame and verify that movie skip to it");	
	testFuncs.myClick(driver, By.xpath("//*[@id='fotorama']/div/div[2]/div/div/div[3]"), 3000);
	time = trim.getPreviewMovieCurPlayTime(driver);
	testFuncs.myAssertTrue("Player did not return to frame ! <time - " + time + ">", time.contains("0:13") || time.contains("0:12"));
	trim.waitTillEndOfVideoPreview(driver);
	testFuncs.myClick(driver, By.xpath("//*[@id='fotorama']/div/div[2]/div/div/div[2]"), 0);
	trim.hoverPreviewVideoPlayer(driver);
	trim.hoverPreviewVideoPlayer(driver);
	time = trim.getPreviewMovieCurPlayTime(driver);
	testFuncs.myAssertTrue("Player did not return to frame ! <time - " + time + ">", time.contains("0:01") || time.contains("0:02") || time.contains("0:03"));
  }
  
  @Test
  public void Test5() throws Exception {
  	  
	Log.startTestCase(name.getMethodName());
		  
	// Step 5 - Login the system and delete the Meeting
	testFuncs.myDebugPrinting("Step 5 - Login the system and delete the Meeting");   
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader());  		
	meeting.deleteMeeting(driver, meetingSubject); 
  }

  // Login and Enter the Preview-Player of Trimming menu
  private void loginAndEnterPreviewTrimmingMenu() {
	  
	  testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader());  		
	  meeting.verifyMeeting(driver, meetingSubject);		
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);		
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO_SESSION_IMAGES);	
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO_SESSION_IMAGES_TRIMMING);	
	  testFuncs.myClick(driver, By.xpath("/html/body/div[3]/div[3]/div[1]/div[2]/div[2]/div/ul/li[3]/div/button[1]"), 2000);	  
	  testFuncs.verifyStrByXpath(driver, "//*[@id='myModalVideo']/div/div/div[1]/h3", meetingSubject.toUpperCase());
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