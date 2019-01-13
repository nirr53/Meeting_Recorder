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
* This test tests the Delegates menu
* -----------------
* Tests:
*    1. Create a Delegate without pressing the SAVE button
*    2. Create a Delegate with pressing the SAVE button
*    3. Delete a Delegate without pressing the SAVE button
*    4. Delete a Delegate with pressing the SAVE button
* 
* Results:
*    1. The Delegate should not be created
*    2. The Delegate should be created successfully
*    3. The Delegate should not be deleted
*    4. The Delegate should be deleted successfully
*
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test8__Settings_delegates {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  static GlobalVars 	testVars;
  static GlobalFuncs	testFuncs;
  static String 		delName;
  static Delegate		delegates;

  // Default constructor for print the name of the used browser 
  public Test8__Settings_delegates(browserTypes browser) {
	  
	  Log.info("Browser - "  + browser);
	  this.usedBrowser = browser; 
  }
  
  //Define each browser as a parameter
  @SuppressWarnings("rawtypes")
  @Parameters(name="{0}")
  public static Collection data() {
	  
	  testVars  = new GlobalVars();
	  testFuncs = new GlobalFuncs(testVars); 
	  delegates = new Delegate(testFuncs);
	  delName   = "myDelName" + testFuncs.getId();
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
  public void Add_delegate() throws Exception {
	  	  
	Log.startTestCase(name.getMethodName());	
			  
	// Login the system, Enter the Settings-menu
	testFuncs.myDebugPrinting("Login the system, Enter the Settings-menu");  
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETTINGS);

	// Step 1 - Create a Delegate with Save
	testFuncs.myDebugPrinting("Step 1 - Create a Delegate with Save");  
	delegates.createDelegate(driver, delName);
  }
  
  @Test
  public void Delete_exisiting_delegate() throws Exception {
	  	  
	Log.startTestCase(name.getMethodName());	
			  
	// Login the system, Enter the Settings-menu
	testFuncs.myDebugPrinting("Login the system, Enter the Settings-menu");  
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader()); 
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETTINGS);

	// Step 1 - Delete a created Delegate with save 
	testFuncs.myDebugPrinting("Step 1 - Delete a created Delegate with save"); 
	delegates.deleteDelegate(driver, delName);
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