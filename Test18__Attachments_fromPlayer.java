package MeetingRecorder;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.*;
import MeetingRecorder.Log;
import MeetingRecorder.enumsClass.*;

/**
* ----------------
* This test checks the Attachments from the Edit page
* -----------------
* Tests:
*    1. attach one file
*    2. attach two files
*    3. cancel a file, abort the upload and hide the upload window
*    4. overwrite and don't overwrite the attachment
*    5. delete attachment.
*    6. download the attach from the Edit page
*    7. download the attach from the List view
*    8. attach scheduled files
* 
* Results:
*    1. 
*    2. 
*    3. 
* 
* @author Dorel Cohen
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test18__Attachments_fromPlayer {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  Meeting				meeting;
  VideoPlayer			videoPlayer;

  // Default constructor for print the name of the used browser 
  public Test18__Attachments_fromPlayer(browserTypes browser) {
	  
	  Log.info("Browser - "  + browser);
	  this.usedBrowser = browser;  
  }
  
  // Define each browser as a parameter
  @SuppressWarnings("rawtypes")
  @Parameters(name="{0}")
  public static Collection data() {
	  
    return Arrays.asList(enumsClass.browserTypes.CHROME);
  }
  
  @BeforeClass 
  public static void setting_SystemProperties() {
	  
	  Log.info("System Properties seting Key value.");
  }  
  
  @Before
  public void setUp() throws Exception {
	  	
	testVars  	= new GlobalVars();
    testFuncs 	= new GlobalFuncs(testVars); 
	meeting   	= new Meeting(testFuncs, testVars);
    videoPlayer = new VideoPlayer(testFuncs, meeting);
    System.setProperty("webdriver.chrome.driver", testVars.getChromeDrvPath());
	testFuncs.myDebugPrinting("Enter setUp(); usedbrowser - " + this.usedBrowser);
	driver = testFuncs.defineUsedBrowser(this.usedBrowser);
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }
  
  @Test 
  public void Attach_File() throws Exception {  
	
	Log.startTestCase(this.getClass().getName());

	String meetingSubject = "meetingSubject " + testFuncs.getId();
	String path 		  = testVars.getAttachmentPath_PPT();
	String fileName 	  = testVars.getAttachmentPPT1name();
	String ip 			  = testVars.getUrl().split("/")[0];
	
	// Login the system, Enter the Import-Meeting menu and create a meeting
	testFuncs.myDebugPrinting("Login the system, Enter the Import-Meeting menu and create a meeting");  
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader());   
	meeting.createMeetingAfterProcMode(driver, meetingSubject, testVars.getMeetingMp4Path(), 240);

	// Step 1 - Enter the Player-Attachments page
	testFuncs.myDebugPrinting("Step 1 - Enter the Player-Attachments page");	
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO_ATTACHMENTS);
	
	//Step 2 - Add one attachment from the Player page
	testFuncs.myDebugPrinting("Step 2 - Add one attachment from the Player page");
	testFuncs.myDebugPrinting("Verifying the contents of the ''Upload Attachment Content'' window", enumsClass.logModes.MINOR);
	String meetingID = driver.getCurrentUrl().split("=")[1];
	String classElm2 = driver.findElement(By.xpath("//*[@id=\"fileuploader\"]/div[1]/div/form")).getAttribute("action");
	testFuncs.myAssertTrue("The ''Overwrite existing file(s)'' option is not selected by default.", classElm2.contains("http://" + ip + "/api/meetings/" + meetingID + "/uploadMeetingAttachments?overrideFile=true"));
	testFuncs.verifyStrByXpath(driver, "//*[@id=\"tab2atts\"]/div[2]/div[1]/div[1]/h5"		  , "Upload Attachment Files");
	testFuncs.verifyStrByXpath(driver, "//*[@id=\"fileuploader\"]/div[1]/span/b"			  ,  "Drag & Drop Files");
	testFuncs.verifyStrByXpath(driver, "//*[@id=\"tab2atts\"]/div[2]/div[2]/div/label/span[2]",  "Overwrite existing file(s)");
		
	//Step 3 - Upload file and Verify attachment slides
	testFuncs.myDebugPrinting("Step 3 - Upload file and Verify attachment slides");
	meeting.addAttachmentViaEditMenu(driver, path, fileName);
	
	// Verifying the Attachment Slides.
	testFuncs.myDebugPrinting("Verifying the Attachment Slides.", enumsClass.logModes.MINOR);
	int fileSlidesNum = videoPlayer.getFileSlidesNumber(driver);
	testFuncs.myAssertTrue("Not all slides of the file are displayed !", fileSlidesNum == 4);
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO_ATTACHMENTS_SLIDES_TAB);
	
	// Verifying the ''Attachments'' window.
	testFuncs.myDebugPrinting("Verifying the ''Attachments'' window.", enumsClass.logModes.MINOR);
	testFuncs.verifyStrByXpath(driver, "//*[@id='tab2atts']/div[1]/div", "");
	String file1 = driver.findElement(By.xpath("//*[@id=\"tab2atts\"]/div[1]/div/img")).getAttribute("src");
	testFuncs.myAssertTrue("The icon of the attachment is incorrect. <" + file1 + ">", file1.contains("img/office/p.svg"));
	String file2 = driver.findElement(By.xpath("//*[@id=\"tab2atts\"]/div[1]/div/div[1]/div[1]/i")).getAttribute("class");
	testFuncs.myAssertTrue("The icon of the attachment is incorrect. <" + file2 + ">", file2.contains("fa fa-trash text-danger"));
	String file3 = driver.findElement(By.xpath("//*[@id=\"tab2atts\"]/div[1]/div/div[1]/div[2]/a/i")).getAttribute("class");
	testFuncs.myAssertTrue("The icon of the attachment is incorrect. <" + file3 + ">", file3.contains("fa fa-download  text-primary"));
	testFuncs.verifyStrByXpath(driver, "//*[@id='tab2atts']/div[1]/div/div[2]/span"	  		  , "Sharing1.pptx");
	testFuncs.verifyStrByXpath(driver, "//*[@id=\"tab2atts\"]/div[2]/div[1]/div[1]/h5"		  , "Upload Attachment Files");
	testFuncs.verifyStrByXpath(driver, "//*[@id=\"fileuploader\"]/div[1]/span/b"	  		  , "Drag & Drop Files");
	testFuncs.verifyStrByXpath(driver, "//*[@id=\"tab2atts\"]/div[2]/div[2]/div/label/span[2]", "Overwrite existing file(s)");	
	testFuncs.myAssertTrue("The ''Overwrite existing file(s)'' option is not selected by default.", classElm2.contains("http://" + ip + "/api/meetings/" + meetingID + "/uploadMeetingAttachments?overrideFile=true"));
	
	//Step 4 - See the attachment from the List View
	testFuncs.myDebugPrinting("Step 4 - See the attachment from the List View", enumsClass.logModes.MINOR);
	testFuncs.pressHomeIcon(driver);
	testFuncs.myClick(driver, By.xpath("//*[@id=\"example\"]/tbody[2]/tr[1]/td[5]/div/button[1]"), 1000);
	testFuncs.myDebugPrinting("Verifying the contents of the ''Attachments'' window from the List view");
	testFuncs.verifyStrByXpath(driver, "//*[@id=\"id_modalAttachments\"]/div/div/div/div[1]/h4", 															"Attachments");
	testFuncs.verifyStrByXpath(driver, "//*[@id=\"id_modalAttachments\"]/div/div/div/div[2]/form/div/div/div/div/div/div/table/thead/tr/th[1]", 			"USER DISPLAY NAME");
	testFuncs.verifyStrByXpath(driver, "//*[@id=\"id_modalAttachments\"]/div/div/div/div[2]/form/div/div/div/div/div/div/table/thead/tr/th[2]", 			"CREATED AT");
	testFuncs.verifyStrByXpath(driver, "//*[@id=\"id_modalAttachments\"]/div/div/div/div[2]/form/div/div/div/div/div/div/table/thead/tr/th[3]", 			"ATTACHMENT NAME");
	testFuncs.verifyStrByXpath(driver, "//*[@id=\"id_modalAttachments\"]/div/div/div/div[2]/form/div/div/div/div/div/div/table/thead/tr/th[4]", 			"USER NAME");
	testFuncs.verifyStrByXpath(driver, "//*[@id=\"id_modalAttachments\"]/div/div/div/div[2]/form/div/div/div/div/div/div/table/thead/tr/th[5]", 			"");
	testFuncs.verifyStrByXpath(driver, "//*[@id=\"id_modalAttachments\"]/div/div/div/div[2]/form/div/div/div/div/div/div/table/tbody/tr/td[1]/span/span", 	"Me");
	testFuncs.verifyStrByXpath(driver, "//*[@id=\"id_modalAttachments\"]/div/div/div/div[2]/form/div/div/div/div/div/div/table/tbody/tr/td[3]/span", 		fileName);
	testFuncs.verifyStrByXpath(driver, "//*[@id=\"id_modalAttachments\"]/div/div/div/div[2]/form/div/div/div/div/div/div/table/tbody/tr/td[4]/span", 		"cloudbond365\\cbpro4");
	testFuncs.myAssertTrue("Missing download attachment button.", driver.findElement(By.xpath("//*[@id=\"id_modalAttachments\"]/div/div/div/div[2]/form/div/div/div/div/div/div/table/tbody/tr/td[5]/a/i")).getAttribute("class").contains("fa fa-download"));
	testFuncs.myAssertTrue("Missing exit button from window.", driver.findElement(By.xpath("//*[@id=\"id_modalAttachments\"]/div/div/div/div[1]/button/i")).getAttribute("class").contains("pg-close fs-14"));
	testFuncs.myClick(driver, By.xpath("//*[@id=\"id_modalAttachments\"]/div/div/div/div[1]/button/i"), 1000);  // exit from the window
	
	// Step 5 - Delete the meeting
	testFuncs.myDebugPrinting("Step 5 - Delete the meeting", enumsClass.logModes.MAJOR);
	testFuncs.pressHomeIcon(driver);
	meeting.deleteMeeting(driver, meetingSubject);
  }

  @Test
  public void Attach_Two_Files() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());

	String meetingSubject = "meetingSubject" + testFuncs.getId();
	String path1 		  = testVars.getAttachmentPath_PPT();
	String path2 	 	  = testVars.getAttachmentPath_PPT2();
	String ip 			  = testVars.getUrl().split("/")[0];
	
	// Login the system, Enter the Import-Meeting menu and create a meeting
	testFuncs.myDebugPrinting("Login the system, Enter the Import-Meeting menu and create a meeting");  
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader());   
	meeting.createMeetingAfterProcMode(driver, meetingSubject, testVars.getMeetingMp4Path(), 240);

	// Step 1 - Enter the Player-Attachments page
	testFuncs.myDebugPrinting("Step 1 - Enter the Player-Attachments page");	
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO_ATTACHMENTS);  
	
  	// Step 2 - Add one attachment from the Player page
  	testFuncs.myDebugPrinting("Step 2 - Add one attachment from the Player page");
  	testFuncs.myDebugPrinting("Verifying the contents of the ''Upload Attachment Content'' window", enumsClass.logModes.MINOR);
  	String meetingID = driver.getCurrentUrl().split("=")[1];
  	String classElm2 = driver.findElement(By.xpath("//*[@id=\"fileuploader\"]/div[1]/div/form")).getAttribute("action");
  	testFuncs.myAssertTrue("The ''Overwrite existing file(s)'' option is not selected by default.", classElm2.contains("http://" + ip + "/api/meetings/" + meetingID + "/uploadMeetingAttachments?overrideFile=true"));
  	testFuncs.verifyStrByXpath(driver, "//*[@id=\"tab2atts\"]/div[2]/div[1]/div[1]/h5"		  , "Upload Attachment Files");
  	testFuncs.verifyStrByXpath(driver, "//*[@id=\"fileuploader\"]/div[1]/span/b"			  , "Drag & Drop Files");
  	testFuncs.verifyStrByXpath(driver, "//*[@id=\"tab2atts\"]/div[2]/div[2]/div/label/span[2]", "Overwrite existing file(s)");
  	
	// Upload two files
  	testFuncs.myDebugPrinting("Upload two files", enumsClass.logModes.NORMAL);
	testFuncs.myDebugPrinting("path1 - " + path1, enumsClass.logModes.MINOR);	
	testFuncs.myDebugPrinting("path2 - " + path2, enumsClass.logModes.MINOR);	
	testFuncs.mySendKeysWithoutWait(driver, By.name("file"), path1, 10000);
	testFuncs.mySendKeysWithoutWait(driver, By.name("file"), path2, 10000);	 
	
	// Click Upload
	testFuncs.myDebugPrinting("Click Upload", enumsClass.logModes.MINOR);	
	testFuncs.myClick(driver, By.xpath("//*[@id=\"tab2atts\"]/div[2]/div[2]/button"), 20000); 
	testFuncs.verifyStrBy(driver, By.id("modalTitleId")  , "Upload File");
	testFuncs.verifyStrBy(driver, By.id("modalContentId"), "Attachment uploaded successfully");
	testFuncs.myClick(driver, By.xpath("/html/body/div[6]/div/button[1]")			, 10000);
	
	// Step 3 - Verify attachment slides
	testFuncs.myDebugPrinting("Step 3 - Verify attachment slides");
	testFuncs.myDebugPrinting("Verifying the Attachment Slides.", enumsClass.logModes.MINOR);
	int fileSlidesNum = videoPlayer.getFileSlidesNumber(driver);
	testFuncs.myAssertTrue("Not all slides of the file are displayed ! <fileSlidesNum - " + fileSlidesNum + ">", fileSlidesNum == 17);
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO_ATTACHMENTS_SLIDES_TAB);
	testFuncs.myDebugPrinting("Verifying the ''Attachments'' window.", enumsClass.logModes.MINOR);
	int filesNum = videoPlayer.getAttachmentNumber(driver);
	testFuncs.myAssertTrue("Not all the attachments are displayed ! <filesNum - " + filesNum + ">", filesNum == 2);
	
	//Step 4 - See the attachment from the List View
	testFuncs.myDebugPrinting("Step 4 - Verify the uploaded attachment from the List View");
	testFuncs.pressHomeIcon(driver);
	testFuncs.myClick(driver, By.xpath("//*[@id=\"example\"]/tbody[2]/tr[1]/td[5]/div/button[1]"), 1000);
	testFuncs.myDebugPrinting("Verifying the contents of the ''Attachments'' window from the List view");
	testFuncs.verifyStrByXpath(driver, "//*[@id=\"id_modalAttachments\"]/div/div/div/div[1]/h4", 															"Attachments");
	testFuncs.verifyStrByXpath(driver, "//*[@id=\"id_modalAttachments\"]/div/div/div/div[2]/form/div/div/div/div/div/div/table/thead/tr/th[1]", 			"USER DISPLAY NAME");
	testFuncs.verifyStrByXpath(driver, "//*[@id=\"id_modalAttachments\"]/div/div/div/div[2]/form/div/div/div/div/div/div/table/thead/tr/th[2]", 			"CREATED AT");
	testFuncs.verifyStrByXpath(driver, "//*[@id=\"id_modalAttachments\"]/div/div/div/div[2]/form/div/div/div/div/div/div/table/thead/tr/th[3]", 			"ATTACHMENT NAME");
	testFuncs.verifyStrByXpath(driver, "//*[@id=\"id_modalAttachments\"]/div/div/div/div[2]/form/div/div/div/div/div/div/table/thead/tr/th[4]", 			"USER NAME");
	testFuncs.verifyStrByXpath(driver, "//*[@id=\"id_modalAttachments\"]/div/div/div/div[2]/form/div/div/div/div/div/div/table/tbody/tr[1]/td[1]/span/span","Me");
	testFuncs.verifyStrByXpath(driver, "//*[@id=\"id_modalAttachments\"]/div/div/div/div[2]/form/div/div/div/div/div/div/table/tbody/tr[1]/td[3]/span", 	"UX - Updated 13 9 18 [Autosaved].pptx");
	testFuncs.verifyStrByXpath(driver, "//*[@id=\"id_modalAttachments\"]/div/div/div/div[2]/form/div/div/div/div/div/div/table/tbody/tr[1]/td[4]/span", 	"cloudbond365\\cbpro4");
	testFuncs.verifyStrByXpath(driver, "//*[@id=\"id_modalAttachments\"]/div/div/div/div[2]/form/div/div/div/div/div/div/table/tbody/tr[2]/td[1]/span/span","Me");
	testFuncs.verifyStrByXpath(driver, "//*[@id=\"id_modalAttachments\"]/div/div/div/div[2]/form/div/div/div/div/div/div/table/tbody/tr[2]/td[3]/span", 	"Sharing1.pptx");
	testFuncs.verifyStrByXpath(driver, "//*[@id=\"id_modalAttachments\"]/div/div/div/div[2]/form/div/div/div/div/div/div/table/tbody/tr[2]/td[4]/span", 	"cloudbond365\\cbpro4");
	testFuncs.myAssertTrue("Missing download attachment button.", driver.findElement(By.xpath("//*[@id=\"id_modalAttachments\"]/div/div/div/div[2]/form/div/div/div/div/div/div/table/tbody/tr[1]/td[5]/a/i")).getAttribute("class").contains("fa fa-download"));
	testFuncs.myAssertTrue("Missing download attachment button.", driver.findElement(By.xpath("//*[@id=\"id_modalAttachments\"]/div/div/div/div[2]/form/div/div/div/div/div/div/table/tbody/tr[2]/td[5]/a/i")).getAttribute("class").contains("fa fa-download"));
	testFuncs.myAssertTrue("Missing exit button from window.", driver.findElement(By.xpath("//*[@id=\"id_modalAttachments\"]/div/div/div/div[1]/button/i")).getAttribute("class").contains("pg-close fs-14"));
	testFuncs.myClick(driver, By.xpath("//*[@id=\"id_modalAttachments\"]/div/div/div/div[1]/button/i"), 2000);

	// Step 5 - Delete the meeting
	testFuncs.myDebugPrinting("Step 5 - Delete the meeting", enumsClass.logModes.MAJOR);
	testFuncs.pressHomeIcon(driver);
	meeting.deleteMeeting(driver, meetingSubject);
  }

  @Test
  public void Cancel_Abort_Hide_Upload() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	 
	String meetingSubject = "meetingSubject" + testFuncs.getId();
	String path 		  = testVars.getAttachmentPath_PPT();
	String fileName 	  = testVars.getAttachmentPPT1name();
	
	// Login the system, Enter the Import-Meeting menu and create a meeting
	testFuncs.myDebugPrinting("Login the system, Enter the Import-Meeting menu and create a meeting");  
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader());   
	meeting.createMeetingAfterProcMode(driver, meetingSubject, testVars.getMeetingMp4Path(), 240);
	
	// Step 1 - Enter the Player-Attachments page
	testFuncs.myDebugPrinting("Step 1 - Enter the Player-Attachments page");	
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO_ATTACHMENTS);  
	
	// Add attachment & Choose a file
	testFuncs.myDebugPrinting("Add attachment & Choose a file", enumsClass.logModes.MINOR);
	testFuncs.myDebugPrinting("path1 - " + path, enumsClass.logModes.MINOR);	
	testFuncs.mySendKeysWithoutWait(driver, By.name("file"), path, 5000);

	// Step 2 - Cancel the file
	testFuncs.myDebugPrinting("Step 2 - Cancel the file");
	testFuncs.verifyStrByXpath(driver, "//*[@id=\"uploadattach\"]/div[1]/div[3]/div/div[4]", "Cancel");
	testFuncs.myClick(driver, By.xpath("//*[@id=\"uploadattach\"]/div[1]/div[3]/div/div[4]"), 1000);
	testFuncs.searchStrFalse(driver, fileName);

	// Step 3 - Abort the upload
	testFuncs.myDebugPrinting("Step 3 - Abort the upload");
	testFuncs.mySendKeysWithoutWait(driver, By.name("file"), path, 7000);
	testFuncs.myClick(driver, By.xpath("//*[@id=\"uploadattach\"]/div[2]/button"), 0);
	testFuncs.myClick(driver, By.xpath("//*[@id=\"uploadattach\"]/div[1]/div[3]/div/div[3]"), 0);
	testFuncs.searchStrFalse(driver, fileName); 
	testFuncs.searchStrFalse(driver, fileName);

	// Step 4 - Delete the meeting
	testFuncs.myDebugPrinting("Step 4 - Delete the meeting", enumsClass.logModes.MAJOR);
	testFuncs.pressHomeIcon(driver);
	meeting.deleteMeeting(driver, meetingSubject);
  }
  
  @Test
  public void Overwrite_Attachment() throws Exception {
	
	Log.startTestCase(this.getClass().getName());

	String meetingSubject = "meetingSubject" + testFuncs.getId();
	String path 		  = testVars.getAttachmentPath_PPT();
	String fileName 	  = testVars.getAttachmentPPT1name();
	String ip			  = testVars.getUrl().split("/")[0];
	  
	// Login the system, Enter the Import-Meeting menu and create a meeting
	testFuncs.myDebugPrinting("Login the system, Enter the Import-Meeting menu and create a meeting");  
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader());   
	meeting.createMeetingAfterProcMode(driver, meetingSubject, testVars.getMeetingMp4Path(), 240);
	
	// Step 1 - Enter the Player-Attachments page
	testFuncs.myDebugPrinting("Step 1 - Enter the Player-Attachments page");	
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO_ATTACHMENTS);	
	
	//Step 2 - Add one attachment from the Player page
	testFuncs.myDebugPrinting("Step 2 - Add one attachment from the Player page");
	testFuncs.myDebugPrinting("Verifying the contents of the ''Upload Attachment Content'' window", enumsClass.logModes.MINOR);
	String meetingID = driver.getCurrentUrl().split("=")[1];
	String classElm2 = driver.findElement(By.xpath("//*[@id=\"fileuploader\"]/div[1]/div/form")).getAttribute("action");
	testFuncs.myAssertTrue("The ''Overwrite existing file(s)'' option is not selected by default.", classElm2.contains("http://" + ip + "/api/meetings/" + meetingID + "/uploadMeetingAttachments?overrideFile=true"));
	meeting.addAttachmentViaEditMenu(driver, path, fileName);
	
	//Step 3 - Overwrite the attachment
	testFuncs.myDebugPrinting("Step 3 - Overwrite the attachment");
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO_ATTACHMENTS_SLIDES_TAB);
	testFuncs.verifyStrByXpath(driver, "//*[@id=\"tab2atts\"]/div[2]/div[2]/div/label/span[2]", 	  "Overwrite existing file(s)");	
	testFuncs.myAssertTrue("The ''Overwrite existing file(s)'' option is not selected by default.", classElm2.contains("http://" + ip + "/api/meetings/" + meetingID + "/uploadMeetingAttachments?overrideFile=true"));
	meeting.addAttachmentViaEditMenu(driver, path, fileName);

	//Step 4 - Don't overwrite the attachment
	testFuncs.myDebugPrinting("Step 4 - Do not overwrite the attachment");
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO_ATTACHMENTS_SLIDES_TAB);
	testFuncs.verifyStrByXpath(driver, "//*[@id=\"tab2atts\"]/div[2]/div[2]/div/label/span[2]", 	  "Overwrite existing file(s)");	
	testFuncs.myAssertTrue("The ''Overwrite existing file(s)'' option is not selected by default.", classElm2.contains("http://" + ip + "/api/meetings/" + meetingID + "/uploadMeetingAttachments?overrideFile=true"));
	testFuncs.myClick(driver, By.xpath("//*[@id=\"tab2atts\"]/div[2]/div[2]/div/label/span[2]"), 1000);
	testFuncs.myDebugPrinting("Upload a the same file from the Player page", enumsClass.logModes.MINOR);
	testFuncs.myDebugPrinting("path - " + path, enumsClass.logModes.MINOR);
	testFuncs.mySendKeysWithoutWait(driver, By.name("file"), path, 7000);
	testFuncs.myClick(driver, By.xpath("//*[@id=\"uploadattach\"]/div[2]/button"), 2000);
	
	// Step 5 - Delete the meeting
	testFuncs.myDebugPrinting("Step 5 - Delete the meeting", enumsClass.logModes.MAJOR);
	testFuncs.pressHomeIcon(driver);
	meeting.deleteMeeting(driver, meetingSubject);	
  }
  
  @Test
  public void Delete_Attachment() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	
	String meetingSubject = "meetingSubject" + testFuncs.getId();
	String path 		  = testVars.getAttachmentPath_PPT();
	String fileName 	  = testVars.getAttachmentPPT1name();

	// Login the system, Enter the Import-Meeting menu and create a meeting
	testFuncs.myDebugPrinting("Login the system, Enter the Import-Meeting menu and create a meeting");  
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader());   
	meeting.createMeetingAfterProcMode(driver, meetingSubject, testVars.getMeetingMp4Path(), 240);
	
	// Step 1 - Enter the Player-Attachments page
	testFuncs.myDebugPrinting("Step 1 - Enter the Player-Attachments page");	
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO_ATTACHMENTS);
 	
  	//Step 2 - Add one attachment from the Player page
  	testFuncs.myDebugPrinting("Step 2 - Add one attachment from the Player page");
  	meeting.addAttachmentViaEditMenu(driver, path, fileName);
  	
  	// Verify the Attachments icon in the List view
	testFuncs.myDebugPrinting("Verify the Attachments icon in the List view", enumsClass.logModes.NORMAL);	
  	testFuncs.pressHomeIcon(driver);
	testFuncs.myClick(driver, By.xpath("//*[@id=\"example\"]/tbody[2]/tr[1]/td[5]/div/button[1]/img"), 2000);
	testFuncs.verifyStrByXpath(driver, "//*[@id=\"id_modalAttachments\"]/div/div/div/div[1]/h4", "Attachments");
	testFuncs.myClick(driver, By.xpath("//*[@id=\"id_modalAttachments\"]/div/div/div/div[1]/button/i"), 1000);
	
	// Re-Enter the Attachments tab
	testFuncs.myDebugPrinting("Re-Enter the Attachments tab", enumsClass.logModes.NORMAL);	
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO_ATTACHMENTS);
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO_ATTACHMENTS_SLIDES_TAB);

	// Step 3 - Delete attachment
	testFuncs.myDebugPrinting("Step 3 - Delete attachment");
	meeting.deleteAttachmentViaEditMenu(driver, fileName);
	
	// Step 4 - Delete the meeting
	testFuncs.myDebugPrinting("Step 4 - Delete the meeting", enumsClass.logModes.MAJOR);
	testFuncs.pressHomeIcon(driver);
	meeting.deleteMeeting(driver, meetingSubject);
  }
  
  @Test
  public void Download_Attachment() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	
	String meetingSubject = "meetingSubject" + testFuncs.getId();
	String path 		  = testVars.getAttachmentPath_PPT();
	String fileName 	  = testVars.getAttachmentPPT1name();
	  
	// Login the system, Enter the Import-Meeting menu and create a meeting
	testFuncs.myDebugPrinting("Login the system, Enter the Import-Meeting menu and create a meeting");  
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader());   
	meeting.createMeetingAfterProcMode(driver, meetingSubject, testVars.getMeetingMp4Path(), 240);
	
	// Step 1 - Enter the Player-Attachments page
	testFuncs.myDebugPrinting("Step 1 - Enter the Player-Attachments page");	
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO);
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO_ATTACHMENTS);
	
   	//Step 2 - Add one attachment from the Player page
   	testFuncs.myDebugPrinting("Step 2 - Add one attachment from the Player page");
   	meeting.addAttachmentViaEditMenu(driver, path, fileName);
   
	// Step 3 - Download Attachment via Player page
	testFuncs.myDebugPrinting("Step 3 - Download Attachment via Player page");	
	testFuncs.enterMenu(driver, enumsClass.menuNames.MEETING_DATA_PLAY_VIDEO_ATTACHMENTS_SLIDES_TAB);
	meeting.downloadAttachViaPlayerPage(driver, fileName);

	// Step 4 - Download an Attachment via the List view
	testFuncs.myDebugPrinting("Step 4 - Download an Attachment via the List view");
	meeting.downloadAttachViaListView(driver, fileName);

	// Step 5 - Delete the meeting
	testFuncs.myDebugPrinting("Step 5 - Delete the meeting", enumsClass.logModes.MAJOR);
	testFuncs.pressHomeIcon(driver);
	meeting.deleteMeeting(driver, meetingSubject);
  }
  
  @After
  public void tearDown() throws Exception {
	  
    driver.quit();
    System.clearProperty("webdriver.chrome.driver");
	System.clearProperty("webdriver.ie.driver");
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
    	
      testFuncs.myFail(verificationErrorString);
    }
  }
}