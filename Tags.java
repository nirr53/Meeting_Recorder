package MeetingRecorder;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class Tags {
	
	GlobalFuncs	testFuncs;
	
	public Tags(GlobalFuncs	testFuncs) {
		
		this.testFuncs = testFuncs;
	}

	/**
	*  Create a tag
	*  @param driver   - given WebDriver driver
	*  @param tageName - given Tag name
	*/ 
	public void createTag(WebDriver driver, String tagName) {
		
		// Create a Tag
		testFuncs.myDebugPrinting("Create a tag - " + tagName, enumsClass.logModes.NORMAL);
		testFuncs.myClick(driver, By.xpath("//*[@id='tab2tagmenu']/div/div[2]/div/table/tbody/tr/td[1]/i"), 5000);
		testFuncs.verifyStrBy(driver, By.id("modalTitleId")									 			, "Add Tag");
		testFuncs.verifyStrBy(driver, By.xpath("//*[@id='modalContentId']/div/div/div/label")			, "USER TAG");
		testFuncs.mySendKeys(driver, By.id("content-id"), tagName, 2000);
		testFuncs.myClick(driver, By.xpath("/html/body/div[4]/div/button[1]"), 3000);
		testFuncs.verifyStrBy(driver, By.id("modalTitleId")  , "Save User Tags");
		testFuncs.verifyStrBy(driver, By.id("modalContentId"), "Successful to save user tags");
		testFuncs.myClick(driver, By.xpath("/html/body/div[4]/div/button[1]"), 3000);
	
		// Verify the created Tag
		testFuncs.myDebugPrinting("Verify the created Tag", enumsClass.logModes.MINOR);		
		testFuncs.myAssertTrue("The created tag <" + tagName + "> was not detected !!", testFuncs.isListConatinsString(driver, By.xpath("//*[@id='tab2tagmenu']/div/div[1]/select"), tagName));	
	}
	
	/**
	*  Delete a tag
	*  @param driver   - given WebDriver driver
	*  @param tageName - given Tag name
	*/ 
	public void deleteTag(WebDriver driver, String tagName) {
		
		// Select the deleted Tag and delete it
		testFuncs.myDebugPrinting("Select the deleted Tag and delete it", enumsClass.logModes.NORMAL);
		Select tagNames = new Select(driver.findElement(By.xpath("//*[@id='tab2tagmenu']/div/div[1]/select")));
		testFuncs.myWait(3000);
		tagNames.selectByVisibleText(tagName);  	
		testFuncs.myWait(2000);
		testFuncs.myClick(driver, By.xpath("//*[@id='tab2tagmenu']/div/div[2]/div/table/tbody/tr/td[2]/div"), 3000);
		testFuncs.verifyStrBy(driver, By.id("modalTitleId")	 , "Delete Tag");
		testFuncs.verifyStrBy(driver, By.id("modalContentId"), "Are you sure you want to delete this tag " + tagName);
		testFuncs.myClick(driver, By.xpath("/html/body/div[4]/div/button[1]"), 3000);
		testFuncs.verifyStrBy(driver, By.id("modalTitleId")	 , "Save User Tags");
		testFuncs.verifyStrBy(driver, By.id("modalContentId"), "Successful to save user tags");
		testFuncs.myClick(driver, By.xpath("/html/body/div[4]/div/button[1]"), 3000);
				
		// Verify the deleted Tag
		testFuncs.myDebugPrinting("Verify the deleted Tag", enumsClass.logModes.MINOR);
		testFuncs.myAssertTrue("The created tag <" + tagName + "> was detected !!", !testFuncs.isListConatinsString(driver, By.xpath("//*[@id='tab2tagmenu']/div/div[1]/select"), tagName));		
	}
}
