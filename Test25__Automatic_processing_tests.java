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
* This test tests the Automatic transition from the PROCESSING to READY state flag
* -----------------
* Tests:
* 	 - Login the system, enter the Settings menu and set the Processing-Ready flag off
* 	 - Create a Meeting when state flag is OFF and add a Participant to it
*    1. Verify that WITHOUT publish, buttons are displayed properly (F, T, T, T)
*    2. Verify that WITHOUT publish, buttons are displayed properly (F, F, F, F)
* 	 - Login the system, enter the Settings menu and set the Processing-Ready flag ON
* 	 - Create a Meeting when state flag is ON and add a Participant to it
*    3. Verify that WITHOUT publish, buttons are displayed properly (F, T, T, T)
*    4. Verify that WITHOUT publish, buttons are displayed properly (F, T, T, F)
* Results:
*    1-4. As excepeted
*
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test25__Automatic_processing_tests {
	
  private WebDriver 	driver, driver2;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  static  GlobalVars 	testVars;
  static  GlobalFuncs	testFuncs;
  static  Meeting		meeting;
  static  Participant	participant;

  // Default constructor for print the name of the used browser 
  public Test25__Automatic_processing_tests(browserTypes browser) {
	  
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
	  participant = new Participant(testFuncs, testVars, meeting);
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
	driver2 = testFuncs.defineUsedBrowser(this.usedBrowser);
    driver2.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }
  
  @Rule
  public TestName name= new TestName();
  
  @Test
  public void Ready_state_off() throws Exception {
	  		
	  Log.startTestCase(name.getMethodName());
	  String meetingSubject  = "meetingSubject" + testFuncs.getId();
	  
	  // Login the system, enter the Settings menu and set the Processing-Ready flag off
	  testFuncs.myDebugPrinting("Login the system, enter the Settings menu and set the Processing-Ready flag off");  	
	  testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 	
	  testFuncs.enterMenu(driver, enumsClass.menuNames.SETTINGS);
	  testFuncs.enterMenu(driver, enumsClass.menuNames.SETTINGS_PREFERENCE_SECTION);
	  setProcessFlag(false);
	  
	  // Create a Meeting when state flag is OFF and add a Participant to it
	  testFuncs.myDebugPrinting("Create a Meeting when state flag is OFF and add a Participant to it");  	
	  testFuncs.pressHomeIcon(driver);
	  meeting.createMeetingAfterProcMode(driver, meetingSubject, testVars.getMeetingPlayerMp4Path(), 1000, false); 		
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO			 );
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO_PARTICIPANTS);
	  participant.addParticipant(driver,  testVars.getPartReg());
	  testFuncs.pressHomeIcon(driver);  
	  meeting.verifyMeeting(driver, meetingSubject);
	    
	  // Step 1 - Verify that WITHOUT publish, buttons are displayed properly (F, T, T, T)
	  testFuncs.myDebugPrinting("Step 1 - Verify that WITHOUT publish, buttons are displayed properly (F, T, T, T)");  	  
	  meeting.verifyButtonsState(driver, false, true);
	
	  // Step 2 - Verify that WITHOUT publish, buttons are displayed properly (F, F, F, F)
	  testFuncs.myDebugPrinting("Step 2 - Verify that WITHOUT publish, buttons are displayed properly (F, F, F, F)");  
	  testFuncs.ntlmLogin(driver2, testVars.getUrl(), testVars.getPartRegNickname(), testVars.getPartPassword(), testVars.getGoodLoginHeader()); 	
	  meeting.verifyMeeting(driver2, meetingSubject);
	  meeting.verifyButtonsState(driver2, false, false); 
	    
	  // Step 3 - Delete the created Meeting
	  testFuncs.myDebugPrinting("Step 3 - Delete the created Meeting");  		
	  meeting.deleteMeeting(driver, meetingSubject);
  }
  
  @Test
  public void Ready_state_on() throws Exception {
	  		
	  Log.startTestCase(name.getMethodName());
	  String meetingSubject  = "meetingSubject" + testFuncs.getId();
	  
	  // Login the system, enter the Settings menu and set the Processing-Ready flag ON
	  testFuncs.myDebugPrinting("Login the system, enter the Settings menu and set the Processing-Ready flag ON");  	
	  testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 	
	  testFuncs.enterMenu(driver, enumsClass.menuNames.SETTINGS);
	  testFuncs.enterMenu(driver, enumsClass.menuNames.SETTINGS_PREFERENCE_SECTION);
	  setProcessFlag(true);
	  
	  // Create a Meeting when state flag is ON and add a Participant to it
	  testFuncs.myDebugPrinting("Create a Meeting when state flag is ON and add a Participant to it");  	
	  testFuncs.pressHomeIcon(driver);
	  meeting.createMeetingAfterProcMode(driver, meetingSubject, testVars.getMeetingPlayerMp4Path(), 1000, false); 		
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO			 );
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO_PARTICIPANTS);
	  participant.addParticipant(driver,  testVars.getPartReg());
	  testFuncs.pressHomeIcon(driver);  
	  meeting.verifyMeeting(driver, meetingSubject);
	    
	  // Step 1 - Verify that WITHOUT publish, buttons are displayed properly (F, T, T, T)
	  testFuncs.myDebugPrinting("Step 1 - Verify that WITHOUT publish, buttons are displayed properly (F, T, T, T)");  	  
	  meeting.verifyButtonsState(driver, true, true);
	
	  // Step 2 - Verify that WITHOUT publish, buttons are displayed properly (F, T, T, F)
	  testFuncs.myDebugPrinting("Step 2 - Verify that WITHOUT publish, buttons are displayed properly (F, T, T, F)");  
	  testFuncs.ntlmLogin(driver2, testVars.getUrl(), testVars.getPartRegNickname(), testVars.getPartPassword(), testVars.getGoodLoginHeader()); 	
	  meeting.verifyMeeting(driver2, meetingSubject);
	  meeting.verifyButtonsState(driver2, true, false); 
	    
	  // Step 3 - Delete the created Meeting
	  testFuncs.myDebugPrinting("Step 3 - Delete the created Meeting");  		
	  meeting.deleteMeeting(driver, meetingSubject);
  }

  // Set Processing flag
  private void setProcessFlag(boolean isCheck) {
	    
	  testFuncs.myDebugPrinting("Set Processing flag", enumsClass.logModes.MINOR);  	  
	  WebElement chElm  = driver.findElement(By.xpath("//*[@id='tab2preference']/div/div/table/tbody/tr/td/div/label"));
	  WebElement chElm2 = driver.findElement(By.xpath("//*[@id='tab2preference']/div/div/table/tbody/tr/td/div/label/input"));
	  if ((isCheck && !chElm2.isSelected()) || (!isCheck && chElm2.isSelected())) {
		  
		  testFuncs.myDebugPrinting("Click on Processing flag checkbox !", enumsClass.logModes.DEBUG);  	  
		  chElm.click();		  	
		  testFuncs.myClick(driver, By.xpath("//*[@id='tab2preference']/div/div/div/button"), 2000);	
		  testFuncs.verifyStrBy(driver, By.id("modalTitleId"), "Save User Events");			
		  testFuncs.verifyStrBy(driver, By.id("modalContentId"), "Successful to save user events");			
		  testFuncs.myClick(driver, By.xpath("/html/body/div[4]/div/button[1]"), 3000);
		  return;
	  }  
	  testFuncs.myDebugPrinting("No Click on Processing flag checkbox was needed !", enumsClass.logModes.DEBUG);  	  
  }

@After
  public void tearDown() throws Exception {
	  
	testFuncs.pressHomeIcon(driver);
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETTINGS);
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETTINGS_PREFERENCE_SECTION);
    setProcessFlag(false);       
    driver.quit(); 
    driver2.quit();
    System.clearProperty("webdriver.chrome.driver");
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
    	
      testFuncs.myFail(verificationErrorString);
    }
  }
}