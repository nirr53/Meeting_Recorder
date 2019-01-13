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
* This test tests the Player of Trimming menu
* -----------------
* Tests:
* 	 - Login, Create a meeting, and enter the Editor Panel and Trimming menu
*	 1. Press the play and pause buttons of the video
*	 2. Press the mute and unmute buttons of the video
*	 3. Check that wave-surfer is displayed
*	 4. Check the full-screen on/off button
*	 5. press on frame and verify that movie skip to it
*	 6. Toggle between the frames
*	 7. Delete the Meeting
*
* Results:
*    1-7. As excepted
*    
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test27__Meeting_editor_trimming_player_tests {
	
  private static WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  static  GlobalVars 	testVars;
  static  GlobalFuncs	testFuncs;
  static  Meeting		meeting;
  static  String 		meetingSubject;
  static  Trim			trim;

  // Default constructor for print the name of the used browser 
  public Test27__Meeting_editor_trimming_player_tests(browserTypes browser) {
	  
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
	meeting.createMeetingAfterProcMode(driver, meetingSubject, testVars.getSlidesMp4Path(), 240);
  }
  
  @Test
  public void Test1() throws Exception {
  	  
	Log.startTestCase(name.getMethodName());
		  
	// Login the system, and enter the Trimming menu
	testFuncs.myDebugPrinting("Login the system, and enter the Trimming menu");  
	loginAndEnterTrimmingMenu();

	// Step 1 - Press the play and pause buttons of the video 
	testFuncs.myDebugPrinting("Step 1 - Press the play and pause buttons of the video");	   
	trim.playTrimMenuVideo(driver	  , 2000);
	trim.verifyPlayState(driver);
	trim.playTrimMenuVideo(driver	  , 2000);
	trim.verifyPauseState(driver);
	trim.playTrimMenuVideo(driver	  , 2000);
  }
  
  @Test
  public void Test2() throws Exception {
  	  
	Log.startTestCase(name.getMethodName());
		  
	// Login the system, and enter the Trimming menu
	testFuncs.myDebugPrinting("Login the system, and enter the Trimming menu");  
	loginAndEnterTrimmingMenu();
	
	// Step 2 - Press the mute and umute buttons of the video
	testFuncs.myDebugPrinting("Step 2 - Press the mute and umute buttons of the video");
	trim.playTrimMenuVideo(driver	  , 2000);
	trim.verifyMuteState(driver, enumsClass.muteState.UNMUTE);	
	trim.pressMuteButton(driver);	
	trim.verifyMuteState(driver, enumsClass.muteState.MUTE);
	trim.pressMuteButton(driver);  
  }
  
  @Test
  public void Test3() throws Exception {
  	  
	Log.startTestCase(name.getMethodName());
		  
	// Login the system, and enter the Trimming menu
	testFuncs.myDebugPrinting("Login the system, and enter the Trimming menu");  
	loginAndEnterTrimmingMenu();
	
	// Step 3 - Check that wave-surfer is displayed
	testFuncs.myDebugPrinting("Step 3 - Check that wave-surfer is displayed");	 
	WebElement waveElm = driver.findElement(By.xpath("//*[@id='example_video_2']/wave"));
	testFuncs.myAssertTrue("Wave surfer is not displayed !", waveElm.getAttribute("style").contains("display: block;"));
  }

  @Test
  public void Test4() throws Exception {
  	  
	Log.startTestCase(name.getMethodName());
		  
	// Login the system, and enter the Trimming menu
	testFuncs.myDebugPrinting("Login the system, and enter the Trimming menu");  
	loginAndEnterTrimmingMenu();
	 
	// Step 4 - Press the full-screen on-off
	testFuncs.myDebugPrinting("Step 4 - Press the full-screen on-off");
	trim.playTrimMenuVideo(driver	  , 2000);
	trim.pressFullScreenButton(driver);
	trim.verifyFullScreenState(driver, enumsClass.fullScreen.FULL);
	trim.pressFullScreenButton(driver);		
	trim.verifyFullScreenState(driver, enumsClass.fullScreen.NON_FULL);
  }
  
  @Test
  public void Test5() throws Exception {
  	  
	Log.startTestCase(name.getMethodName());
		  
	// Login the system, and enter the Trimming menu
	testFuncs.myDebugPrinting("Login the system, and enter the Trimming menu");  
	loginAndEnterTrimmingMenu();
	 
	// Step 5 - press on frame and verify that movie skip to it
	testFuncs.myDebugPrinting("Step 5 - press on frame and verify that movie skip to it");	
	switchFrames("//*[@id='fotorama1']/div/div[2]/div/div/div[3]", new String[]{"3:48", "3:49", "3:50", "3:51"});
	switchFrames("//*[@id='fotorama1']/div/div[2]/div/div/div[4]", new String[]{"6:20", "6:21", "6:22", "6:23"});
	switchFrames("//*[@id='fotorama1']/div/div[2]/div/div/div[5]", new String[]{"7:10", "7:11", "7:12", "7:13"});
  }
  
  @Test
  public void Test6() throws Exception {
  	  
	Log.startTestCase(name.getMethodName());
		  
	// Login the system, and enter the Trimming menu
	testFuncs.myDebugPrinting("Login the system, and enter the Trimming menu");  
	loginAndEnterTrimmingMenu();
	 
	// Step 6 - Switch between all frames
	testFuncs.myDebugPrinting("Step 6 - Switch between all frames");	
	int framesNum = driver.findElements(By.xpath("//*[@id='fotorama1']/div/div[2]/div/div/*")).size() - 1;
	testFuncs.myDebugPrinting("framesNum - " + framesNum, enumsClass.logModes.MINOR);
	int framesClicksNum = framesNum - 1;
	for (int i = 0; i < framesClicksNum; ++i) {
		
		testFuncs.myClick(driver, By.xpath("//*[@id='tr-fotorama1']/td/table/tbody/tr/td[3]/div"), 1000);
	}
	
	for (int i = 0; i < framesClicksNum; ++i) {
		
		testFuncs.myClick(driver, By.xpath("//*[@id='tr-fotorama1']/td/table/tbody/tr/td[1]/div"), 1000);
	}
  }
  
  @Test
  public void Test7() throws Exception {
  	  
	Log.startTestCase(name.getMethodName());
	
	// Step 7 - Login the system and delete the Meeting
	testFuncs.myDebugPrinting("Step 7 - Login the system and delete the Meeting");   
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader());  		
	meeting.deleteMeeting(driver, meetingSubject); 
  }

  // Login and Enter the Trimming menu
  private void loginAndEnterTrimmingMenu() {
	  
	  testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader());  		
	  meeting.verifyMeeting(driver, meetingSubject);		
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);		
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO_SESSION_IMAGES);	
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO_SESSION_IMAGES_TRIMMING);			 
  }
  
  // Click on Frame, and verify that player playing jumps to it, than pause the playing
  private void switchFrames(String frameXpath, String[] validTimes) {
	  
	  testFuncs.myClick(driver, By.xpath(frameXpath), 3000);
	  testFuncs.myHover(driver, By.xpath("//*[@id='example_video_1_html5_api']"), 0);	
	  String time = trim.getMovieCurPlayTime(driver);
	  testFuncs.myAssertTrue("Player did not return to frame ! <time - " + time + ">", checkTimes(time, validTimes));
	  testFuncs.myHover(driver, By.xpath("//*[@id='example_video_1_html5_api']"), 0);	
	  trim.playTrimMenuVideo(driver	  , 2000);
  }

  // Loop of validTimes array and return TRUE if time is one of them
  private Boolean checkTimes(String time, String[] validTimes) {
	  
	  for (String validTime : validTimes) {
		  
		  if (time.contains(validTime)) {
			  
			  return true;
		  }
	  }
	
	  return false;
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