package MeetingRecorder;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import MeetingRecorder.enumsClass.menuNames;

/**
* This class holds all the functions which been used by the tests
* @author Nir Klieman
* @version 1.00
*/

public class GlobalFuncs extends BasicFuncs {
	
	  GlobalVars 		   testVars;

	  /**
	  *  Default constructor
	  */
	  public GlobalFuncs(GlobalVars testVars) {
		  
		  this.testVars = testVars;	  
	  }
	  
	  /**
	  *  Login method
	  *  @param driver   - given driver for make all tasks
	  *  @param	url		 - given url to connect 
	  *  @param username - given string for system
	  *  @param password - given password for the system
	  *  @headers			TODO add
	  */
	  public void login(WebDriver 	driver, String url, String username, String password, String [] headers) {
		  
	      driver.get(url);
	      myWait(3000);
    	  searchStr(driver, headers[0]);  
	      myDebugPrinting("username - " + username ,enumsClass.logModes.MINOR);
    	  myDebugPrinting("password - " + password ,enumsClass.logModes.MINOR);
    	  mySendKeys(driver, By.xpath("//*[@id='loginform']/div[1]/input")	   , username, 2500);
    	  mySendKeys(driver, By.xpath("//*[@id='loginform']/div[2]/input")	   , password, 2500);    	  
    	  myClick(driver, By.xpath("//*[@id='loginform']/div[4]/div[2]/button"), 3000);
	      
	      // Verify good access
    	  myAssertTrue("Login fails ! (mainStr - " + headers[1] + " was not detected !!)", driver.findElement(By.tagName("body")).getText().contains(headers[1]));
	  }
	  
	  
	
	  /**
		
	  *  Convert time to seconds
	  *  
	  *  @param time 		- string that represent time. I.e. 13:12 01:23:11 etc.
	  *  @return secsTime - current seconds number
	  */ 
	  public int timeToSeconds(String time) {
		  
		  int secsTime = -1;	
		  myDebugPrinting("<timeToSeconds> time - " + time, enumsClass.logModes.MINOR); 
		  String [] timeParts = time.split(":");
		  if 		  (timeParts.length == 1) {
						
			  secsTime = Integer.valueOf(timeParts[0]);
		  } else if (timeParts.length == 2) {		
			
			  secsTime = ((Integer.valueOf(timeParts[0]) * 60) + Integer.valueOf(timeParts[1]));	
		  } else if (timeParts.length == 3) {
			
			  secsTime = ((Integer.valueOf(timeParts[0]) * 3600) + (Integer.valueOf(timeParts[1]) * 60) + Integer.valueOf(timeParts[2]));	
		  }
		  myDebugPrinting("<timeToSeconds> returned  secsTime - " + secsTime, enumsClass.logModes.MINOR); 
		  return secsTime;	  
	  }
	  
