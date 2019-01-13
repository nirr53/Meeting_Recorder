package MeetingRecorder;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import MeetingRecorder.enumsClass.fullScreen;
import MeetingRecorder.enumsClass.muteState;
import MeetingRecorder.enumsClass.surferMode;

public class VideoPlayer {
	
	
	GlobalFuncs			testFuncs;
	Meeting				meeting;
	
	public VideoPlayer(GlobalFuncs testFuncs) {
		
		this.testFuncs = testFuncs;	
	}
	
	public VideoPlayer(GlobalFuncs testFuncs, Meeting meeting) {
		
		this.testFuncs = testFuncs;	
		this.meeting   = meeting;
	}
	
	/**
	*  Play a video for the first time
	*  @param driver  - given WebDriver driver
	*  @param timeOut - given timeOut after the send of the Play command
	*/ 
	public void playVideoFirstTime2(WebDriver driver, int timeOut) {
		
		// Play a Video for the first time
		testFuncs.myDebugPrinting("Play a Video for the first time", enumsClass.logModes.NORMAL);
		testFuncs.myClick(driver, By.xpath("//*[@id='example_video_1']/button"), timeOut);
	}
	
	/**
	*  Play a video (not first time)
	*  @param driver  - given WebDriver driver
	*  @param timeOut - given timeOut after the send of the Play command
	*/ 
	public void playVideo(WebDriver driver, int timeOut) {
		
		// Play a Video
		testFuncs.myDebugPrinting("Play a Video", enumsClass.logModes.MINOR);	
		String videoClassAtt = driver.findElement(By.xpath("//*[@id='example_video_1']")).getAttribute("class");	
		if (videoClassAtt.contains("vjs-paused")) {
			
			testFuncs.myDebugPrinting("Play a Video for first time", enumsClass.logModes.MINOR);	
			testFuncs.myClick(driver, By.xpath("//*[@id='example_video_1']/button"), timeOut);	

		} else {
			
			testFuncs.myDebugPrinting("Play a Video after it was already been played", enumsClass.logModes.MINOR);
			testFuncs.myClick(driver, By.xpath("//*[@id='example_video_1']/div[5]/button[1]"), timeOut);
		}
	}
	
	/**
	*  When a video is played, wait till it ended
	*  @param driver - given WebDriver driver
	*/
	public void waitTillEndOfVideo(WebDriver driver) {
		
		testFuncs.myDebugPrinting("Wait for end of the video", enumsClass.logModes.NORMAL);
		while (true) {
			
			String playerElmClass = driver.findElement(By.xpath("//*[@id='example_video_1']")).getAttribute("class");
			if (playerElmClass.contains("vjs-paused") && playerElmClass.contains("vjs-ended")) {		  
				break;	
			}			
			testFuncs.myWait(1000);
		}	  
		testFuncs.myWait(1000);	  
		testFuncs.myDebugPrinting("Playing of the meeting ended", enumsClass.logModes.MINOR); 
	}
	
	/**
	*  Skip till video is ended
	*  @param driver - given WebDriver driver
	*/
	public void skipTillEndOfVideo(WebDriver driver) {
		
		testFuncs.myDebugPrinting("Skip till end of the video", enumsClass.logModes.NORMAL);
		playVideo(driver, 1000);
		while (true) {
			
			String playerElmClass = driver.findElement(By.xpath("//*[@id='example_video_1']")).getAttribute("class");
			if (playerElmClass.contains("vjs-paused") && playerElmClass.contains("vjs-ended")) {		  
				break;	
			}
			hoverVideoPlayer(driver);
			pressSkipForwards(driver, 2000);
			hoverVideoPlayer(driver);
		}	  
		testFuncs.myWait(1000);	  
		testFuncs.myDebugPrinting("Skip till end of the video ended successfully", enumsClass.logModes.NORMAL);
	}
	
