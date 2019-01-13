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
* This test tests the Add,edit & delete Actions to Meeting mechanism
* -----------------
* Tests:
*    - Login the system, Enter the Import-Meeting menu and create a tag and meeting
*    1. Add the created Tag to the created meeting
*    2. Enter the Share-meeting option
*    	-Try to submit empty data
*    	-Select 'HIGHLIGHT A PART IN THE MEETING' option and try confirm part of the meeting
*    3. Via the Play-Video menu, delete the created Tag
*    4. Delete the created Tag
*    5. Delete the created Meeting
* 
* Results:
*    1. Tag should be added properly
*    2. Share meeting options should be displayed properly
*    	- Empty data should not be displayed
*    	- Part of the Meeting allow to be displayed
*    3. Delete Tag from meeting should end successfully
*    4. Delete Tag from should end successfully
*    5. Delete the created Meeting should end successfully
*
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test5__meeting_tags_add_edit_delete {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  static GlobalVars 	testVars;
  static GlobalFuncs	testFuncs;
  static Meeting		meeting;
  static Tags			tags;

  // Default constructor for print the name of the used browser 
  public Test5__meeting_tags_add_edit_delete(browserTypes browser) {
	  
	  Log.info("Browser - "  + browser);
	  this.usedBrowser = browser;  
  }
  
  //Define each browser as a parameter
  @SuppressWarnings("rawtypes")
  @Parameters(name="{0}")
  public static Collection data() {
	  
		testVars  = new GlobalVars();
	    testFuncs = new GlobalFuncs(testVars); 
	    meeting	  = new Meeting(testFuncs, testVars);
	    tags	  = new Tags(testFuncs); 
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
  public void Meeting_signal_add_delete_Tags() throws Exception {
	  	  
	Log.startTestCase(name.getMethodName());	
	String id 			  = testFuncs.getId();
	String meetingSubject = "meetingSubject" + id;
	String tagName   	  = "myTagName" 	 + id;
	  
	// Login the system, Enter the Import-Meeting menu and create a meeting
	testFuncs.myDebugPrinting("Login the system, Enter the Import-Meeting menu and create a meeting");  
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader());   
	meeting.createMeetingAfterProcMode(driver, meetingSubject, testVars.getMeetingMp4Path(), 240);
	
    // Enter the System Settings menu and create a Tag
	testFuncs.myDebugPrinting("Enter the System Settings menu and create a Tag");  
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETTINGS);
	tags.createTag(driver, tagName);
	testFuncs.pressHomeIcon(driver);

    // Step 1 - Enter the Play-Video option of the meeting and add a Tag
	testFuncs.myDebugPrinting("Step 1 - Enter the Play-Video option of the meeting and add a Tag");  	
    meeting.verifyMeeting(driver, meetingSubject);
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);
	meeting.addTag(driver, tagName, enumsClass.tagModes.PUBLIC);
	   
	// Step 2 - Enter the Share-meeting option and check it
	testFuncs.myDebugPrinting("Step 2 - Enter the Share-meeting option and check it");  
	checkShareMeeting();
	
	// Step 3 - Via the Play-Video menu, delete the created Tag
	testFuncs.myDebugPrinting("Step 3 - Via the Play-Video menu, delete the created Tag"); 
	testFuncs.pressHomeIcon(driver);
    meeting.verifyMeeting(driver, meetingSubject);
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);
    meeting.deleteTag(driver, meetingSubject, tagName);

	// Step 4 - Delete the created tag
	testFuncs.myDebugPrinting("Step 4 - Delete the created tag");
	testFuncs.pressHomeIcon(driver);
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETTINGS);
	tags.deleteTag(driver, tagName);
    
	// Step 5 - Delete the created Meeting
	testFuncs.myDebugPrinting("Step 5 - Delete the created Meeting");
	testFuncs.pressHomeIcon(driver);
	meeting.deleteMeeting(driver, meetingSubject);
  }
  
  @Test
  public void Meeting_multiple_add_delete_Tags() throws Exception {
	  	  
	Log.startTestCase(name.getMethodName());	
	String id 			   = testFuncs.getId();
	String meetingSubject  = "meetingSubject" + id;
	String [] tagNames     = {"myFirstTagName" + id, "mySecondTagName" + id, "myThirdTagName" + id};
	  
	// Login the system, Enter the Import-Meeting menu and create a meeting
	testFuncs.myDebugPrinting("Login the system, Enter the Import-Meeting menu and create a meeting");  
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader());   
	meeting.createMeetingAfterProcMode(driver, meetingSubject, testVars.getMeetingMp4Path(), 240);
    
    // Enter the System Settings menu and create Tags
	testFuncs.myDebugPrinting("Enter the System Settings menu and create Tags");  
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETTINGS);
	for (String tagName: tagNames) {
		
		tags.createTag(driver, tagName);		
	}
	testFuncs.pressHomeIcon(driver);

    // Step 1 - Enter the Play-Video option of the meeting and add Tags
	testFuncs.myDebugPrinting("Step 1 - Enter the Play-Video option of the meeting and add Tags");  	
    meeting.verifyMeeting(driver, meetingSubject);
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);
	for (String tagName: tagNames) {
		
		meeting.addTag(driver, tagName, enumsClass.tagModes.PUBLIC);
	}
	
	// Step 2 - Delete the created Tags
	testFuncs.myDebugPrinting("Step 2 - Delete the created Tags"); 
	for (String tagName: tagNames) {
		
	    meeting.deleteTag(driver, meetingSubject, tagName);
	}

	// Step 3 - Delete the created tags
	testFuncs.myDebugPrinting("Step 3 - Delete the created tags");
	testFuncs.pressHomeIcon(driver);
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETTINGS);
	for (String tagName: tagNames) {
		
		tags.deleteTag(driver, tagName);
	}
    
	// Step 4 - Delete the created Meeting
	testFuncs.myDebugPrinting("Step 4 - Delete the created Meeting");
	testFuncs.pressHomeIcon(driver);
	meeting.deleteMeeting(driver, meetingSubject);
  }

  // Check Share Meeting option
  private void checkShareMeeting() {
	  
	  // Check menu	  
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div[3]/div[2]/div[1]/div[1]/table/tbody/tr/td[1]/table/tbody/tr[2]/td/table/tbody/tr/td[3]/table/tbody/tr/td[2]/div"), 2000);	  
	  testFuncs.verifyStrBy(driver, By.xpath("//*[@id='sharePanel']/div[2]/div/div[1]/table[1]/tbody/tr[2]/td/div/div[1]/label"), "SEND FULL MEETING");     
	  testFuncs.verifyStrBy(driver, By.xpath("//*[@id='sharePanel']/div[2]/div/div[1]/table[1]/tbody/tr[2]/td/div/div[2]/label"), "HIGHLIGHT A PART IN THE MEETING");     
	 
	  // Try to submit empty data
	  testFuncs.myDebugPrinting("Try to submit empty data", enumsClass.logModes.NORMAL);
	  testFuncs.myClick(driver, By.xpath("//*[@id='sharePanel']/div[2]/div/div[2]/button"), 100);	
	  testFuncs.verifyStrBy(driver, By.id("modalTitleId"), "Failed Action");	
	  testFuncs.verifyStrBy(driver, By.id("modalContentId"), "Bad Request. \"You must provide at least one email address\".");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[6]/div/button[1]"), 100);

	  // Select 'HIGHLIGHT A PART IN THE MEETING' option and try confirm part of the meeting
	  testFuncs.myDebugPrinting("Select 'HIGHLIGHT A PART IN THE MEETING' option and try confirm part of the meeting", enumsClass.logModes.NORMAL);
	  testFuncs.myClick(driver, By.id("radioPartial"), 100); 
	  testFuncs.verifyStrBy(driver, By.xpath("//*[@id='sharePanel']/div[2]/div/div[1]/table[2]/tbody/tr[1]/td/div/label"), "START TIME"); 
	  testFuncs.verifyStrBy(driver, By.xpath("//*[@id='sharePanel']/div[2]/div/div[1]/table[2]/tbody/tr[2]/td/div/label"), "END TIME");
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='sharePanel']/div[2]/div/div[1]/table[2]/tbody/tr[1]/td/div/div/input[2]"), "1", 200);
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='sharePanel']/div[2]/div/div[1]/table[2]/tbody/tr[2]/td/div/div/input[2]"), "4", 200);
	  testFuncs.myClick(driver, By.xpath("//*[@id='sharePanel']/div[2]/div/div[2]/button"), 100);
	  testFuncs.verifyStrBy(driver, By.id("modalTitleId"), "Failed Action");	
	  testFuncs.verifyStrBy(driver, By.id("modalContentId"), "Bad Request. \"You must provide at least one email address\".");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[6]/div/button[1]"), 100);
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