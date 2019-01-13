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
* This test tests the Trimming menu on Editor Panel
* -----------------
* Tests:
* 	 - Login, Create a meeting, and enter the Editor Panel and Trimming menu
*	 1. Check that all Meeting length is free for Trimming
*	 2. Press on Trim-start and trim-end without playing
*	 3. Play the video, press the Trim-Start button, verify movie length at Preview menu
*	 4. Press Reset, verify movie length at Preview menu
*	 5. Play the video, press the Trim-End button, verify movie length at Preview menu
*	 6. Press Reset, verify movie length at Preview menu
*	 7. Delete the Meeting 
*
* Results:
*    1-6. As excepted
*      7. The Meeting should be deleted successfully
*
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test26__Meeting_editor_trimming_tests {
	
  private static WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  static  GlobalVars 	testVars;
  static  GlobalFuncs	testFuncs;
  static  Meeting		meeting;
  static  String 		meetingSubject;
  static  Trim			trim;
  static  VideoPlayer	videoPlayer;


  // Default constructor for print the name of the used browser 
  public Test26__Meeting_editor_trimming_tests(browserTypes browser) {
	  
	  this.usedBrowser = browser;
	  Log.info("Browser - "  + browser); 
  }
  
  //Define each browser as a parameter
  @SuppressWarnings("rawtypes")
  @Parameters(name="{0}")
  public static Collection data() {
	  
	  testVars    = new GlobalVars();
	  testFuncs   = new GlobalFuncs(testVars); 
	  videoPlayer = new VideoPlayer(testFuncs);
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
  public void Trimming_tests() throws Exception {
	  
	  Log.startTestCase(name.getMethodName());
	  
	  // Set data
	  meetingSubject = "meetingSubject" + testFuncs.getId();
		
	  // Login the system, create a Meeting and enter the Trimming menu
	  testFuncs.myDebugPrinting("Login the system, create a Meeting and enter the Trimming menu");  
	  testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 
	  meeting.createMeetingAfterProcMode(driver, meetingSubject, testVars.getMeetingMp4Path(), 240);
	  meeting.verifyMeeting(driver, meetingSubject);
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO_SESSION_IMAGES);
	  testFuncs.myClick(driver, By.linkText("TRIMMING"), 5000);
	  
	  // Step 1 - Check the menu appearance
	  testFuncs.myDebugPrinting("Step 1 - Check the menu appearance");  
	  testFuncs.verifyStrByXpath(driver, "//*[@id='tab2trim']/table/tbody/tr/td[1]/div/table[1]/tbody/tr[1]/td[1]" , "Start Time:"); 
	  testFuncs.verifyStrByXpath(driver, "//*[@id='tab2trim']/table/tbody/tr/td[1]/div/table[1]/tbody/tr[2]/td[1]" , "End Time:"); 
	  testFuncs.verifyStrByXpath(driver, "/html/body/div[3]/div[3]/div[1]/div[2]/div[2]/div/ul/li[3]/div/button[1]", "Preview"); 		
	  testFuncs.verifyStrByXpath(driver, "/html/body/div[3]/div[3]/div[1]/div[2]/div[2]/div/ul/li[3]/div/button[2]", "Publish"); 		

	  // Step 2 - Verify that pressing the Trim-start and Trim-end when player is paused do not affect them
	  testFuncs.myDebugPrinting("Step 2 - Verify that pressing the Trim-start and Trim-end when player is paused do not affect them");    
	  trim.checkTrimButtonsInActive(driver);
	  	  
	  // Step 3 - Play the video and Press Trim-Start button
	  testFuncs.myDebugPrinting("Step 3 - Play the video and Press Trim-Start button");	   
	  int origMovieLength = trim.getMovieLength(driver, videoPlayer);  
	  trim.playTrimMenuVideo(driver	  , 3000); 
	  trim.pressTrimStartButton(driver, 2000);	  
	  int currMovieLength = trim.getMovieLength(driver, videoPlayer);  
	  trim.pressPreviewButton(driver, 3000, meetingSubject);
	  trim.playTrimMenuVideoPreview(driver, 3000);
	  int previewLength = videoPlayer.getCurrPlayedTime(driver, "//*[@id='video_prev_id']/div[4]/div[4]/span[2]");
	  testFuncs.myAssertTrue("Video was not trim successfully ! (\n" +
							 "currMovieLength - " + currMovieLength + "\n" +
							 "previewLength - "   + previewLength   + ")",
							  previewLength == currMovieLength);	
	  trim.closePreviewMenu(driver, 2000);
	
	  // Step 4 - Press Reset button
	  testFuncs.myDebugPrinting("Step 4 - Press Reset button");	  
	  trim.resetTrimming(driver, 3000);
	  int aftreResetMovieLength = trim.getMovieLength(driver, videoPlayer);  
	  testFuncs.myAssertTrue("video was not rseet successfully! (aftreResetMovieLength - " + aftreResetMovieLength +
			  				 "origMovieLength - " 	   + origMovieLength,
			  				 aftreResetMovieLength == origMovieLength);
	  
	  // Step 5 - Play the video and Press Trim-End button
	  testFuncs.myDebugPrinting("Step 5 - Play the video and Press Trim-End button");	   
	  trim.playTrimMenuVideo(driver	  , 3000); 
	  trim.pressTrimEndButton(driver, 4000);	  
	  currMovieLength = trim.getMovieLength(driver, videoPlayer);  
	  trim.pressPreviewButton(driver, 3000, meetingSubject);
	  trim.playTrimMenuVideoPreview(driver, 3000);
	  previewLength = videoPlayer.getCurrPlayedTime(driver, "//*[@id='video_prev_id']/div[4]/div[4]/span[2]");
	  testFuncs.myAssertTrue("Video was not trim successfully ! (\n" +
							 "currMovieLength - " 					 + currMovieLength + "\n" +
							 "previewLength - "   					 + previewLength   + ")",
								    								   previewLength == currMovieLength);	
	  trim.closePreviewMenu(driver, 2000);
	  
	  // Step 6 - Press Reset button
	  testFuncs.myDebugPrinting("Step 6 - Press Reset button");	  
	  trim.resetTrimming(driver, 3000);
	  aftreResetMovieLength = trim.getMovieLength(driver, videoPlayer);  
	  testFuncs.myAssertTrue("video was not rseet successfully! (aftreResetMovieLength - " + aftreResetMovieLength +
			  				 "origMovieLength - " 	   + origMovieLength,
			  				 aftreResetMovieLength == origMovieLength);
	  
	  // Step 7 - Delete the Meeting
	  testFuncs.myDebugPrinting("Step 7 - Delete the Meeting");	  
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