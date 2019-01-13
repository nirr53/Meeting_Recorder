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
*  This test tests the slides menu
* -----------------
* Tests:
* - Login, Create a meeting, and enter the Editor Panel
*  1. Verify that some slides have auto-hide icon and some don't
*  2. Set manual hide for non-hidden, manual-hidden and automatic hidden slides
*  3. Set manual hide for a slide and than press reset
*  4. Set manual hide and quit with and without save
*  5. Delete the created Meeting
* 
* Results:
*  1. Congiuration should match the searched data
*  2. Set should succeed
*  3. Reset should succeed.
*  4. Save is needed before quit.
*  5. Meeting should be deleted.  
* 
* @author Dorel Cohen
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test30__Meeting_slides_hide_feature {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  static GlobalVars 	testVars;
  static GlobalFuncs	testFuncs;
  static Meeting		meeting;
  static String 		meetingSubject = "meetingSubject11030901";
  
  // Default constructor for print the name of the used browser 
  public Test30__Meeting_slides_hide_feature(browserTypes browser) {
	  
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
  public void Test0() throws Exception {
	  
	  Log.startTestCase(name.getMethodName());
	  
	  // Set data
	  meetingSubject = "meetingSubject" + testFuncs.getId();
		
	  // Login the system, Enter the Import-Meeting menu and create a meeting	
	  testFuncs.myDebugPrinting("Login the system, Enter the Import-Meeting menu and create a meeting");  
	  testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader());  		
	  meeting.createMeetingAfterProcMode(driver, meetingSubject, testVars.getSlidesMp4Path(), 1000);
  }
  
  @Test
  public void Test1() throws Exception {
	  
	  Log.startTestCase(name.getMethodName());	
	  String attIcon   = null;
	  String iconStyle = null;
		
	  // Login the system and enter the Session-Images menu
	  testFuncs.myDebugPrinting("Login the system and enter the Session-Images menu");  
	  testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader());  		
	  loginAndEnterSeesionImagesMenu();
	  
	  // Step 1 - Verify that some slides have auto-hide icon
	  testFuncs.myDebugPrinting("Step 1 - Verify that some slides have auto-hide icon");
	  String autoHideIcon = testVars.getAutoHideIcon();
	  int [] autoHideIdxs = {1 , 2 ,  3,  5,  6,  7,  8,  9, 10, 12,
			  				 13, 14, 15, 16, 17, 18, 19, 20, 22, 24};
	  for (int tmpIdx : autoHideIdxs) {
		  
		  testFuncs.myDebugPrinting("Verify auto-hide-icon for slide <" + tmpIdx + ">", enumsClass.logModes.MINOR);
		  attIcon = driver.findElement(By.xpath("//*[@id='tab2slides']/div/div[1]/div[2]/div[" + tmpIdx + "]/div/div[3]/img")).getAttribute("src");
		  testFuncs.myAssertTrue("Icon was not detected !!", attIcon.contains(autoHideIcon));
	  }
	  
	  // Step 2 - Verify that some slides don't have auto-hide icon
	  testFuncs.myDebugPrinting("Step 2 - Verify that some slides don't have auto-hide icon");
	  int [] nonHideIdxs = {4, 11, 21, 23};
	  for (int tmpIdx : nonHideIdxs) {
		  
		  testFuncs.myDebugPrinting("Verify auto-hide-icon for slide <" + tmpIdx + "> is hidden", enumsClass.logModes.MINOR);
		  iconStyle = driver.findElement(By.xpath("//*[@id='tab2slides']/div/div[1]/div[2]/div[" + tmpIdx + "]/div/div[2]")).getAttribute("style");
		  testFuncs.myAssertTrue("Hide icon was not hidden !!", iconStyle.contains("display: none;"));
	  }
  }
  
  @Test
  public void Test2() throws Exception {
	  
	  Log.startTestCase(name.getMethodName());		
		
	  // Login the system and enter the Session-Images menu
	  testFuncs.myDebugPrinting("Login the system and enter the Session-Images menu");  
	  testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader());  		
	  loginAndEnterSeesionImagesMenu();
	  
	  // Step 3 - Set manual hide for non-hidden slide
	  testFuncs.myDebugPrinting("Step 3 - Set manual hide for non-hidden slide");	
	  testFuncs.myClick(driver, By.xpath("//*[@id='tab2slides']/div/div[1]/div[2]/div[4]"), 5000);  		
	  testFuncs.myClick(driver, By.xpath("//*[@id='tab2slides']/div/div[2]/div[2]/button/img"), 2000);  		
	  String attIcon = driver.findElement(By.xpath("//*[@id='tab2slides']/div/div[1]/div[2]/div['4']/div/div[3]/img")).getAttribute("src");
	  testFuncs.myAssertTrue("Hide icon was not detected !!", attIcon.contains(testVars.getAutoHideIcon()));
	  
	  // Step 4 - Set manual hide for manual-hidden slide
	  testFuncs.myDebugPrinting("Step 4 - Set manual hide for manual-hidden slide");
	  testFuncs.myClick(driver, By.xpath("//*[@id='tab2slides']/div/div[1]/div[2]/div[4]"), 5000);  		
	  testFuncs.myClick(driver, By.xpath("//*[@id='tab2slides']/div/div[2]/div[2]/button/img"), 2000);  		
	  String iconStyle = driver.findElement(By.xpath("//*[@id='tab2slides']/div/div[1]/div[2]/div[4]/div/div[2]")).getAttribute("style");
	  testFuncs.myAssertTrue("Hide icon was not hided !!", iconStyle.contains("display: none;"));

	  // Step 5 - Set manual hide for auto-hidden slide
	  testFuncs.myDebugPrinting("Step 5 - Set manual hide for auto-hidden slide");
	  testFuncs.myClick(driver, By.xpath("//*[@id='tab2slides']/div/div[1]/div[2]/div[5]"), 5000);  		
	  testFuncs.myClick(driver, By.xpath("//*[@id='tab2slides']/div/div[2]/div[2]/button/img"), 2000);  		
	  String iconStyle2 = driver.findElement(By.xpath("//*[@id='tab2slides']/div/div[1]/div[2]/div[5]/div/div[2]")).getAttribute("style");
	  testFuncs.myAssertTrue("Hide icon was not hided !!", iconStyle2.contains("display: none;"));
  }
  
  @Test
  public void Test3() throws Exception {
	  
	  Log.startTestCase(name.getMethodName());	
	  String attIcon = null;
		
	  // Login the system and enter the Session-Images menu
	  testFuncs.myDebugPrinting("Login the system and enter the Session-Images menu");  
	  testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader());  		
	  loginAndEnterSeesionImagesMenu();
	  
	  // Step 6 - Set manual hide for slide and than press Reset
	  testFuncs.myDebugPrinting("Step 6 - Set manual hide for slide and than press Reset");
	  testFuncs.myClick(driver, By.xpath("//*[@id='tab2slides']/div/div[1]/div[2]/div[4]"), 5000);  		
	  testFuncs.myClick(driver, By.xpath("//*[@id='tab2slides']/div/div[2]/div[2]/button/img"), 2000);  		
	  attIcon = driver.findElement(By.xpath("//*[@id='tab2slides']/div/div[1]/div[2]/div['4']/div/div[3]/img")).getAttribute("src");
	  testFuncs.myAssertTrue("Hide icon was not detected !!", attIcon.contains(testVars.getAutoHideIcon())); 		
	  
	  // Press the Restore button
	  testFuncs.myDebugPrinting("Press the Restore button", enumsClass.logModes.MINOR);	
	  testFuncs.myClick(driver, By.xpath("//*[@id='tab2slides']/div/div[2]/div[4]/button"), 2000);
	  testFuncs.verifyStrBy(driver, By.id("modalTitleId")  , "Reset");
	  testFuncs.verifyStrBy(driver, By.id("modalContentId"), "Are you sure you want to reset this session?");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[5]/div/button[1]"), 4000);	
	  String iconStyle = driver.findElement(By.xpath("//*[@id='tab2slides']/div/div[1]/div[2]/div[4]/div/div[2]")).getAttribute("style");
	  testFuncs.myAssertTrue("Hide icon was not restored !!", iconStyle.contains("display: none;"));
  }
  
  @Test
  public void Test4() throws Exception {
	  
	  Log.startTestCase(name.getMethodName());
	  String attIcon   = null;
	  String iconStyle = null;
		
	  // Login the system and enter the Session-Images menu
	  testFuncs.myDebugPrinting("Login the system and enter the Session-Images menu");  
	  testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader());  		
	  loginAndEnterSeesionImagesMenu();
	  
	  // Step 7 - Set manual hide for slide, and leave menu without save
	  testFuncs.myDebugPrinting("Step 7 - Set manual hide for slide, and leave menu without save");
	  testFuncs.myClick(driver, By.xpath("//*[@id='tab2slides']/div/div[1]/div[2]/div[4]"), 5000);  		
	  testFuncs.myClick(driver, By.xpath("//*[@id='tab2slides']/div/div[2]/div[2]/button/img"), 2000);  		
	  attIcon = driver.findElement(By.xpath("//*[@id='tab2slides']/div/div[1]/div[2]/div['4']/div/div[3]/img")).getAttribute("src");
	  testFuncs.myAssertTrue("Hide icon was not detected !!", attIcon.contains(testVars.getAutoHideIcon())); 
	  
	  // Leave the page without save, re-enter the menu and verify that change the was not saved
	  testFuncs.myDebugPrinting("Leave the page without save and re-enter the menu", enumsClass.logModes.MINOR);	
	  driver.findElement(By.xpath("//a[@href='javascript:history.back()']")).click();
	  testFuncs.myWait(2000);	  
	  Alert alert = driver.switchTo().alert();
	  alert.accept();
	  testFuncs.myWait(3000);
	  meeting.verifyMeeting(driver, meetingSubject);				
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);				
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO_SESSION_IMAGES);	
	  iconStyle = driver.findElement(By.xpath("//*[@id='tab2slides']/div/div[1]/div[2]/div[4]/div/div[2]")).getAttribute("style");
	  testFuncs.myAssertTrue("Hide icon was saved !!", iconStyle.contains("display: none;"));
  
	  // Step 8 - Set manual hide for slide, and leave menu with save
	  testFuncs.myDebugPrinting("Step 8 - Set manual hide for slide, and leave menu with save");
	  testFuncs.myClick(driver, By.xpath("//*[@id='tab2slides']/div/div[1]/div[2]/div[4]"), 5000);  		
	  testFuncs.myClick(driver, By.xpath("//*[@id='tab2slides']/div/div[2]/div[2]/button/img"), 2000);  
	  
	  // Leave the page after save and re-enter the menu
	  testFuncs.myDebugPrinting("Leave the page after save and re-enter the menu", enumsClass.logModes.MINOR);	
	  testFuncs.myClick(driver, By.xpath("//*[@id='tab2slides']/div/div[2]/div[3]/button"), 3000);
	  testFuncs.pressHomeIcon(driver);
	  meeting.verifyMeeting(driver, meetingSubject);				
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);				
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO_SESSION_IMAGES);		  
	  attIcon = driver.findElement(By.xpath("//*[@id='tab2slides']/div/div[1]/div[2]/div['4']/div/div[3]/img")).getAttribute("src");
	  testFuncs.myAssertTrue("Hide icon was not detected !!", attIcon.contains(testVars.getAutoHideIcon())); 
  }
  
  @Test
  public void Test5() throws Exception {
  	  
	Log.startTestCase(name.getMethodName());
		  
	// Step 8 - Login the system and delete the Meeting
	testFuncs.myDebugPrinting("Step 8 - Login the system and delete the Meeting");   
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader());  		
	meeting.deleteMeeting(driver, meetingSubject); 
  }
  
  // Login and enter the Session Images menu
  private void loginAndEnterSeesionImagesMenu() {
		  
	  testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader());  				
	  meeting.verifyMeeting(driver, meetingSubject);				
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);				
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO_SESSION_IMAGES);	
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