	  /**
	  *  Login the Meeting Recorder web-page
	  *  @param driver   - given WebDriver driver
	  *  @param url    	 - given WebDriver driver
	  *  @param username - given user-name for the web-page
	  *  @param password - given password for the web-page
	  *  @param header   - given header for verify good login
	  */
	  public void ntlmLogin(WebDriver driver, String url, String username, String password, String header) {
		
		  String tempUrl = "http://" + username + ":" + password + "@" + url + "/";
		  myDebugPrinting("tempUrl - " + tempUrl,enumsClass.logModes.MINOR);
		  driver.get(tempUrl); 
		  WebDriverWait wait = new WebDriverWait(driver, 30);
		  wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.id("searchInput")));
		  myWait(3000);
		  
		  // Verify good access
		  myAssertTrue("Login fails ! (Header - " + header + " was not detected !!)", driver.findElement(By.tagName("body")).getText().contains(header));  	
	  }
	  
	  /**
	  *  Press Home button
	  *  @param driver   - given driver for make all tasks
	  */
	  public void pressHomeIcon(WebDriver driver) {
		  
    	  myClick(driver, By.xpath("//a[@href='javascript:history.back()']"), 5000);	  
    	  verifyStrByXpath(driver, "//*[@id='findyourmeeting']/div[1]/h3", "Find your meeting");
    	  myDebugPrinting("pressHomeIcon() ended successfully !", enumsClass.logModes.DEBUG);	   		
	  }
	  
	  /**
	  *  Enter Edit page
	  *  @param driver   - given driver for make all tasks
	  */
	  public void enterEditPage(WebDriver driver) {
		  
		  // Enter to the Edit page
		  myDebugPrinting("Select Basic Info tab", enumsClass.logModes.NORMAL);
		  myClick(driver, By.xpath("//*[@id=\"btnGroupDrop1\"]/img")						    , 1000);
		  myClick(driver, By.xpath("//*[@id=\"example\"]/tbody[2]/tr[1]/td[6]/div/div/div/a[1]"), 3000);
	  }
	  
	  /**
	  *  Enter a given menu
	  *  @param driver        - given WebDriver driver
	  *  @param meetingImport - given menu name (enum)
	  */
	  public void enterMenu(WebDriver driver, menuNames menuName) {
		  
		  String header = menuName.getStrForSearch();
		  myDebugPrinting("Enter menu <" + menuName + "> with header <" + header + ">", enumsClass.logModes.NORMAL);
		  switch (menuName) {	
		    case MEETING_IMPORT:
		    	tryEnterMultipleTimes(driver, By.xpath("/html/body/nav/div[3]/div[1]/ul/li[2]/span"), header, 3);       
		    	break;	  		
		  	case MEETING_DATA_PLAY_VIDEO:
		  	    myClick(driver, By.xpath("//*[@id='example']/tbody[2]/tr/td[5]/div/button[4]"), 5000);   
		  	    searchStr(driver, header); 
		  		break;	
		  	case MEETING_DATA_PLAY_VIDEO_ADV_BUTTONS:
		  		myClick(driver, By.id("menu1")		, 2000);
		  		break;
		  	case MEETING_DATA_PLAY_VIDEO_SHARE_MEETING:
		  		myClick(driver, By.xpath("/html/body/div[2]/div[3]/div[2]/div[1]/div[1]/table/tbody/tr/td[1]/table/tbody/tr[2]/td/table/tbody/tr/td[3]/table/tbody/tr/td[2]/div"), 2000);
		  		break;  		
		  	case MEETING_DATA_PLAY_VIDEO_SESSION_IMAGES:
		  		myClick(driver, By.xpath("/html/body/div[2]/div[3]/div[2]/div[1]/div[1]/table/tbody/tr/td[1]/table/tbody/tr[2]/td/table/tbody/tr/td[3]/table/tbody/tr/td[3]/div"), 2000);	
		  		break;  		
		  	case MEETING_DATA_PLAY_VIDEO_SESSION_IMAGES_TRIMMING:
		  	  myClick(driver, By.linkText("TRIMMING"), 5000);	  		
		  	  break;	
		  	case MEETING_DATA_PLAY_VIDEO_ATTACHMENTS:
		  		myClick(driver, By.xpath("/html/body/div[2]/div[3]/div[2]/div[1]/div[1]/table/tbody/tr/td[1]/table/tbody/tr[2]/td/table/tbody/tr/td[3]/table/tbody/tr/td[4]/div"), 2000);
		  		break; 
		  	case MEETING_DATA_PLAY_VIDEO_ATTACHMENTS_SLIDES_TAB:
		  		myClick(driver, By.xpath("/html/body/div[2]/div[3]/div[2]/div[1]/div[1]/table/tbody/tr/td[4]/div/div[1]/ul/li[2]/a"), 2000);
		  		break;
		  	case MEETING_DATA_PLAY_VIDEO_ATTACHMENTS_TAB:
		  		myClick(driver, By.xpath("/html/body/div[2]/div[3]/div[2]/div[1]/div[1]/table/tbody/tr/td[4]/div/div[1]/ul/li[2]/a/img"), 2000);
		  		break; 
		  		
		  	case MEETING_DATA_PLAY_VIDEO_MEETING_FEED:
		  	    myClick(driver, By.partialLinkText("MEETING FEED"), 5000);
		  	    searchStr(driver, header); 
		  		break;	  		
		  	case MEETING_DATA_PLAY_VIDEO_ADD_ACTION_NOTE_TAG:
		  	    myClick(driver, By.partialLinkText("COMMENTS AND TAGS"), 5000);     
		  	    searchStr(driver, header); 
		  		break;		  		
		  	case MEETING_DATA_PLAY_VIDEO_PARTICIPANTS:
		  	    JavascriptExecutor js = (JavascriptExecutor)driver;
		        js.executeScript("window.scrollBy(0,-1000)");
		        myWait(1000);
		  	    myClick(driver, By.xpath("/html/body/div[2]/div[3]/div[2]/div[1]/div[1]/table/tbody/tr/td[5]/table/tbody/tr[1]/td/ul/li[2]/a"), 5000);
		  	    searchStr(driver, header);	
		  		break;	
		  	case MEETING_EDIT_MENU:
		  	    myClick(driver, By.xpath("//*[@id='btnGroupDrop1']/img")						 , 5000);
		  	    myClick(driver, By.xpath("//*[@id='example']/tbody[2]/tr/td[6]/div/div/div/a[1]"), 5000);
		  	    searchStr(driver, header);	  	    
		  		break;	
		  	case MEETING_EDIT_MENU_TAG_SECTION:
		  	    myClick(driver, By.xpath("/html/body/div[3]/div[3]/div/div[2]/div/ul/li[2]/a")   , 5000);
		  	    searchStr(driver, header);	  	    
		  		break;  
		  	case MEETING_EDIT_MENU_PARTICIPANT_SECTION:
		  	    myClick(driver, By.xpath("/html/body/div[3]/div[3]/div/div[2]/div/ul/li[7]/a")   , 5000);
		  	    searchStr(driver, header);
		  		break;	  		
		  	case SETTINGS:
		    	tryEnterMultipleTimes(driver, By.xpath("/html/body/nav/div[3]/div[1]/ul/li[3]/span"), header, 3); 
		  		break;
		  	case SETTINGS_DELEGATES_SECTION:	
		  		myClick(driver, By.linkText("DELEGATES"), 5000);
		  		searchStr(driver, header); 
		  		break;		  	
		  	case SETTINGS_SUBSCRIPTIONS_SECTION:	
		  		myClick(driver, By.linkText("SUBSCRIPTIONS"), 5000);
		  		searchStr(driver, header); 
		  		break;
		  	case SETTINGS_PREFERENCE_SECTION:	
		  		myClick(driver, By.linkText("PREFERENCE"), 5000);
		  		searchStr(driver, header); 
		  		break;
		  		
		  		
		  			
		  		
		  }    
	  }
	  
	  /**
	  *  Try enter to a menu for several times
	  *  @param driver     - given WebDriver driver
	  *  @param byType     - given By element for access
	  *  @param header     - given header of menu name for search
	  *  @param maxRetries - max retries
	  */
	  private void tryEnterMultipleTimes(WebDriver driver, By by, String header, int maxRetries) {
		   	
		  for (int i = 0; i < maxRetries; ++i) {
	    		
			  myClick(driver, by, 5000);  			
			  if (driver.findElement(By.tagName("body")).getText().contains(header)) {
	    			
				  return;
			  }
		  }
		  myFail("After <" + maxRetries + "> retries, <" + header + "> was not detected !!");  
	  }

	/**
	*  Get an index of specific row at given table
	*  @param driver  - given WebDriver driver
	*  @param byType  - given By element to search the table
	*  @param tagName - given record name
	*  @param index   - integer index of return record (-1 if not found)
	*/
	public int getIdxFromTable(WebDriver driver, By byType, String recName) {
		  	  	
		  WebElement table 	  = driver.findElement(byType);
		  String [] rowTables = table.getText().split("\n");	  
		  int i = 1;
		  
		  myDebugPrinting("searched recName <" + recName + ">",enumsClass.logModes.DEBUG);
//		  myDebugPrinting("searched table <" + table.getText() + ">",enumsClass.logModes.MINOR);
		  for (String tmpRowTxt : rowTables) {
			    
			  myDebugPrinting("tmpRowTxt - " + tmpRowTxt + " i - " + i,enumsClass.logModes.DEBUG);
			  if (tmpRowTxt.contains(recName)) {
				  
				  myDebugPrinting("Record <" + recName + "> was detected !! <idx - " + (i)/2 + ">", enumsClass.logModes.DEBUG);
				  return (i)/2;
			  }
			  if (!tmpRowTxt.isEmpty()) {
				  
				  i++;  
			  }
		  }
		
		  myDebugPrinting("Record <" + recName + "> was not detected !!", enumsClass.logModes.DEBUG);
		  return -1;
	}
	  
	/**
	*  Get an index of specific row at given table
	*  @param driver  - given WebDriver driver
	*  @param byType  - given By element to search the table
	*  @param tagName - given record name
	*  @param index   - integer index of return record (-1 if not found)
	*/
	public int getIdxFromTableNoDup(WebDriver driver, By byType, String recName) {
		
		WebElement table 	  = driver.findElement(byType);  
		String [] rowTables = table.getText().split("\n");		
		int i = 1;	
		myDebugPrinting("searched recName <" + recName + ">",enumsClass.logModes.DEBUG);	
		for (String tmpRowTxt : rowTables) {
			
			myDebugPrinting("tmpRowTxt - " + tmpRowTxt + " i - " + i,enumsClass.logModes.DEBUG);	
			if (tmpRowTxt.contains(recName)) {
			
				myDebugPrinting("Record <" + recName + "> was detected !! <idx - " + i + ">", enumsClass.logModes.DEBUG);	
				return i;	  
			}
			if (!tmpRowTxt.isEmpty()) {
			
				i++;		
			}	  
		}	
		myDebugPrinting("Record <" + recName + "> was not detected !!", enumsClass.logModes.DEBUG);		
		return -1;
	}

	/**
     *  Send a string to a given element using given parameters without clear
     *  @param driver  - given driver
     *  @param byType  - given By element (By xpath, name or id)
     *  @param currUsr - given string to send
     */
     public void mySendKeysWithoutWait(WebDriver driver, By byType, String currUsr, int timeOut) {
            
            WebElement  clickedElem = driver.findElement(byType);  
            clickedElem.sendKeys(currUsr);           
            myWait(timeOut);  
     }

}