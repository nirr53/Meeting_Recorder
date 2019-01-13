package MeetingRecorder;
import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class GlobalVars {
	
	
	// XML path
	private final String dataPath;
	private String downloadsPath;
	
	/**
	*  Default constructor
	*/
	public GlobalVars() {
		
		// initialize path
		dataPath = System.getProperty("user.dir") + "\\inputData\\data.xml";
		System.out.println("--------------Create new GlobalVars() object ------------");
		System.out.println("dataPath - " + dataPath);	
		
		// Set downloads path
    	this.downloadsPath = "C:\\Users\\" + System.getProperty("user.name") + "\\Downloads";
		System.out.println("downloadsPath - " + downloadsPath);
	}
	
	/**
	*  Default method for return the downloads path
	*  @return version
	*/
	public String getDownloadsPath() { return  downloadsPath; }
	
	/**  
	*  Get data from xml by given variable name and attribute name
	*  @param varName  - Name of variable from its attribute we want to get data
	*  @param varAttName - Name of attribute from which we want to take the data
	*  @return needed data (else - null)
	*/
	@SuppressWarnings("static-access")
	public String getXMLData (String varName, String varAttName) {
		
		int nodeIdx = 0;	   
		try {
	
			File fXmlFile = new File(dataPath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();	
			   
			// Loop on all kids and print their name 	
			NodeList nList = doc.getFirstChild().getChildNodes();
			int nodesNum = nList.getLength();
			for (nodeIdx = 0; nodeIdx < nodesNum; nodeIdx++) {
		    
				Node nNode = nList.item(nodeIdx);	   
				if (nNode.getNodeType() == nNode.ELEMENT_NODE) {
	   
					String tempName = nNode.getNodeName();
					if (tempName.contains(varName)) {
						
						Element eElement = (Element) nNode;
						return eElement.getAttribute(varAttName);
					}
				}
			}		
				
			return null;
		    
		} catch (Exception e) {
			
			e.printStackTrace();    
		}	
		
		return "";
	}
	
	// Get functions
	public String getUsername() 			{ return getXMLData("loginData"  , "username") 	     ; }
	public String getPassword() 			{ return getXMLData("loginData"  , "password") 	     ; }
	public String getExtUsername() 			{ return getXMLData("loginData"  , "extUsername") 	 ; }
	public String getExtPassword() 			{ return getXMLData("loginData"  , "extPassword") 	 ; }
	public String getGoodLoginHeader()  	{ return getXMLData("loginData"  , "goodLogin")	     ; }
	public String getDomain()  				{ return getXMLData("loginData"  , "domain")	     ; }
	public String getChromeDrvPath() 		{ return getXMLData("driver"     , "path")	 	     ; }
	public String getUrl() 					{ return getXMLData("siteData"   , "ip")		     ; }
	public String getMeetingMp4Path()   	{ return getXMLData("meeting"    , "mp4path")  	     ; }
	public String getMeetingLongMp4Path()   { return getXMLData("meeting"    , "longMp4path")    ; }
	public String getMeetingPlayerMp4Path() { return getXMLData("meeting"    , "playerMp4path")  ; }
	public String getActionIconName()  	 	{ return getXMLData("meeting"    , "actionIconName") ; }
	public String getNoteIconName()     	{ return getXMLData("meeting"    , "noteIconName")   ; }
	public String getTagIconName()      	{ return getXMLData("meeting"    , "tagIconName")    ; }
	public String getMeetingEditIcon()   	{ return getXMLData("meeting"    , "editIcon")    	 ; }
	public String getPartUnreg()      		{ return getXMLData("meeting"    , "unRegPartName")  ; }
	public String getPartReg()      		{ return getXMLData("meeting"    , "regPartName")    ; }
	public String getPartRegNickname()  	{ return getXMLData("meeting"    , "regPartNickname"); }
	public String getPartPassword()  		{ return getXMLData("meeting"    , "regPartPassword"); }
	public String getSlidesMp4Path() 		{ return getXMLData("meeting"    , "slidesMp4Path")  ; }
	public String getAttachmentPath_PPT()	{ return getXMLData("attachments", "PPTPath")		 ; }
	public String getAttachmentPPT1name()	{ return getXMLData("attachments", "PPT1name")		 ; }
	public String getAttachmentPath_PPT2()	{ return getXMLData("attachments", "PPTPath2")		 ; }
	public String getAttachmentPPT2name()	{ return getXMLData("attachments", "PPT2name")		 ; }
	public String getNonPptFile() 			{ return getXMLData("slides"	 , "nonPPTfile")	 ; }
	public String getPptxFile() 			{ return getXMLData("slides"	 , "pptxFile")		 ; }
	public String getAutoHideIcon() 		{ return getXMLData("slides"	 , "autoHideIcon")	 ; }

}
