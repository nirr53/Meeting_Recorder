package MeetingRecorder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
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
* This test tests the Upper Meeting Video Player slides options
* -----------------
* Tests:
* 	 - Create a meeting, and enter the Video of the meeting
*	 1. Check the Main-Frames / All-Frames button
*    2. Nir (12/12/18): NOT-ACTIVE - Check the Play-Frame button
*    3. Check the Frames-Gallery menu.
*    4. Check the hover and hover& play actions of Frames-Gallery.
*    5. Delete the created meeting
*
* Results:
*    1-4. As excepted
*      5. The Meeting should be deleted successfully
*
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test17__Meeting_video_sub_menu_slides {
	
  private static WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  static  GlobalVars 	testVars;
  static  GlobalFuncs	testFuncs;
  static  Meeting		meeting;
  static  VideoPlayer	videoPlayer;
  static  String 		meetingSubject;

  // Default constructor for print the name of the used browser 
  public Test17__Meeting_video_sub_menu_slides(browserTypes browser) {
	  
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
	  videoPlayer = new VideoPlayer(testFuncs, meeting);    
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
  public void A_create() throws Exception {
	  
	  Log.startTestCase(name.getMethodName());
	  
	  // Set data
	  String id 	 = testFuncs.getId();
	  meetingSubject = "meetingSubject" + id;
		
	  // Login the system, selected the pre-created Meeting and enter the Play-Video menu
	  testFuncs.myDebugPrinting("Login the system, selected the pre-created Meeting and enter the Play-Video menu");  
	  testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 
	  meeting.createMeetingAfterProcMode(driver, meetingSubject, testVars.getSlidesMp4Path(), 1000); 	  
  }
  
  @Ignore
  @Test
  public void Check_main_frames_button() throws Exception {
	  	  
	Log.startTestCase(name.getMethodName());
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 
    meeting.verifyMeeting(driver, meetingSubject);
	
	// Step 1 - Play an Action via Player Actions menu and verify that frames are displayed
	testFuncs.myDebugPrinting("Step 1 - Play an Action via Player Actions menu and verify that frames are displayed"); 
	testFuncs.pressHomeIcon(driver);	
	meeting.verifyMeeting(driver, meetingSubject);	
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);		
	int mainFramesNum = videoPlayer.getFramesNumber(driver);
	testFuncs.myAssertTrue("Main frames are not displayed !! <mainFramesNum - " + mainFramesNum + ">", mainFramesNum > 0);
	
	// Step 2 - Press All-Meeting-Frames button
	testFuncs.myDebugPrinting("Step 2 - Press All-Meeting-Frames button"); 	
	videoPlayer.toggleFramesNum(driver, enumsClass.vidPlayerFrmMode.FULL.getMode());
	int allFramesNum = videoPlayer.getFramesNumber(driver);
	testFuncs.myAssertTrue("Main frames are not displayed !! <allFramesNum - " + allFramesNum + ">", allFramesNum > mainFramesNum);
	
	// Step 3 - Press Main-Meeting-Frames button
	testFuncs.myDebugPrinting("Step 3 - Press Main-Meeting-Frames button"); 
	videoPlayer.toggleFramesNum(driver, enumsClass.vidPlayerFrmMode.MAIN.getMode());
	int mainFramesNumNew = videoPlayer.getFramesNumber(driver);
	testFuncs.myAssertTrue("Main frames number was changed !! <mainFramesNumNew - " + mainFramesNumNew + ">", mainFramesNumNew == mainFramesNum);
  }
  
  @Test
  public void Check_play_frame_option() throws Exception {
	  	  
	Log.startTestCase(name.getMethodName());
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 
    meeting.verifyMeeting(driver, meetingSubject);
	
	// Step 1 - Press on frame Play icon on beginning of Play
	testFuncs.myDebugPrinting("Step 1 - Press on frame Play icon on beginning of Play"); 
	testFuncs.pressHomeIcon(driver);	
	meeting.verifyMeeting(driver, meetingSubject);	
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);
	String frameTimestamp = driver.findElement(By.xpath("//*[@id='jssor_1']/div[1]/div/div[2]/div[1]/div[2]/div[3]/span")).getText();
	int time0 = testFuncs.timeToSeconds(frameTimestamp);	
	videoPlayer.framePressPlayButton(driver, 1);	
	int time1 = videoPlayer.getCurrPlayedTime(driver, "//*[@id='example_video_1']/div[5]/div[2]/div");	
	testFuncs.myAssertTrue("Player did not play the displayed time !", (time1 - time0) < 5);
	testFuncs.myWait(5000);
	
	// Step 2 - Wait 10 seconds and than press the Play icon again
	testFuncs.myDebugPrinting("Step 2 - Wait 10 seconds and than press the Play icon again"); 	
	testFuncs.myWait(10000);
	videoPlayer.framePressPlayButton(driver, 1);	
	int time2 = videoPlayer.getCurrPlayedTime(driver, "//*[@id='example_video_1']/div[5]/div[2]/div");	
	testFuncs.myAssertTrue("Player did not play the displayed time !", (time2 - time0) < 5);
  }
 
  @Test
  public void Check_frames_gallaery() throws Exception {
	  	  
	Log.startTestCase(name.getMethodName());
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 
	meeting.verifyMeeting(driver, meetingSubject);
	
	// Step 1 - test the Gallery / Video mode button
	testFuncs.myDebugPrinting("Step 1 - test the Gallery / Video mode button");
	testFuncs.pressHomeIcon(driver);	
	meeting.verifyMeeting(driver, meetingSubject);	
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);		
	videoPlayer.toggleDisplayMode(driver, enumsClass.vidPlayerDispMode.GALLERY.getMode());
	videoPlayer.toggleDisplayMode(driver, enumsClass.vidPlayerDispMode.PLAYER.getMode());

	// Step 2 - Verify at Gallery mode that all Main frames are displayed
	testFuncs.myDebugPrinting("Step 2 - Verify at Gallery mode that all Main frames are displayed"); 
	int videoMainFramesNum = videoPlayer.getFramesNumber(driver);
	videoPlayer.toggleDisplayMode(driver, enumsClass.vidPlayerDispMode.GALLERY.getMode());
	int galleryMainFramesNum = videoPlayer.getFramesNumberGallery(driver);
	testFuncs.myAssertTrue("Frames number do not match !!", videoMainFramesNum == galleryMainFramesNum);
	
