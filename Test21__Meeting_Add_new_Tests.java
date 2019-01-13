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
* This test tests the Add-New and See-All buttons
* -----------------
* Tests:
* Login the system, selected the pre-created Meeting and enter the Play-Video menu
*    1. Check that See-All button is disabled
*    2. Add Action, Note and Tag to the Video and verify that See-All button is activate
*    3. Verify that Action and Note are displayed via See-All button
*    4. Delete the created Meeting
*    
* Results:
*    1-4. As excepted
*
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test21__Meeting_Add_new_Tests {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  static  GlobalVars 	testVars;
  static  GlobalFuncs	testFuncs;
  static  Meeting		meeting;
  static  String 		meetingSubject, id, actionData, noteData, tagData;

  // Default constructor for print the name of the used browser 
  public Test21__Meeting_Add_new_Tests(browserTypes browser) {
	  
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
  public void Test0() throws Exception {
	  	  
	  Log.startTestCase(name.getMethodName());
	  
	  id 			   =  testFuncs.getId();	
	  meetingSubject = "meetingSubject" + id;

	  // Login the system, selected the pre-created Meeting and enter the Play-Video menu
	  testFuncs.myDebugPrinting("Login the system, selected the pre-created Meeting and enter the Play-Video menu");  
	  testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 
	  meeting.createMeetingAfterProcMode(driver, meetingSubject, testVars.getMeetingMp4Path(), 240);
  }
  
  @Test
  public void Test1() throws Exception {

	  Log.startTestCase(name.getMethodName());

	  // Step 1 - Check that See-All button is disabled
	  testFuncs.myDebugPrinting("Step 1 - Check that See-All button is disabled");
	  testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 
	  meeting.verifyMeeting(driver, meetingSubject);
	  testFuncs.myAssertTrue("See All button for Meeting <" + meetingSubject + "> is not disabled !!", driver.findElement(By.xpath("//*[@id='example']/tbody[2]/tr/td[7]/button[1]")).getAttribute("disabled") != null);	
  }
  
  @Test
  public void Test2() throws Exception {
	  	  
	  Log.startTestCase(name.getMethodName());

	  actionData     = "myActionData"   + id;
	  noteData       = "myNoteData"     + id;
	  tagData        = "All Tags";
	  	
	  // Step 2 - Add Action, Note and Tag to the Video and verify that See-All button is activate
	  testFuncs.myDebugPrinting("Step 2 - Add Action, Note and Tag to the Video and verify that See-All button is activate"); 
	  testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 
	  meeting.verifyMeeting(driver, meetingSubject);
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);
	  meeting.addAction(driver, actionData, enumsClass.meetingActType.ACTION.getMode(), testVars.getActionIconName());
	  meeting.addAction(driver, noteData  , enumsClass.meetingActType.NOTE.getMode()  , testVars.getNoteIconName());	
	  meeting.addTag(driver   , tagData   , enumsClass.tagModes.PUBLIC);	
	  testFuncs.pressHomeIcon(driver);	
	  meeting.verifyMeeting(driver, meetingSubject);
	  testFuncs.myAssertTrue("See All button for Meeting <" + meetingSubject + "> is disabled !!", driver.findElement(By.xpath("//*[@id='example']/tbody[2]/tr/td[7]/button[1]")).getAttribute("disabled") == null);
  }
  
  @Test
  public void Test3() throws Exception {

	  // Step 3 - Verify that Action and Note are displayed via See-All button
	  testFuncs.myDebugPrinting("Step 3 - Verify that Action and Note are displayed via See-All button"); 
	  testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 
	  meeting.verifyMeeting(driver, meetingSubject);
	  meeting.pressSeeAllButton(driver);
	  meeting.verifySeeAllItem(driver, actionData, testVars.getActionIconName());
	  meeting.verifySeeAllItem(driver, noteData  , testVars.getNoteIconName());
	  testFuncs.myClick(driver, By.xpath("//*[@id='id_modalActions_Notes']/div/div/div/div/div/table/tbody/tr[1]/td[7]/img"), 3000);
	  testFuncs.pressHomeIcon(driver);
  }
  
  @Test
  public void Test4() throws Exception {

	  // Step 4 - Delete the created Meeting
	  testFuncs.myDebugPrinting("Step 4 - Delete the created Meeting");
	  testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 
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