	/**
	*  When a video is played, wait till it ended
	*  @param driver 	   - given WebDriver driver
	*  @param searchedTime - given time for search a text in it
	*  @param searchedText - given text for search at video player
	*/ 
	public boolean verifyThatItemIsDisplayed(WebDriver driver, String searchedTime, String searchText) {
		
		// Wait till end of video, and in given time, search for displayed item
		testFuncs.myDebugPrinting("Wait till end of video, and in given time, search for displayed item", enumsClass.logModes.NORMAL);
		searchedTime = searchedTime.split(":")[2];
		String currTxt = null;
		while (true) {
			
			// Detect if playing was ended
			testFuncs.myDebugPrinting("Detect if playing was ended", enumsClass.logModes.MINOR);
			String playerElmClass = driver.findElement(By.xpath("//*[@id='example_video_1']")).getAttribute("class");
			if (playerElmClass.contains("vjs-paused") && playerElmClass.contains("vjs-ended")) {		  
				
				testFuncs.myDebugPrinting("Playing ended and < " + searchedTime + " > was not detected !!", enumsClass.logModes.MINOR); 						
				return false;	
			}			
			
			// Detect current time
			String currTime = driver.findElement(By.xpath("//*[@id='example_video_1']/div[5]/div[5]/div/div[3]")).getAttribute("data-current-time");
			currTime = currTime.split(":")[1];
			testFuncs.myDebugPrinting("currTime - "  + currTime + " <--> searchedTime - " + searchedTime, enumsClass.logModes.DEBUG); 	
			while (searchedTime.contains(currTime)) {
				
				// The current time should display the item
				testFuncs.myDebugPrinting("The current time should display the item", enumsClass.logModes.NORMAL); 	
				hoverVideoPlayer(driver, 300);
				for (int i = 0; i < 8; ++i) {
					
					try {
						
						currTxt = driver.findElement(By.xpath("//*[@id='example_video_1']/div[8]/div/div")).getText();	  
					} catch (Exception e) {
						
							return false;		
					}
					testFuncs.myDebugPrinting("currTxt - " + currTxt, enumsClass.logModes.DEBUG);
					testFuncs.myWait(500);
					if (currTxt.contains(searchText)) {	
						
						testFuncs.myDebugPrinting("< " + searchText + " > was detected !!", enumsClass.logModes.MINOR); 						
						return true;
					}
				}	
				
				testFuncs.myFail("<After time detect " + searchText + " > was not detected !!");
			}
			
			testFuncs.myWait(900);
		}
	  }
	
	/**
	*  Get current time from played video
	*  @param driver 	   - given WebDriver driver
	*  @param counterXpath - xpath of counter
	*  @return currTime	   - current time
	*/ 
	public int getCurrPlayedTime(WebDriver driver, String counterXpath) {
		
		hoverVideoPlayer(driver);
		String time  = driver.findElement(By.xpath(counterXpath)).getText();
		int currTime = -1;
		time = (time.contains("\n")) ? time.split("\n")[1] : time;
		testFuncs.myDebugPrinting("<getCurrPlayedTime> time - " + time, enumsClass.logModes.MINOR); 
		String [] timeParts = time.split(":");
		if 		  (timeParts.length == 1) {
					
			currTime = Integer.valueOf(timeParts[0]);
		} else if (timeParts.length == 2) {
			
			currTime = ((Integer.valueOf(timeParts[0]) * 60) + Integer.valueOf(timeParts[1]));
		} else if (timeParts.length == 3) {
			
			currTime = ((Integer.valueOf(timeParts[0]) * 3600) + (Integer.valueOf(timeParts[1]) * 60) + Integer.valueOf(timeParts[2]));
		}
		testFuncs.myDebugPrinting("<currTime> currTime - " + currTime, enumsClass.logModes.MINOR); 
		return currTime;
	}
	
	/**
	*  Get current time from played video in string mode
	*  @param driver 	   - given WebDriver driver
	*  @param counterXpath - xpath of counter
	*  @return time	   	   - current time
	*/ 
	public String getCurrPlayedTimeStr(WebDriver driver, String counterXpath) {
		
		String time = driver.findElement(By.xpath(counterXpath)).getText().split("\n")[1];
		testFuncs.myDebugPrinting("time - " + time, enumsClass.logModes.MINOR);
		return time;
	}
	
