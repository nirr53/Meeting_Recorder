package MeetingRecorder;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class Delegate {
	
	GlobalFuncs			testFuncs;
	
	public Delegate(GlobalFuncs	testFuncs) {
		
		this.testFuncs = testFuncs;		
	}
	
	/**
	*  Create a new Delegate
	*  @param driver  - given WebDriver driver
	*  @param delName - given Delegate name
	*/
	public void createDelegate(WebDriver driver, String delName) {
				
		// Add delegate
		testFuncs.myDebugPrinting("Add delegate", enumsClass.logModes.NORMAL);  
		testFuncs.enterMenu(driver, enumsClass.menuNames.SETTINGS_DELEGATES_SECTION);
		testFuncs.myClick(driver, By.xpath("//*[@id='tab2delegate']/div/div/p[1]/i"), 2000);
		testFuncs.verifyStrBy(driver, By.id("modalTitleId"), "Add Delegated User");
		testFuncs.mySendKeys(driver, By.id("content-id"), delName, 2000);
		testFuncs.myClick(driver, By.xpath("/html/body/div[4]/div/button[1]"), 2000);
		testFuncs.verifyStrBy(driver, By.id("modalTitleId")	 , "Save User Delegates");
		testFuncs.verifyStrBy(driver, By.id("modalContentId"), "Successful to add user delegate");
		testFuncs.myClick(driver, By.xpath("/html/body/div[4]/div/button[1]"), 2000);

		// Verify delegate
		testFuncs.myDebugPrinting("Verify delegate", enumsClass.logModes.MINOR); 
		testFuncs.enterMenu(driver, enumsClass.menuNames.SETTINGS_DELEGATES_SECTION);
		testFuncs.myAssertTrue("Delegate <" + delName + "> was not created !!", testFuncs.getIdxFromTable(driver, By.xpath("//*[@id='tab2delegate']/div/div/table/tbody"), delName) != -1);		
	}
	
	/**
	*  Delete a Delegate
	*  @param driver  - given WebDriver driver
	*  @param delName - given Delegate name
	*/
	public void deleteDelegate(WebDriver driver, String delName) {
		
		int idx = 0;
		
		// Delete a delegate
		testFuncs.myDebugPrinting("Delete a delegate", enumsClass.logModes.NORMAL);  
		testFuncs.enterMenu(driver, enumsClass.menuNames.SETTINGS_DELEGATES_SECTION);
		testFuncs.myAssertTrue("Delegate <" + delName + "> was not detected !!", (idx = testFuncs.getIdxFromTableNoDup(driver, By.xpath("//*[@id='tab2delegate']/div/div/table/tbody"), delName)) != -1);
		testFuncs.myClick(driver, By.xpath("//*[@id='tab2delegate']/div/div/table/tbody/tr[" + idx + "]/td[2]"), 2000);			
		testFuncs.verifyStrBy(driver, By.id("modalTitleId")	 , "Delete Delegated User");
		testFuncs.verifyStrBy(driver, By.id("modalContentId"), "Are you sure you want to delete this delegated user?");
		testFuncs.myClick(driver, By.xpath("/html/body/div[4]/div/button[1]"), 2000);	
		testFuncs.verifyStrBy(driver, By.id("modalTitleId")	 , "Save User Delegates");
		testFuncs.verifyStrBy(driver, By.id("modalContentId"), "Successful to delete user delegate");
		testFuncs.myClick(driver, By.xpath("/html/body/div[4]/div/button[1]"), 2000);	
		
		// Verify that Delegate was removed from table
		testFuncs.myDebugPrinting("Verify that Delegate was removed from table", enumsClass.logModes.MINOR);  
		testFuncs.enterMenu(driver, enumsClass.menuNames.SETTINGS_DELEGATES_SECTION);
		testFuncs.myAssertTrue("Delegate <" + delName + "> was not deleted !!", (idx = testFuncs.getIdxFromTableNoDup(driver, By.xpath("//*[@id='tab2delegate']/div/div/table/tbody"), delName)) == -1);
	}
}
