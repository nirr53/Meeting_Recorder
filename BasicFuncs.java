package MeetingRecorder;

import static org.junit.Assert.fail;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import MeetingRecorder.enumsClass.*;

/**
* This class holds all the base functions which been used by the tests
* @author Nir Klieman
* @version 1.00
*/

public class BasicFuncs  {
	  
	private static final Logger logger = LogManager.getLogger();

	/**
	*  Default constructor
	*/  
	public BasicFuncs() {  
		
	}
	
	/**
	*  Verify string in page based on read the whole page
	*  @param driver  - given driver
	*  @param strName - given string for detect
	*/  
	public void searchStr(WebDriver 	driver, String strName) {
		  	  
		String bodyText     = driver.findElement(By.tagName("body")).getText();	
		if (bodyText.contains(strName)) {
			  	
			myDebugPrinting("<" + strName + "> was detected !!",  enumsClass.logModes.DEBUG);
		  } 
		else {
			  	  
			myFail("<" + strName + "> was not detected !! \nbodyText - " + bodyText);	  
		}  
	}	  
	  
	/**
	*  Delete all files in directory by given prefix
	*  @param dir    - given directory path
	*  @param prefix - given prefix
	*/  
	public void deleteFilesByPrefix(String dir, String prefix) {
	    	
		myDebugPrinting("dir    - " + dir   ,  enumsClass.logModes.MINOR);
		myDebugPrinting("prefix - " + prefix,  enumsClass.logModes.MINOR);
    	File[] dirFiles = new File(dir).listFiles();
    	int filesNum = dirFiles.length;
    	for (int i = 0; i < filesNum; i++) {
    		
    	    if (dirFiles[i].getName().startsWith(prefix, 0)) {
    	    	
    			myDebugPrinting("Delete file - " + dirFiles[i].getName(),  enumsClass.logModes.MINOR);
    	        new File(dir + "\\" + dirFiles[i].getName()).delete();
    		    myWait(1000);    
    	    }
    	}	
	    myWait(10000);  
	}
	
	/**
	*  Click on element by its displayed name
	*  @param driver  - given driver
	*  @param elName  - given string for detect
	*  @param timeout - given timeout after the press
	*/  
	public void clickByName(WebDriver driver, String elName, int timeout) {
	
		List<WebElement> hrefs = driver.findElements(By.tagName("a"));
		for (WebElement href : hrefs) {
			
			if (href.getText().contains(elName)) {
				
				href.click();
				myWait(timeout);
				return;
			}
		}
		myFail("<" + elName + "> was not detected !!");
	}
	
	/**
	*  Verify that Link is not found
	*  @param driver  - given driver
	*  @param elName  - given string for not detect
	*/  
	public void verifyLinkNotFound(WebDriver driver, String elName) {
	
		List<WebElement> hrefs = driver.findElements(By.tagName("a"));
		for (WebElement href : hrefs) {
			
			if (href.getText().contains(elName)) {
				
				myFail("<" + elName + "> was detected !!");
			}
		}
	}
	
	/**
	*  read file method
	*  @param  path    - given path for file to read
	*  @return content - string of the readed file
	*/
	String readFile(String path) {
		  
		    String content = null;
		    File file = new File(path);
		    FileReader reader = null;
		    try {
		    	
		        reader = new FileReader(file);
		        char[] chars = new char[(int) file.length()];
		        reader.read(chars);
		        content = new String(chars);
		        reader.close();
		    } catch (IOException e) {
		    } finally {
		    	
		        if(reader !=null) {
		        	
		        	try {
		        		
		        		reader.close();
		        	} catch (IOException e) {}
		        }
		    }
		    
			myWait(3000);
	    	myDebugPrinting("content - " + content, enumsClass.logModes.MINOR);
		    return content;	  
	}
	  
