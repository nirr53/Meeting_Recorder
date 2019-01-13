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
*  Test-1
*    - Login the system, Enter the Import-Meeting menu and create two tags
*    - Create a Meeting and add the first Tag to it
*    - Create a Meeting and add the second Tag to it
*    - Create a Meeting and add two Tags to it
*    - Create a Meeting don't add any Tag to it
*    1. Search Meeting by first Tag
*    2. Search Meeting by secondTag
*    3. Search Meeting by two tags Tag
*    4. Search by part of first tag
*    5. Search by upper-case letters of first tag
*    6. Search by lower-case letters of first tag
*    7. Search by only Tag name (without Tag:"")
*    8. Search by two Tags (without Tag:"")
*    9. Delete all created Tags
*    10. Delete all created Meetings
*    
*  Test-2
*    - Login the system, Enter the Import-Meeting menu and create Tag
*    - Create a Meeting and add the Tag to it
*    - Create a Meeting and add the Tag to it
*    - Create a Meeting don't add any Tag to it
*    1. Click on searched Meeting tag and verify that the filter is changed for filtering by selected Tag
*    2. Delete all created Tags
*    3. Delete all created Meetings
* 
* Results:
*     1+2. Every meeting that has the Tag should be filtered
*       3. Only the Meeting that has both Tags should be filtered
*     4+7. Every Meeting with first Tag should be filtered 
*       8. Every meeting with at least on of the tags should be filtered
*    9+10. As excepted
*    
*     	1. Filtering should be changed to Filter-by-tag
*     2+3. As excepted
*
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test16__Meeting_search_by_tags_tests {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  static GlobalVars 	testVars;
  static GlobalFuncs	testFuncs;
  static Meeting		meeting;
  static Tags			tags;

  // Default constructor for print the name of the used browser 
  public Test16__Meeting_search_by_tags_tests(browserTypes browser) {
	  
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
  public void Tags_filter_tests() throws Exception {
	  	  
	Log.startTestCase(name.getMethodName());	
	String id 			    = testFuncs.getId();
	String noTagsMeeting    = "noTagsMeeting" 	 + id;
	String firstTagMeeting  = "firstTagMeeting"  + id;
	String secondTagMeeting = "secondTagMeeting" + id;
	String twoTagsMeeting   = "twoTagsMeeting"   + id;
	String firstTag			= "firstTag"		 + id;
	String secondTag		= "secondTag"		 + id;
	  
	// Login the system, Enter the Import-Meeting menu and create four meetings
	testFuncs.myDebugPrinting("Login the system, Enter the Import-Meeting menu and create four meetings");  
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader());   
	meeting.createMeetingAfterProcMode(driver, noTagsMeeting   , testVars.getMeetingMp4Path(), 240, false);
	meeting.createMeetingAfterProcMode(driver, firstTagMeeting , testVars.getMeetingMp4Path(), 240, false);
	meeting.createMeetingAfterProcMode(driver, secondTagMeeting, testVars.getMeetingMp4Path(), 240, false);
	meeting.createMeetingAfterProcMode(driver, twoTagsMeeting  , testVars.getMeetingMp4Path(), 240, false);
	
    // Enter the System Settings menu and create two Tags
	testFuncs.myDebugPrinting("Enter the System Settings menu and create two Tags");  
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETTINGS);
	tags.createTag(driver, firstTag);
	tags.createTag(driver, secondTag);
	testFuncs.pressHomeIcon(driver);

    // Add Tags to Meetings
	testFuncs.myDebugPrinting("Add Tags to Meetings");  
	addTagToMeeting(firstTagMeeting , firstTag);
	addTagToMeeting(secondTagMeeting, secondTag);
	addTagToMeeting(twoTagsMeeting  , firstTag);
	addTagToMeeting(twoTagsMeeting  , secondTag);
	   
	// Step 1 - Search Meeting by first Tag
	testFuncs.myDebugPrinting("Step 1 - Search Meeting by first Tag");  
	meeting.verifyMeetingByGivenFilter(driver, "tag:\"" + firstTag + "\"");
	testFuncs.searchStr(driver	   , firstTagMeeting);
	testFuncs.searchStr(driver	   , twoTagsMeeting);
	testFuncs.searchStrFalse(driver, secondTagMeeting);
	testFuncs.searchStrFalse(driver, noTagsMeeting);
	
	// Step 2 - Search Meeting by second Tag
	testFuncs.myDebugPrinting("Step 2 - Search Meeting by second Tag");  
	meeting.verifyMeetingByGivenFilter(driver, "tag:\"" + secondTag + "\"");
	testFuncs.searchStr(driver	   , secondTag);
	testFuncs.searchStr(driver	   , twoTagsMeeting);
	testFuncs.searchStrFalse(driver, firstTagMeeting);
	testFuncs.searchStrFalse(driver, noTagsMeeting);
	
	// Step 3 - Search Meeting by two Tags
	testFuncs.myDebugPrinting("Step 3 - Search Meeting by two Tags");  
	meeting.verifyMeetingByGivenFilter(driver, "\"" + firstTag + "\",\"" + secondTag + "\"");
	testFuncs.searchStr(driver	   , twoTagsMeeting);
	testFuncs.searchStrFalse(driver, firstTagMeeting);
	testFuncs.searchStrFalse(driver, secondTagMeeting);
	testFuncs.searchStrFalse(driver, noTagsMeeting);
	
	// Step 4 - Search Meeting by part of first Tag
	testFuncs.myDebugPrinting("Step 4 - Search Meeting by part of first Tag");  
	meeting.verifyMeetingByGivenFilter(driver, "tag:\"" + firstTag.substring(1) + "\"");
	testFuncs.searchStr(driver	   , firstTagMeeting);
	testFuncs.searchStr(driver	   , twoTagsMeeting);
	testFuncs.searchStrFalse(driver, secondTagMeeting);
	testFuncs.searchStrFalse(driver, noTagsMeeting);

	// Step 5 - Search Meeting by first Tag (upper-case letters)
	testFuncs.myDebugPrinting("Step 5 - Search Meeting by first Tag (upper-case letters)");  
	meeting.verifyMeetingByGivenFilter(driver, "tag:\"" + firstTag.toUpperCase() + "\"");
	testFuncs.searchStr(driver	   , firstTagMeeting);
	testFuncs.searchStr(driver	   , twoTagsMeeting);
	testFuncs.searchStrFalse(driver, secondTagMeeting);
	testFuncs.searchStrFalse(driver, noTagsMeeting);
	
	// Step 6 - Search Meeting by first Tag (lower-case letters)
	testFuncs.myDebugPrinting("Step 6 - Search Meeting by first Tag (lower-case letters)");  
	meeting.verifyMeetingByGivenFilter(driver, "tag:\"" + firstTag.toLowerCase() + "\"");
	testFuncs.searchStr(driver	   , firstTagMeeting);
	testFuncs.searchStr(driver	   , twoTagsMeeting);
	testFuncs.searchStrFalse(driver, secondTagMeeting);
	testFuncs.searchStrFalse(driver, noTagsMeeting);
	
	// Step 7 - Search by only Tag name (without Tag:"")
	testFuncs.myDebugPrinting("Step 7 - Search by only Tag name (without Tag:\"\")");  
	meeting.verifyMeetingByGivenFilter(driver, firstTag);
	testFuncs.searchStr(driver	   , firstTagMeeting);
	testFuncs.searchStr(driver	   , twoTagsMeeting);
	testFuncs.searchStrFalse(driver, secondTagMeeting);
	testFuncs.searchStrFalse(driver, noTagsMeeting);
	
	// Step 8 - Search Meeting by two Tags (without Tag:"")
	testFuncs.myDebugPrinting("Step 8 - Search Meeting by two Tags (without Tag:\"\")");  
	meeting.verifyMeetingByGivenFilter(driver, "tag:" + firstTag + "," + secondTag);
	testFuncs.searchStr(driver	   , twoTagsMeeting);
	testFuncs.searchStr(driver	   , firstTagMeeting);
	testFuncs.searchStr(driver	   , secondTagMeeting);
	testFuncs.searchStrFalse(driver, noTagsMeeting);

	// Step 9 - Delete the created tags
	testFuncs.myDebugPrinting("Step 9 - Delete the created tags");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETTINGS);
	tags.deleteTag(driver, firstTag);
	tags.deleteTag(driver, secondTag);
  
	// Step 10 - Delete the created Meetings
	testFuncs.myDebugPrinting("Step 10 - Delete the created Meetings");
	testFuncs.pressHomeIcon(driver);
	meeting.deleteMeeting(driver, noTagsMeeting	  , false);
	meeting.deleteMeeting(driver, firstTagMeeting , false);
	meeting.deleteMeeting(driver, secondTagMeeting, false);
	meeting.deleteMeeting(driver, twoTagsMeeting  , false);
  }
  
  @Ignore
  @Test
  public void Tags_link_tests() throws Exception {
  	  
	Log.startTestCase(name.getMethodName());	
	String id 			  = testFuncs.getId();
	String firstMeeting   = "firstMeetingSubject"  + id;
	String secondMeeting  = "secondMeetingSubject" + id;
	String noTagsMeeting  = "noTagsMeeting" 	   + id;
	String meetingTag	  = "meetingTag"	  	   + id;
	  
	// Login the system and create meetings
	testFuncs.myDebugPrinting("Login the system and create meetings");  
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader());   
	meeting.createMeetingAfterProcMode(driver, firstMeeting , testVars.getMeetingMp4Path(), 240, false);
	meeting.createMeetingAfterProcMode(driver, secondMeeting, testVars.getMeetingMp4Path(), 240, false);
	meeting.createMeetingAfterProcMode(driver, noTagsMeeting, testVars.getMeetingMp4Path(), 240, false);

    // Enter the System Settings menu, create a Tag and Add Tag to the created Meetings
	testFuncs.myDebugPrinting("Enter the System Settings menu, create a Tag and Add Tag to the created Meetings");  
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETTINGS);
	tags.createTag(driver, meetingTag);
	testFuncs.pressHomeIcon(driver);
	addTagToMeeting(firstMeeting , meetingTag);
	addTagToMeeting(secondMeeting, meetingTag);
	   
	// Step 1 - Press the Tag and verify that it assigned to the filter
	testFuncs.myDebugPrinting("Step 1 - Press the Tag and verify that it assigned to the filter");  
	meeting.verifyMeeting(driver   , firstMeeting);
	testFuncs.searchStrFalse(driver, secondMeeting);
	testFuncs.searchStrFalse(driver, noTagsMeeting);

	// Verify that filter is now works with Tags  
	testFuncs.myDebugPrinting("Verify that filter is now works with Tags", enumsClass.logModes.NORMAL);
	testFuncs.myClick(driver, By.xpath("//*[@id='tagtd']/div/span"), 2000);
	testFuncs.searchStr(driver	   , firstMeeting);
	testFuncs.searchStr(driver	   , secondMeeting);
	testFuncs.searchStrFalse(driver, noTagsMeeting);

	// Step 2 - Delete the created Tag
	testFuncs.myDebugPrinting("Step 2 - Delete the created Tag");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETTINGS);
	tags.deleteTag(driver, meetingTag);
  
	// Step 3 - Delete the created Meetings
	testFuncs.myDebugPrinting("Step 3 - Delete the created Meetings");
	testFuncs.pressHomeIcon(driver);
	meeting.deleteMeeting(driver, noTagsMeeting, false);
	meeting.deleteMeeting(driver, firstMeeting , false);
	meeting.deleteMeeting(driver, secondMeeting, false);
  }
  
  // Add given tag to given Meeting
  private void addTagToMeeting(String givengMeeting, String givenTag) {
	  
	  // Add given tag to given Meeting
	  testFuncs.myDebugPrinting("Add given tag to given Meeting", enumsClass.logModes.NORMAL);
	  meeting.verifyMeeting(driver, givengMeeting);
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);
	  meeting.addTag(driver, givenTag, enumsClass.tagModes.PUBLIC);
	  testFuncs.pressHomeIcon(driver);
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