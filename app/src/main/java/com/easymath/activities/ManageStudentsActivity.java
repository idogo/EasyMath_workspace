package com.easymath.activities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import com.easymath.common.User;
import com.easymath.util.FirebaseDBUtils;
import com.easymath.R;

/**
 * Screen for teachers to manage their connected students 
 * @author liadpc
 */
public class ManageStudentsActivity extends Activity {

	String arg;

	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Handle context
		setContentView(R.layout.manage_students);
		Firebase.setAndroidContext(this);
		final Context context = this;
		
		// Handle view and background
		View view = getWindow().getDecorView();
		Drawable myIcon = getResources().getDrawable(R.drawable.sky);
		view.setBackgroundDrawable(myIcon);
		
		// Spinner (dropdown list) for all the students of this teacher
		final Spinner spinner = (Spinner) findViewById(R.id.spinnerShowStude);
		final Firebase allStudentsRef = new Firebase(FirebaseDBUtils.getDatabaseURL() + "users/students/");
		showSpinnerStudents(spinner, allStudentsRef, context);
		
		// "Show student profile" button
		Button showProfile = (Button) findViewById(R.id.btnShowProfileToTeacher);
		showProfile.setBackgroundResource(R.drawable.green);
		showProfile.setTextSize(30);
		
		// "Delete student" button
		Button deleteStudent = (Button) findViewById(R.id.btndeleteStudent);
		deleteStudent.setBackgroundResource(R.drawable.green);
		deleteStudent.setTextSize(30);
		
		// Clicked "Show student profile"
		showProfile.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				arg = spinner.getSelectedItem().toString();
				
				// Redirect to ProfileActivity with the student name as argument
				Intent myIntent= null;
				myIntent = new Intent(ManageStudentsActivity.this, com.easymath.activities.ProfileActivity.class);
				myIntent.putExtra("arg",  getText());
				ManageStudentsActivity.this.startActivity(myIntent);
				
			}
		});
		
		// Clicked "Delete student"
		deleteStudent.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// Fetch the student from the DB
				Firebase studentRef = allStudentsRef.child(spinner.getSelectedItem().toString());
				// Set "-1" as teacher name
				studentRef.child("teacherName").setValue("-1");
				// Update the spinner list
				showSpinnerStudents(spinner, allStudentsRef, context);
			}
		});

	}

	/**
	 * Finds the student of the current teacher user and lists them in a spinner list
	 */
	private void showSpinnerStudents(final Spinner spinner, final Firebase allStudentsReference, final Context context) {
		
		// Receive events
		allStudentsReference.addListenerForSingleValueEvent(new ValueEventListener() {
			
			@Override
			public void onDataChange(DataSnapshot snapShot) {
				
				// Iterate over all the students
				Iterator<DataSnapshot> iterator = snapShot.getChildren().iterator();
				List<String> list = new ArrayList<String>();
				while(iterator.hasNext()) {
					DataSnapshot next = iterator.next();
					// If the student's teacher is the current user, add it to the list
					if(next.child("teacherName").getValue().equals(User.userName)){
						list.add(next.getKey());
					}
				}
				// Add the students found to the spinner list
				ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, list);
			    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			    spinner.setAdapter(dataAdapter);
			}
			@Override
			public void onCancelled(FirebaseError arg0) {
			}
		});
	}
	public String getText() {
		return arg;
	}
}