	/**
	*  Verify xpath contains a string
	*  @param driver   - given driver
	*  @param elemName - given element xpath
	*  @param strName  - given string for detect
	*/
	public void verifyStrByXpathContains(WebDriver 	driver, String xpath, String strName) {
	  	  
		  String txt = driver.findElement(By.xpath(xpath)).getText();
		  if (txt.contains(strName)) {
			  
		    	myDebugPrinting("<" + strName + "> was detected", enumsClass.logModes.DEBUG);
		  } else {
			  
			  myDebugPrinting(driver.findElement(By.xpath(xpath)).getText());
			  myFail ("<" + strName + "> was not detected !! (txt - <" + txt + ">)");
		  }
		  myWait(1000);  
	}
		
	/**
	*  Get text from a given By element
	*  @param driver - given driver
	*  @param byElem - given By element
	*  @return text of given element
	*/	  
	public String myGetText(WebDriver driver, By byElem) {
			    	  
		return driver.findElement(byElem).getText();
	}
	    
	/**
	*  Print a given string to the console
	*  @param str   - given string to print
	*  @param level - given print level (MAJOR, NORMAL, MINOR, DEBUG)
	*/
	public void myDebugPrinting(String str, logModes level) {
		    	  
		logger.info(level.getLevel() + str);
	}
	  
	/**
	*  Print a given string to the console with default level of MAJOR
	*  @param str - A given string to print
	*/
    public void myDebugPrinting(String str) {
				   
    	logger.info(enumsClass.logModes.MAJOR.getLevel() + str); 
    }
      
	/**
	*  Print a given error string and declares the test as a myFailure
	*  @param str - A given error string
	*/
    public void myFail(String str) {
				
      logger.error(str);		
      fail(str); 
    }
      
	/**
	*  Verify string  method by xpath
	*  @param driver   - given driver
	*  @param elemName - given element name
	*  @param strName  - given string for detect
	*/
	public void verifyStrByXpath(WebDriver 	driver, String elemName, String strName) {		  
	   
		markElemet(driver, driver.findElement(By.xpath(elemName)));	   
		String txt = driver.findElement(By.xpath(elemName)).getText();  	   
		myAssertTrue("<" + strName + "> was not detected by xpath!! <" + txt + ">", txt.contains(strName));	  	 		
		myDebugPrinting("<" + strName + "> was detected by xpath!", enumsClass.logModes.DEBUG);	   		
		myWait(50);  
	}
	
	/**
	*  Verify string method by By element
	*  @param driver    - given driver
	*  @param byElement - given By element
	*  @param strName   - given string for detect
	*/
	public void verifyStrBy(WebDriver 	driver, By byElement, String strName) {		  
	   
		markElemet(driver, driver.findElement(byElement));	   
		String txt = driver.findElement(byElement).getText();  	   
		myAssertTrue("<" + strName + "> was not detected by xpath!! <" + txt + ">", txt.contains(strName));	  	 		
		myDebugPrinting("<" + strName + "> was detected by xpath!", enumsClass.logModes.DEBUG);	   		
		myWait(50);  
	}
	  
	/**
	*  Highlight given element
	*  @param driver  - given driver
	*  @param element - given element
	*/
	public void markElemet(WebDriver 	driver, WebElement element) {
					
	  // Mark element    
	  try {
    	  
		  ((JavascriptExecutor)driver).executeScript("arguments[0].style.border='3px solid yellow'", element);
		  TimeUnit.MILLISECONDS.sleep(500);
	  } catch (InterruptedException e1) {}   
	  ((JavascriptExecutor)driver).executeScript("arguments[0].style.border=''", element);  
	}
	
	/**
	*  Sleep for a given time
	*  @param sleepValue - given sleep factor
	*/
	public void myWait(int sleepValue) {
		
		try {
			TimeUnit.MILLISECONDS.sleep(sleepValue);		
	
	  
		} catch (InterruptedException e1) {
		}	
	}
	