	/**
	*  Verify Play state
	*  @param driver 	   - given WebDriver driver
	*/ 
	public void verifyPlayState(WebDriver driver) {
		
		String playButtonClassWhenPlayed = driver.findElement(By.xpath("//*[@id='example_video_1']/div[5]/button[1]")).getAttribute("class");
		testFuncs.myAssertTrue("Play button is not at Play state !!", playButtonClassWhenPlayed.contains("vjs-playing"));		
		testFuncs.myWait(1000);
	}
	
	/**
	*  Verify Pause state
	*  @param driver 	   - given WebDriver driver
	*/ 
	public void verifyPausetate(WebDriver driver) {
		
		String playButtonClassWhenPlayed = driver.findElement(By.xpath("//*[@id='example_video_1']/div[5]/button[1]")).getAttribute("class");
		testFuncs.myAssertTrue("Play button is not at Pause state !!", playButtonClassWhenPlayed.contains("vjs-paused"));			
	}
	
	/**
	*  Press Full Screen button
	*  @param driver 	   - given WebDriver driver
	*/ 
	public void pressFullScreenButton(WebDriver driver) {
		
		// Press Full Screen button
		testFuncs.myDebugPrinting("Press Full Screen button", enumsClass.logModes.NORMAL); 
		hoverVideoPlayer(driver, 1000);
		testFuncs.myClick(driver, By.xpath("//*[@id='example_video_1']/div[5]/button[4]"), 5000);
	}
	
	/**
	*  Verify Full / Non Full screen State
	*  @param driver 	   - given WebDriver driver
	 * @param fState - Full screen state (FULL or NON_FULL) 
	*/ 
	public void verifyFullScreenState(WebDriver driver, fullScreen fState) {
		
		String fullScreenStateStr = driver.findElement(By.xpath("//*[@id='example_video_1']/div[5]/button[4]")).getAttribute("title");
		if (fState == fullScreen.FULL) {
			
			testFuncs.myAssertTrue("Full-Screen button is not at <" + fState.toString() + "> state !! <" + fullScreenStateStr + ">", fullScreenStateStr.contains("Non-Fullscreen"));						
		} else if (fState == fullScreen.NON_FULL){
			
			testFuncs.myAssertTrue("Full-Screen button is not at <" + fState.toString() + "> state !! <" + fullScreenStateStr + ">", fullScreenStateStr.contains("Fullscreen"));			
		}
	}
	
	/**
	*  Press Skip forwards
	*  @param driver 	   - given WebDriver driver
	*  @param timeout	   - given timeout after the skip-forwards pressing
	*/ 
	public void pressSkipForwards(WebDriver driver, int timeout) {
		
		// Press Skip forwards
		testFuncs.myDebugPrinting("Press Skip forwards", enumsClass.logModes.NORMAL);
		hoverVideoPlayer(driver);
		testFuncs.myClick(driver, By.xpath("//*[@id='example_video_1']/div[5]/button[3]"), timeout);	
	}
	
	/**
	*  Press Skip Backwards
	*  @param driver  - given WebDriver driver
	 * @param timeout - timeout after the press
	*/ 
	public void pressSkipBackwards(WebDriver driver, int timeout) {
		
		// Press Skip Backwards
		testFuncs.myDebugPrinting("Press Skip backwards", enumsClass.logModes.NORMAL);
		hoverVideoPlayer(driver);
		testFuncs.myClick(driver, By.xpath("//*[@id='example_video_1']/div[5]/button[2]"), timeout);
	}
	
	/**
	*  Press Change Wave Surfer display mode
	*  @param driver 	   - given WebDriver driver
	*/ 
	public void pressWaveSurferButton(WebDriver driver) {
		
		// Press Change Wave Surfer display mode
		testFuncs.myDebugPrinting("Press Change Wave Surfer display mode", enumsClass.logModes.NORMAL); 
		testFuncs.myClick(driver, By.xpath("//*[@id='wavesurferBtn']/a"), 5000);	
	}
	
