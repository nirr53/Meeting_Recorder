package MeetingRecorder;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.ParallelComputer;
import org.junit.runner.JUnitCore;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
 
public class Grid_SetUp {
	
	  public static GlobalFuncs	testFuncs;	
	  public static GlobalVars 	testVars;
	  private static int 		testNumber 		  	  = 1;
	  private static String		SelDirPath 	   		  = "C:\\Users\\nirk\\Desktop\\Selenium";
	  private static String	    usedStandAloneJarName = "selenium-server-standalone-3.13.0.jar";
	  private static String	    usedStandAlonePort 	  = "4499";

	  //Define each browser as a parameter
	  @SuppressWarnings("rawtypes")
	  @Parameters(name="{0}")
	  public static Collection data() {
	  
		  return Arrays.asList(enumsClass.browserTypes.CHROME);
	  }

	  @BeforeClass 
	  public static void setting_SystemProperties() throws IOException {
		  
		  testVars  = new GlobalVars();	    
		  testFuncs = new GlobalFuncs(testVars); 
		  
		  
		  System.out.println(System.getProperty("user.dir"));
		 	
//		  Process process = new ProcessBuilder("java -jar selenium-server-standalone-" + usedSelVersion + ".jar",
//				  																					"-port 4455",
//					 						   														"-role hub").directory(new File(SelDirPath)).start();
		  
		  Process process = new ProcessBuilder("runHub.bat").directory(new File(SelDirPath)).start();
		 	
		  BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));	    
		 
		  String line;		    
		 
		  while ((line = br.readLine()) != null) {
			    	
			  testFuncs.myDebugPrinting(line, enumsClass.logModes.MINOR);	
		 	}
		  
		  
		  Log.info("Start HUb");  
	  } 

	  @Test
	  public void runAllTests() {
		
		  final int testsNum = 1;
		  Class<?>[] classes = new Class<?>[testsNum];
		  for (int i = 0; i < testsNum; ++i) {
    	
			  classes[i] = ParallelTest.class;
		  }

		  // ParallelComputer(true,true) will run all classes and methods 
		  // in parallel.  (First arg for classes, second arg for methods)
		  JUnitCore.runClasses(new ParallelComputer(true, true), classes);      
//		  final Result result = JUnitCore.runClasses(classes);
//		  for (final Failure failure : result.getFailures()) {
//          
//			  System.out.println("11" + failure.toString());
//		  }
//		  System.out.println("00" + result.wasSuccessful());
	  }
 
	  // Example test
	  public static class ParallelTest {
		  
		  @Test 
		  public void Test1() throws MalformedURLException, InterruptedException {
			  
			  String URL = "http://cbpro4:smarttap@172.17.127.108:5080/ui/";		 
			  String Node = "http://172.17.113.30:4455/wd/hub";		 
			  String header = "Find your meeting";		 
			  System.out.println("-------------------------------------------------------------------");		 	 
			  System.out.println("Test <" + testNumber + "> started !!");		  
			  WebDriver driver =  getDriver(Node);	 
			  driver.get(URL);		 
			  WebDriverWait wait = new WebDriverWait(driver, 30);	 
			  wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.id("searchInput")));	  
			  Thread.sleep(1000);	  
			  testFuncs.myAssertTrue("Login fails ! (Header - " + header + " was not detected !!)", driver.findElement(By.tagName("body")).getText().contains(header));  		 
			  System.out.println("Test <" + testNumber + "> ended !!\n\n");	
			  testNumber++;
			  driver.quit(); 
		  }
	  }


	  public void startSelGrid(String cmd, String port) throws IOException {
	
		  // Build command line
		  testFuncs.myDebugPrinting("Build command line", enumsClass.logModes.MINOR);
		  //myDebugPrinting("crUserBatName - " + cmd, enumsClass.logModes.MINOR);

			
		//	BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
		//	String line;
		//	while ((line = br.readLine()) != null) {
		//	
		//	myDebugPrinting(line, enumsClass.logModes.MINOR);
			//}
	  }

	  private static WebDriver getDriver(String Node) throws MalformedURLException {
	
		  System.setProperty("webdriver.chrome.driver", "C:\\Users\\nirk\\Desktop\\Selenium\\chromedriver_win32_4\\chromedriver.exe");
		  DesiredCapabilities chromeCapabilities = DesiredCapabilities.chrome();
		  return (new RemoteWebDriver(new URL(Node), chromeCapabilities));  	
	  }
}