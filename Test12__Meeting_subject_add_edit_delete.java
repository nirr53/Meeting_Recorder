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
* This test checks the Subject editing from the List view
* -----------------
* Tests:
* 	 Login the system, Enter the Import-Meeting menu and create a meeting
*    1. Change the Meeting subject
*    2. Delete the Meeting subject
*    3. Add a subject for a Meeting without a subject
*    4. Delete the created Meeting
* 
* Results:
*    1. Subject should changed successfully
*    2. Subject should deleted successfully
*    3. Subject should created successfully
*    4. Meeting should deleted successfully
* 
* @author Dorel Cohen
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test12__Meeting_subject_add_edit_delete {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  static GlobalVars 	testVars;
  static GlobalFuncs	testFuncs;
  static Meeting		meeting;
  static String 		meetingSubject;
  static String 		updatedSubject;
  static String 		addSubject;
  static String 		testId;
  
  // Default constructor for print the name of the used browser 
  public Test12__Meeting_subject_add_edit_delete(browserTypes browser) {
	  
	  Log.info("Browser - "  + browser);
	  this.usedBrowser = browser;  
  }
  
  //Define each browser as a parameter
  @SuppressWarnings("rawtypes")
  @Parameters(name="{0}")
  public static Collection data() {
	  
		testVars  = new GlobalVars();
	    testFuncs = new GlobalFuncs(testVars); 
		meeting   = new Meeting(testFuncs, testVars);
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
  public void Test1() throws Exception {
	  
	  Log.startTestCase(name.getMethodName());		
	  testId				= testFuncs.getId();	
	  updatedSubject		= "updated Subject" + testId;
	  meetingSubject 		= "meetingSubject"  + testId;  
		
	  // Login the system, Enter the Import-Meeting menu and create a meeting	
	  testFuncs.myDebugPrinting("Login the system, Enter the Import-Meeting menu and create a meeting");  
	  testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader());  		
	  meeting.createMeetingAfterProcMode(driver, meetingSubject, testVars.getMeetingMp4Path(), 240);	  
		
	  // Step 1 - Update the subject	  
	  testFuncs.myDebugPrinting("Step 1 - Update the subject");		
	  meeting.updateSubject_listView(driver, meetingSubject, updatedSubject);
  }
  
  @Test
  public void Test2() throws Exception {
	  
	  Log.startTestCase(name.getMethodName());		
		
	  // Login the system and select the updated Meeting
	  testFuncs.myDebugPrinting("Login the system and select the updated Meeting");  
	  testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader());  		  
	  meeting.verifyMeeting(driver, updatedSubject);	
		
	  // Step 2 - Delete the Subject
	  testFuncs.myDebugPrinting("Step 2 - Delete the Subject");
	  meeting.deleteSubject_listView(driver, updatedSubject);
  }
  
  @Test
  public void Test3() throws Exception {
	  
	  Log.startTestCase(name.getMethodName());		
	  addSubject 	= "new subject " 	+ testId;
		
	  // Login the system and select the updated Meeting
	  testFuncs.myDebugPrinting("Login the system and select the updated Meeting");  
	  testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader());  		  
		
	  // Step 3 - add a subject
	  testFuncs.myDebugPrinting("Step 3 - add a subject");
	  meeting.addSubject_listView(driver, addSubject);
  }
  
  @Test
  public void Test4() throws Exception {
	  
	  Log.startTestCase(name.getMethodName());		
		
	  // Login the system and select the updated Meeting
	  testFuncs.myDebugPrinting("Login the system and select the updated Meeting");  
	  testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader());  		  
			
	  // Step 4 - Delete the created Meeting	
	  testFuncs.myDebugPrinting("Step 4 - Delete the created Meeting");	
	  meeting.deleteMeeting(driver, addSubject);
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