	/**
	*  Verify State of Surfer mode by given state
	*  @param driver 	   - given WebDriver driver
	*  @param surfDispMode - given Surfer mode (VIEW or HIDE) 
	*/ 
	public void verifySurferMode(WebDriver driver, surferMode surfDispMode) {
		
		String isHideElmnStyle = driver.findElement(By.xpath("//*[@id='example_video_2']")).getAttribute("style");
		testFuncs.myDebugPrinting("isHideElmnStyle - " + isHideElmnStyle, enumsClass.logModes.MINOR); 
		if (surfDispMode == surferMode.VIEW) {
			
			testFuncs.myAssertTrue("Surfer mode is not at <" + surfDispMode.toString() + "> state !! <" + isHideElmnStyle + ">", isHideElmnStyle.contains(surferMode.VIEW.getMode()));						
		} else if (surfDispMode == surferMode.HIDE){
			
			testFuncs.myAssertTrue("Surfer mode is not at <" + surfDispMode.toString() + "> state !! <" + isHideElmnStyle + ">", isHideElmnStyle.contains(surferMode.HIDE.getMode()));						
		}
	}
	
	/**
	*  Press Mute / Unmute button
	*  @param driver - given WebDriver driver
	*/ 
	public void pressMuteButton(WebDriver driver) {
		
		// Press Mute / Unmute button
		testFuncs.myDebugPrinting("Press Mute / Unmute button", enumsClass.logModes.NORMAL); 
		testFuncs.myClick(driver, By.xpath("//*[@id='example_video_1']/div[5]/div[1]"), 3000);	
	}
	
	/**
	*  Verify Mute mode by given state
	*  @param driver 	   - given WebDriver driver
	*  @param muteState - given Mute mode (MUTE or UNMUTE) 
	*/ 
	public void verifyMuteState(WebDriver driver, muteState tmMuteState) {
		
		
		//*[@id="example_video_1"]/div[5]/div[1]
		
		String muteStateStr = driver.findElement(By.xpath("//*[@id='example_video_1']/div[5]/div[1]")).getAttribute("title");
		testFuncs.myDebugPrinting("muteStateStr - " + muteStateStr, enumsClass.logModes.MINOR); 
		if (tmMuteState == muteState.MUTE) {
			
			testFuncs.myAssertTrue("Mute mode is not at mute state !! <" + muteStateStr + ">", muteStateStr.contains("Mute"));						
		} else if (tmMuteState == muteState.UNMUTE){
			
			testFuncs.myAssertTrue("Mute mode is not at Unmute state !! <" + muteStateStr + ">", muteStateStr.contains("Unmute"));						
		}
	}
	
	/**
	*  Get current Play Speed
	*  @param driver - given WebDriver driver
	*  @return speed - current Video Speed
	*/ 
	public String getCurrVideoSpeed(WebDriver driver) {
		
		// Get Current Speed
		testFuncs.myDebugPrinting("Get Current Speed", enumsClass.logModes.MINOR); 
		return driver.findElement(By.xpath("//*[@id='example_video_1']/div[5]/div[9]/div[2]")).getText();	
	}
	
	/**
	*  Change Video speed by given speed
	*  @param driver     - given WebDriver driver
	*  @return giveSpeed - given speed (1x, 2x, 3x etc.)
	*/
	public void changeVideoSpeed(WebDriver driver, String giveSpeed) {

		// Change Video speed by given speed
		testFuncs.myDebugPrinting("Change Video speed by given speed <" + giveSpeed + ">", enumsClass.logModes.NORMAL); 
		hoverVideoPlayer(driver, 1000);
		String iniSpeed = getCurrVideoSpeed(driver);
		testFuncs.myDebugPrinting("iniSpeed - " + iniSpeed, enumsClass.logModes.MINOR); 
		while (true) {
			
			testFuncs.myClick(driver, By.xpath("//*[@id='example_video_1']/div[5]/div[9]"), 500);
			String tmpCurSpeed = getCurrVideoSpeed(driver);
			testFuncs.myDebugPrinting("tmpCurSpeed - " + tmpCurSpeed, enumsClass.logModes.MINOR); 
			if (tmpCurSpeed.contains(giveSpeed)) {
				
				break;
			} else if (tmpCurSpeed.contains(iniSpeed)) {
				
				testFuncs.myFail("<" + giveSpeed + "> was not detected !!");
			}
		}
	}
	
