package MeetingRecorder;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
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
* This test tests the Events tag
* -----------------
* Tests:
*    1. Check menu appearance
*    2. Check all tags and NOT press the SAVE button 
*    3. Check all tags and press the SAVE button
* 
* Results:
*    1. The menu should be displayed properly
*    2. The configuration should not be saved
*    3. The configuration should be saved successfully
*
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test9__Settings_events {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  static GlobalVars 	testVars;
  static GlobalFuncs	testFuncs;

  // Default constructor for print the name of the used browser 
  public Test9__Settings_events(browserTypes browser) {
	  
	  Log.info("Browser - "  + browser);
	  this.usedBrowser = browser; 
  }
  
  //Define each browser as a parameter
  @SuppressWarnings("rawtypes")
  @Parameters(name="{0}")
  public static Collection data() {
	  
	  testVars  = new GlobalVars();
	  testFuncs = new GlobalFuncs(testVars); 
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
  public void Check_menu_appearnce() throws Exception {
	  	  
	Log.startTestCase(name.getMethodName());	

	// Login the system, Enter the Settings-menu and Subscriptions tag
	testFuncs.myDebugPrinting("Login the system, Enter the Settings-menu and Subscriptions tag");  
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETTINGS);
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETTINGS_SUBSCRIPTIONS_SECTION);

	// Step 1 - Check headers
	testFuncs.myDebugPrinting("Step 1 - Check headers"); 
	String [] listOfHeaders = {"Meeting content changed",
							   "Meeting started",
							   "Meeting was delegated/undelegated to you",
							   "Meeting processing is finished, review before publishing",
							   "Meeting recording failed",
							   "Meeting recording finished, processing started",
							   "Meeting is ready for review",
							   "Meeting recorder shared with me. Meeting Recording sharing with me removed",
							   "I was added as a delegatee. I was removed from delegationlist"};
	for (String header : listOfHeaders) {
		
		testFuncs.myDebugPrinting("Search for header <" + header + ">", enumsClass.logModes.NORMAL); 
		testFuncs.searchStr(driver, header);
	}
  }
  
  @Test
  public void Check_all_values_without_save() throws Exception {
	  	  
	Log.startTestCase(name.getMethodName());	

	// Login the system, Enter the Settings-menu and Events tag
	testFuncs.myDebugPrinting("Login the system, Enter the Settings-menu and Events tag");  
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETTINGS);
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETTINGS_SUBSCRIPTIONS_SECTION);
	
	// Step 1 - Check all checkboxes WITHOUT save
	testFuncs.myDebugPrinting("Step 1 - Check all checkboxes WITHOUT save"); 
	WebElement table = driver.findElement(By.xpath("//*[@id='tab2events']/div/div/table/tbody"));	
	List<WebElement> tableRows = table.findElements(By.tagName("tr"));
	for (int i = 1; i < tableRows.size(); ++i) {
		
		WebElement tempElm = driver.findElement(By.xpath("//*[@id='tab2events']/div/div/table/tbody/tr[" + i + "]/td[1]/div/label/input"));
		testFuncs.myDebugPrinting("<" + i + ">. element - " + tempElm.getText(), enumsClass.logModes.MINOR); 
		if (!tempElm.isSelected()) {
			
			testFuncs.myDebugPrinting("<" + i + ">. element - " + tempElm.getText() + " is pressed !!", enumsClass.logModes.MINOR); 
			testFuncs.myClick(driver, By.xpath("//*[@id='tab2events']/div/div/table/tbody/tr[" + i + "]/td[1]/div/label"), 1000);
		}
	}
	
	// Step 2 - Return to main menu, and verify that not all values are checked
	testFuncs.myDebugPrinting("Step 2 - Return to main menu, and verify that not all values are checked"); 
	testFuncs.pressHomeIcon(driver);
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETTINGS);
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETTINGS_SUBSCRIPTIONS_SECTION);
	Boolean notAllSelected = false;
	for (int i = 1; i < tableRows.size(); ++i) {
		
		WebElement tempElm = driver.findElement(By.xpath("//*[@id='tab2events']/div/div/table/tbody/tr[" + i + "]/td[1]/div/label/input"));
		testFuncs.myDebugPrinting("<" + i + ">. element - " + tempElm.getText(), enumsClass.logModes.MINOR); 
		if (!tempElm.isSelected()) {
			
			notAllSelected = true;
			break;	
		}
	}
	testFuncs.myAssertTrue("Configuration was saved !!", notAllSelected);
  }
  
  @Test
  public void Check_all_values_with_save() throws Exception {
	  	  
	Log.startTestCase(name.getMethodName());	

	// Login the system, Enter the Settings-menu and Events tag
	testFuncs.myDebugPrinting("Login the system, Enter the Settings-menu and Events tag");  
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETTINGS);
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETTINGS_SUBSCRIPTIONS_SECTION);
	
	// Step 1 - Check all checkboxes WITH save
	testFuncs.myDebugPrinting("Step 1 - Check all checkboxes WITH save"); 
	WebElement table = driver.findElement(By.xpath("//*[@id='tab2events']/div/div/table/tbody"));	
	List<WebElement> tableRows = table.findElements(By.tagName("tr"));
	for (int i = 1; i < tableRows.size(); ++i) {
			
		WebElement tempElm = driver.findElement(By.xpath("//*[@id='tab2events']/div/div/table/tbody/tr[" + i + "]/td[1]/div/label/input"));
		testFuncs.myDebugPrinting("<" + i + ">. element - " + tempElm.getText(), enumsClass.logModes.MINOR); 
		if (!tempElm.isSelected()) {
			
			testFuncs.myDebugPrinting("<" + i + ">. element - " + tempElm.getText() + " is pressed !!", enumsClass.logModes.MINOR); 
			testFuncs.myClick(driver, By.xpath("//*[@id='tab2events']/div/div/table/tbody/tr[" + i + "]/td[1]/div/label"), 1000);
		}
	}
	
	// Save user configuration
	testFuncs.myDebugPrinting("Save user configuration", enumsClass.logModes.NORMAL); 
	SaveUserConfiguration();
		
	// Step 2 - Return to main menu, and verify that not all values are checked
	testFuncs.myDebugPrinting("Step 2 - Return to main menu, and verify that not all values are checked"); 
	testFuncs.pressHomeIcon(driver);
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETTINGS);
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETTINGS_SUBSCRIPTIONS_SECTION);
	for (int i = 1; i < tableRows.size(); ++i) {
		
		WebElement tempElm = driver.findElement(By.xpath("//*[@id='tab2events']/div/div/table/tbody/tr[" + i + "]/td[1]/div/label/input"));
		testFuncs.myDebugPrinting("<" + i + ">. element - " + tempElm.getText(), enumsClass.logModes.MINOR); 
		testFuncs.myAssertTrue("Configuration was not saved !!", tempElm.isSelected());
	}
  }
  
  public void SaveUserConfiguration() {
	  
	  testFuncs.myClick(driver, By.xpath("//*[@id='tab2events']/div/div/div/button"), 2000);
	  testFuncs.verifyStrBy(driver, By.id("modalTitleId")	 , "Save User Events");
	  testFuncs.verifyStrBy(driver, By.id("modalContentId"), "Successful to save user events");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[4]/div/button[1]"), 2000);  
  }

  @After
  public void tearDown() throws Exception {
	  
	  // Restore configuration
	  testFuncs.myDebugPrinting("Restore configuration", enumsClass.logModes.NORMAL); 
	  testFuncs.pressHomeIcon(driver);
	  testFuncs.enterMenu(driver, enumsClass.menuNames.SETTINGS);
	  testFuncs.enterMenu(driver, enumsClass.menuNames.SETTINGS_SUBSCRIPTIONS_SECTION);
	  int [] indxs = {3,4,5};
	  for (int i : indxs) {
		  
		  WebElement tempElm = driver.findElement(By.xpath("//*[@id='tab2events']/div/div/table/tbody/tr[" + i + "]/td[1]/div/label/input"));
		  if (tempElm.isSelected()) {
				
			  testFuncs.myClick(driver, By.xpath("//*[@id='tab2events']/div/div/table/tbody/tr[" + i + "]/td[1]/div/label"), 1000);
		  }
	  }
	  SaveUserConfiguration();	   
	  driver.quit();  
	  System.clearProperty("webdriver.chrome.driver");	
	  String verificationErrorString = verificationErrors.toString();   
	  if (!"".equals(verificationErrorString)) {
		  testFuncs.myFail(verificationErrorString);
	  }
  }
}