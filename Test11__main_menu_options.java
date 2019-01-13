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
import org.openqa.selenium.support.ui.Select;

import MeetingRecorder.Log;
import MeetingRecorder.enumsClass.*;

/**
* ----------------
* This test tests the Main menu options
* -----------------
* Tests:
*    1. Login and check the Audiocodes link at button
*    2. Check different display methods for the Meetings list
*    	- Press the calendar-view button
*    	- Press the title-view button
*     	- Press the list-view button
*    3. Check number of Meetings per page
* 
* Results:
*    1. The link should reroute you to Audiocodes page
*  	 2. Different display methods should be displayed properly.
*  	 3. Displayed number should match the selected criteria
*
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test11__main_menu_options {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  static GlobalVars 	testVars;
  static GlobalFuncs	testFuncs;
  static Meeting		meeting;

  // Default constructor for print the name of the used browser 
  public Test11__main_menu_options(browserTypes browser) {
	  
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
  public void Main_menu_audiocodes_link() throws Exception {
	  	  
	Log.startTestCase(name.getMethodName());	
	  
	// Login the system
	testFuncs.myDebugPrinting("Login the system");  
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 

	// Step 1 - Press the Audiocodes link
	testFuncs.myDebugPrinting("Step 1 - Press the Audiocodes link");
	testFuncs.myClick(driver, By.linkText("Site By Audiocodes Group"), 10000);
	String bodyText  = driver.findElement(By.tagName("body")).getText();	
	if (!bodyText.contains("Request unsuccessful. Incapsula incident ID:")) {
		
		testFuncs.searchStr(driver, "SOLUTIONS & PRODUCTS");
		testFuncs.searchStr(driver, "SERVICES & SUPPORT");
		testFuncs.searchStr(driver, "PARTNERS");
		testFuncs.searchStr(driver, "Resource center");
		testFuncs.searchStr(driver, "Contact us");
		testFuncs.searchStr(driver, "Skype for Business & Microsoft Teams");
	}
  }
  
  @Test
  public void Main_menu_different_display_modes() throws Exception {
	  	  
	Log.startTestCase(name.getMethodName());	
	  
	// Login the system
	testFuncs.myDebugPrinting("Login the system");  
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 

	// Step 1 - Press the calendar-view button
	testFuncs.myDebugPrinting("Step 1 - Press the calendar-view button");
	testFuncs.myClick(driver, By.id("calendar"), 5000);	
	verifyCalenderView();
	
	// Step 2 - Press the title-view button
	testFuncs.myDebugPrinting("Step 2 - Press the title-view button");
	testFuncs.myClick(driver, By.id("tile")    , 5000);
	verifyTitleView();
	
	// Step 3 - Press the list-view button
	testFuncs.myDebugPrinting("Step 3 - Press the list-view button");
	testFuncs.myClick(driver, By.id("list")    , 5000);
	testFuncs.searchStr(driver, "DATE DURATION OWNER SUBJECT DATA");
  }
  
  @Test
  public void Number_of_meeting_per_page_tests() throws Exception {
	  	  
	Log.startTestCase(name.getMethodName());	
	String usedMp4File 	 	 = testVars.getMeetingMp4Path();
	String meetingPrefix	 = "numOfMeetingTest" + testFuncs.getId();
	final int maxMeetingsNum = 10;
  
	// Login the system and create 10 meetings
	testFuncs.myDebugPrinting("Login the system and create 10 meetings");  
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 
	for (int i = 0; i < maxMeetingsNum; ++i) {
		
		testFuncs.myDebugPrinting("Create Meeting <" + meetingPrefix + "_" + i + ">");  
		meeting.createMeetingAfterProcMode(driver, meetingPrefix + "_" + i, usedMp4File, 240, false);
	}
	
	// Step 1 - Filter the Meeting by meetingPrefix and verify that only 7 Meetings are displayed
	testFuncs.myDebugPrinting("Step 1 - Filter the Meeting by " + meetingPrefix + "and verify that only 7 Meetings are displayed");
    new Select(driver.findElement(By.id("maxRows"))).selectByVisibleText("7");
    testFuncs.myWait(2000);
	meeting.verifyMeeting(driver, meetingPrefix);
	String bodyText     = driver.findElement(By.tagName("body")).getText();	
	String temp 	    = bodyText.replace(meetingPrefix, meetingPrefix.substring(1));
	int meetingNum 		= (bodyText.length() - temp.length());
	testFuncs.myAssertTrue("7 Meetings are not displayed ! <meetingNum - " + meetingNum + ">", meetingNum == 7);
 
	// Step 2 - Filter the Meeting by meetingPrefix and verify that only 10 Meetings are displayed
	testFuncs.myDebugPrinting("Step 2 - Filter the Meeting by " + meetingPrefix + "and verify that only 10 Meetings are displayed");
    new Select(driver.findElement(By.id("maxRows"))).selectByVisibleText("10");
    testFuncs.myWait(2000);
	meeting.verifyMeeting(driver, meetingPrefix);
	bodyText     = driver.findElement(By.tagName("body")).getText();	
	temp 	    = bodyText.replace(meetingPrefix, meetingPrefix.substring(1));
	meetingNum 		= (bodyText.length() - temp.length());
	testFuncs.myAssertTrue("10 Meetings are not displayed ! <meetingNum - " + meetingNum + ">", meetingNum == 10);

	// Step 3 - Delete the created Meetings
	testFuncs.myDebugPrinting("Step 3 - Delete the created Meetings");
	for (int i = 0; i < maxMeetingsNum; ++i) {
		
		meeting.deleteMeeting(driver, meetingPrefix + "_" + i, false);		
	}
  }

  private void verifyTitleView() {
	  
	  testFuncs.myDebugPrinting("Check month", enumsClass.logModes.MINOR);
	  String date = testFuncs.getDateTime("MMMM yyyy");
	  
	  // Verify that Data is not displayed
	  testFuncs.myDebugPrinting("Verify that Data is not displayed", enumsClass.logModes.MINOR);
	  String bodyText     = driver.findElement(By.tagName("body")).getText();	
	  testFuncs.myAssertTrue("Data <" + date + "> is displayed !!", !bodyText.contains(date));
	  
	  // Verify that column titles are not displayed
	  testFuncs.myDebugPrinting("Verify that column titles are not displayed", enumsClass.logModes.MINOR);
	  String [] titles = {"DATE", "DURATION", "OWNER", "SUBJECT", "DATA", "TAGS", "ACTIONS AND NOTES"};	  
	  for (String title : titles) {
		  
		  testFuncs.myAssertTrue("Title <" + title + "> is displayed !!", !bodyText.contains(title));  
	  }
  }

  // Check Calendar-View
  private void verifyCalenderView() {	  
	  
	  // Check month		
	  testFuncs.myDebugPrinting("Check month", enumsClass.logModes.MINOR);
	  String date = testFuncs.getDateTime("MMMM yyyy");
	  testFuncs.myDebugPrinting("date - " + date, enumsClass.logModes.MINOR);
	  testFuncs.myAssertTrue("Data <" + date + "> was not detected !!", driver.findElement(By.xpath("//*[@id='nav']/div/div[2]")).getText().contains(date));
	  
	  // Check Days
	  testFuncs.myDebugPrinting("Check Days", enumsClass.logModes.MINOR);
	  String [] days = {"Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"};
	  int i = 4;
	  for (String day : days) {
		  
		  testFuncs.verifyStrByXpath(driver, "//*[@id='nav']/div/div[" + i + "]/span", day);
		  i +=7;
	  }
	  
	  // Check days at main table
	  testFuncs.myDebugPrinting("Check days at main table", enumsClass.logModes.MINOR);
	  String [] fullDays = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
	  for (String fullDay : fullDays) {
		  
		  testFuncs.searchStr(driver, fullDay);
	  }
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