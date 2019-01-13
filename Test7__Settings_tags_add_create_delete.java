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
* This test tests the Add-notes to Meeting mechanism
* -----------------
* Tests:
*    - Login the system
*    1. Create a Tag
*    2. Delete a Tag
* 
* Results:
*    1. The tag should be created successfully
*    2. The tag should be deleted successfully
*
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test7__Settings_tags_add_create_delete {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  static GlobalVars 	testVars;
  static GlobalFuncs	testFuncs;
  static Tags			tags;
  static String 		tagName;

  // Default constructor for print the name of the used browser 
  public Test7__Settings_tags_add_create_delete(browserTypes browser) {
	  
	  Log.info("Browser - "  + browser);
	  this.usedBrowser = browser;  
  }
  
  //Define each browser as a parameter
  @SuppressWarnings("rawtypes")
  @Parameters(name="{0}")
  public static Collection data() {
	  		
	  testVars  = new GlobalVars();	  
	  testFuncs = new GlobalFuncs(testVars); 	  
	  tags	    = new Tags(testFuncs);		
	  tagName   = "myTagName" + testFuncs.getId();
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
  public void Add___tag() throws Exception {
	  	  
	Log.startTestCase(name.getMethodName());	
			  
	// Login the system, Enter the Settings-menu
	testFuncs.myDebugPrinting("Login the system, Enter the Settings-menu");  
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETTINGS);

	// Create a tag
	testFuncs.myDebugPrinting("Create a tag");  
	tags.createTag(driver, tagName);
  }
 
  @Test
  public void Delete_tag() throws Exception {
	  	  
	Log.startTestCase(name.getMethodName());	
			  
	// Login the system, Enter the Settings-menu
	testFuncs.myDebugPrinting("Login the system, Enter the Settings-menu");  
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETTINGS);

	// Delete a created tag
	testFuncs.myDebugPrinting("Delete a created tag");  
	tags.deleteTag(driver, tagName);
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