	/**
	*  Press the Player Actions button
	*  @param driver     - given WebDriver driver
	*/
	public void pressPlayerActionsButton(WebDriver driver) {
		
		// Press the Player Actions button
		testFuncs.myDebugPrinting("Press the Player Actions button", enumsClass.logModes.NORMAL); 
		testFuncs.myClick(driver, By.xpath("//*[@id='downloadButton']/a/i"), 1000);	
		testFuncs.verifyStrBy(driver, By.id("modalTitleId"), "Current Position Actions");
	}
	
	/**
	*  Add an Action via the Player Actions menu
	*  @param driver     - given WebDriver driver
	*  @param actionName - given Action Name
	*/
	public void addAction(WebDriver driver, String actionName) {
		
		// Add an Action via the Player Actions menu
		testFuncs.myDebugPrinting("Add an Action via the Player Actions menu", enumsClass.logModes.NORMAL); 
		testFuncs.myClick(driver, By.xpath("//*[@id='modalContentId']/div[3]/a[1]"), 2000);	
		testFuncs.verifyStrBy(driver, By.id("modalTitleId"), "Add Action");	
		testFuncs.mySendKeys(driver, By.id("content-id"), actionName, 1000, false);	
		testFuncs.myClick(driver, By.xpath("/html/body/div[6]/div/button[1]"), 6000);
		
		// Verify add
		testFuncs.myDebugPrinting("Verify add", enumsClass.logModes.MINOR); 		
		testFuncs.myAssertTrue("Action <" + actionName + "> was not created !!", meeting.getActionIdx(driver, actionName) != -1);
	}
	
	/**
	*  Add a Note via the Player Actions menu
	*  @param driver   - given WebDriver driver
	*  @param noteName - given Note Name
	*/
	public void addNote(WebDriver driver, String noteName) {
		
		// Add a Note via the Player Actions menu
		testFuncs.myDebugPrinting("Add a Note via the Player Actions menu", enumsClass.logModes.NORMAL); 
		testFuncs.myClick(driver, By.xpath("//*[@id='modalContentId']/div[3]/a[2]"), 2000);	
		testFuncs.verifyStrBy(driver, By.id("modalTitleId"), "Add Note");	
		testFuncs.mySendKeys(driver, By.id("content-id"), noteName, 1000, false);	
		testFuncs.myClick(driver, By.xpath("/html/body/div[6]/div/button[1]"), 6000);
		
		// Verify add
		testFuncs.myDebugPrinting("Verify add", enumsClass.logModes.MINOR); 
		testFuncs.myAssertTrue("Note <" + noteName + "> was not created !!", meeting.getActionIdx(driver, noteName) != -1);
	}

	/**
	*  Add a Tag via the Player Actions menu
	*  @param driver   - given WebDriver driver
	*  @param tagName  - given Tag Name
	*/
	public void addTag(WebDriver driver, String tagName) {
		
		// Add a Tag via the Player Actions menu
		testFuncs.myDebugPrinting("Add a Tag via the Player Actions menu", enumsClass.logModes.NORMAL); 
		testFuncs.myClick(driver, By.xpath("//*[@id='modalContentId']/div[3]/a[3]"), 2000);	
		testFuncs.verifyStrBy(driver, By.id("modalTitleId"), "Add Tag");	
		Select tagNames = new Select(driver.findElement(By.id("tag-id")));
		testFuncs.myWait(2000);
		tagNames.selectByVisibleText(tagName); 
		testFuncs.myClick(driver, By.xpath("/html/body/div[6]/div/button[1]"), 6000);
		
		// Verify add
		testFuncs.myDebugPrinting("Verify add", enumsClass.logModes.MINOR); 
		testFuncs.myAssertTrue("tag <" + tagName + "> was not created !!", meeting.getActionIdx(driver, tagName) != -1);
	}
	
