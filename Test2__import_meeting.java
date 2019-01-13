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
* This test tests the Import Meeting mechanism
* -----------------
* Tests:
*    1. Enter the Import-Meeting and try to continue without a subject
*    2. Enter the Import-Meeting and continue with subject and mp4 file
*    3. Enter the Import-Meeting, create a meeting and confirm it after 2 minutes
*    4. Enter the Import-Meeting, create a meeting and verify that it displayed at main menu
* 
* Results:
*    1. Error prompt should be displayed
*    2. The meeting should be created successfully
*    3. The meeting should be created successfully but error prompt should be displayed
*    4. The meeting should be displayed properly
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test2__import_meeting {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  static GlobalVars 	testVars;
  static GlobalFuncs	testFuncs;
  static Meeting		meeting;

  // Default constructor for print the name of the used browser 
  public Test2__import_meeting(browserTypes browser) {
	  
	  Log.info("Browser - "  + browser);
	  this.usedBrowser = browser;  
  }
  
  //Define each browser as a parameter
  @SuppressWarnings("rawtypes")
  @Parameters(name="{0}")
  public static Collection data() {
	  
	  testVars  = new GlobalVars();
	  testFuncs = new GlobalFuncs(testVars); 
	  meeting	= new Meeting(testFuncs, testVars);
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
  public void Import_Meeting_Without_Subject() throws Exception {
	  	  
	Log.startTestCase(name.getMethodName());	
			  
	// Login the system    	  
	testFuncs.myDebugPrinting("Login the system");  
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 
	    
	// Enter the Import-Meeting menu
    testFuncs.myDebugPrinting("Enter the Import-Meeting menu"); 
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_IMPORT);	
    testFuncs.verifyStrByXpath(driver, "/html/body/div[3]/div[3]/div/div[1]/h2"							  , "IMPORT MEETING");
    testFuncs.verifyStrByXpath(driver, "/html/body/div[3]/div[3]/div/div[2]/table/tbody/tr[1]/td[1]/label", "Subject");
    
	// Step 1 - Try to continue without enter a subject
    testFuncs.myDebugPrinting("Step 1 - Try to continue without enter a subject"); 
    driver.findElement(By.xpath("/html/body/div[3]/div[3]/div/div[2]/table/tbody/tr[1]/td[3]/div/button")).click();
    Alert alert = driver.switchTo().alert();
    String tempAuthStr = alert.getText();
    testFuncs.myDebugPrinting("alert.getText() - " +  tempAuthStr, enumsClass.logModes.MINOR); 
    testFuncs.myAssertTrue("Error message was not detected !! <" + tempAuthStr + ">", tempAuthStr.contains("Please set the meeting subject"));
    driver.switchTo().alert().accept();   
  }
  
  @Test
  public void Import_Meeting_With_Subject() throws Exception {
	  	  
	Log.startTestCase(name.getMethodName());	
	String meetingSubject   = "meetingSubject" + testFuncs.getId();
			  
	// Login the system and enter the Import-Meeting menu
	testFuncs.myDebugPrinting("Login the system and enter the Import-Meeting menu");  
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader());     
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_IMPORT);	

	// Step 1 - Enter subject and continue
    testFuncs.myDebugPrinting("Step 1 - Enter subject and continue");
    testFuncs.mySendKeys(driver, By.id("subject"), meetingSubject, 2000);
    testFuncs.myClick(driver, By.xpath("/html/body/div[3]/div[3]/div/div[2]/table/tbody/tr[1]/td[3]/div/button"), 5000);
    testFuncs.verifyStrByXpath(driver, "/html/body/div[3]/div[3]/div/div[2]/table/tbody/tr[2]/td/div/div[1]/h3", "Upload Meeting Content"); 
    String path = testVars.getMeetingMp4Path();
    testFuncs.myDebugPrinting("path - " + path, enumsClass.logModes.MINOR);
	driver.findElement(By.name("file[]")).sendKeys(path);
	testFuncs.myWait(5000);
    testFuncs.myAssertTrue("Processing of Meeting was not ended after 240 seconds !!", meeting.waitForProcessingEnd(driver, 240));
        
	// Step 2 - Delete the created Meeting
	testFuncs.myDebugPrinting("Step 2 - Delete the created Meeting");
	testFuncs.pressHomeIcon(driver);
	meeting.publishMeeting(driver, meetingSubject);
	meeting.deleteMeeting(driver, meetingSubject); 
  }
  
  @Test
  public void Verify_Imported_Meeting() throws Exception {
	  	  
	Log.startTestCase(name.getMethodName());	
	String meetingSubject   = "meetingSubject" + testFuncs.getId();
			  
	// Login the system and enter the Import-Meeting menu
	testFuncs.myDebugPrinting("Login the system and enter the Import-Meeting menu");  
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader());      
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_IMPORT);	
    meeting.createMeeting(driver, meetingSubject, testVars.getMeetingMp4Path(), true); 

    // Step 1 - Verify the imported meeting
    testFuncs.myDebugPrinting("Step 1 - Verify the imported meeting");
    meeting.verifyMeeting(driver, meetingSubject);
    
	// Step 2 - Delete the created Meeting
	testFuncs.myDebugPrinting("Step 2 - Delete the created Meeting");
	meeting.publishMeeting(driver, meetingSubject);
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