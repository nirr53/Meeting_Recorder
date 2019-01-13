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
* This test tests the login to Meeting Recorder web-page
* -----------------
* Tests:
*    1. Try to login the web-page with correct username and password
* 
* Results:
*    1. Login should succeed
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test1__login {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;

  
	public final static void main(String[] args) {
		  
		System.out.println("Hello world !!");
	  }  
  
  // Default constructor for print the name of the used browser 
  public Test1__login(browserTypes browser) {
	  
	  Log.info("Browser - "  + browser);
	  this.usedBrowser = browser;  
  }
  
  //Define each browser as a parameter
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
	  	
	testVars  = new GlobalVars();
    testFuncs = new GlobalFuncs(testVars); 
    System.setProperty("webdriver.chrome.driver", testVars.getChromeDrvPath());
	testFuncs.myDebugPrinting("Enter setUp(); usedbrowser - " + this.usedBrowser);
	driver = testFuncs.defineUsedBrowser(this.usedBrowser);
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }
  
  @Rule
  public TestName name= new TestName();

  @Test
  public void Login_correct_usrnm_correct_pswd() throws Exception {
	  
	Log.startTestCase(name.getMethodName());

    // Step 1 - Login the web-page with correct username and password
    testFuncs.myDebugPrinting("Login the web-page with correct username and password");
	testFuncs.ntlmLogin(driver, testVars.getUrl(), testVars.getUsername(), testVars.getPassword(), testVars.getGoodLoginHeader());  
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