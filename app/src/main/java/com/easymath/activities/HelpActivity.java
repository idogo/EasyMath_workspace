package com.easymath.activities;

import com.easymath.util.Constants;
import com.firebase.client.Firebase;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import com.easymath.R;

/**
 * Screen for displaying help
 * @author liadpc
 *
 */
@SuppressWarnings("deprecation")
public class HelpActivity extends TabActivity {


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Handle context
		Firebase.setAndroidContext(this);
		setContentView(R.layout.help_screen);

		// 2 tabs, teacher and student
		TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);
		TabSpec tab1 = tabHost .newTabSpec("teacher");
		TabSpec tab2 = tabHost.newTabSpec("student");
		
		// Redirect to teacher help
		tab1.setIndicator(Constants.TEACHER);
        tab1.setContent(new Intent(this,HelpTeacherActivity.class));
        
     // Redirect to student help
        tab2.setIndicator(Constants.STUDENT);
        tab2.setContent(new Intent(this,HelpStudentActivity.class));
        
        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
	}
}