package MeetingRecorder;


public class enumsClass {
	
	// Browser types
	public enum browserTypes     { CHROME, FF, IE; }
	  	
	// Log modes
	public enum logModes {
		 	 
		MAJOR(""), NORMAL("   "), MINOR("      "), DEBUG("         ");  		 
		private String level = "";	
		private logModes(String level) { this.level = level; }
		public String getLevel() 	   { return level;		 }
	 }
	
	// Menu names
	public enum menuNames {
		 	 
		MEETING_IMPORT("IMPORT MEETING"),
		MEETING_DATA_PLAY_VIDEO("COMMENTS AND TAGS"),
		MEETING_DATA_PLAY_VIDEO_ADV_BUTTONS(""),
		MEETING_DATA_PLAY_VIDEO_SHARE_MEETING(""),
		MEETING_DATA_PLAY_VIDEO_SESSION_IMAGES(""),
		MEETING_DATA_PLAY_VIDEO_SESSION_IMAGES_TRIMMING(""),
		MEETING_DATA_PLAY_VIDEO_ATTACHMENTS("ATTACHMENTS SLIDES"),
		MEETING_DATA_PLAY_VIDEO_ATTACHMENTS_SLIDES_TAB("Upload Attachment Files"),
		MEETING_DATA_PLAY_VIDEO_ATTACHMENTS_TAB(""),
		MEETING_DATA_PLAY_VIDEO_MEETING_FEED("MEETING FEED\n+ ACTIONS & NOTES\nPARTICIPANTS"),
		MEETING_DATA_PLAY_VIDEO_ADD_ACTION_NOTE_TAG("COMMENTS AND TAGS"),
		MEETING_DATA_PLAY_VIDEO_PARTICIPANTS("Me\nOwner"),
		MEETING_EDIT_MENU("BASIC INFO\nTAGS\nACTIONS\nNOTES\nSHARE WITH\nDELEGATES\nPARTICIPANTS\nEVENTS\nATTACHMENTS"),
		MEETING_EDIT_MENU_TAG_SECTION("ADDED BY ADDED AT NAME VISIBILITY TYPE"),
		MEETING_EDIT_MENU_PARTICIPANT_SECTION("NAME TYPE JOINED LEFT"),
		SETTINGS("TAGS\nDELEGATES\nSUBSCRIPTIONS\nPREFERENCE"),
		SETTINGS_DELEGATES_SECTION("DELEGATES"),
		SETTINGS_SUBSCRIPTIONS_SECTION("NOTIFICATIONS"),
		SETTINGS_PREFERENCE_SECTION("ENABLED");
		private String strForSearch = "";	
		private menuNames(String strForSearch) { this.strForSearch = strForSearch; }
		public String getStrForSearch() 	   { return strForSearch;		 	   }
	}	
	
	// Meeting action types
	public enum meetingActType {
		 	 
		NOTE(1), ACTION(2), TAG(3);  		 
		private int mode = 0;	
		private meetingActType(int mode) { this.mode = mode; }
		public int getMode()             { return mode;		 } 
	}
	
	// Tag working modes
	public enum tagModes {
		 	 
		PUBLIC("Public"), PRIVATE("Private");  		 
		private String mode = "";	
		private tagModes(String mode) { this.mode = mode; }
		public String getMode()       { return mode;	  }
	}
	
	// Mute states
	public enum fullScreen {
		 	 
		FULL("Fullscreen"), NON_FULL("Non-Fullscreen");
		private String mode = "";
		private fullScreen(String mode) { this.mode = mode; }
		public String getMode()         { return mode;	    }
	}
	
	// Surfer modes states
	public enum surferMode {
		 	 
		VIEW("display: block;"), HIDE("display: none;");
		private String mode = "";
		private surferMode(String mode) { this.mode = mode; }
		public String getMode()         { return mode;	    }
	}
	
	// Mute modes states
	public enum muteState {
		 	 
		MUTE("muted"), UNMUTE("Unmute");
		private String mode = "";
		private muteState(String mode) { this.mode = mode; }
		public String getMode()        { return mode;	    }
	}
	
	// VideoPlayer frames mode	
	public enum vidPlayerFrmMode {
	 	 
		FULL(1), MAIN(2);  		 
		private int mode = 0;	
		private vidPlayerFrmMode(int mode) { this.mode = mode; }
		public int getMode()               { return mode;		 } 
	}
	
	// VideoPlayer display mode	
	public enum vidPlayerDispMode {
	 	 
		PLAYER(1), GALLERY(2);  		 
		private int mode = 0;	
		private vidPlayerDispMode(int mode) { this.mode = mode; }
		public int getMode()                { return mode;		 } 
	}

}