	/**
	*  Hover a given element
	*  @param driver - given driver
	*  @param byElm	 - given By element
	*  @param timeout -given timeout after the Hover
	*/
	public void myHover(WebDriver driver, By byElm, int timeout) {

  	    WebElement we = driver.findElement(byElm);
		markElemet(driver, we);
  	    Actions action = new Actions(driver);
  	    action.moveToElement(we).perform();
  	    action.moveToElement(we,1,1).perform();
		myWait(timeout);
//		action = null;
	}
	  
	/**
	*  Create a unique Id based on current time
	*  @return - unique id based on current time 
	*/
	public String getId() {
		
	    // set id
	    DateFormat dateFormat = new SimpleDateFormat("HH_mm_dd_MM");
	    Date date     = new Date();
	    String id     = dateFormat.format(date);
	    id = id.replaceAll("_", "");
		myDebugPrinting("Id is:" + id, enumsClass.logModes.MAJOR);
		
	    return id;  
	}
	
	/**
	*  Return the current date and time
	*  @param format - format for SimpleDateFormat() class
	*  @return - current date and time id based on current time 
	*/
	
	public String getDateTime(String format) {
		
	    DateFormat dateFormat = new SimpleDateFormat(format);
	    Date date     = new Date();
	    String id     = dateFormat.format(date);
		myDebugPrinting("Id is:" + id, enumsClass.logModes.MAJOR);
		
	    return id;  
	}
	  
	/**
	*  Wrap assertTrue with logger
	*  @param errorStr  - error message for display at the logger
	*  @param condition - condition for mark if the assert succeeded or not
	*/
	public void myAssertTrue(String errorStr, Boolean condition) {
		  
		  if (!condition) {
			  myFail(errorStr);  
		  }
	}
	  
	/**
	*  Wrap assertFalse with logger
	*  @param errorStr  - error message for display at the logger
	*  @param condition - condition for mark if the assert succeeded or not
	*/
	public void myAssertFalse(String errorStr, Boolean condition) {
		  
		  if (condition) {
			  myFail(errorStr);  
		  }
	}
	  
	/**
	*  Get Random number according to given limit
	*  @param limit - upper limit for the random function
	*  @return      - random number in range of [1 - <limit>]
	*/
	public int getNum(int limit) {
		  
	  Random rand = new Random();	  
	  return (rand.nextInt(limit) + 1);
	}
	  
	/**
	*  Get a Random IP address
	*  @return - random IP address
	*/
	public String getRandomIp() {
		  	  	  
		return  (String.valueOf(getNum(128))   + "." +
				   String.valueOf(getNum(128)) + "." +
				   String.valueOf(getNum(128)) + "." +			 
				   String.valueOf(getNum(128)));  
	}
	  
	/**
	*  Get a Random Port (at range of 1000-9999 range)
	*  @return - random Port number
	*/
	public String getRandomPort() {
		  	  
		return  (String.valueOf(getNum(9)) +
				 String.valueOf(getNum(9)) +
				 String.valueOf(getNum(9)) +			 
				 String.valueOf(getNum(9)));  
	}
	  
	/**
	*  Find files in a given directory by a given prefix
	*  @param dir    - given directory path
	*  @param prefix - given prefix
	*  @return       - TRUE if files were found
	*/
	public boolean findFilesByGivenPrefix(String dir, String prefix) {
	    	
		myDebugPrinting("dir    - " + dir   ,  enumsClass.logModes.MINOR);
		myDebugPrinting("prefix - " + prefix,  enumsClass.logModes.MINOR);
    	File[] dirFiles = new File(dir).listFiles();
    	int filesNum = dirFiles.length;
    	for (int i = 0; i < filesNum; i++) {
    				
	    	myDebugPrinting(dirFiles[i].getName(),  enumsClass.logModes.DEBUG);
    	    if (dirFiles[i].getName().startsWith(prefix, 0)) {
    			
    	    	if (dirFiles[i].getName().contains("crdownload")) {
    	    		
	    	    	myDebugPrinting("crdownload suffix is detected. Waiting for another 60 seconds.",  enumsClass.logModes.MINOR);
    	    		myWait(60000);
    	    	}
    	    	myDebugPrinting("Find a file ! (" + dirFiles[i].getName() + ")",  enumsClass.logModes.MINOR);
    	        return true;
    	    }
    	}
    	
    	return false;
	}
	  
