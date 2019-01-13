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
*    1. Add the created Tag to the created meeting via main menu
*    2. Delete the created tag
*    3. Delete the created Meeting
* 
* Results:
*    1. Tag should be added properly
*    2. Delete the created Meeting should end successfully
*    3. Delete the created Tag should end successfully
*
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test6__meeting_tags_add_via_main_menu {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  static GlobalVars 	testVars;
  static GlobalFuncs	testFuncs;
  static Meeting		meeting;
  static Tags			tags;

  // Default constructor for print the name of the used browser 
  public Test6__meeting_tags_add_via_main_menu(browserTypes browser) {
	  
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
  public void Meeting_add_delete_Tags() throws Exception {
	  	  
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
    meeting.verifyMeeting(driver, meetingSubject);

    // Step 1 - Add Tag to Meeting via main menu
	testFuncs.myDebugPrinting("Step 1 - Add Tag to Meeting via main menu");
	meeting.addTagViaMainMenu(driver, tagName, enumsClass.tagModes.PUBLIC);	
	
	// Step 2 - Delete the created tag
	testFuncs.myDebugPrinting("Step 2 - Delete the created tag");
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