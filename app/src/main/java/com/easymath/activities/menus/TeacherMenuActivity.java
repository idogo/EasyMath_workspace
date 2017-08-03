package com.easymath.activities.menus;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import com.easymath.R;

/**
 * The menu screen for teachers
 * @author liadpc
 */
public class TeacherMenuActivity extends Activity {
	
	String arg;

	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teacher_menu_screen);
		
		// Handle view and background
		View view = getWindow().getDecorView();
		Drawable myIcon = getResources().getDrawable(R.drawable.classbackground);
		view.setBackgroundDrawable(myIcon);
		
		// "Add Exercise" button
		Button addEx = (Button)findViewById(R.id.btnaddEx);
		addEx.setBackgroundResource(R.drawable.green);
		addEx.setTextSize(30);
		
		// "Connected Students" button
		Button connectStud = (Button) findViewById(R.id.buttonconstud);
		connectStud.setBackgroundResource(R.drawable.green);
		connectStud.setTextSize(30);
		
		// "Manage Students" button
		Button manageStudents = (Button) findViewById(R.id.btnManageStudents);
		manageStudents.setBackgroundResource(R.drawable.green);
		manageStudents.setTextSize(30);
		
		// "Help" button
		Button help = (Button) findViewById(R.id.btnHelpTeacher);
		
		// Clicked "Add Exercise"
		addEx.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Pass argument
				arg = "ADD_EX";
				// Redirect to LearningActivity (also used for adding exercises, depends on the argument it gets)
				Intent myIntent= null;
				myIntent = new Intent(TeacherMenuActivity.this, com.easymath.activities.learn.LearningActivity.class);
				myIntent.putExtra("arg",  getText());
				TeacherMenuActivity.this.startActivity(myIntent);
			}
		});
		
		// Clicked "Help"
		help.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Redirect to HelpActivity
				Intent myIntent= null;
				myIntent = new Intent(TeacherMenuActivity.this, com.easymath.activities.HelpActivity.class);
				TeacherMenuActivity.this.startActivity(myIntent);
			}
		});
		
		// Clicked "Connected Students"
		connectStud.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Redirect to ConnectStudentsActivity
				Intent myIntent= null;
				myIntent = new Intent(TeacherMenuActivity.this, com.easymath.activities.ConnectStudentsActivity.class);
				TeacherMenuActivity.this.startActivity(myIntent);
			}
		});
		
	
		// Clicked "Manage Students"
		manageStudents.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// Redirect ManageStudentsActivity
			Intent myIntent= null;
			myIntent = new Intent(TeacherMenuActivity.this, com.easymath.activities.ManageStudentsActivity.class);
			TeacherMenuActivity.this.startActivity(myIntent);
		}
		});
	}
	
	public String getText() {
		return arg;
	}
	
	/**
	 * When exiting the app
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (keyCode == KeyEvent.KEYCODE_BACK ) {
	    	// Alert "Are you sure you want to exit?" message
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			alertDialogBuilder.setTitle("לצאת?");
			alertDialogBuilder.setMessage("האם ברצונך לצאת מהאפליקציה").setCancelable(false)
			.setPositiveButton("יציאה",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
					finish();
				}
			}).setNegativeButton("רוצה להישאר", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();

				}
			});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
	    	
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
