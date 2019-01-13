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
*  1. Try to upload non-ppt (or pptx) file
*  2. Try to upload a pptx file
*  3. Replace one frame of the Meeting with the first ppt-file
*  4. Replace another frame of the Meeting with the first ppt-file
*  5. Press the Reset button to reset the initial configuration
*  6. Leave the slides menu without save and verify that changes were not saved
*  7. Leave the slides menu with save and verify that changes were not saved
*  8. Delete the Meeting
* 
* Results:
*  1. Upload should not succeed
*  2. Upload should succeed
*  3. Frame should be replaced.
*  4. Frame should be replaced.
*  5. All original Meeting frames should be restored.
*  6. Replacement should not be saved.
*  7. Replacement should be saved.
*  8. Meeting should be deleted.  
* 
* @author Dorel Cohen
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test29__Meeting_slides {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  static GlobalVars 	testVars;
  static GlobalFuncs	testFuncs;
  static Meeting		meeting;
  static String 		meetingSubject;
  
  // Default constructor for print the name of the used browser 
  public Test29__Meeting_slides(browserTypes browser) {
	  
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
	  meeting.createMeetingAfterProcMode(driver, meetingSubject, testVars.getMeetingMp4Path(), 240);
  }
  
  @Test
  public void Test1() throws Exception {
	  
	  Log.startTestCase(name.getMethodName());		
		
	  // Login the system and enter the Session-Images menu
	  testFuncs.myDebugPrinting("Login the system and enter the Session-Images menu");  
	  testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader());  		
	  loginAndEnterSeesionImagesMenu();
	  
	  // Step 1 - Try to upload non ppt file 
	  testFuncs.myDebugPrinting("Step 1 - Try to upload non ppt file");	   
	  uploadFile(testVars.getNonPptFile(), true, "Bad Request");
	  int slidesNum = driver.findElements(By.xpath("//*[@id='tab2slides']/div/div[3]/div[3]/*")).size();
	  testFuncs.myDebugPrinting("slidesNum - " + slidesNum, enumsClass.logModes.MINOR);
	  testFuncs.myAssertTrue("Non pptx (or ppt) was uploaded successfully !", slidesNum == 0);
  }
  
  @Test
  public void Test2() throws Exception {
	  
	  Log.startTestCase(name.getMethodName());		
		
	  // Login the system and enter the Session-Images menu
	  testFuncs.myDebugPrinting("Login the system and enter the Session-Images menu"); 
	  testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader());  		
	  loginAndEnterSeesionImagesMenu();
	  
	  // Step 2 - Upload pptx slides file
	  testFuncs.myDebugPrinting("Step 2 - Upload pptx slides file");	   
	  uploadFile(testVars.getPptxFile(), true);
	  int slidesNum = driver.findElements(By.xpath("//*[@id='tab2slides']/div/div[3]/div[3]/*")).size();
	  testFuncs.myDebugPrinting("slidesNum - " + slidesNum, enumsClass.logModes.MINOR);
	  testFuncs.myAssertTrue("Non pptx (or ppt) was not uploaded successfully !", slidesNum != 0);
  }
  
  @Test
  public void Test3() throws Exception {
	  
	  Log.startTestCase(name.getMethodName());		
		
	  // Login the system and enter the Session-Images menu
	  testFuncs.myDebugPrinting("Login the system and enter the Session-Images menu");  
	  testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader());  		
	  loginAndEnterSeesionImagesMenu();
	  
	  // Step 3 - Replace one of the Meeting slides with uploaded file slides
	  testFuncs.myDebugPrinting("Step 3 - Replace one of the Meeting slides with uploaded file slides");	
	  
	  // Select slide to be replaced
	  testFuncs.myDebugPrinting("Select slide to be replaced", enumsClass.logModes.NORMAL);	
	  testFuncs.myClick(driver, By.xpath("//*[@id='tab2slides']/div/div[1]/div[2]/div[1]/div/img"), 2000); 
	  String origSlide = driver.findElement(By.xpath("//*[@id='tab2slides']/div/div[1]/div[2]/div[1]/div/img")).getAttribute("src"); 
	  origSlide = origSlide.split("&file=")[1].split("\\.png")[0];
	  testFuncs.myDebugPrinting("origSlide - " + origSlide, enumsClass.logModes.MINOR);	
	  
	  // Select slide for replace
	  testFuncs.myDebugPrinting("Select slide for replace", enumsClass.logModes.NORMAL);	
	  testFuncs.myClick(driver, By.xpath("//*[@id='tab2slides']/div/div[3]/div[3]/div[1]/div/img"), 2000);		  
	  String repSlide = driver.findElement(By.xpath("//*[@id='tab2slides']/div/div[3]/div[3]/div[1]/div/img")).getAttribute("src");
	  repSlide = repSlide.split("&file=")[1].split("\\.png")[0];
	  testFuncs.myDebugPrinting("repSlide - " + repSlide, enumsClass.logModes.MINOR);	
	  
	  // Press the Replace button and verify replace
	  testFuncs.myDebugPrinting("Press the Replace button and verify replace", enumsClass.logModes.NORMAL);	
	  testFuncs.myClick(driver, By.xpath("//*[@id='tab2slides']/div/div[2]/div[1]/button"), 2000);	  
	  String orgSlideAfRepl = driver.findElement(By.xpath("//*[@id='tab2slides']/div/div[1]/div[2]/div[1]/div/img")).getAttribute("src");
	  orgSlideAfRepl = orgSlideAfRepl.split("&file=")[1].split("\\.png")[0];
	  testFuncs.myDebugPrinting("orgSlideAfRepl - " + orgSlideAfRepl, enumsClass.logModes.MINOR);	
	  testFuncs.myAssertTrue("Slide after replace does not equal to replaced slide",  orgSlideAfRepl.equals(repSlide));
	  testFuncs.myAssertTrue("Slide after replace do equal to original slide"	   , !orgSlideAfRepl.equals(origSlide));
  }
  
  @Test
  public void Test4() throws Exception {
	  
	  Log.startTestCase(name.getMethodName());		
		
	  // Login the system and enter the Session-Images menu
	  testFuncs.myDebugPrinting("Login the system and enter the Session-Images menu");   
	  testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader());  		
	  loginAndEnterSeesionImagesMenu();
	  
	  // Step 4 - Replace another slide of the Meeting slides with uploaded file slides
	  testFuncs.myDebugPrinting("Step 4 - Replace another slide of the Meeting slides with uploaded file slides");	
	  
	  // Select slides to be replaced
	  testFuncs.myDebugPrinting("Select slides to be replaced", enumsClass.logModes.NORMAL);	
	  testFuncs.myClick(driver, By.xpath("//*[@id='tab2slides']/div/div[1]/div[2]/div[1]/div/img"), 2000); 
	  String origSlide = driver.findElement(By.xpath("//*[@id='tab2slides']/div/div[1]/div[2]/div[1]/div/img")).getAttribute("src"); 
	  origSlide = origSlide.split("&file=")[1].split("\\.png")[0];
	  testFuncs.myDebugPrinting("origSlide - " + origSlide, enumsClass.logModes.MINOR);
	  testFuncs.myClick(driver, By.xpath("//*[@id='tab2slides']/div/div[1]/div[2]/div[2]/div/img"), 2000); 
	  String origSlide2 = driver.findElement(By.xpath("//*[@id='tab2slides']/div/div[1]/div[2]/div[2]/div/img")).getAttribute("src"); 
	  origSlide2 = origSlide2.split("&file=")[1].split("\\.png")[0];
	  testFuncs.myDebugPrinting("origSlide2 - " + origSlide2, enumsClass.logModes.MINOR);
	  
	  // Select slides for replace
	  testFuncs.myDebugPrinting("Select slides for replace", enumsClass.logModes.NORMAL);	
	  testFuncs.myClick(driver, By.xpath("//*[@id='tab2slides']/div/div[3]/div[3]/div[1]/div/img"), 2000);		  
	  String repSlide = driver.findElement(By.xpath("//*[@id='tab2slides']/div/div[3]/div[3]/div[1]/div/img")).getAttribute("src");
	  repSlide = repSlide.split("&file=")[1].split("\\.png")[0];
	  testFuncs.myDebugPrinting("repSlide - " + repSlide, enumsClass.logModes.MINOR);
	  testFuncs.myClick(driver, By.xpath("//*[@id='tab2slides']/div/div[3]/div[3]/div[2]/div/img"), 2000);		  
	  String repSlide2 = driver.findElement(By.xpath("//*[@id='tab2slides']/div/div[3]/div[3]/div[1]/div/img")).getAttribute("src");
	  repSlide2 = repSlide2.split("&file=")[1].split("\\.png")[0];
	  testFuncs.myDebugPrinting("repSlide2 - " + repSlide2, enumsClass.logModes.MINOR);
	  
	  // Press the Replace button and verify replace
	  testFuncs.myDebugPrinting("Press the Replace button and verify replace", enumsClass.logModes.NORMAL);	
	  testFuncs.myClick(driver, By.xpath("//*[@id='tab2slides']/div/div[2]/div[1]/button"), 2000);	  
	  String orgSlideAfRepl = driver.findElement(By.xpath("//*[@id='tab2slides']/div/div[1]/div[2]/div[1]/div/img")).getAttribute("src");
	  orgSlideAfRepl = orgSlideAfRepl.split("&file=")[1].split("\\.png")[0];
	  testFuncs.myDebugPrinting("orgSlideAfRepl - " + orgSlideAfRepl, enumsClass.logModes.MINOR);	
	  String orgSlideAfRepl2 = driver.findElement(By.xpath("//*[@id='tab2slides']/div/div[1]/div[2]/div[1]/div/img")).getAttribute("src");
	  orgSlideAfRepl2 = orgSlideAfRepl2.split("&file=")[1].split("\\.png")[0];
	  testFuncs.myDebugPrinting("orgSlideAfRepl2 - " + orgSlideAfRepl2, enumsClass.logModes.MINOR);	  
	  testFuncs.myAssertTrue("Slide after replace does not equal to replaced slide",  orgSlideAfRepl.equals(repSlide));
	  testFuncs.myAssertTrue("Slide after replace do equal to original slide"	   , !orgSlideAfRepl.equals(origSlide));
	  testFuncs.myAssertTrue("Slide after replace does not equal to replaced slide",  orgSlideAfRepl2.equals(repSlide2));
	  testFuncs.myAssertTrue("Slide after replace do equal to original slide"	   , !orgSlideAfRepl2.equals(origSlide2));
  }
  
  @Test
  public void Test5() throws Exception {
	  
	  Log.startTestCase(name.getMethodName());		
		
	  // Login the system and enter the Session-Images menu
	  testFuncs.myDebugPrinting("Login the system and enter the Session-Images menu");  
	  testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader());  		
	  loginAndEnterSeesionImagesMenu();
	  
	  // Step 5 - Replace one of the Meeting slides with uploaded file slides
	  testFuncs.myDebugPrinting("Step 5 - Replace one of the Meeting slides with uploaded file slides");
	  String [] ret = replace();
	  String origSlide = ret[0];
	  String repSlide  = ret[1]; 
	    
	  // Press the Restore button
	  testFuncs.myDebugPrinting("Press the Restore button", enumsClass.logModes.MINOR);	
	  testFuncs.myClick(driver, By.xpath("//*[@id='tab2slides']/div/div[2]/div[4]/button"), 2000);
	  testFuncs.verifyStrBy(driver, By.id("modalTitleId")  , "Reset");
	  testFuncs.verifyStrBy(driver, By.id("modalContentId"), "Are you sure you want to reset this session?");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[5]/div/button[1]"), 4000);	
	  String orgSlideAfRepl = driver.findElement(By.xpath("//*[@id='tab2slides']/div/div[1]/div[2]/div[1]/div/img")).getAttribute("src");
	  orgSlideAfRepl = orgSlideAfRepl.split("&file=")[1].split("\\.png")[0];
	  testFuncs.myDebugPrinting("orgSlideAfRepl - " + orgSlideAfRepl, enumsClass.logModes.MINOR);
	  
	  // Verify restore
	  testFuncs.myDebugPrinting("Verify restore", enumsClass.logModes.MINOR);	
	  testFuncs.myAssertTrue("Slide after pressing on Restore button is still replaced !!",  orgSlideAfRepl.equals(origSlide));
	  testFuncs.myAssertTrue("Slide after pressing on Restore button is still replaced !!", !orgSlideAfRepl.equals(repSlide));
  }

  @Test
  public void Test6() throws Exception {
	  
	  Log.startTestCase(name.getMethodName());		
		
	  // Login the system and enter the Session-Images menu
	  testFuncs.myDebugPrinting("Login the system and enter the Session-Images menu");   
	  testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader());  		
	  loginAndEnterSeesionImagesMenu();
	  
	  // Step 6 - Replace one of the Meeting slides with uploaded file slides
	  testFuncs.myDebugPrinting("Step 6 - Replace one of the Meeting slides with uploaded file slides");
	  String [] ret = replace();
	  String origSlide = ret[0];
	  String repSlide  = ret[1]; 
	  
	  // Leave the page without save and re-enter the menu
	  testFuncs.myDebugPrinting("Leave the page without save and re-enter the menu", enumsClass.logModes.MINOR);	
	  driver.findElement(By.xpath("//a[@href='javascript:history.back()']")).click();
	  testFuncs.myWait(2000);	  
	  Alert alert = driver.switchTo().alert();
	  alert.accept();
	  testFuncs.myWait(3000);
	  meeting.verifyMeeting(driver, meetingSubject);				
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);				
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO_SESSION_IMAGES);	

	  // Verify that the replace was not saved
	  testFuncs.myDebugPrinting("Verify that the replace was not saved");	
	  String orgSlideAfRepl = driver.findElement(By.xpath("//*[@id='tab2slides']/div/div[1]/div[2]/div[1]/div/img")).getAttribute("src");
	  orgSlideAfRepl = orgSlideAfRepl.split("&file=")[1].split("\\.png")[0];
	  testFuncs.myDebugPrinting("orgSlideAfRepl - " + orgSlideAfRepl, enumsClass.logModes.MINOR);
	  testFuncs.myAssertTrue("Slide after leave-without-save is still replaced !!",  orgSlideAfRepl.equals(origSlide));
	  testFuncs.myAssertTrue("Slide after leave-without-save is still replaced !!", !orgSlideAfRepl.equals(repSlide));
  }
  
  @Test
  public void Test7() throws Exception {
	  
	  Log.startTestCase(name.getMethodName());		
		
	  // Login the system and enter the Session-Images menu
	  testFuncs.myDebugPrinting("Login the system and enter the Session-Images menu");
	  testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader());  		
	  loginAndEnterSeesionImagesMenu();
	  
	  // Step 7 - Replace one of the Meeting slides with uploaded file slides
	  testFuncs.myDebugPrinting("Step 6 - Replace one of the Meeting slides with uploaded file slides");
	  String [] ret = replace();
	  String origSlide = ret[0];
	  String repSlide  = ret[1];
	  
	  // Leave the page after save and re-enter the menu
	  testFuncs.myDebugPrinting("Leave the page after save and re-enter the menu", enumsClass.logModes.MINOR);	
	  testFuncs.myClick(driver, By.xpath("//*[@id='tab2slides']/div/div[2]/div[3]/button"), 3000);
	  testFuncs.pressHomeIcon(driver);
	  meeting.verifyMeeting(driver, meetingSubject);				
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);				
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO_SESSION_IMAGES);	

	  // Verify that the replace was saved
	  testFuncs.myDebugPrinting("Verify that the replace was saved");	
	  String orgSlideAfRepl = driver.findElement(By.xpath("//*[@id='tab2slides']/div/div[1]/div[2]/div[1]/div/img")).getAttribute("src");
	  orgSlideAfRepl = orgSlideAfRepl.split("&file=")[1].split("\\.png")[0];
	  testFuncs.myDebugPrinting("orgSlideAfRepl - " + orgSlideAfRepl, enumsClass.logModes.MINOR);
	  testFuncs.myAssertTrue("Slide after leave-with-save was not replaced !!", !orgSlideAfRepl.equals(origSlide));
	  testFuncs.myAssertTrue("Slide after leave-with-save was not replaced !!",  orgSlideAfRepl.equals(repSlide));
  }
  
  @Test
  public void Test8() throws Exception {
  	  
	Log.startTestCase(name.getMethodName());
		  
	// Step 8 - Login the system and delete the Meeting
	testFuncs.myDebugPrinting("Step 8 - Login the system and delete the Meeting");   
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader());  		
	meeting.deleteMeeting(driver, meetingSubject); 
  }
  
  // Upload file
  private void uploadFile(String file, boolean isOverwrite) {
	  
	  uploadFile(file, isOverwrite, "Attachment uploaded successfully");
  }
  
  // Upload file
  private void uploadFile(String file, boolean isOverwrite, String msgContentHeader) {
	  
	  // Set file
	  testFuncs.myDebugPrinting("Uploaded file - " + file, enumsClass.logModes.MINOR);
	  testFuncs.myClick(driver, By.xpath("//*[@id='tab2slides']/div/div[3]/div[2]/div/button"), 2000);	  
	  driver.findElement(By.name("file")).sendKeys(file);
	
	  // Check overwrite exiting checkbox
	  testFuncs.myDebugPrinting("Check overwrite exiting checkbox", enumsClass.logModes.MINOR);
	  WebElement chElm  = driver.findElement(By.xpath("//*[@id='modalUpload']/div/div/div/div[2]/div/div/table/tbody/tr[2]/td/div/label/input"));
	  if ((chElm.isSelected() && !isOverwrite) || (!chElm.isSelected() && isOverwrite)) {
		  
		  testFuncs.myDebugPrinting("Click checkbox is needed", enumsClass.logModes.MINOR);
		  testFuncs.myClick(driver, By.xpath("//*[@id='modalUpload']/div/div/div/div[2]/div/div/table/tbody/tr[2]/td/div/label"), 1000);
	  } else {
		  
		  testFuncs.myDebugPrinting("Click checkbox is NOT needed", enumsClass.logModes.MINOR);		  
	  }
	  testFuncs.myClick(driver, By.xpath("//*[@id='modalUpload']/div/div/div/div[2]/div/div/table/tbody/tr[2]/td/button"), 4000);
	  testFuncs.verifyStrBy(driver, By.id("modalTitleId")  , "Upload File");
	  testFuncs.verifyStrBy(driver, By.id("modalContentId"), msgContentHeader);
	  testFuncs.myClick(driver, By.xpath("/html/body/div[5]/div/button[1]"), 4000);	
  }

  // Login and enter the Session Images menu
  private void loginAndEnterSeesionImagesMenu() {
		  
	  testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader());  				
	  meeting.verifyMeeting(driver, meetingSubject);				
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);				
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO_SESSION_IMAGES);	
  }
  
  // Make a replace of first slide
  private String[] replace() {
	  
	  testFuncs.myClick(driver, By.xpath("//*[@id='tab2slides']/div/div[1]/div[2]/div[1]/div/img"), 2000); 
	  String origSlide = driver.findElement(By.xpath("//*[@id='tab2slides']/div/div[1]/div[2]/div[1]/div/img")).getAttribute("src"); 
	  origSlide = origSlide.split("&file=")[1].split("\\.png")[0];
	  testFuncs.myDebugPrinting("origSlide - " + origSlide, enumsClass.logModes.MINOR);
	  testFuncs.myClick(driver, By.xpath("//*[@id='tab2slides']/div/div[3]/div[3]/div[1]/div/img"), 2000);		  
	  String repSlide = driver.findElement(By.xpath("//*[@id='tab2slides']/div/div[3]/div[3]/div[1]/div/img")).getAttribute("src");
	  repSlide = repSlide.split("&file=")[1].split("\\.png")[0];
	  testFuncs.myDebugPrinting("repSlide - " + repSlide, enumsClass.logModes.MINOR);	
	  testFuncs.myClick(driver, By.xpath("//*[@id='tab2slides']/div/div[2]/div[1]/button"), 2000);
	  
	  String [] ret = {origSlide, repSlide};
	  return ret;
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