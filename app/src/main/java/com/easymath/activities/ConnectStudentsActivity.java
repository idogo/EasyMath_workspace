package com.easymath.activities;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.easymath.common.User;
import com.easymath.util.FirebaseDBUtils;
import com.easymath.R;

/**
 * Screen for connecting a student to a teacher (for teachers)
 * @author liadpc
 *
 */
public class ConnectStudentsActivity extends Activity{

	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Handle context
		setContentView(R.layout.connectstudents_screen);
		final Context context = this;		
		
		// Handle view and background
		View view = getWindow().getDecorView();
		Drawable myIcon = getResources().getDrawable(R.drawable.sky);
		view.setBackgroundDrawable(myIcon);
		
		// "Add Student" Button
		Button addStud = (Button)findViewById(R.id.btnAddStud);
		addStud.setBackgroundResource(R.drawable.green);
		addStud.setTextSize(30);
		
		// Button clicked
		addStud.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// Fetch students from DB wtih connection string
				final Firebase ref = new Firebase(FirebaseDBUtils.getDatabaseURL() + "users/students");
				
				// Get student name input
				final TextView studentNameTextView = (TextView) findViewById(R.id.tfaddStudents);
				final String studentNameString = studentNameTextView.getText().toString();
				
				// Receive events
				ref.addListenerForSingleValueEvent(new ValueEventListener() {
					
					@Override
					public void onDataChange(DataSnapshot snapshot) {
						// If a studnet like that exists
						if (snapshot.child(studentNameString).exists()) {
							
							// Fetch the student's current teacher
							DataSnapshot data = snapshot.child(studentNameString);
							String teacherName = (String) data.child("teacherName").getValue();
							
							// If the student is already connected to a teacher
							if(!teacherName.equalsIgnoreCase("-1")) {
								// Alert
								AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
								alertDialogBuilder.setTitle("תלמיד תפוס");
								alertDialogBuilder.setMessage("התלמיד כבר מקושר למורה, אנא נסה תלמיד אחר").setCancelable(false).setPositiveButton(PropertiesUtil.getOkMessage(),new DialogInterface.OnClickListener() {
													public void onClick(DialogInterface dialog,int id) {
														dialog.cancel();
													}
												});
								AlertDialog alertDialog = alertDialogBuilder.create();
								alertDialog.show();
							}
							// Student is free to add
							else {
								// Set the value for "teacherName" of the student to the current teacher username
								Firebase newRef = ref.child(studentNameString);
								newRef.child("teacherName").setValue(User.userName);
								
								// Alert
								AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
								alertDialogBuilder.setTitle("התלמיד נוסף");
								alertDialogBuilder.setMessage("התלמיד נוסף בהצלחה").setCancelable(false).setPositiveButton(PropertiesUtil.getOkMessage(),new DialogInterface.OnClickListener() {
													public void onClick(DialogInterface dialog,int id) {
														dialog.cancel();
													}
												});
								AlertDialog alertDialog = alertDialogBuilder.create();
								alertDialog.show();
							}
							
						}
						// Student doesn't exist
						else {
							// Alert
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
							alertDialogBuilder.setTitle("תלמיד לא קיים");
							alertDialogBuilder.setMessage("התלמיד אינו קיים במערכת, אנא בדוק שהכנסת שם תקין").setCancelable(false).setPositiveButton(PropertiesUtil.getOkMessage(),new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog,int id) {
													dialog.cancel();
												}
											});
							AlertDialog alertDialog = alertDialogBuilder.create();
							alertDialog.show();
						}
					}
					@Override
					public void onCancelled(FirebaseError arg0) {
					}
				});
			}
		});
	}
}
