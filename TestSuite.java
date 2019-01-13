package MeetingRecorder;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	
	Test1__login.class											,								
	Test2__import_meeting.class									,
	Test3__meeting_action_add_edit_delete.class  				,
	Test4__meeting_notes_add_edit_delete.class					,
	Test5__meeting_tags_add_edit_delete.class					,
	Test6__meeting_tags_add_via_main_menu.class					,
	Test7__Settings_tags_add_create_delete.class				,
	Test8__Settings_delegates.class								,
	Test9__Settings_events.class								,
	Test10__Settings_participiants.class						,
	Test11__main_menu_options.class								,
	Test12__Meeting_subject_add_edit_delete.class				,
	Test13__Meeting_subject_search_tests.class					,
	Test14__Meeting_video_action_note_tag.class					,
	Test15__Meeting_video_sub_menu_actions.class				,
	Test16__Meeting_search_by_tags_tests.class					,
	Test17__Meeting_video_sub_menu_slides.class					,
	Test18__Attachments_fromPlayer.class						,
	Test19__Meeting_video_upper_buttons.class					,
	Test20__Meeting_video_advanced_tests.class					,
	Test21__Meeting_Add_new_Tests.class							,
	Test22__Meeting_See_all_actions.class						,
	Test23__Meeting_video_edit_action_tests.class				,
	Test24__Delegates_advaned_tests.class						,
	Test25__Automatic_processing_tests.class					,
	Test26__Meeting_editor_trimming_tests.class					,
	Test27__Meeting_editor_trimming_player_tests.class			,
	Test28__Meeting_editor_trimming_preview_player_tests.class	,
	Test29__Meeting_slides.class								,
	Test30__Meeting_slides_hide_feature.class
})

public class TestSuite {
}