	/**
	*  Toggle Frames number (main or all)
	*  @param driver - given WebDriver driver
	*  @param mode	 - given video Frames mode (1 - FULL, 2 - MAIN)
	*/
	public void toggleFramesNum(WebDriver driver, int mode) {

		// Toggle Frames number
		testFuncs.myDebugPrinting("Toggle Frames number", enumsClass.logModes.NORMAL);			
		testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div[3]/div[2]/div[1]/div[2]/table/tbody/tr/td/div/div[3]/a[" + mode + "]"), 4000);	
	}
	
	/**
	*  Return current Frames number of video
	*  @param driver     - given WebDriver driver
	*  @return framesNum - Frames number
	*/
	public int getFramesNumber(WebDriver driver) {
		
		// Get Frames number
		testFuncs.myDebugPrinting(" Get Frames number", enumsClass.logModes.NORMAL);
		int tmpFrmNum = driver.findElements(By.xpath("//*[@id='jssor_1']/div[1]/div/div[2]/*")).size();
		testFuncs.myDebugPrinting("tmpFrmNum - " + tmpFrmNum, enumsClass.logModes.MINOR); 
		testFuncs.myWait(2000);
		return tmpFrmNum;
	}
	
	/**
	*  Return current Frames number of video from Gallery mode
	*  @param driver     - given WebDriver driver
	*  @return framesNum - Frames number
	*/
	public int getFramesNumberGallery(WebDriver driver) {
		
		// Get Frames number from Gallery mode
		testFuncs.myDebugPrinting(" Get Frames number from Gallery mode", enumsClass.logModes.NORMAL);
		int rowsNum    = driver.findElements(By.xpath("/html/body/div[2]/div[3]/div[2]/div[1]/div[1]/table/tbody/tr/td[3]/div/div/div/table/tbody/*")).size();
		int lastRowNum = driver.findElements(By.xpath("/html/body/div[2]/div[3]/div[2]/div[1]/div[1]/table/tbody/tr/td[3]/div/div/div/table/tbody/tr[" + rowsNum + "]/*")).size();	
		testFuncs.myDebugPrinting("rowsNum - " 	  + rowsNum	  , enumsClass.logModes.DEBUG); 
		testFuncs.myDebugPrinting("lastRowNum - " + lastRowNum, enumsClass.logModes.DEBUG); 
		int tmpFrmNum = ((rowsNum - 1) * 3) + lastRowNum;	
		testFuncs.myDebugPrinting("tmpFrmNum - " + tmpFrmNum, enumsClass.logModes.MINOR); 
		return tmpFrmNum;
	}
	
	/**
	*  Press the Play button of given frame
	*  @param driver      - given WebDriver driver
	*  @param frameNumber - number of wanted Frame
	*  @return framesNum  - Frames number
	*/
	public void framePressPlayButton(WebDriver driver, int frameNumber) {

		// Press Frames Play button
		testFuncs.myDebugPrinting("Press Frames Play button", enumsClass.logModes.NORMAL);	
		testFuncs.myHover(driver, By.xpath("//*[@id='jssor_1']/div[1]/div/div[2]/div[" + frameNumber + "]/div[2]/div[5]")		    , 1000);
		testFuncs.myClick(driver, By.xpath("//*[@id='jssor_1']/div[1]/div/div[2]/div[" + frameNumber + "]/div[2]/div[5]")		    , 1000);
		testFuncs.myClick(driver, By.xpath("//*[@id='jssor_1']/div[1]/div/div[2]/div[" + frameNumber +"]/div[2]/div[5]/div/div/a/i"), 500);
		while (true) {
			
			String classElm = driver.findElement(By.xpath("//*[@id='example_video_1']")).getAttribute("class");
			if (!classElm.contains("vjs-waiting")) {
				
				testFuncs.myWait(1000);
				break;
			} else {
				
				testFuncs.myWait(1000);
			}	
		}
	}
	
