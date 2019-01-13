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
* This test tests the Meeting Video Player upper buttons
* -----------------
* Tests:
*    1. Check the Attachments button link
*    2. Check Share Meeting button link
*    3. Check Session Images button link
*    4. Check advanced links - download, meta-data and Delete
*    
* Results:
*    1-4. As excepted
*
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test19__Meeting_video_upper_buttons {
	
  private WebDriver 	driver;
  private browserTypes  usedBrowser;
  private StringBuffer  verificationErrors = new StringBuffer();
  static  GlobalVars 	testVars;
  static  GlobalFuncs	testFuncs;
  static  Meeting		meeting;
  static  VideoPlayer	videoPlayer;

  // Default constructor for print the name of the used browser 
  public Test19__Meeting_video_upper_buttons(browserTypes browser) {
	  
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
  public void Attachments_link_tests() throws Exception {
	  	  
	Log.startTestCase(name.getMethodName()); 
	String meetingSubject = "meetingSubject" + testFuncs.getId();
	
	// Login the system, selected the pre-created Meeting and enter the Play-Video menu
	testFuncs.myDebugPrinting("Login the system, selected the pre-created Meeting and enter the Play-Video menu");  
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 
	meeting.createMeetingAfterProcMode(driver, meetingSubject, testVars.getMeetingMp4Path(), 1000); 
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);	
	
	// Step 1 - Press on the Attachments link
	testFuncs.myDebugPrinting("Step 1 - Press on the Attachments link"); 	
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO_ATTACHMENTS);
	testFuncs.verifyStrBy(driver, By.xpath("//*[@id='tab2atts']/div[2]/div[1]/div[1]/h5")		 , "Upload Attachment Files");
	testFuncs.verifyStrBy(driver, By.xpath("//*[@id='tab2atts']/div[2]/div[2]/div/label/span[2]"), "Overwrite existing file(s)");

	// Step 2 - Press on Back to previous page (Nir 11\12\18 - bug here - back return to Meeting- List)
	testFuncs.myDebugPrinting("Step 2 - Press on Back to previous page"); 
	testFuncs.myClick(driver, By.xpath("//*[@id='sidebarMenu']/nav/div[3]/ul/li[2]/span/a"), 2000);
    meeting.verifyMeeting(driver, meetingSubject);
  
	// Step 3 - Delete the created Meeting
	testFuncs.myDebugPrinting("Step 3 - Delete the created Meeting"); 
	testFuncs.pressHomeIcon(driver);
	meeting.deleteMeeting(driver, meetingSubject);
  } 
  
  @Test
  public void Share_Meeting_button_tests_link() throws Exception {
	  	  
	Log.startTestCase(name.getMethodName());
	String meetingSubject = "meetingSubject" + testFuncs.getId();

	// Login the system, selected the pre-created Meeting and enter the Play-Video menu
	testFuncs.myDebugPrinting("Login the system, selected the pre-created Meeting and enter the Play-Video menu");  
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 
	meeting.createMeetingAfterProcMode(driver, meetingSubject, testVars.getMeetingMp4Path(), 1000); 
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);	
	
	// Step 1 - Press the Share Meeting button
	testFuncs.myDebugPrinting("Step 1 - Press the Share Meeting button"); 		
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO_SHARE_MEETING);
	testFuncs.verifyStrBy(driver, By.xpath("//*[@id='sharePanel']/div[1]/div[1]/div[2]/span")								  , "Share Meeting");
	testFuncs.verifyStrBy(driver, By.xpath("//*[@id='sharePanel']/div[2]/div/div[1]/table[1]/tbody/tr[2]/td/div/div[1]/label"), "SEND FULL MEETING");
	testFuncs.verifyStrBy(driver, By.xpath("//*[@id='sharePanel']/div[2]/div/div[1]/table[1]/tbody/tr[2]/td/div/div[2]/label"), "HIGHLIGHT A PART IN THE MEETING");
	
	// Step 2 - Close the menu
	testFuncs.myDebugPrinting("Step 2 - Close the menu"); 	
	testFuncs.myClick(driver, By.xpath("//*[@id='sharePanel']/div[1]/div[1]/div[3]"), 2000);
	
	// Step 3 - Delete the created Meeting
	testFuncs.myDebugPrinting("Step 3 - Delete the created Meeting"); 
	testFuncs.pressHomeIcon(driver);
	meeting.deleteMeeting(driver, meetingSubject);
  }
  
  @Test
  public void Session_Images_button_link() throws Exception {
	  	  
	Log.startTestCase(name.getMethodName());
	String meetingSubject = "meetingSubject" + testFuncs.getId();

	// Login the system, selected the pre-created Meeting and enter the Play-Video menu
	testFuncs.myDebugPrinting("Login the system, selected the pre-created Meeting and enter the Play-Video menu");  
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 
	meeting.createMeetingAfterProcMode(driver, meetingSubject, testVars.getMeetingMp4Path(), 1000); 
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);	
	
	// Step 1 - test the Session-Images button link
	testFuncs.myDebugPrinting("Step 1 - test the Session-Images button link"); 	
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO_SESSION_IMAGES);
	testFuncs.verifyStrBy(driver, By.xpath("//*[@id='tab2slides']/div/div[1]/div[1]/span")  , "My Session");
	testFuncs.verifyStrBy(driver, By.xpath("/html/body/div[4]/div[3]/div/div/div[2]/div/ul/li[1]/a"), "SLIDES");	
	testFuncs.verifyStrBy(driver, By.xpath("/html/body/div[4]/div[3]/div/div/div[2]/div/ul/li[2]/a"), "TRIMMING");
	testFuncs.verifyStrBy(driver, By.xpath("//*[@id='tab2slides']/div/div[3]/div[2]/div/span"), "Upload file");
	testFuncs.verifyStrBy(driver, By.xpath("//*[@id='tab2slides']/div/div[1]/div[1]/span")    , "My Session");
	
	// Step 2 - Press on Back to previous page
	testFuncs.myDebugPrinting("Step 2 - Press on Back to previous page"); 
	testFuncs.myClick(driver, By.xpath("//*[@id='sidebarMenu']/nav/div[3]/ul/li[2]/span/a"), 2000);
	testFuncs.verifyStrBy(driver, By.xpath("/html/body/div[2]/div[3]/div[2]/div[1]/div[1]/table/tbody/tr/td[5]/table/tbody/tr[1]/td/ul/li[1]/a"), "COMMENTS AND TAGS");
	testFuncs.verifyStrBy(driver, By.xpath("/html/body/div[2]/div[3]/div[2]/div[1]/div[1]/table/tbody/tr/td[1]/table/tbody/tr[1]/td[2]/p")		, meetingSubject);

	// Step 3 - Delete the created Meeting
	testFuncs.myDebugPrinting("Step 3 - Delete the created Meeting"); 
	testFuncs.pressHomeIcon(driver);
	meeting.deleteMeeting(driver, meetingSubject);
  }
  
  @Test
  public void Meeting_advanced_actions() throws Exception {
	  	  
	Log.startTestCase(name.getMethodName());
	String meetingSubject 	   = "meetingSubject" + testFuncs.getId();
	String downloadMeetingname = "video_result";

	// Clean old downloads, Login the system, select the pre-created Meeting and enter the Play-Video menu
	testFuncs.myDebugPrinting("Clean old downloads, Login the system, select the pre-created Meeting and enter the Play-Video menu");  
	testFuncs.deleteFilesByPrefix(System.getProperty("user.dir"), "downloadMeetingname");		  
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 
	meeting.createMeetingAfterProcMode(driver, meetingSubject, testVars.getMeetingMp4Path(), 1000); 
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);	
	
	// Step 1 - Download Meeting
	testFuncs.myDebugPrinting("Step 1 - Download Meeting"); 
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO_ADV_BUTTONS);	
	testFuncs.clickByName(driver, "Download Meeting", 3000);
	testFuncs.myAssertTrue("Meeting was not downloaded successfully !!", testFuncs.findFilesByGivenPrefix(testVars.getDownloadsPath(), downloadMeetingname));

	// Step 2 - Edit Meta data
	testFuncs.myDebugPrinting("Step 2 - Edit Meta data"); 
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO_ADV_BUTTONS);	
	testFuncs.clickByName(driver, "Edit Meta Data", 3000);
	String [] headers = {"Subject", "Creator Name", "Organizer Name", "Start Time", "End Time", "Status"};
	for (String header : headers) {
		
		testFuncs.searchStr(driver, header);
	}
	testFuncs.myClick(driver, By.xpath("//*[@id='sidebarMenu']/nav/div[3]/ul/li[2]/span/a")		, 2000);
	
	// Step 3 - Delete a Meeting via the Advanced options toolbar
	testFuncs.myDebugPrinting("Step 3 - Delete a Meeting via the Advanced options toolbar"); 
	meeting.deleteMeetingViaPlayerMenu(driver, meetingSubject);
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