	/**
	*  Upload file with given path displayed on the screen
	*  @param driver  		  - given driver
	*  @param path    		  - path for a file for upload
	*  @param uploadFieldXpath  - xpath for upload field
	*  @param uploadButtonXpath - xpath for upload button
	*/
	public void uploadFile(WebDriver driver, String path, String uploadFieldXpath, String uploadButtonXpath) {
	      		  
		myDebugPrinting("path -   " + path, enumsClass.logModes.MINOR);
		mySendKeys(driver, By.xpath(uploadFieldXpath), path  , 2000);
		myClick(driver   , By.xpath(uploadButtonXpath)		 , 200000);
		if (driver.findElement(By.tagName("body")).getText().contains("Failed to import from selected file.")) {
			
			myFail("Upload configuration-file was failed !!");
		}
	}
	  
	/**
	*  Upload file with given path with confirm message-box
	*  @param driver  		   - given driver
	*  @param path    		   - path for a file for upload
	*  @param uploadFieldXpath   - xpath for upload field
	*  @param uploadButtonXpath  - xpath for upload button
	*  @param confirmMessageStrs - array of confirm-box strings
	*/
	public void uploadFile(WebDriver driver, String path, String uploadFieldXpath, String uploadButtonXpath, String[] confirmMessageStrs) {
	      		  
		myDebugPrinting("path -   " + path, enumsClass.logModes.MINOR);
		mySendKeys(driver, By.xpath(uploadFieldXpath), path  , 2000);
		myClick(driver   , By.xpath(uploadButtonXpath)		 , 5000);
		if (driver.findElement(By.tagName("body")).getText().contains("Failed to import from selected file.")) {
			
			myFail("Upload configuration-file was failed !!");
		}
		
		if (confirmMessageStrs != null && !confirmMessageStrs[0].isEmpty()) {
		
	    	verifyStrByXpath(driver, "//*[@id='modalTitleId']"	, confirmMessageStrs[0]);	
	    	verifyStrByXpath(driver, "//*[@id='modalContentId']", confirmMessageStrs[1]);	
			myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 20000);	
		}  
	}
	  
	/**
	* Set a driver according to a given browser name
	* @param  usedBrowser - given browser name (Chrome, FireFox or IE)
	* @return driver      - driver object (Failure if usedBrowser is not a valid browser name)
	*/
	public WebDriver defineUsedBrowser(browserTypes usedBrowser) {
		  
		  switch (usedBrowser) {  
		  
			  case CHROME:
				  ChromeOptions options = new ChromeOptions();			
				  options.addArguments("--start-maximized");			
				  return new ChromeDriver(options);
		
			  default:
				  myFail("<" + usedBrowser.toString() + "> is not valid !");	
				  break;
			  
		  }		  
		return null;
	}  
	  
	/**
	*  Send a string to a given element using given parameters
	*  @param driver  - given driver
	*  @param byType  - given By element (By xpath, name or id)
	*  @param currUsr - given string to send
	*  @param timeout - given timeout (Integer)
	*/
	public void mySendKeys(WebDriver driver, By byType, String currUsr, int timeOut) {
		
		  WebElement  clickedElem = driver.findElement(byType);
		  WebDriverWait wait 	  = new WebDriverWait(driver , 20);
		  wait.until(ExpectedConditions.visibilityOfElementLocated(byType));
		  wait.until(ExpectedConditions.elementToBeClickable(byType));	
		  wait.until(ExpectedConditions.presenceOfElementLocated(byType));	
		  clickedElem.clear();
		  myWait(1000);
		  clickedElem.sendKeys(currUsr);
		  waitForLoad(driver);
		  myWait(timeOut);	
		  wait = null;
	}
	
	/**
	*  Send a string to a given element using given parameters without clear
	*  @param driver  - given driver
	*  @param byType  - given By element (By xpath, name or id)
	*  @param currUsr - given string to send
	*  @param timeout - given timeout (Integer)
	*/
	public void mySendKeys(WebDriver driver, By byType, String currUsr, int timeOut, Boolean isClear) {
		
		WebElement  clickedElem = driver.findElement(byType);
		markElemet(driver, driver.findElement(byType));
		WebDriverWait wait 	    = new WebDriverWait(driver , 20);	  
//		wait.until(ExpectedConditions.and(ExpectedConditions.elementToBeClickable(byType),
//				  						  ExpectedConditions.visibilityOfElementLocated(byType)));			
		wait.until(ExpectedConditions.visibilityOfElementLocated(byType));
		wait.until(ExpectedConditions.elementToBeClickable(byType));
		if (isClear) {
			
			clickedElem.clear();
			myWait(2000);
		}
		clickedElem.sendKeys(currUsr);		
		myWait(timeOut); 
		wait = null;
	}
	  
	/**
	*  Click on given element by given xpath and waits a given timeout
	*  @param driver  - given driver
	*  @param byType  - given By element (By xpath, name or id)
	*  @param timeout - given timeout value (Integer)
	*/
	public void myClick(WebDriver driver, By byType, int timeout) {
			  		  	  
		markElemet(driver, driver.findElement(byType));	   
		WebElement  clickedElem = driver.findElement(byType);	
		WebDriverWait wait 	    = new WebDriverWait(driver , 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(byType));
		wait.until(ExpectedConditions.elementToBeClickable(byType));		
		clickedElem.click();	 
		waitForLoad(driver);
		myWait(timeout);
		wait = null;
	}
	
	
	/**
	*  Wait for page to load
	*  @param driver  		   - given driver
	*/ 
	
	public void waitForLoad(WebDriver driver) {
		    
		ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
		      
				return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");	  
			}
		};	  
		WebDriverWait wait = new WebDriverWait(driver,30);	        	          	  
		try {
		        	             	  
			wait.until(expectation);        	          
		} catch(Throwable error) {
		        	            
			  myFail("Timeout waiting for Page Load Request to complete.");	        	      	  
		}	
	  }
	
	/**
	*  Get current data in dd.MM.YYYY format
	*/
	public String getCurrdate() {
	 	  
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");    
		Date date     		= new Date();   
		String myDate     	= dateFormat.format(date);
		myDebugPrinting("date - " + myDate, enumsClass.logModes.MINOR);
		return myDate;
	}
	
	/**
	*  Get a path to list item and check if given string exists
	*  @param driver - given driver
	*  @param byType - given By element (By xpath, name or id)
	*  @param str    - given searched string
	*  @return true \ false
	*/
	public Boolean isListConatinsString(WebDriver driver, By byType, String str) {
		
		
		List<WebElement> listOfTags = driver.findElements(byType);
		for (WebElement currTag : listOfTags) {
			
			String currStr = currTag.getText();
//			myDebugPrinting("currTag.getText() - " + currStr, enumsClass.logModes.DEBUG);
			if (currStr.contains(str)) {
				
				myDebugPrinting("String <" + str + "> was detected !!", enumsClass.logModes.MINOR);
				return true;
			}
		}
		
		return false;
	}
	
	/**
	*  Verify that string should not be on the page. I.e. because it was deleted, because the window was closed, etc.
	*  @param driver  - given driver
	*  @param strName - given string for detect
	*/  
	public void searchStrFalse(WebDriver driver, String strName) {
	  	  
		String bodyText     = driver.findElement(By.tagName("body")).getText();	
		if (bodyText.contains(strName)) {
			
			myFail("<" + strName + "> was detected !! \nbodyText - " + bodyText);
		  } 
		else {
			  	  
			myDebugPrinting("<" + strName + "> was not detected !!",  enumsClass.logModes.DEBUG);	  
		}  
	}
}