package MeetingRecorder;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import MeetingRecorder.enumsClass.fullScreen;
import MeetingRecorder.enumsClass.muteState;

public class Trim {
	
	GlobalFuncs			testFuncs;
	
	/**
	*  Default constructor
	*/
	public Trim(GlobalFuncs testFuncs) {
				
		this.testFuncs = testFuncs;
	}
	
	/**
	*  Press Trim-Start button
	*  @param driver  	  - given WebDriver driver
	*  @param timeout     - given timeout after the pressing
	*/ 
	public void pressTrimStartButton(WebDriver driver, int timeout) {
		
		// Press Trim-Start button
		testFuncs.myDebugPrinting("Press Trim-Start button", enumsClass.logModes.MINOR);	  	 
		testFuncs.myClick(driver, By.xpath("//*[@id='tab2trim']/table/tbody/tr/td[1]/div/table[2]/tbody/tr[1]/td[1]/img"), timeout); 
	}
	
	/**
	*  Press Trim-End button
	*  @param driver  	  - given WebDriver driver
	*  @param timeout     - given timeout after the pressing
	*/ 
	public void pressTrimEndButton(WebDriver driver, int timeout) {
		
		// Press Trim-End button
		testFuncs.myDebugPrinting("Press Trim-End button", enumsClass.logModes.MINOR);	  	 
		testFuncs.myClick(driver, By.xpath("//*[@id='tab2trim']/table/tbody/tr/td[1]/div/table[2]/tbody/tr[1]/td[2]/img"), timeout); 
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
	*  When a Preview video is played, wait till it ended
	*  @param driver - given WebDriver driver
	*/
	public void waitTillEndOfVideoPreview(WebDriver driver) {
		
		testFuncs.myDebugPrinting("Wait for end of the video", enumsClass.logModes.NORMAL);
		while (true) {
			
			String playerElmClass = driver.findElement(By.xpath("//*[@id='video_prev_id']")).getAttribute("class");
			if (playerElmClass.contains("vjs-paused") && playerElmClass.contains("vjs-ended")) {		  
				break;	
			}			
			testFuncs.myWait(1000);
		}  
		testFuncs.myWait(1000);	  
		testFuncs.myDebugPrinting("Playing of the meeting ended", enumsClass.logModes.MINOR); 
	}
	
	/**
	*  Press play button via Preview menu
	*  @param driver  	  - given WebDriver driver
	*  @param timeout     - given timeout after the pressing
	*/ 
	public void playTrimMenuVideoPreview(WebDriver driver, int timeout) {
		
		
		String videoClassAtt = driver.findElement(By.xpath("//*[@id='video_prev_id']")).getAttribute("class");	
		if (videoClassAtt.contains("vjs-paused")) {
			
			testFuncs.myDebugPrinting("Play a Video for first time", enumsClass.logModes.MINOR);	
			testFuncs.myClick(driver, By.xpath("//*[@id='video_prev_id']/button"), timeout);	

		} else {
			
			testFuncs.myDebugPrinting("Play a Video after it was already been played", enumsClass.logModes.MINOR);
			testFuncs.myClick(driver, By.xpath("//*[@id='video_prev_id']/div[4]/button[1]"), timeout);
		}
	}
	
	/**
	*  Press play button
	*  @param driver  	  - given WebDriver driver
	*  @param timeOut     - given timeout after the pressing
	*/ 
	public void playTrimMenuVideo(WebDriver driver, int timeOut) {
		
		String videoClassAtt = driver.findElement(By.xpath("//*[@id='example_video_1']")).getAttribute("class");	
		if (videoClassAtt.contains("vjs-paused")) {
			
			testFuncs.myDebugPrinting("Play a Video for first time", enumsClass.logModes.MINOR);	
			testFuncs.myClick(driver, By.xpath("//*[@id='example_video_1']/button"), timeOut);	

		} else {
			
			testFuncs.myDebugPrinting("Play a Video after it was already been played", enumsClass.logModes.MINOR);
			testFuncs.myClick(driver, By.xpath("//*[@id='example_video_1']/div[4]/button[1]"), timeOut);
		}
	}
	
	/**
	*  Get Trim Start
	*  @param driver - given WebDriver driver
	*  @return Trim start value
	*/ 
	public String getTrimStart(WebDriver driver) {
		
		String tmpTrimStart = testFuncs.myGetText(driver, By.xpath("//*[@id='tab2trim']/table/tbody/tr/td[1]/div/table[1]/tbody/tr[1]/td[2]/b"));	
		testFuncs.myDebugPrinting("getTrimStart() - " + tmpTrimStart, enumsClass.logModes.MINOR);	
		
		return tmpTrimStart;	
	}
	
	/**
	*  Get Trim End
	*  @param driver - given WebDriver driver
	*  @return Trim end value
	*/ 
	public String getTrimEnd(WebDriver driver) {
		
		String tmpTrimEnd = testFuncs.myGetText(driver, By.xpath("//*[@id='tab2trim']/table/tbody/tr/td[1]/div/table[1]/tbody/tr[2]/td[2]/b"));	
		testFuncs.myDebugPrinting("getTrimEnd() - " + tmpTrimEnd, enumsClass.logModes.MINOR);	
		
		return tmpTrimEnd;	
	}
	
	/**
	*  Check that pressing the Trim button not effective when video is not played
	*  @param driver  	  - given WebDriver driver
	*  @param timeout     - given timeout after the pressing
	*/  
	public void checkTrimButtonsInActive(WebDriver driver) {
		
		// Check that pressing the Trim button not effective when video is not played
		testFuncs.myDebugPrinting("Check that pressing the Trim button not effective when video is not played", enumsClass.logModes.NORMAL);	
		String movieStartTime = getTrimStart(driver);
		String movieEndTime   = getTrimEnd(driver);  
		
		// Click and verify	
		pressTrimStartButton(driver, 5000);
		pressTrimEndButton(driver  , 5000);
		String movieStartTime2 = getTrimStart(driver);
		String movieEndTime2   = getTrimEnd(driver);   
		testFuncs.myAssertTrue("Trim-start value was changed !!", movieStartTime2.equals(movieStartTime));	
		testFuncs.myAssertTrue("Trim-end value was changed !!"  , movieEndTime2.equals(movieEndTime));  
	}
	
	/**
	*  Get movie length by Start-time and End-time as they displayed at Trim menu
	*  @param driver  	   - given WebDriver driver
	*  @param videoPlayer  - given VideoPlayer object
	*  @return movieLength - movie Length
	*/ 
	public int getMovieLength(WebDriver driver, VideoPlayer videoPlayer) {
		
		// Get movie length by Start-time and End-time as they displayed at Trim menu
		testFuncs.myDebugPrinting("Get movie length by Start-time and End-time as they displayed at Trim menu", enumsClass.logModes.NORMAL);	
		int origStart = videoPlayer.getCurrPlayedTime(driver, "//*[@id='tab2trim']/table/tbody/tr/td[1]/div/table[1]/tbody/tr[1]/td[2]/b");
		int origEnd 	= videoPlayer.getCurrPlayedTime(driver, "//*[@id='tab2trim']/table/tbody/tr/td[1]/div/table[1]/tbody/tr[2]/td[2]/b");
		testFuncs.myDebugPrinting("Movie length - " + (origEnd - origStart), enumsClass.logModes.MINOR);	
		
		return (origEnd - origStart);
	}
	
	/**
	*  Press Preview button
	*  @param driver  	  - given WebDriver driver
	*  @param timeout     - given timeout after the pressing
	*  @param meetingName - given Meeting name
	*/ 
	public void pressPreviewButton(WebDriver driver, int timeout, String meetingName) {
		
		// Press Trim-Start button
		testFuncs.myDebugPrinting("Press Trim-Start button", enumsClass.logModes.MINOR);	  	 
		testFuncs.myClick(driver, By.xpath("/html/body/div[3]/div[3]/div[1]/div[2]/div[2]/div/ul/li[3]/div/button[1]"), timeout); 
		testFuncs.verifyStrByXpathContains(driver, "//*[@id='myModalVideo']/div/div/div[1]/h3", meetingName.toUpperCase());
	}

	/**
	*  Close Preview menu
	*  @param driver  	  - given WebDriver driver
	*  @param timeout     - given timeout after the pressing
	*/ 
	public void closePreviewMenu(WebDriver driver, int timeout) {
		
		// Close Preview menu
		testFuncs.myDebugPrinting("Close Preview menu", enumsClass.logModes.MINOR);	  	 
		testFuncs.myClick(driver, By.xpath("//*[@id='myModalVideo']/div/div/div[1]/button/span"), timeout); 
	}
	
	/**
	*  Reset Trimming
	*  @param driver  	  - given WebDriver driver
	*  @param timeout     - given timeout after the pressing
	*/ 
	public void resetTrimming(WebDriver driver, int timeout) {
		
		// Press Reset button
		testFuncs.myDebugPrinting("Press Reset button", enumsClass.logModes.MINOR);	  	 
		testFuncs.myClick(driver, By.xpath("//*[@id='tab2trim']/table/tbody/tr/td[1]/div/table[2]/tbody/tr[2]/td/button"), timeout); 
	}
	
	/**
	*  Verify Play state
	*  @param driver 	   - given WebDriver driver
	*/ 
	public void verifyPlayState(WebDriver driver) {

		String playButtonClassWhenPlayed = driver.findElement(By.xpath("//*[@id='example_video_1']")).getAttribute("class");
		testFuncs.myAssertTrue("Play button is not at Play state !!", playButtonClassWhenPlayed.contains("vjs-playing"));		
		testFuncs.myDebugPrinting("Video is playing ...", enumsClass.logModes.MINOR);	  	 
	}
	
	/**
	*  Verify Play state of Preview
	*  @param driver 	   - given WebDriver driver
	*/ 
	public void verifyPlayStatePreview(WebDriver driver) {

		String playButtonClassWhenPlayed = driver.findElement(By.xpath("//*[@id='video_prev_id']")).getAttribute("class");
		testFuncs.myAssertTrue("Play button is not at Play state !!", playButtonClassWhenPlayed.contains("vjs-playing"));		
		testFuncs.myDebugPrinting("Video is playing ...", enumsClass.logModes.MINOR);	  	 
	}
	
	/**
	*  Verify Pause state
	*  @param driver 	   - given WebDriver driver
	*/ 
	public void verifyPauseState(WebDriver driver) {
				
		String playButtonClassWhenPlayed = driver.findElement(By.xpath("//*[@id='example_video_1']")).getAttribute("class");
		testFuncs.myAssertTrue("Play button is not at Pause state !!" + playButtonClassWhenPlayed, playButtonClassWhenPlayed.contains("vjs-paused"));			
		testFuncs.myDebugPrinting("Video is pausing ...", enumsClass.logModes.MINOR);	  	 
	}
	
	/**
	*  Verify Pause state via Preview
	*  @param driver 	   - given WebDriver driver
	*/ 
	public void verifyPauseStatePreview(WebDriver driver) {
				
		String playButtonClassWhenPlayed = driver.findElement(By.xpath("//*[@id='video_prev_id']")).getAttribute("class");
		testFuncs.myAssertTrue("Play button is not at Pause state !!" + playButtonClassWhenPlayed, playButtonClassWhenPlayed.contains("vjs-paused"));			
		testFuncs.myDebugPrinting("Video is pausing ...", enumsClass.logModes.MINOR);	  	 
	}
	
	/**
	*  Verify mute state
	*  @param driver 	   - given WebDriver driver
	*  @param muteState - given Mute mode (MUTE or UNMUTE) 
	*/
	public void verifyMuteState(WebDriver driver, muteState tmMuteState) {
		
		String muteStateStr = driver.findElement(By.xpath("//*[@id='example_video_1_html5_api']")).getAttribute("muted");
		if (tmMuteState == muteState.MUTE) {
			
			testFuncs.myAssertTrue("Mute mode is not muted !!", muteStateStr != null);						
		} else if (tmMuteState == muteState.UNMUTE){
			
			testFuncs.myAssertTrue("Mute mode is not un-mute !! ", muteStateStr == null);						
		}
	}
	
	/**
	*  Verify mute state via Preview
	*  @param driver 	   - given WebDriver driver
	*  @param muteState - given Mute mode (MUTE or UNMUTE) 
	*/
	public void verifyMuteStatePreview(WebDriver driver, muteState tmMuteState) {
		
		String muteStateStr = driver.findElement(By.id("video_prev_id_html5_api")).getAttribute("muted");
		if (tmMuteState == muteState.MUTE) {
			
			testFuncs.myAssertTrue("Mute mode is not muted !!", muteStateStr != null);						
		} else if (tmMuteState == muteState.UNMUTE){
			
			testFuncs.myAssertTrue("Mute mode is not un-mute !! ", muteStateStr == null);						
		}
	}
	
	
	//*[@id="video_prev_id_html5_api"]
	
	

	/**
	*  Press Mute / Unmute button
	*  @param driver - given WebDriver driver
	*/ 
	public void pressMuteButton(WebDriver driver) {
		
		// Press Mute / Unmute button
		testFuncs.myDebugPrinting("Press Mute / Unmute button", enumsClass.logModes.NORMAL); 
		testFuncs.myClick(driver, By.xpath("//*[@id='example_video_1']/div[4]/div[1]/button"), 5000);	
	}
	
	/**
	*  Press Mute / Unmute button via Preview
	*  @param driver - given WebDriver driver
	*/ 
	public void pressMuteButtonPreview(WebDriver driver) {
		
		// Press Mute / Unmute button
		testFuncs.myDebugPrinting("Press Mute / Unmute button", enumsClass.logModes.NORMAL); 
		testFuncs.myClick(driver, By.xpath("//*[@id='video_prev_id']/div[4]/div[1]/button"), 5000);	
	}

	/**
	*  Press Full Screen button
	*  @param driver 	   - given WebDriver driver
	*/ 
	public void pressFullScreenButton(WebDriver driver) {
		
		// Press Full Screen button
		testFuncs.myDebugPrinting("Press Full Screen button", enumsClass.logModes.NORMAL); 
		testFuncs.myClick(driver, By.xpath("//*[@id='example_video_1']/div[4]/button[2]"), 3000);
	}
	
	/**
	*  Press Full Screen button via Preview
	*  @param driver 	   - given WebDriver driver
	*/ 
	public void pressFullScreenButtonPreview(WebDriver driver) {
		
		// Press Full Screen button
		testFuncs.myDebugPrinting("Press Full Screen button", enumsClass.logModes.NORMAL); 
		testFuncs.myClick(driver, By.xpath("//*[@id='video_prev_id']/div[4]/button[2]"), 3000);
	}

	/**
	*  Verify Full / Non Full screen State
	*  @param driver 	   - given WebDriver driver
	*  @param fState - Full screen state (FULL or NON_FULL) 
	*/ 
	public void verifyFullScreenState(WebDriver driver, fullScreen fState) {
		
		String fullScreenStateStr = driver.findElement(By.xpath("//*[@id='example_video_1']")).getAttribute("class");
		if (fState == fullScreen.FULL) {
			
			testFuncs.myAssertTrue("Full-Screen button is not at <" + fState.toString() + "> state !! <" + fullScreenStateStr + ">",  fullScreenStateStr.contains("vjs-fullscreen"));						
		} else if (fState == fullScreen.NON_FULL){
			
			testFuncs.myAssertTrue("Full-Screen button is not at <" + fState.toString() + "> state !! <" + fullScreenStateStr + ">", !fullScreenStateStr.contains("vjs-fullscreen"));			
		}
	}
	
	/**
	*  Verify Full / Non Full screen State via Preview
	*  @param driver 	   - given WebDriver driver
	*  @param fState - Full screen state (FULL or NON_FULL) 
	*/ 
	public void verifyFullScreenStatePreview(WebDriver driver, fullScreen fState) {
		
		String fullScreenStateStr = driver.findElement(By.xpath("//*[@id='video_prev_id']")).getAttribute("class");
		if (fState == fullScreen.FULL) {
			
			testFuncs.myAssertTrue("Full-Screen button is not at <" + fState.toString() + "> state !! <" + fullScreenStateStr + ">",  fullScreenStateStr.contains("vjs-fullscreen"));						
		} else if (fState == fullScreen.NON_FULL){
			
			testFuncs.myAssertTrue("Full-Screen button is not at <" + fState.toString() + "> state !! <" + fullScreenStateStr + ">", !fullScreenStateStr.contains("vjs-fullscreen"));			
		}
	}
	
	/**
	*  Hover Video Player
	*  @param driver      - given WebDriver driver
	*/
	public void hoverVideoPlayer(WebDriver driver) {
		
		// Hover Video Player
		testFuncs.myDebugPrinting("<Hover Video Player>", enumsClass.logModes.MINOR);		
		testFuncs.myHover(driver, By.xpath("//*[@id='example_video_1_html5_api']"), 1000);	
	}
	
	/**
	*  Hover Preview Video Player
	*  @param driver      - given WebDriver driver
	*/
	public void hoverPreviewVideoPlayer(WebDriver driver) {
		
		// Hover Video Player
		testFuncs.myDebugPrinting("<Hover Video Player>", enumsClass.logModes.MINOR);		
		testFuncs.myHover(driver, By.id("video_prev_id_html5_api"), 250);	
	}
	
	/**
	*  Return current playing time
	*  @param driver 	- given WebDriver driver
	*  @return currTime - current playing time 
	*/ 
	public String getMovieCurPlayTime(WebDriver driver) {
		
		
		hoverVideoPlayer(driver);
		String currTime = driver.findElement(By.xpath("//*[@id='example_video_1']/div[4]/div[2]/span[2]")).getText();
		testFuncs.myDebugPrinting("Player currTime is  - " + currTime, enumsClass.logModes.MINOR);	  	 
		
		return currTime;
	}
	
	/**
	*  Return current playing time preview
	*  @param driver 	- given WebDriver driver
	*  @return currTime - current playing time 
	*/ 
	public String getPreviewMovieCurPlayTime(WebDriver driver) {
			
		hoverPreviewVideoPlayer(driver);
		String currTime = driver.findElement(By.xpath("//*[@id='video_prev_id']/div[4]/div[2]/span[2]")).getText();
		testFuncs.myDebugPrinting("Player currTime is  - " + currTime, enumsClass.logModes.MINOR);	  	 

		return currTime;
	}
}