//	// Step 3 - Verify at Gallery mode that all frames are displayed
//	testFuncs.myDebugPrinting("Step 3 - Verify at Gallery mode that all frames are displayed"); 
//	videoPlayer.toggleDisplayMode(driver, enumsClass.vidPlayerDispMode.PLAYER.getMode());
//	videoPlayer.toggleFramesNum(driver, enumsClass.vidPlayerFrmMode.FULL.getMode());
//	int videoAllFramesNum = videoPlayer.getFramesNumber(driver);  
//	testFuncs.pressHomeIcon(driver);	
//	meeting.verifyMeeting(driver, meetingSubject);	
//	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);		
//	videoPlayer.toggleDisplayMode(driver, enumsClass.vidPlayerDispMode.GALLERY.getMode());
//	int galleryAllFramesNum = videoPlayer.getFramesNumberGallery(driver);
//	testFuncs.myAssertTrue("Frames number do not match !!", videoAllFramesNum == galleryAllFramesNum);
  }
  
  @Test
  public void Check_frames_gallaery_hover_and_play() throws Exception {
	  	  
	Log.startTestCase(name.getMethodName());
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 
    meeting.verifyMeeting(driver, meetingSubject);
	
	// Enter the Gallery mode and count frames		
	testFuncs.pressHomeIcon(driver);	
	meeting.verifyMeeting(driver, meetingSubject);	
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);		
	videoPlayer.toggleDisplayMode(driver, enumsClass.vidPlayerDispMode.GALLERY.getMode());
	int galleryFramesNum = videoPlayer.getFramesNumberGallery(driver);

	// Step 1 - Hover all frames and check that main image is changed respectively
	testFuncs.myDebugPrinting("Step 1 - Hover all frames and check that main image is changed respectively");  
	List<WebElement> listImages    = driver.findElements(By.tagName("img"));	 
	List<String> 	  listFramesSrc = buildFramesSrcs(listImages,"src");
	for (int i = 1; i <= galleryFramesNum; ++i) {
		
		int realRow = (i / 4) + 1;
		int realCol = ((i % 3) == 0) ? 3 : i % 3;
		testFuncs.myDebugPrinting("<realRow - " + realRow + "> <realCol - " + realCol + ">", enumsClass.logModes.MINOR);  
		testFuncs.myHover(driver, By.xpath("/html/body/div[2]/div[3]/div[2]/div[1]/div[1]/table/tbody/tr/td[3]/div/div/div/table/tbody/tr[" + realRow + "]/td[" + realCol + "]/table")	   , 1000);	
		String srcFrame    = listFramesSrc.get(i);
		String srcMajFrame = driver.findElement(By.xpath("/html/body/div[2]/div[3]/div[2]/div[1]/div[1]/table/tbody/tr/td[1]/table/tbody/tr[3]/td/div[2]/div/div/img")).getAttribute("src");		
		srcFrame    = srcFrame.split("/out")[1].split(".jpg")[0];
		srcMajFrame = srcMajFrame.split("/out")[1].split(".png")[0];
		testFuncs.myAssertTrue("Displayed image <" + srcMajFrame + "> and hovered frame image <" + srcFrame + "> does not match !!", srcFrame.contains(srcMajFrame));
		testFuncs.myWait(2000);	
	}
	
	// Step 2 - Test the Play button of one of the frames
	testFuncs.myDebugPrinting("Step 2 - Test the Play button of one of the frames"); 
	List<String> listFramesTime = buildFramesSrcs(listImages, "numsec");
	int 				  time0 = Integer.valueOf(listFramesTime.get(2));	
	testFuncs.myDebugPrinting("time0 - "    + time0	, enumsClass.logModes.MINOR); 
	testFuncs.myHover(driver, By.xpath("/html/body/div[2]/div[3]/div[2]/div[1]/div[1]/table/tbody/tr/td[3]/div/div/div/table/tbody/tr[1]/td[2]")	   , 1000);	
	testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div[3]/div[2]/div[1]/div[1]/table/tbody/tr/td[3]/div/div/div/table/tbody/tr[1]/td[2]")	   , 1000);		
	int time1 = videoPlayer.getCurrPlayedTime(driver, "//*[@id='example_video_1']/div[5]/div[2]/div");	
	testFuncs.myDebugPrinting("time1 - "    + time1	, enumsClass.logModes.MINOR);
	testFuncs.myAssertTrue("Player was not jump till the right frame place !!", (time1 - time0) < 5);
  }

  @Test
  public void Delete_created_meeting_delete_action_delete_note_delete_tag() throws Exception {
	  	
	  Log.startTestCase(name.getMethodName());
	  
	  // Delete a Meeting
	  testFuncs.myDebugPrinting("Delete a Meeting"); 	
	  testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 
	  meeting.deleteMeeting(driver, meetingSubject);
  }
  
  // Filter the src attributes from Frames images
  private List<String> buildFramesSrcs(List<WebElement> listImages, String mode) {
	  
	  List<String> listFramesSrc =  new ArrayList<String>();
	  for( WebElement image : listImages) {
	         
		  if(image.isDisplayed()) {
	             
			  String src = image.getAttribute("src"); 
			  if (src.contains("file=slides")) {
				  
				  if (mode.contains("src")) {
	            	      	 
					  listFramesSrc.add(src);	
				  } else if (mode.contains("numsec")) {
					  
					  listFramesSrc.add(image.getAttribute("numsec"));	
				  }	  
			  }     
		  }   
	  }
	  return listFramesSrc;
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