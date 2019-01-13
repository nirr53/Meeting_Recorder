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
* This test tests the delete of Action/Note/Tag Meeting Video Player options
* -----------------
* Tests:
* 	 - Login the system and create meeting with different subject names
*    1. Verify that search by subject:"srMeetingName" will filter all created Meetings
*    2. Verify that search by subject:srMeetingName   will filter only whole-word names
*    3. Verify that search by srMeetingName 		  will filter only whole-word names
*    4. Verify that search by cut srMeetingName will filter all created Meetings
*    5. Verify that search by subject:"srmeetingname" (lower case letters) will filter all created Meetings
*    6. Verify that search by subject:"SRMEETINGNAME" (upper case letters) will filter all created Meetings
*    7. Delete all created Meetings
*    
* Results:
*    1-7. As excepted
*
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test13__Meeting_subject_search_tests {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  static  GlobalVars 	testVars;
  static  GlobalFuncs	testFuncs;
  static  Meeting		meeting;

  // Default constructor for print the name of the used browser 
  public Test13__Meeting_subject_search_tests(browserTypes browser) {
	  
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
  public void Subject_filter_tests() throws Exception {
	  	  
	Log.startTestCase(name.getMethodName());
	String srMeetingName     		= "meetingPrefix" + testFuncs.getId();
	String meetingNameMiddle 		= "prefix" 	  	  + srMeetingName + "suffix";
	String meetingNameMiddleSpaces  = "prefix "   	  + srMeetingName + " suffix";
	  
	// Login the system and create meeting with different subject names
	testFuncs.myDebugPrinting("Login the system and create meeting with different subject names");  
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 
	meeting.createMeetingAfterProcMode(driver, srMeetingName	, testVars.getMeetingMp4Path(), 240, false);
	meeting.createMeetingAfterProcMode(driver, meetingNameMiddle, testVars.getMeetingMp4Path(), 240, false);
	meeting.createMeetingAfterProcMode(driver, meetingNameMiddleSpaces , testVars.getMeetingMp4Path(), 240, false);
	
	// Step 1 - Verify that search by subject:"srMeetingName" will filter all created Meetings
	testFuncs.myDebugPrinting("Step 1 - Verify that search by subject:\"" + srMeetingName + "\" will filter all created Meetings");
	meeting.verifyMeeting(driver, srMeetingName);
	testFuncs.searchStr(driver, srMeetingName);
	testFuncs.searchStr(driver, meetingNameMiddle);
	testFuncs.searchStr(driver, meetingNameMiddleSpaces);

	// Step 2 - Verify that search by subject:srMeetingName will filter only whole-word names
	testFuncs.myDebugPrinting("Step 1 - Verify that search by subject:" + srMeetingName + " will filter only whole-word names");
	meeting.verifyMeetingByGivenFilter(driver,"subject:" + srMeetingName);
	testFuncs.searchStr(driver	   , srMeetingName);
	testFuncs.searchStr(driver	   , meetingNameMiddleSpaces);
	testFuncs.searchStrFalse(driver, meetingNameMiddle);
	
	// Step 3 - Verify that search by srMeetingName will filter only ones that has clear srMeetingName
	testFuncs.myDebugPrinting("Step 3 - Verify that search by " + srMeetingName + " will filter only ones that has clear srMeetingName");
	meeting.verifyMeetingByGivenFilter(driver, srMeetingName);
	testFuncs.searchStr(driver	   , srMeetingName);
	testFuncs.searchStr(driver	   , meetingNameMiddleSpaces);
	testFuncs.searchStrFalse(driver, meetingNameMiddle);
	
	// Step 4 - Verify that search by cut srMeetingName will filter all created Meetings
	testFuncs.myDebugPrinting("Step 4 - Verify that search by cut " + srMeetingName.substring(1) + " will filter all created Meetings");
	meeting.verifyMeeting(driver, srMeetingName.substring(1));
	testFuncs.searchStr(driver, srMeetingName);
	testFuncs.searchStr(driver, meetingNameMiddle);
	testFuncs.searchStr(driver, meetingNameMiddleSpaces);
	
	// Step 5 - Verify that search by subject:"srmeetingname" (lower case letters only) will filter all created Meetings
	testFuncs.myDebugPrinting("Step 5 - Verify that search by subject:\"" + srMeetingName.toLowerCase() + "\" (lower case letters only) will filter all created Meetings");	
	meeting.verifyMeetingByGivenFilter(driver,"subject:\"" + srMeetingName.toLowerCase() + "\"");
	testFuncs.searchStr(driver, srMeetingName);
	testFuncs.searchStr(driver, meetingNameMiddle);
	testFuncs.searchStr(driver, meetingNameMiddleSpaces);
	
	// Step 6 - Verify that search by subject:"SRMEETINGNAME" (upper case letters only) will filter all created Meetings
	testFuncs.myDebugPrinting("Step 6 - Verify that search by subject:\"" + srMeetingName.toUpperCase() + "\" (upper case letters only) will filter all created Meetings");
	meeting.verifyMeetingByGivenFilter(driver,"subject:\"" + srMeetingName.toUpperCase() + "\"");
	testFuncs.searchStr(driver, srMeetingName);
	testFuncs.searchStr(driver, meetingNameMiddle);
	testFuncs.searchStr(driver, meetingNameMiddleSpaces);
	
	// Step 7 - Delete all created Meetings
	testFuncs.myDebugPrinting("Step 7 - Delete all created Meetings");
	meeting.deleteMeeting(driver, meetingNameMiddleSpaces, false);
	meeting.deleteMeeting(driver, meetingNameMiddle		 , false);
	meeting.deleteMeeting(driver, srMeetingName			 , false);
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