	/**
	*  Hover Video Player
	*  @param driver      - given WebDriver driver
	*/
	public void hoverVideoPlayer(WebDriver driver) {
		
		// Hover Video Player
		testFuncs.myDebugPrinting("<Hover Video Player>", enumsClass.logModes.NORMAL);		
		testFuncs.myHover(driver, By.xpath("//*[@id='example_video_1_html5_api']"), 1500);	
	}
	
	/**
	*  Hover Video Player
	*  @param driver      - given WebDriver driver
	*  @param driver      - given timeout
	*/
	public void hoverVideoPlayer(WebDriver driver, int timeout) {
		
		// Hover Video Player
		testFuncs.myDebugPrinting("<Hover Video Player>", enumsClass.logModes.NORMAL);		
		testFuncs.myHover(driver, By.xpath("//*[@id='example_video_1_html5_api']"), timeout);	
		
	}
	
	/**
	*  Toggle Video Player display mode (GALLERY or PLAYER)
	*  @param driver 	- given WebDriver driver
	*  @param mode	 	- given video display mode (1 - PLAYER, 2 - GALLERY)
	*/
	public void toggleDisplayMode(WebDriver driver, int mode) {

		// Toggle Video display mode
		testFuncs.myDebugPrinting("Toggle Video display mode <" + mode + ">", enumsClass.logModes.NORMAL);		
		testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div[3]/div[2]/div[1]/div[1]/table/tbody/tr/td[1]/table/tbody/tr[1]/td[3]/table/tbody/tr/td[" + mode + "]/div"), 3000);	
		String styleAtt = driver.findElement(By.xpath("/html/body/div[2]/div[3]/div[2]/div[1]/div[1]/table/tbody/tr/td[3]")).getAttribute("style");
		if (mode == 1) {
			
			testFuncs.myAssertTrue("Toggle Video display mode <" + mode + "> failed !!", styleAtt.contains("display: none;"));
		} else {
			
			testFuncs.myAssertTrue("Toggle Video display mode <" + mode + "> failed !!", styleAtt.isEmpty());
		}
	}

	/**
	*  Return current Frames number of attachment
	*  @param driver     - given WebDriver driver
	*  @return framesNum - Frames number
	*/
	public int getFileSlidesNumber(WebDriver driver) {
		
		// Get Frames number
		testFuncs.myDebugPrinting("Get Slides number of attachment", enumsClass.logModes.NORMAL);
		int tmpFrmNum = driver.findElements(By.xpath("//*[@id=\"tab2ppts\"]/div/div/div/*")).size();
		testFuncs.myDebugPrinting("tmpFrmNum = " + tmpFrmNum, enumsClass.logModes.MINOR); 
		
		return tmpFrmNum;
	}
	
	/**
	*  Return current Frames number of attachment
	*  @param driver     - given WebDriver driver
	*  @return framesNum - Frames number
	*/
	public int getAttachmentNumber(WebDriver driver) {
		
		// Get Frames number
		testFuncs.myDebugPrinting("Get Attachments number", enumsClass.logModes.NORMAL);
		int tmpFrmNum = driver.findElements(By.xpath("//*[@id=\"tab2atts\"]/div[1]/*")).size();
		testFuncs.myDebugPrinting("tmpFrmNum = " + tmpFrmNum, enumsClass.logModes.MINOR); 
		
		return tmpFrmNum;
	}
	
	/**
	*  Toggle Attachments display mode (SLIDES or UPLOAD)
	*  @param driver 	- given WebDriver driver
	*  @param mode	 	- given video display mode (1 - PLAYER, 2 - GALLERY)
	*  @param searchStr - given strung for verify good toggle
	*/
	public void toggleDisplayMode_Attachments(WebDriver driver, int mode, String string) {

		// Toggle Video display mode
		testFuncs.myDebugPrinting("Toggle Attachments display mode", enumsClass.logModes.NORMAL);	
		testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div[3]/div[2]/div[1]/div[1]/table/tbody/tr/td[4]/div/div[1]/ul/li[" + mode + "]/a"), 3000);
		testFuncs.searchStr(driver, string);	
	}
}